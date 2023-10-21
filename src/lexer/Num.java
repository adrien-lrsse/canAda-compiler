package lexer;

public class Num extends Token {
    public final int value;

    public Num(int value) {
        super(Tag.NUMCONST);
        this.value = value;
    }

    @Override
    public String toString() {

        return "Num{" + tag + ", " + value + "}";
    }
}
