import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class TextParser {

    public static final int MAX_AMOUNT_OF_THREADS = 5;
    public static final int LINES_TO_READ = 1000;

    private Aggregator aggregator;
    private Scanner scanner;
    private final String[] wordsToFind;

    public void runWordFinder() {
        String currentLine;
        StringBuffer stringBuffer;
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(MAX_AMOUNT_OF_THREADS);

        //Skips first 2000 rows
        for (int i = 0; i < 2000; i++) {
            scanner.nextLine();
        }
        int bulkCounter = 1;
        while (scanner.hasNext()) {
            stringBuffer = new StringBuffer();
            for (int i = 0; i < LINES_TO_READ; i++) {
                if (!scanner.hasNext())
                    break;
                currentLine = scanner.nextLine();
                stringBuffer.append(currentLine);
            }

            bulkCounter++;
            Matcher m = new Matcher(stringBuffer, bulkCounter * LINES_TO_READ, wordsToFind);
            executor.execute(m);
            aggregator.AddMatcher(m);
        }

        executor.shutdown();
        aggregator.populateMatchersArray();

        System.out.println(aggregator);
    }

    public TextParser(URL url, String[] wordsToFind) throws Exception {
        this.aggregator = new Aggregator(wordsToFind);
        this.scanner = new Scanner(url.openStream());
        this.wordsToFind = wordsToFind;
    }

    public String[] getWordsToFind() {
        return this.wordsToFind;
    }
}
