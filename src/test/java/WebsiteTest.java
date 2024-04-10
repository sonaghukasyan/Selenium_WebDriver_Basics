import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class WebsiteTest {
    private static WebDriver driver;
    private static final String baseUrl = "https://www.nopcommerce.com/en";

    @BeforeClass
    public static void initWebDriver() {
        // Set the path to the chromedriver executable
        System.setProperty("chromedriver.exe", "drivers/chromedriver-win32/");

        // Initialize WebDriver instance for Chrome
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test
    public void webDriverCommandTests() {
        driver.get(baseUrl);
        driver.getTitle();
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(2000));
        driver.manage().window().fullscreen();
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(2000));
        driver.navigate().refresh();
        driver.manage().window().minimize();
    }

    @Test
    public void menuExpandTest() {
        driver.get(baseUrl);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(500000));

        // Find the menu element using its XPath
        WebElement ulElement = driver.findElement(By.xpath("//header/nav[1]/div[3]/div[1]/ul[1]"));

        // Find all sub-menu elements inside the menu
        List<WebElement> liElements = ulElement.findElements(By.xpath("./li"));

        Actions actions = new Actions(driver);

        // Iterate over the elements
        for (WebElement liElement : liElements) {
            // Hover on the element to expand the menu bar
            actions.moveToElement(liElement).perform();
            // Explicitly wait for the item to be expanded
            wait.until(d -> liElement.isDisplayed());
            Assert.assertTrue("displayed", liElement.isDisplayed());
        }
    }

    @Test
    public void scrollLanguagesAndRedirect() {
        driver.get(baseUrl);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(500000));
        Actions actions = new Actions(driver);
        // Find the sublist element
        WebElement sublist = driver.findElement(By.className("navigation-top-menu-language-selector"));

        actions.moveToElement(sublist).perform();
        wait.until(d -> sublist.isDisplayed());

        // Find all <li> elements inside the sublist
        List<WebElement> subItems = sublist.findElements(By.className("navigation-top-menu-link"));

        // Iterate over the elements
        for(int i = 0; i < subItems.size(); i++){
             WebElement liElement = subItems.get(i);
            // Hover on the element to expand the menu bar
            actions.moveToElement(liElement).perform();
            // Explicitly wait for the item to be expanded
            wait.until(d -> liElement.isDisplayed());
            if(i == 8){
                String actualHref = liElement.getAttribute("href");
                String expectedHref = "https://www.nopcommerce.com/en/change-language/8?returnUrl=%2Fen";
                Assert.assertEquals("Actual href value doesn't match the expected one", expectedHref, actualHref);
                liElement.click();
                driver.manage().timeouts().implicitlyWait(Duration.ofMillis(2000));
                break;
            }
        }
    }

    @Test
    public void redirectToShop(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(500000));

        driver.navigate().to(baseUrl + "/demo");
        String text = driver.findElement(By.tagName("h1")).getText();
        Assert.assertEquals("nopCommerce Store Demo" ,text);

        //to perform Scroll
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,350)", "");

        //redirect to the store
        WebElement frontButton = driver.findElement(By.cssSelector("body.loaded._desktop._scrolled:nth-child(2) div.master-wrapper-page:nth-child(8) div.master-wrapper-content div.container div.master-column-wrapper div.center-1 div.page.topic-page div.page-body.page-body-wrapper div.demo-section-wrapper div.demo-section div.demo-image-wrapper div.demo-img div.demo-buttons > a.btn.frontend-button:nth-child(2)"));
        frontButton.click();
        wait.until(d -> frontButton.isDisplayed());
    }

    @Test
    public void searchTest() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(500000));

        driver.get("https://demo.nopcommerce.com/");
        //test search functionality
        // Locate the search box input field // Find the element using the absolute XPath
        WebElement searchContainer = driver.findElement(By.className("master-wrapper-page"));
        WebElement searchBox = searchContainer.findElement(By.id("small-searchterms"));
        // Type something in the search box
        searchBox.sendKeys("COMP_CUST");

        // Locate the search button
        WebElement searchButton = driver.findElement(By.xpath("//button[@class='button-1 search-box-button']"));

        // Click on the search button to submit the search
        searchButton.click();

        //scroll to find the searched item
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,700)", "");

        //add to card
        WebElement addToCardButton = driver.findElement(By.cssSelector("div.master-wrapper-page:nth-child(7) div.master-wrapper-content div.master-column-wrapper div.center-2 div.page.search-page div.page-body div.search-results div.products-container div.products-wrapper div.product-grid div.item-grid div.item-box div.product-item div.details div.add-info div.buttons > button.button-2.product-box-add-to-cart-button:nth-child(1)"));
        addToCardButton.click();

        driver.navigate().to("https://demo.nopcommerce.com/build-your-own-computer");
        // Find the <input> element based on its attribute value
        js.executeScript("window.scrollBy(0,700)", "");

        // Click on the radio button to select it
        WebElement HDDButton = driver.findElement(By.xpath("/html[1]/body[1]/div[6]/div[3]/div[1]/div[2]/div[1]/div[1]/form[1]/div[2]/div[1]/div[2]/div[6]/dl[1]/dd[3]/ul[1]/li[1]/input[1]"));
        HDDButton.click();
        wait.until(d -> HDDButton.isSelected());
        WebElement RAMButton = driver.findElement(By.xpath("/html[1]/body[1]/div[6]/div[3]/div[1]/div[2]/div[1]/div[1]/form[1]/div[2]/div[1]/div[2]/div[6]/dl[1]/dd[2]/select[1]/option[2]"));
        RAMButton.click();
        wait.until(d -> RAMButton.isSelected());

        WebElement addButton = driver.findElement(By.cssSelector("#add-to-cart-button-1"));
        addButton.click();
    }
}
