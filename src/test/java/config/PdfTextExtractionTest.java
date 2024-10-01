package config;

import com.codeborne.selenide.Selenide;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;

    public class PdfTextExtractionTest {

        @Test
        public void testPdfContainsText() throws IOException {
            // Открываем страницу с печатью
            open("chrome://print");

            // Даем время загрузиться
            sleep(3000); // Можно заменить на WebDriverWait для более надежного кода

            // Находим элемент embed
            String pdfUrl = $("embed[type='application/x-google-chrome-pdf']").getAttribute("src");

            // Загружаем PDF-документ
            PDDocument document = PDDocument.load(new File("путь_к_вашему_документу.pdf"));
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);

            // Закрываем документ
            document.close();

            // Проверяем, что текст "статья 165" содержится в извлеченном тексте
            assertTrue(text.contains("статья 165"));
        }
    }

