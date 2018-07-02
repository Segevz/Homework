public class Match {

    private String word;
    private int location;
    private int lineOffset;

    public Match(String word, int location, int lineOffset) {
        this.word = word;
        this.location = location;
        this.lineOffset = lineOffset;
    }

    public String getWord() {
        return this.word;
    }

    public int getLocation() {
        return this.location;
    }

    public int getLineOffset() {
        return this.lineOffset;
    }

    @Override
    public String toString() {
        return "[LineOffset: " + lineOffset + ", charOffset: " + location + "]";
    }
}
