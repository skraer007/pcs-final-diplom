import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {

    private Map<String, List<PageEntry>> wordOnPage = new HashMap<>();
    //???

    public BooleanSearchEngine(File pdfsDir) throws IOException {
        for (File pdf : Objects.requireNonNull(pdfsDir.listFiles())) {
            var doc = new PdfDocument(new PdfReader(pdf));
            int numberOfPages = doc.getNumberOfPages();
            for (int i = 0; i < numberOfPages; i++) {
                Map<String, Integer> freqs = new HashMap<>();
                var page = doc.getPage(i + 1);
                var text = PdfTextExtractor.getTextFromPage(page);
                var words = text.split("\\P{IsAlphabetic}+");
                for (var word : words) { // перебираем слова
                    if (word.isEmpty()) {
                        continue;
                    }
                    word = word.toLowerCase();
                    freqs.put(word, freqs.getOrDefault(word, 0) + 1);
                }
                for (var word : freqs.keySet()) {
                    if (word.isEmpty()) {
                        continue;
                    }
                    word = word.toLowerCase();
                    PageEntry pageEntry = new PageEntry(pdf.getName(), i + 1, freqs.get(word));
                    if (wordOnPage.containsKey(word)) {
                        List<PageEntry> updateList = new ArrayList<>(wordOnPage.get(word));
                        updateList.add(pageEntry);
                        Collections.sort(updateList);
                        wordOnPage.put(word, updateList);
                    } else {
                        List<PageEntry> list = new ArrayList<>();
                        list.add(pageEntry);
                        wordOnPage.put(word, list);
                    }
                }
            }
        }
        // прочтите тут все pdf и сохраните нужные данные,
        // тк во время поиска сервер не должен уже читать файлы
    }

    @Override
    public List<PageEntry> search(String word) {
        String wordToLowerCase = word.toLowerCase();
        return wordOnPage.getOrDefault(wordToLowerCase, Collections.emptyList());
        // тут реализуйте поиск по слову
    }
}