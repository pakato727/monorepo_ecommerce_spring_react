package com.example.ecommercespring.controller;

import com.example.ecommercespring.model.*;
import com.example.ecommercespring.repo.*;
import com.example.ecommercespring.service.*;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@RequestMapping("/server")
public class ControllerApp {
    @Autowired
    ProdottoService ps;

    @Autowired
    UserService us;

    @Autowired
    AccountService as;

    @Autowired
    AccountRepository ar;

    @Autowired
    RoleService rs;

    @Autowired
    RoleRepository rr;

    @Autowired
    ProdottoRepository rp;

    @Autowired
    UserRepository ur;

    @Autowired
    TokenRepository tr;

    @Autowired
    VerificationService vs;

    @Autowired
    CartRepository cr;


    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private WebClient webClient;



    @PostMapping("/signIn")
    @ResponseBody
    public String registerUser(@ModelAttribute Account account, @ModelAttribute User user, Cart cart, Model model) throws MessagingException {

        Optional<Account> optionalA = ar.findByEmail(account.getEmail());
        if (optionalA.isPresent()) {  //se ha trovato è presente
            Account acc = optionalA.get(); //si prende l account dall email

            if (!acc.isEnabled()) {//se non è false
                int accId = acc.getId();
                List<VerificationToken> tokens = tr.findAll();
                for (VerificationToken token : tokens) {
                    if (token.getAccount().getId() == accId) {
                        tr.delete(token);
                        ar.deleteById(accId);
                    }
                }
            } else {
                model.addAttribute("giacreato", true); //manda una variabile al front
                return readAllProducts(model);


            }
        }
        Role role = new Role();
        role.setId(3);
        role.setRuolo("GUEST");
        account.setUser(user);
        account.setEnabled(false);
        account.setRole(role);
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        cart.setEmail(account.getEmail());
        account.setCart(cart);
        cart.setAccount(account);

        rr.save(role);
        as.insertAccount(account);
        us.insertUser(user);

        String token = vs.generateVerificationToken(account);
        if (vs.sendVerificationEmail(account, token)) {
            System.out.println("email inviata con successo");
        } else {
            System.out.println("email non inviata");
        }
        boolean emailSent = vs.sendVerificationEmail(account, token);
        model.addAttribute("inviata", true);
        return readAllProducts(model);

    }


    @GetMapping("/accountVerify")
    public String verifyAccount(Account account, @RequestParam String token, Model model) {
        Optional<VerificationToken> optionalToken = tr.findByToken(token);
        VerificationToken verificationToken = optionalToken.get();

        if (optionalToken.isEmpty()) {
            model.addAttribute("successo", false);
            return readAllProducts(model);
        }


        account = verificationToken.getAccount();
        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            model.addAttribute("successo", false);
            tr.delete(verificationToken);
            ar.delete(account);
            return readAllProducts(model);
        }

        account.setEnabled(true);
        ar.save(account);
        tr.delete(verificationToken);

