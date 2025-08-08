package com.example.ecommercespring;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.time.Duration;

public class automazioneInserimento {

    private static final WebDriver driver=new ChromeDriver();
    private static final WebDriverWait wait=new WebDriverWait(driver,Duration.ofSeconds(10));
    private static final JavascriptExecutor js= (JavascriptExecutor) driver;


    @BeforeClass
    public static void setUp(){
        try {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driver.manage().window().maximize();

            System.out.println("connesso a: "+driver.getCurrentUrl());
        } catch (Exception e) {
            System.out.println("ERRORE CARICAMENTO DEL SITO. ERRORE: "+e.getMessage());
        };

    };

    @AfterClass
    public void close(){
        driver.quit();
        System.out.println("DRIVER CHIUSO");
    }


    @Test
    public static void inserisciUtentiTest() throws IOException, InterruptedException {
        driver.get("http://localhost:8095/");
        int x=1;
        do{
            Thread.sleep(Duration.ofSeconds(1));
            WebElement registra=driver.findElement(By.cssSelector("body > div.navbar > a:nth-child(2)"));
            registra.click();
            WebElement nome=driver.findElement(By.id("nome"));
            nome.click();
            nome.sendKeys("utente"+x);
            WebElement cognome=driver.findElement(By.id("cognome"));
            cognome.click();
            cognome.sendKeys("cognome");
            WebElement cf=driver.findElement(By.id("codicefiscale"));
            cf.click();
            cf.sendKeys("MRCLDH089"+x);
            WebElement zipc=driver.findElement(By.id("zipcode"));
            zipc.click();
            zipc.sendKeys("80040");
            WebElement city=driver.findElement(By.id("city"));
            city.click();
            city.sendKeys("volla");

            WebElement prov=driver.findElement(By.id("province"));
            prov.click();
            prov.sendKeys("napoli");

            WebElement user=driver.findElement(By.id("username"));
            user.click();
            user.sendKeys("username"+x);

            WebElement email=driver.findElement(By.id("email"));
            email.click();
            email.sendKeys("guest"+x+"@gmail.com");

            WebElement pass=driver.findElement(By.id("password"));
            pass.click();
            pass.sendKeys("root");

            WebElement addr=driver.findElement(By.id("address"));
            addr.click();
            addr.sendKeys("via manzoni 2");

            WebElement date=driver.findElement(By.id("date"));
            date.click();
            date.sendKeys("05091999");
            WebElement registrati=driver.findElement(By.cssSelector("body > div.container.mt-4 > div.signIn-content > form > div:nth-child(14) > button"));
            registrati.click();
            x++;
        }while(x<50);







    }

    @Test
    public static void inserisciProdottiTest() throws IOException, InterruptedException {
        driver.get("http://localhost:8095/");
        int x=1;

        WebElement accedi=driver.findElement(By.id("login"));
        accedi.click();
        WebElement email=wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
        email.click();
        email.sendKeys("admin@gmail.com");
        WebElement password=wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
        password.click();
        password.sendKeys("root");
        WebElement login=wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#loginModal > div > div > div.modal-body > form > button")));
        login.click();
        Thread.sleep(Duration.ofSeconds(1));
        do{
            WebElement registra=wait.until(ExpectedConditions.visibilityOfElementLocated((By.xpath("/html/body/nav/div/div/ul/li[3]/a"))));
            registra.click();
            WebElement immagine=driver.findElement(By.id("file"));
            String path="C:\\Users\\Mirco\\Desktop\\WORSPACES\\intellijWorkspace\\ecommercespring\\src\\test\\resources\\iphone.jpg";
            immagine.sendKeys(path);


            WebElement nome=driver.findElement(By.id("nomeProd"));
            nome.click();
            nome.sendKeys("Apple iphone 15 pro");
            WebElement des=driver.findElement(By.id("descrizione"));
            des.click();
            des.sendKeys("argento 256gb scanner idiar, schermo 4.7 pollici");
            WebElement prezzo=driver.findElement(By.id("prezzo"));
            prezzo.click();
            prezzo.sendKeys("799");
            WebElement zipc=driver.findElement(By.id("quantita"));
            zipc.click();
            zipc.sendKeys("5");
            WebElement categoria=driver.findElement(By.id("categoria"));
            categoria.click();
            categoria.sendKeys("telefonia");

            WebElement marca=driver.findElement(By.id("marca"));
            marca.click();
            marca.sendKeys("apple");


            WebElement salva=driver.findElement(By.cssSelector("body > div.container-fluid.d-flex.justify-content-center.align-items-center.min-vh-100.py-5 > div.insProd-content > div > div > form > div > div.col-12.text-center > button"));
            salva.click();
            x++;
        }while(x<15);







    }







}