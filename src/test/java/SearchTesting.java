import com.google.common.base.Predicate;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by Nik on 25.10.2016.
 */
public class SearchTesting {
    String searchText = new String("РОБОЧИЙ ЧАС — УКРАЇНЦЯМ!");
    String textInResult = new String("СКОРОЧЕНИЙ європейський РОБОЧИЙ ЧАС — УКРАЇНЦЯМ!");
    WebDriver driver;

    //additional method to check that element of the page is exist
    public boolean isElementPresent(By locator, WebDriver driver) {
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        boolean result = driver.findElements(locator).size() > 0;
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        return result;
    }

    //additional method that wait until the element with xpath will appear on the page
    public void waitResultPage(WebDriver driver) {
        WebDriverWait waiter = new WebDriverWait(driver, 10, 500);
        waiter.until(new Predicate<WebDriver>() {
            @Override
            public boolean apply(WebDriver driver) {
                return isElementPresent(By.xpath("//h1[text()='ВСІ ЕЛЕКТРОННІ ПЕТИЦІЇ']"), driver);
            }
        });

    }

    @Test
    public void searchTest() {
        //      Using Firefox
        WebDriver driver = new FirefoxDriver();

/*      Using IE11
        System.setProperty("webdriver.ie.driver", "E:\\SS\\IEDriverServer_x64_2.53.1\\IEDriverServer.exe");
        WebDriver driver = new InternetExplorerDriver();
*/
/*
        Using Chrome

        System.setProperty("webdriver.chrome.driver", "E:\\SS\\chromedriver_win32\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        */
        driver.get("https://petition.president.gov.ua/");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        ArrayList<WebElement> resultsList;
        Assert.assertTrue(isElementPresent(By.cssSelector(".txt_input.vat"), driver), "Search field is not exist!");
        WebElement searchField = driver.findElement(By.cssSelector(".txt_input.vat"));
        searchField.clear();
        searchField.sendKeys(searchText);
        searchField.submit();

        waitResultPage(driver);

        resultsList = (ArrayList<WebElement>) driver.findElements(By.xpath("//a[ancestor::div[@class='pet_title']]"));
        Assert.assertTrue(resultsList.size() > 0, "No search results!");
        System.out.println(resultsList.size() + " search results was found.");
        for (int i = 0; i < resultsList.size(); i++) {
            if (resultsList.get(i).getText().equals(textInResult)) {
                driver.get(resultsList.get(i).getAttribute("href"));
            }
        }

        Assert.assertTrue(driver.findElement(By.tagName("h1")).getText().equals(textInResult), "No searched text in headers of resulted page!");
        System.out.println("Text was found");

        driver.quit();
    }


}
