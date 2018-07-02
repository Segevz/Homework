import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Aggregator {
    private ArrayList<Matcher> allMatchers = new ArrayList<Matcher>();
    private Matcher[] allMatchesInFile;
    private static String[] wordsToFind = {"James", "John", "Robert", "Michael", "William", "David", "Richard", "Charles", "Joseph", "Thomas", "Christopher", "Daniel", "Paul", "Mark", "Donald", "George", "Kenneth", "Steven", "Edward", "Brian", "Ronald", "Anthony", "Kevin", "Jason", "Matthew", "Gary", "Timothy", "Jose", "Larry", "Jeffrey", "Frank", "Scott", "Eric", "Stephen", "Andrew", "Raymond", "Gregory", "Joshua", "Jerry", "Dennis", "Walter", "Patrick", "Peter", "Harold", "Douglas", "Henry", "Carl", "Arthur", "Ryan", "Roger"};
    private Map<String, Integer> dictionary = new HashMap<String, Integer>();

    public Aggregator() {
        allMatchers = new ArrayList<Matcher>();
        buildDictionary();
        buildCleanMatchersArray();

    }

    private void buildDictionary() {
        for (int i = 0; i < wordsToFind.length; i++) {
            dictionary.put(wordsToFind[i], i);
        }
    }

    public void buildCleanMatchersArray() {
        allMatchesInFile = new Matcher[dictionary.size()];
        for (int i = 0; i < allMatchesInFile.length; i++) {
            allMatchesInFile[i] = new Matcher();
        }
    }

    public void populateMatchersArray() {
        int index;
        for (Matcher matcher : allMatchers) {
            for (Match match : matcher.getMatches()) {
                index = dictionary.get(match.getWord());
                allMatchesInFile[index].addMatch(match);
            }
        }
    }

    public void AddMatcher(Matcher matcher) {
        this.allMatchers.add(matcher);
    }

    public ArrayList<Matcher> getAllMatchers() {
        return this.allMatchers;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Matcher matcher : allMatchesInFile) {
            s.append(matcher);
        }
        return s.toString();
    }
}
