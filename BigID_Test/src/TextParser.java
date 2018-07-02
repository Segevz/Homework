import javax.xml.soap.Text;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class TextParser {

    public static final int MAX_AMOUNT_OF_THREADS = 5;

    private Aggregator aggregator;
    private Scanner scanner;

    public void runWordFinder() {

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(MAX_AMOUNT_OF_THREADS);

        String currentLine;
        StringBuffer stringBuffer;

        //Skips first 2000 rows
        for (int i = 0; i < 2000; i++) {
            scanner.nextLine();
        }
        int count = 1;
        while (scanner.hasNext()) {

            stringBuffer = new StringBuffer();
            for (int i = 0; i < 1000; i++) {
                if (!scanner.hasNext())
                    break;
                currentLine = scanner.nextLine();
                stringBuffer.append(currentLine);
            }
            count++;
            Matcher m = new Matcher(stringBuffer, count * 1000);
            executor.execute(m);
            aggregator.AddMatcher(m);
        }
        executor.shutdown();
        aggregator.populateMatchersArray();

        System.out.println(aggregator);
    }

    public TextParser(URL url) throws Exception {
        this.aggregator = new Aggregator();
        this.scanner = new Scanner(url.openStream());
    }
}
