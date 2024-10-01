package config;

public class WebDriverConfig {


    public Browser getBrowser() {
        return Browser.CHROME;
    }

    public String getBaseUrl () {
        return "http://base.consultant.ru/cons/";
    }
}
