package driver;


import com.thoughtworks.gauge.AfterScenario;
import com.thoughtworks.gauge.BeforeScenario;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.DriverCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

public class Driver {

    public static WebDriver driver;
    protected Logger logger = LoggerFactory.getLogger(getClass());
    ChromeOptions chromeOptions;

    protected static By getDriver() {
        return null;
    }


    @BeforeScenario
    public void setUp() throws MalformedURLException,Exception {

        String Baseurl = "https://www.milliyet.com.tr/";
        DesiredCapabilities capabilities;

        if (StringUtils.isEmpty(System.getenv("key"))) {
            capabilities = DesiredCapabilities.chrome();
            ChromeOptions options = new ChromeOptions();
            capabilities.setCapability(ChromeOptions.CAPABILITY, options);

            System.setProperty("webdriver.chrome.driver", "web_driver/chromedriver.exe");
            driver = new ChromeDriver(capabilities);

        }else{
            capabilities = DesiredCapabilities.chrome();
            capabilities.setCapability("key", System.getenv("key"));
        }

        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS).implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        driver.get(Baseurl);
    }

    @AfterScenario
    public void tearDown()throws Exception{
        driver.quit();

    }

}
