package com.example.ecommercespring.controller;

import com.example.ecommercespring.model.*;
import com.example.ecommercespring.repo.*;
import com.example.ecommercespring.service.*;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class RestControllerApi {
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



    @GetMapping("/products")
    public ResponseEntity<List<Map<String, Object>>> getProducts() {
        List<Prodotto> listaProd = ps.readAll();

        List<Map<String, Object>> listaJSON = new ArrayList<>();

        Map<String, Object> json;
        for (Prodotto prodotto : listaProd) {
            json = new HashMap<>();
            json.put("id", prodotto.getId());
            json.put("nomeProdotto", prodotto.getNomeProdotto());
            json.put("descrizione", prodotto.getDescrizione());
            json.put("prezzo", prodotto.getPrezzo());
            json.put("quantita", prodotto.getQuantita());
            json.put("categoria", prodotto.getCategoria());
            json.put("marca", prodotto.getMarca());

            if (prodotto.getData() != null) {
                String base64image = Base64.getEncoder().encodeToString(prodotto.getData());
                json.put("base64image", base64image);
            }

            listaJSON.add(json);
        }

        //stream che stampa tutti gli oggetti della lista tranne base64image
//        listaJSON.stream().forEach(oggetto-> {
//            oggetto.entrySet().stream()
//                    .filter(entry ->!entry.getKey().equalsIgnoreCase("base64image"))
//                    .forEach(entry-> System.out.println(entry.getKey()+ ": "+ entry.getValue()));
//        });

        return ResponseEntity.ok(listaJSON);
    }

    //DI SEGUITO, TUTTI I METODI PRECEDENTI
    @PostMapping("/signIn")
    public ResponseEntity<?> registerUser(@ModelAttribute Account account, @ModelAttribute User user, Cart cart) throws MessagingException {
        Optional<Account> optionalA = ar.findByEmail(account.getEmail());
        Map<String,Object> risposta= new HashMap<>();

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
                risposta.put("message","esistente");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(risposta);
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
        risposta.put("message","creata");
        return ResponseEntity.ok(risposta);
    }



    @GetMapping("/accountVerify")
    public ResponseEntity<?> verifyAccount(Account account, @RequestParam String token) {
        Optional<VerificationToken> optionalToken = tr.findByToken(token);



        Map<String, Object> risposta = new HashMap<>();

        if (!optionalToken.isPresent()) {

            risposta.put("successo", "vuoto");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(risposta);
        }

        VerificationToken verificationToken = optionalToken.get();
        account = verificationToken.getAccount();
        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {

            risposta.put("successo", "expired");
            tr.delete(verificationToken);
            ar.delete(account);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(risposta);
        }


        account.setEnabled(true);
        ar.save(account);
        tr.delete(verificationToken);

        risposta.put("successo", "verificato");
        return ResponseEntity.ok(risposta);
    }


    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken);
    }

    public static String convertToBase64(byte[] imageBytes) {
        return Base64.getEncoder().encodeToString(imageBytes);
    }
}

