import java.net.URL;
import java.util.Scanner;

public class Main {

    public static final String[] wordsToFind = {"James", "John", "Robert", "Michael", "William", "David", "Richard", "Charles", "Joseph", "Thomas", "Christopher", "Daniel", "Paul", "Mark", "Donald", "George", "Kenneth", "Steven", "Edward", "Brian", "Ronald", "Anthony", "Kevin", "Jason", "Matthew", "Gary", "Timothy", "Jose", "Larry", "Jeffrey", "Frank", "Scott", "Eric", "Stephen", "Andrew", "Raymond", "Gregory", "Joshua", "Jerry", "Dennis", "Walter", "Patrick", "Peter", "Harold", "Douglas", "Henry", "Carl", "Arthur", "Ryan", "Roger"};


    public static void main(String[] args) throws Exception {
        URL url = new URL("http://norvig.com/big.txt");
        TextParser tp = new TextParser(url);

        tp.runWordFinder();
//        System.out.println(tp.aggregator);
    }
}
