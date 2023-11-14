package lexer;

public class IntegerOverflowException extends RuntimeException {
    public IntegerOverflowException(int line) {
        super("Integer overflow at line " + line);
    }
}
