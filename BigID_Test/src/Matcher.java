

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;



public class Matcher extends Thread {

    private StringBuffer stringBuffer;
    private static final String[] wordsToFind = {"James", "John", "Robert", "Michael", "William", "David", "Richard", "Charles", "Joseph", "Thomas", "Christopher", "Daniel", "Paul", "Mark", "Donald", "George", "Kenneth", "Steven", "Edward", "Brian", "Ronald", "Anthony", "Kevin", "Jason", "Matthew", "Gary", "Timothy", "Jose", "Larry", "Jeffrey", "Frank", "Scott", "Eric", "Stephen", "Andrew", "Raymond", "Gregory", "Joshua", "Jerry", "Dennis", "Walter", "Patrick", "Peter", "Harold", "Douglas", "Henry", "Carl", "Arthur", "Ryan", "Roger"};
    private ArrayList<Match> matches = new ArrayList<Match>();
    private int lineOffset;

    public Matcher(StringBuffer currentStringBuffer, int lineOffset){
        this.stringBuffer = currentStringBuffer;
        this.lineOffset = lineOffset;
//        run();

    }

    public Matcher(){}

    @Override
    public void run() {

            String currentWord;
            System.out.println(Thread.currentThread().getName());
            for (int i = 0; i < wordsToFind.length; i++) {
                currentWord = wordsToFind[i];
                Pattern p = Pattern.compile(currentWord);//, Pattern.CASE_INSENSITIVE);
                java.util.regex.Matcher m = p.matcher(stringBuffer);
                // indicate all matches in the text
                while (m.find()) {
                    matches.add(new Match(currentWord,m.start(),lineOffset));
                }
            }
    }


    public void addMatch (Match match){
        matches.add(match);
    }
    public ArrayList<Match> getMatches() {
        return this.matches;
    }

    public int getLineOffset () {
        return this.lineOffset;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        if (!matches.isEmpty()){
            s.append(matches.get(0).getWord());
            s.append("("+matches.size()+")");
            s.append("--> [");
            for (Match match:matches) {
                s.append("[lineOffSet="+ match.getLineOffset() + ", charOffSet=" + match.getLocation()+"]");
            }
            s.append("]");
            return s.toString()+ "\n";
        }
        return "";
    }
}
