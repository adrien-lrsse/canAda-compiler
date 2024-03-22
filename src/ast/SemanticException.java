package ast;

import java.io.IOException;

public class SemanticException extends IOException {
    public SemanticException(String message, int line) {
        super(message + ((line == -1) ? "" : " (line " + line + ")"));
    }
}
