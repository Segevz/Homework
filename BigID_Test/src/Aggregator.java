import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Aggregator {
    private ArrayList<Matcher> allMatchers;
    private ArrayList<Match>[] allMatchesListByWord;
    private String[] wordsToFind;
    private Map<String, Integer> dictionary = new HashMap<String, Integer>();

    public Aggregator(String[] wordsToFind) {
        allMatchers = new ArrayList<Matcher>();
        this.wordsToFind = wordsToFind;
        buildDictionary();
        buildCleanMatchersArray();
    }

    private void buildDictionary() {
        for (int i = 0; i < wordsToFind.length; i++) {
            dictionary.put(wordsToFind[i], i);
        }
    }

    public void buildCleanMatchersArray() {
        allMatchesListByWord = new ArrayList[dictionary.size()];
        for (int i = 0; i < allMatchesListByWord.length; i++) {
            allMatchesListByWord[i] = new ArrayList<Match>();
        }
    }

    public void populateMatchersArray() {
        int index;
        for (Matcher matcher : allMatchers) {
            for (Match match : matcher.getMatches()) {
                index = dictionary.get(match.getWord());
                allMatchesListByWord[index].add(match);
            }
        }
    }

    public void AddMatcher(Matcher matcher) {
        this.allMatchers.add(matcher);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (ArrayList<Match> list : allMatchesListByWord) {
            if (list.size() > 0) {
                s.append(list.get(0).getWord() + "--> [");
                for (int i = 0; i < list.size() - 1; i++) {
                    s.append(list.get(i) + ",");
                }
                //Add last match to StringBuilder (without ",")
                s.append(list.get(list.size() - 1));
                s.append("]\n");
            }
        }
        return s.toString();
    }
}
