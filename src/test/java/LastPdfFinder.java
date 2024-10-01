import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;


public class LastPdfFinder {
        public static Path findLastPdf() throws IOException {
        // Используем Files.walk для обхода папки рекурсивно
        return Files.walk(Paths.get("C:\\Users\\user\\IdeaProjects\\TestConsPlus\\build\\downloads"))
                .filter(path -> path.toString().toLowerCase().endsWith(".pdf")) // Фильтруем только PDF
                .max(Comparator.comparingLong(path -> path.toFile().lastModified())) // Находим последний по времени изменения
                .orElse(null); // Возвращаем null, если PDF файлы не найдены
    }
}
