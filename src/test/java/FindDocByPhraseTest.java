import config.WebDriverProvider;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class FindDocByPhraseTest {

    private WebDriver driver = new WebDriverProvider().get();

@Test
    public void findDocByPhrase() throws InterruptedException {
    WebElement searchBox = driver.findElement(By.cssSelector("input.x-input__field.x-search-box__input"));
    searchBox.sendKeys("нк ч2");
    WebElement searchBox2 = driver.findElement(By.cssSelector(".x-button"));
    searchBox2.click();
    try {
        Thread.sleep(3000);
    } catch (InterruptedException e) {
        throw new RuntimeException(e);
    }
    assertThat(driver.findElement(By.cssSelector(".x-list.x-page-search-plus-results__list")));
    driver.quit();
}
@Test
public void findDocByPhrase2  () {
    open("http://base.consultant.ru/cons/");
      $(".x-input__field").setValue("нк ч2").pressEnter();
    try {
        Thread.sleep(3000);
    } catch (InterruptedException e) {
        throw new RuntimeException(e);
    }
    assertThat($(".x-list").isDisplayed());
    driver.quit();
 }
}
