package lexer;

public class InvalidCharacterException extends RuntimeException {
    public InvalidCharacterException(char c, int line) {
        super("Invalid character: " + c + " at line " + line);
    }
}
