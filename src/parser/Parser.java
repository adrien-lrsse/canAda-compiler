package parser;

import lexer.Lexer;
import lexer.Tag;

import java.util.Stack;

public class Parser {
    Stack<Tag> stack = new Stack<Tag>();
    Lexer lexer;
    public Parser(Lexer lexer){
        this.lexer = lexer;
    }
}
