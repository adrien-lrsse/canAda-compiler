package lexer;

public class Symbol extends Token {
    public final char value;

    public Symbol(char value) {
        super(Tag.SYMBOL);
        this.value = value;
    }

    @Override
    public String getStringValue() {
        return String.valueOf(value);
    }

    @Override
    public String toString() {

        return "Symbol{" + tag + ", " + value + "}";
    }
}
