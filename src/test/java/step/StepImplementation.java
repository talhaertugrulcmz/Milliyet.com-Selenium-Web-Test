package step;


import com.thoughtworks.gauge.Gauge;
import com.thoughtworks.gauge.Step;
import driver.Driver;
import io.github.bonigarcia.wdm.WebDriverManager;
import method.Methods;
import org.apache.log4j.LogManager;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;


import javax.swing.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.*;
import java.util.logging.Logger;

import static java.awt.SystemColor.window;
import static org.assertj.core.api.Assertions.*;


public class StepImplementation extends Driver {

    Methods methods;

    public StepImplementation() {
        methods = new Methods();

    }


    // Bekleme Süresi
    @Step("<int> saniye kadar bekle")
    public void waitBySeconds(long seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    // URL Kontrolü
    @Step("URL Kontrolü")
    public void UrlKontrols() {

        String SayfaKonrolu = driver.getCurrentUrl();


        if (SayfaKonrolu.contains("https://www.milliyet.com.tr/")) {
            logger.info("Dogru URL Adresi...> " + SayfaKonrolu);
        } else {
            logger.info("URL adresi dogru degil !! Lutfen kontrol ediniz...");
        }
    }


    // Gundem Kategori Tıklama ve Reklam Gizleme
    @Step("<css> li Gundem butonuna tikla")
    public void SayfaKontrol(String css) {

        methods.findElement(By.cssSelector(css)).click();
        logger.info("GUNDEM" + " Kategorisine tiklanmistir...");

    }


    // Yeni Sekme URL Kontrolü
    @Step("Yeni Sekme Url Kontrolu")
    public void UrlKontrolu() {

        String UrlKontrol = driver.getWindowHandle();
        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(UrlKontrol)) {
                driver.switchTo().window(handle);
                break;
            }
        }

        String newTabURL = driver.getCurrentUrl();
        System.out.println("New tab URL: " + newTabURL);

        if (newTabURL.contains("https://www.milliyet.com.tr/gundem/")) {
            logger.info("Dogru URL Adresi ----> " + newTabURL);
        } else {
            logger.info("URL adresi dogru degil !! Lutfen kontrol ediniz...");
        }

    }


    // Sağ Sol Bar İle Swipe Kontrolu
    @Step("<css> li sol bar tiklama , <css> li sag bar tiklama")
    public void SwipeKontrolu1(String css1, String css2) throws InterruptedException {

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scroll(0,300)");
        Thread.sleep(1000);

        driver.findElement(By.cssSelector(css2)).click();
        logger.info("Sol Bar Elementine Basari Ile Tiklandi");
        Thread.sleep(2000);

        driver.findElement(By.cssSelector(css1)).click();
        logger.info("Sag Bar Elementine Basari Ile Tiklandi...");
        Thread.sleep(2000);
    }


    // Alt tarafta yer alan numaralar ile swipe kontrolu
    @Step("<css> li Alt Navbar Swipe Kontrolu")
    public void SwipeKontrolu2(String css3) throws InterruptedException {
        methods.hoverElement(By.cssSelector(css3));
        logger.info("Haberi Alt Numaralardan Swipe Islemi Yapildi");
        Thread.sleep(1000);
    }


    // Mouse İle Swipe Kontrolu
    @Step("Mouse Ile Swipe Testi")
    public void SwipeKontrolu3() throws InterruptedException {

        methods.MouseActionElement();
        logger.info("Mouse Tutarak Swipe Yapma Ozelligi Basarili Calisti");
        Thread.sleep(2000);


        System.out.println("Tum Swipe Islemleri Basarili Bir Sekilde Gerceklestirildi...");

    }


    // Dogru Haber Tıklama Kontrolu
    @Step("Dogru Haber Kontrol Testi")
    public void HaberKontrol() throws InterruptedException {
        String title1 = driver.findElement(By.xpath("//*[@id]/div/div[1]/div[5]/a/div[2]/strong")).getText();
        System.out.println("Tiklanan Haber : " + title1);
        driver.findElement(By.cssSelector("[data-slider-index='4']")).click();
        Thread.sleep(1000);

        String title2 = driver.findElement(By.xpath("//div[1]/section[1]/div[3]/h1")).getText();
        System.out.println("Acilan Haber :" + title2);

        if (title1.equals(title2)) {
            Assertions.assertTrue(true);
            logger.info("Dogru Haber Basarili Bir Sekilde Acildi");

        } else {
            Assertions.assertFalse(false);
            logger.info("Dogru Habere Tiklanamadi");
        }
        driver.navigate().back();


    }

    // Status Code Kontrol

    @Step("Status Code Kontrolu")
    public void StatusCodeKontrol() throws InterruptedException, IOException {

        JavascriptExecutor js1 = (JavascriptExecutor) driver;
        js1.executeScript("window.scroll(0,300)");

        List<WebElement> altbarLink = driver.findElements(By.cssSelector("a[data-slider-index]"));
        List<String> linkHaber = new ArrayList<>();
        System.out.println("Alt Navbar Haber Size = " + altbarLink.size());

        Assertions.assertEquals(altbarLink.size(),5);
        logger.info("Haber Size'i Basarili Bir Sekilde Alindi...");


        WebElement s;

        if (!altbarLink.isEmpty()) {
            for (int i = 0; i < altbarLink.size(); i++) {
                System.out.println(altbarLink.get(i));
                s = altbarLink.get(i);
                altbarLink.get(i).click();
                System.out.println("Linke tiklandi");

                HttpURLConnection huc = null;
                List<WebElement> links = driver.findElements(By.tagName("ul"));

                Iterator<WebElement> it = links.iterator();
                while (it.hasNext()) {
                    String url1 = it.next().getAttribute("data-og-url");

                    if (url1 == null || url1.isEmpty()) {
                        continue;
                    }

                    try {
                        huc = (HttpURLConnection) (new URL(url1).openConnection());
                        huc.setRequestMethod("HEAD");
                        huc.connect();

                        int respCode = huc.getResponseCode();

                        if (respCode >= 201) {
                            logger.info(url1 + " Hatali Baglanti... ResponseCode-> " + respCode);
                            break;

                        } else {
                            logger.info(url1 + " Dogru Baglanti.... ResponseCode-> " + respCode);


                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                driver.navigate().back();
                JavascriptExecutor js2 = (JavascriptExecutor) driver;
                js2.executeScript("window.scroll(0,300)");
                break;
            }
            System.out.println("Status Code Basarili Bir Sekilde Test Edildi...");
        }

    }
}







