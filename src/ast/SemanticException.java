package ast;

import java.io.IOException;

public class SemanticException extends IOException {
    public SemanticException(String message) {
        super(message);
    }
}