        model.addAttribute("successo", true);
        return readAllProducts(model);
    }


    @GetMapping("/profile")
    public String mostraProfilo(Model model) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();

        Optional<Account> account = ar.findByEmail(email);
        User user = account.get().getUser();
        model.addAttribute("user", user);

        return "profile";
    }


    @PostMapping("/insertProd")
    public String insertProd(@ModelAttribute Prodotto prodotto, @RequestParam("file") MultipartFile file, Model model) {

        try {
            if (!file.isEmpty()) {
                prodotto.setData(file.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        boolean x = ps.insertProdotto(prodotto);
        if (x) {
            System.out.println("Prodotto inserito correttamente.");
        } else {
            System.out.println("Errore nell'inserimento del prodotto.");
        }
        return readAllProducts(model);
    }

    @GetMapping("/deleteProd/{id}")
    public String deleteById(@PathVariable("id") int id, Model model) {
        Optional<Prodotto> optionalProd = rp.findById(id);
        rp.delete(optionalProd.get());
        return readAllProdPage(model, 0, 10);
    }

    @GetMapping("/deleteUser/{id}")
    public String deleteByIdUser(@PathVariable("id") int id, Model model) {
        Optional<User> optionalUser = ur.findById(id);
        ur.delete(optionalUser.get());
        return readAllUsersPage(model, 0, 20);
    }

    @PostMapping("/editProd/{id}")
    public String editProdById(@RequestParam("file") MultipartFile file, @ModelAttribute Prodotto prodotto, Model model, @PathVariable("id") int id, Model model1) throws IOException {
        Optional<Prodotto> optionalProd = rp.findById(id);
        Prodotto prod = optionalProd.get();
        prod.setNomeProdotto(prodotto.getNomeProdotto());
        prod.setCategoria(prodotto.getCategoria());
        prod.setMarca(prodotto.getMarca());
        prod.setId(id);
        prod.setData(file.getBytes());
        prod.setDescrizione(prodotto.getDescrizione());
        prod.setPrezzo(prodotto.getPrezzo());
        prod.setQuantita(prodotto.getQuantita());

        rp.save(prod);


        return readAllProdPage(model1, 0, 10);
    }

    @GetMapping("/table1")
    public String readAllProdPage(
            Model model,
            @RequestParam(defaultValue = "0") int page,  // Numero pagina (default: 0)
            @RequestParam(defaultValue = "10") int size  // Elementi per pagina (default: 10)
    ) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Prodotto> prodottiPage = ps.findAllPageProd(pageable);

        for (Prodotto prodotto : prodottiPage.getContent()) {
            if (prodotto.getData() != null) {
                String base64Image = convertToBase64(prodotto.getData());
                prodotto.setBase64Image(base64Image);
            }
        }


        model.addAttribute("prodotti", prodottiPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", prodottiPage.getTotalPages());


        return "/table";
    }

    @GetMapping("/table2")
    public String readAllUsersPage(
            Model model,
            @RequestParam(defaultValue = "0") int page,  // Numero pagina (default: 0)
            @RequestParam(defaultValue = "20") int size  // Elementi per pagina (default: 10)
    ) {

        Pageable pageable = PageRequest.of(page, size);

        Page<User> usersPage = us.findAllPageUsers(pageable);

        List<User> filteredUsers = usersPage.getContent().stream()
                .filter(user -> user.getAccount().getRole().getId() != 4)
                .collect(Collectors.toList());

        Page<User> filteredUsersPage = new PageImpl<>(filteredUsers, pageable, filteredUsers.size());


        model.addAttribute("users", filteredUsers);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", filteredUsersPage.getTotalPages());


        return "/tableUser";
    }


    @GetMapping("/banned/{id}")
    public String banned(@PathVariable("id") int id, Model model) {
        Account account = as.getAccount(id);
        Role bannedRole = rr.findById(4).orElseThrow(() -> new RuntimeException("Ruolo banned non trovato"));
        account.setRole(bannedRole);
        ar.save(account);
        return readAllUsersPage(model, 0, 20);
    }

    @GetMapping("/setAdmin/{id}")
    public String setAdmin(@PathVariable("id") int id, Model model) {
        Account account = as.getAccount(id);
        Role adminRole = rr.findById(2).orElseThrow(() -> new RuntimeException("Ruolo banned non trovato"));
        account.setRole(adminRole);
        ar.save(account);
        return readAllUsersPage(model, 0, 20);
    }

    @GetMapping("/product/{id}")
    @ResponseBody
    public byte[] getProdottoImage(@PathVariable("id") int id) {
        Prodotto prodotto = ps.getProdotto(id);
        return prodotto != null ? prodotto.getData() : null;
    }

    public static String convertToBase64(byte[] imageBytes) {
        return Base64.getEncoder().encodeToString(imageBytes);
    }

    @GetMapping("/findUser")
    public String cercaUtenti(@RequestParam("findUser") String cerca,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              Model model) {

        List<User> users = us.readAll();


        List<User> usersCercati = users.stream()
                .filter(u -> u.getName().toLowerCase().contains(cerca.toLowerCase()) ||
                        u.getFiscalCode().toLowerCase().contains(cerca.toLowerCase()) ||
                        u.getSurname().toLowerCase().contains(cerca.toLowerCase()) ||
                        u.getCity().toLowerCase().contains(cerca.toLowerCase()))
                .toList(); // Converte in lista immutabile


        Pageable pageable = PageRequest.of(page, size);


        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), usersCercati.size());
        Page<User> pagedList = new PageImpl<>(usersCercati.subList(start, end), pageable, usersCercati.size());


        String ricercato = "Risultati ricerca per: " + cerca;
        model.addAttribute("ricercato", ricercato);
        model.addAttribute("users", pagedList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pagedList.getTotalPages());

        return "tableUser";
    }

    @GetMapping("/findProd")
    public String cercaProdotti(@RequestParam("find") String cerca,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size,
                                Model model) {

        List<Prodotto> prodotti = ps.readAll();


        List<Prodotto> prodCercati = prodotti.stream()
                .filter(p -> p.getNomeProdotto().toLowerCase().contains(cerca.toLowerCase()) ||
                        p.getDescrizione().toLowerCase().contains(cerca.toLowerCase()) ||
                        p.getMarca().toLowerCase().contains(cerca.toLowerCase()) ||
                        p.getCategoria().toLowerCase().contains(cerca.toLowerCase()))
                .toList(); // Converte in lista immutabile


        Pageable pageable = PageRequest.of(page, size);


        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), prodCercati.size());
        Page<Prodotto> pagedList = new PageImpl<>(prodCercati.subList(start, end), pageable, prodCercati.size());

        for (Prodotto prodotto : pagedList.getContent()) {
            if (prodotto.getData() != null) {
                String base64Image = convertToBase64(prodotto.getData());
                prodotto.setBase64Image(base64Image);
            }
        }
        String ricercato = "Risultati ricerca per: " + cerca;
        model.addAttribute("ricercato", ricercato);
        model.addAttribute("prodotti", pagedList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pagedList.getTotalPages());

        return "table";
    }

    @GetMapping("/readAllProducts")
    public String readAllProducts(Model model) {
        if (isAuthenticated()) {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String email = userDetails.getUsername();

            Optional<Account> account = ar.findByEmail(email);
            User user = account.get().getUser();
            String name = user.getName();
            model.addAttribute("name", name);
        }
        List<Prodotto> prodotti = ps.readAll();
        for (Prodotto prodotto : prodotti) {
            if (prodotto.getData() != null) {
                String base64Image = convertToBase64(prodotto.getData());
                prodotto.setBase64Image(base64Image);
            }
        }

        if (prodotti.size() > 5) {
            model.addAttribute("void", true);
            model.addAttribute("prodotti", prodotti);
        } else {
            model.addAttribute("void", false);
        }


        return "home";
    }

    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken);
    }


    @GetMapping("/addCart/{id}")
    public String addToCart(Model model, @PathVariable("id") int id) {
        Optional<Prodotto> prodotto = rp.findById(id);
        Prodotto prodCercato = prodotto.get();

        if (isAuthenticated()) {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String email = userDetails.getUsername();
            Optional<Account> accountOptional = ar.findByEmail(email);
            Account account = accountOptional.get();
            Cart cart = account.getCart();

            cart.getProdotti().add(prodCercato);
            prodCercato.getCarts().add(cart);
            cr.save(cart);


            String nomeProd = prodCercato.getNomeProdotto();
            model.addAttribute("aggiunto", true);
            model.addAttribute("nomeProd", nomeProd);

        } else {
            model.addAttribute("prodottoS", prodCercato.getId());
        }

        return readAllProducts(model);

    }

    @GetMapping("/addStorageToCart")
    public String addStorageToCart(Model model, @RequestParam String carrello, HttpServletRequest request) {
        String paginaProvenienza = request.getHeader("Referer");
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();
        Optional<Account> accountOptional = ar.findByEmail(email);
        Account account = accountOptional.get();
        Cart cart = account.getCart();
        String carrelloDecoded = URLDecoder.decode(carrello, StandardCharsets.UTF_8);
        carrelloDecoded = carrelloDecoded.substring(1, carrelloDecoded.length() - 1);

        List<Integer> listaId = Arrays.stream(carrelloDecoded.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        Iterable<Prodotto> prodotti = rp.findAllById(listaId);
        for (Prodotto prod : prodotti) {
            cart.getProdotti().add(prod);
        }


        cr.save(cart);

        return readAllProducts(model);


    }

    @GetMapping("/readCart")
    public String readCart(Model model) {
        if (isAuthenticated()) {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String email = userDetails.getUsername();
            Optional<Account> accountOptional = ar.findByEmail(email);
            Account account = accountOptional.get();
            List<Prodotto> prodotti = account.getCart().getProdotti();
            for (Prodotto prodotto : prodotti) {
                if (prodotto.getData() != null) {
                    String base64Image = convertToBase64(prodotto.getData());
                    prodotto.setBase64Image(base64Image);
                }
            }

            Map<Prodotto, Long> prodottiConQuantita = prodotti.stream()
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            model.addAttribute("prodottiConQuantita", prodottiConQuantita);
            model.addAttribute("autenticato", true);

            return "/cart";
        } else {
            model.addAttribute("autenticato", false);
            return "/cart";
        }
    }

    @GetMapping("/deleteProdCart/{id}")
    public String deletePCartById(@PathVariable("id") int id, Model model) {
        Optional<Prodotto> prodotto = rp.findById(id);
        Prodotto prodCercato = prodotto.get();

        if (isAuthenticated()) {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String email = userDetails.getUsername();
            Optional<Account> accountOptional = ar.findByEmail(email);
            Account account = accountOptional.get();
            Cart cart = account.getCart();

            cart.getProdotti().remove(prodCercato);
            cr.save(cart);
            model.addAttribute( "eliminato",true);
        }
        return readCart(model);
    }

};





//    @GetMapping("/restart")
//    public String restart() {
//        System.out.println("Richiesta di riavvio ricevuta!");
//        EcommercespringApplication.restart();
//        return "redirect:/home";
//    }



