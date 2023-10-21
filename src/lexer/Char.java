package lexer;

public class Char extends Token {
    public final char value;

    public Char(char value) {
        super(Tag.CHARCONST);
        this.value = value;
    }

    @Override
    public String toString() {

        return "Char{" + tag + ", " + value + "}";
    }
}
