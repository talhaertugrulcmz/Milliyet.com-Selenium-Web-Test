package method;

import com.thoughtworks.gauge.Step;

import driver.Driver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.*;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Collections;

import static java.time.Duration.*;


public class Methods {
    WebDriver driver;
    FluentWait<WebDriver> wait;
    JavascriptExecutor jsdriver;
    Logger logger = LogManager.getLogger(Methods.class);

    WebElement actionButton;



    public Methods() {
        driver = Driver.driver;
        wait = new FluentWait<>(driver);
        wait.withTimeout(ofSeconds(30)).pollingEvery(ofMillis(300)).ignoring(NoSuchElementException.class);
        jsdriver = (JavascriptExecutor) driver;
    }

    public WebElement findElement(By by) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(by));

    }

    public void click(By by){
        findElement(by).click();
    }


    public void hoverElement(By by){
        Actions actions = new Actions(driver);
        actions.moveToElement(findElement(by)).build().perform();
    }

    public void MouseActionElement(){

        Actions actions = new Actions(driver);
        WebElement src = driver.findElement(By.xpath("//*[@id]/div/div[1]/div[5]/a"));
        actions.clickAndHold(src)
                .moveByOffset(50,0)
                .release()
                .build()
                .perform();
        actions.clickAndHold(src)
                .moveByOffset(50,0)
                .release()
                .build()
                .perform();
    }
}


