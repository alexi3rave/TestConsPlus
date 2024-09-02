package config;

public class WebDriverConfig {

    public Browser getBrowser() {
        return Browser.FIREFOX;
    }

    public String getBaseUrl () {
        return "http://base.consultant.ru/cons/";
    }
}
