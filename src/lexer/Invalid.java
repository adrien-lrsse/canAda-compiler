package lexer;

public class Invalid extends Token {

    public Invalid() {
        super(Tag.INVALID);
    }

    @Override
    public String toString() {
        return "Invalid{" + tag + "}";
    }

}
