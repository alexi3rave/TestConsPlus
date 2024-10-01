import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.logevents.SelenideLogger;
import config.ReportConfig;
import io.qameta.allure.selenide.AllureSelenide;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class FindDocByPhraseTest extends ReportConfig {

    private long startTime =0;
    private long endTime=0;

@Test
public void findDocByPhrase2  () {

    SelenideLogger.addListener("allure", new AllureSelenide());
    step("Выполнить поиск документов по фразе «нк ч2»", () -> {
        open ("http://base.consultant.ru/cons/");
        $(".x-input__field").setValue("нк ч2").pressEnter();
        assertThat($(".x-list").shouldBe(visible));
       });
    step("Открыть первый документ из списка результатов поиска", () -> {
        $(".x-page-search-plus__results--see-also-margin.fontResizeIndex3 div.x-list.x-page-search-plus-results__list div.x-list__inner div.x-page-components-search-result-item.x-page-search-plus-results-item.lcLAW.x-page-components-search-result-item--current div.x-page-components-search-result-item__content.x-page-components-search-result-item__content--current.x-page-components-search-result-item__content--adts").click();
        startTime = System.currentTimeMillis();
        switchTo().window(1);
        switchTo().frame($("#mainContent > div.textContainer.visible > iframe"));
        assertThat($(byText("Статья 145")).exists());
        endTime = System.currentTimeMillis();

 });
    step("Проверка времени открытия документа", () -> {
        long loadTime = endTime - startTime;
        System.out.println(loadTime);
        assertThat(loadTime).isLessThanOrEqualTo(10000);
        });
    step("Проверить, что открылся правильный документ по наличию текста «налоговый кодекс» и текста «часть вторая»", () -> {
       assertThat(title().toLowerCase()).contains("налоговый кодекс", "часть вторая");
       assertThat($("#z0").getText().toLowerCase()).contains("налоговый кодекс", "часть вторая");
    });
    step("Проверить, что в панели «Поиск в тексте» находится поисковая фраза «нк ч2»", () -> {
        switchTo().defaultContent();
        assertThat($("#dictFilter").getValue().contains("нк ч2"));
           });
    step("Открыть оглавление, выполнить поиск по оглавлению «статья 163», перейти по оглавлению на эту статью в тексте документа", () -> {

        $("#mainContent > div.contextPanel > table > tbody > tr > td.buttonsPanel > div > button.contents").click();
        $("#page2 > div.pageContent > div > div.contentsBar > div.yellow > div > div > div.x-combo-box > div > input").setValue("статья 163").pressEnter();
        $("#segm1").click();
        switchTo().frame($("#mainContent > div.textContainer.visible > iframe"));
        assertThat($("#p1859").getText().toLowerCase()).contains("налоговый период","налоговые агенты");

    });
    step("Выделить в тексте документа весь текст статьи 163, нажать кнопку «Печать» (с иконкой в виде принтера) на верхней панели", () -> {

        WebElement webElement1 = $("#p1854").getWrappedElement();
        WebElement webElement2 = $("#p1855").getWrappedElement();
        WebElement webElement3 = $("#p1856").getWrappedElement();
        WebElement webElement4 = $("#p1859").getWrappedElement();

        // Выполнить выделение текста с помощью JavaScript
        String selectedText = (String)executeJavaScript(
                "var range = document.createRange();" +
                        "range.setStartBefore(arguments[0]);" + // Начинаем перед первым элементом
                        "range.setEndAfter(arguments[3]);" + // Кончаем после последнего элемента
                        "var sel = window.getSelection();" +
                        "sel.removeAllRanges();" + // Убираем предыдущие выделения
                        "sel.addRange(range);"+
                        "return sel.toString();", // Возвращаем выделенный текст
                webElement1,
                webElement2,
                webElement3,
                webElement4
        );
switchTo().defaultContent();
$("#mainContent > div.contextPanel > table > tbody > tr > td.contextToolbar > button.print").click();
/// Даем время загрузиться, можно использовать WebDriverWait для лучшей практики
        sleep(3000);
        switchTo().window(2);
        // Получаем элемент хоста Shadow DOM
        SelenideElement shadowHost = $("print-preview-app");

        Boolean hasShadowRoot = (Boolean) executeJavaScript("return !!arguments[0].shadowRoot;", shadowHost);
        System.out.println("Has shadowRoot: " + hasShadowRoot);

        // Получаем элемент previewArea из первого уровня Shadow DOM
        WebElement previewArea = (WebElement) executeJavaScript(
                "return arguments[0].shadowRoot.querySelector('print-preview-preview-area');",
                shadowHost
        );
        Boolean hasShadowRoot2 = (Boolean) executeJavaScript("return !!arguments[0].shadowRoot;", previewArea);
        System.out.println("Has shadowRoot2: " + hasShadowRoot2);

                // Получаем iframe pdf-viewer из второго уровня Shadow DOM
        WebElement pdfViewerIframeWebElement = (WebElement) executeJavaScript(
                "return arguments[0].shadowRoot.querySelector('#pdf-viewer');",
                previewArea
        );

        //Возвращаемся в Селенид
        SelenideElement pdfViewerIframe = $(pdfViewerIframeWebElement);
//Ищем ссылку на пдф
        String findSrc = pdfViewerIframe.getAttribute("src");
        // Регулярное выражение для поиска нужной части
        Pattern pattern = Pattern.compile("chrome-untrusted://[^\"]+");
        Matcher matcher = pattern.matcher(findSrc);



        // Проверяем, найдено ли совпадение
        if (matcher.find()) {
            String extractedPart = matcher.group();// Извлекаем подстроку, получаем URL PDF
            System.out.println("Извлеченная часть: " + extractedPart);

            Selenide.open(extractedPart);
            sleep(3000); // Можно добавить явное ожидание, если необходимо

// Скачиваем (находим?) PDF файл
            File pdfFile = new File(String.valueOf(LastPdfFinder.findLastPdf()));
            System.out.println(pdfFile);

            // Извлечение текста из PDF-файла
            try (PDDocument document = PDDocument.load(pdfFile)) {
                String text = new PDFTextStripper().getText(document);

                // Проверка наличия контрольного слова
                assertThat(text.equals(selectedText));

            }

            // Удалить временный файл
            pdfFile.delete();}
        else {
            throw new IOException("Не удалось найти PDF URL в embed элементе.");
        }


    });


    /*closeWebDriver();*/
}}
