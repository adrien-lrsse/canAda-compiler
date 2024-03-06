package lexer;

public class AsciiPrintableCharacters {

    public static boolean isLetter(int c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    public static boolean isDigit(int c) {
        return c >= '0' && c <= '9';
    }

    public static boolean isLetterOrDigit(int c) {
        return isLetter(c) || isDigit(c);
    }

    public static boolean isAsciiPrintable(int c) {
        return c >= 32 && c <= 126;
    }
}
