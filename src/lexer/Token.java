package lexer;

public class Token {
    protected final int tag;

    public Token(int tag) {
        this.tag = tag;
    }

    public int getTag() {
        return tag;
    }

    @Override
    public String toString() {
        return "<" + tag + ">";
    }
}
