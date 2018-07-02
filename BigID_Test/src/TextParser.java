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

    Aggregator aggregator;
    Scanner scanner;

//    public static void main(String[] args) throws Exception {
//        TextParser tp = new TextParser();
//
//        Scanner s = ScannerFromFile();
//        //BufferedReader br = BufferedReaderFromFile();
//        String currentLine;
//        StringBuffer stringBuffer = new StringBuffer();
//        for (int i = 0; i < 2000; i++) {
//            s.nextLine();
//        }
//        int count = 1;
//        while (s.hasNext()) {
//
//            stringBuffer = new StringBuffer();
//            for (int i = 0; i < 1000; i++) {
//                if (!s.hasNext())
//                    break;
//                currentLine = s.nextLine();
//                stringBuffer.append(currentLine);//.toLowerCase());
//            }
//            count++;
//            Matcher m = new Matcher(stringBuffer, count*1000);
//            tp.aggregator.AddMatcher(m);
//        }
//
//        System.out.println(tp.aggregator);
//    }
    public void runWordFinder () {
        //BufferedReader br = BufferedReaderFromFile();
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);

        String currentLine;
        StringBuffer stringBuffer = new StringBuffer();
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
                stringBuffer.append(currentLine);//.toLowerCase());
            }
            count++;
            Matcher m = new Matcher(stringBuffer, count*1000);
            executor.execute(m);
//            m.start();
//            System.out.println(Thread.currentThread().toString());
            aggregator.AddMatcher(m);
        }
        executor.shutdown();

        aggregator.populateMatchersArray();
//        System.out.println(aggregator);
    }

    public TextParser (URL url) throws Exception {
        this.aggregator = new Aggregator();
        this.scanner = new Scanner(url.openStream());
    }

    private static Scanner ScannerFromFile() throws Exception
    {
        URL url = new URL("http://norvig.com/big.txt");
        Scanner s = new Scanner(url.openStream());

        return s;
    }
}
