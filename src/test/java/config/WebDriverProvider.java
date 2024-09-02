package config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.function.Supplier;

public class WebDriverProvider implements Supplier<WebDriver> {

    public WebDriverConfig config;

    public WebDriverProvider() {
        config = new WebDriverConfig();
    }
@Override
    public WebDriver get() {
    WebDriver driver = createWebDriver();
    driver.get(config.getBaseUrl());
    return driver;
}
private WebDriver createWebDriver() {

    if (config.getBrowser().equals(Browser.FIREFOX)) {
        return new FirefoxDriver();
    }
    if (config.getBrowser().equals(Browser.CHROME)) {
        return new ChromeDriver();
    }
    throw new NullPointerException ("No brows");
}
}
