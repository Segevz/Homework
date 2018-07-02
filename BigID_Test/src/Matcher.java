import java.util.ArrayList;
import java.util.regex.Pattern;


public class Matcher extends Thread {

    private StringBuffer stringBuffer;
    private String[] wordsToFind;
    private ArrayList<Match> matches = new ArrayList<Match>();
    private int lineOffset;

    public Matcher(StringBuffer currentStringBuffer, int lineOffset, String[] wordsToFind) {
        this.stringBuffer = currentStringBuffer;
        this.lineOffset = lineOffset;
        this.wordsToFind = wordsToFind;
    }

    @Override
    public void run() {
        String currentWord;

        for (int i = 0; i < wordsToFind.length; i++) {
            currentWord = wordsToFind[i];
            Pattern p = Pattern.compile(currentWord);
            java.util.regex.Matcher m = p.matcher(stringBuffer);

            // indicate all matches in the text
            while (m.find()) {
                matches.add(new Match(currentWord, m.start(), lineOffset));
            }
        }
    }

    public ArrayList<Match> getMatches() {
        return this.matches;
    }
}
