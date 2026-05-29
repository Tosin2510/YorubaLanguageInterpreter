public class Token {
    private final String word;
    private final PartOfSpeech pos;
    private final String phonetic;

    public Token(String word, PartOfSpeech pos, String phonetic) {
        this.word = word;
        this.pos = pos;
        this.phonetic = phonetic;
    }

    public String getWord() { return word; }
    public PartOfSpeech getPos() { return pos; }
    public String getPhonetic() { return phonetic; }
}