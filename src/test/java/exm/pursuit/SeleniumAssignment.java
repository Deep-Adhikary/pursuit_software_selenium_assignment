package exm.pursuit;

import java.nio.file.FileSystems;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

public class SeleniumAssignment {
        /**
         * Step1 open "https://www.google.com/"
         * 
         * Step2 Type "COVID-19" in search bar
         * 
         * Step3 Click News in search result page (as highlighted in image)
         * 
         * Step4 Read all the news providing agencies show on the search result page in
         * that ArrayList (as highlighted in image).
         * 
         * Step5 Count unique news agencies found in step4 if the count is < 3 print
         * "Missing Leading News Agencies" else print total number of unique news
         * agencies found along with their names.
         */
        private WebDriver driver;
        private WebDriverWait wait;

        @BeforeSuite
        public void setUp() {
                String chromedriverpath = FileSystems
                                .getDefault()
                                .getPath("./src/test/java/exm/pursuit/drivers/chromedriver.exe").normalize()
                                .toAbsolutePath().toString();
                System.setProperty("webdriver.chrome.driver", chromedriverpath);

                driver = new ChromeDriver();
                driver.manage().window().maximize();
                driver.manage().timeouts().implicitlyWait(
                                Duration.ofSeconds(20));
                driver.manage().timeouts().pageLoadTimeout(
                                Duration.ofSeconds(20));
                driver.manage().timeouts().scriptTimeout(
                                Duration.ofSeconds(10));
                wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        }

        @Test
        public void seleniumAssignment() {
                driver.navigate().to("https://www.google.com");
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("q"))).sendKeys("COVID-19");

                wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[value='Google Search']")))
                                .click();
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[.='News']"))).click();
                wait.until(ExpectedConditions
                                .numberOfElementsToBeMoreThan(
                                                By.cssSelector("div#search a.WlydOe div.CEMjEf span"), 1));
                List<String> newsAgencies = driver.findElements(
                                By.cssSelector("div#search a.WlydOe div.CEMjEf span"))
                                .stream()
                                .map(element -> element.getText())
                                .collect(Collectors.toList());
                long totalUniqueAgencies = newsAgencies.stream().distinct().count();
                if (totalUniqueAgencies < 3) {
                        System.out.println("Missing Leading News Agencies");
                        return;
                }
                System.out.println("Total Unique Agencies Found: " + totalUniqueAgencies);
                newsAgencies.stream().distinct().forEach(System.out::println);
        }

        @AfterSuite
        public void tearDown() {
                driver.quit();
        }
}
