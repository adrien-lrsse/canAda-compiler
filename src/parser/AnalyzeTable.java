package parser;

import lexer.*;

import java.awt.desktop.ScreenSleepEvent;
import java.io.IOException;
import java.util.EmptyStackException;
import java.util.Objects;

public class AnalyzeTable {

    public Parser parser;
    public Token current;
    private boolean checkIdent = false;

    public AnalyzeTable(Parser parser) {
        this.parser = parser;
    }

    public void analyze() throws IOException {
        current = parser.lexer.scan();
         try {
            this.ficher();
         } catch (Error e) {
             parser.ast.close();
             throw new Error("Error line " + parser.lexer.getLine() + " : expected <" + Tag.EOF + " 'EOF'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
         }
        int temp = parser.stack.pop();
        if (temp != Tag.FICHIER) {
            throw new Error("Reduction/Stack error : expected <" + Tag.FICHIER + "> but found <" + current.getTag() + "> at line " + parser.lexer.getLine() + " '" + current.getStringValue() + "'");
        }
        parser.ast.close();
    }

    private void ficher() throws IOException {
        //FICHIER ::= with Ada . Text_IO ; use Ada . Text_IO ; PROCEDURE BEGIN_INSTRUCTION ; EOF (lecture de with)
        parser.ast.addNode("ROOT");
        parser.ast.addEdge(parser.ast.lastNode, parser.ast.addNode("PROCEDURE"));
        parser.ast.buffer.push(parser.ast.lastNode);
        if (current.getTag() == Tag.WITH) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            if ((current.getTag() == Tag.ID) && (current.getStringValue().equals("ada"))) {
                parser.stack.push(current.getTag());
                current = parser.lexer.scan();
                if ((current.getTag() == Tag.SYMBOL) && (current.getStringValue().equals("."))) {
                    parser.stack.push(current.getTag());
                    current = parser.lexer.scan();
                    if ((current.getTag() == Tag.ID) && (current.getStringValue().equals("text_io"))) {
                        parser.stack.push(current.getTag());
                        current = parser.lexer.scan();
                        if ((current.getTag() == Tag.SYMBOL) && (current.getStringValue().equals(";"))) {
                            parser.stack.push(current.getTag());
                            current = parser.lexer.scan();
                            if (current.getTag() == Tag.USE) {
                                parser.stack.push(current.getTag());
                                current = parser.lexer.scan();
                                if ((current.getTag() == Tag.ID) && (current.getStringValue().equals("ada"))) {
                                    parser.stack.push(current.getTag());
                                    current = parser.lexer.scan();
                                    if ((current.getTag() == Tag.SYMBOL) && (current.getStringValue().equals("."))) {
                                        parser.stack.push(current.getTag());
                                        current = parser.lexer.scan();
                                        if ((current.getTag() == Tag.ID) && (current.getStringValue().equals("text_io"))) {
                                            parser.stack.push(current.getTag());
                                            current = parser.lexer.scan();
                                            if ((current.getTag() == Tag.SYMBOL) && (current.getStringValue().equals(";"))) {
                                                parser.stack.push(current.getTag());
                                                current = parser.lexer.scan();
                                                this.procedure();
                                                this.begin_instruction();
                                                if ((current.getTag() == Tag.SYMBOL) && (current.getStringValue().equals(";"))) {
                                                    parser.stack.push(current.getTag());
                                                    current = parser.lexer.scan();
                                                    if (current.getTag() == Tag.EOF) {
                                                        parser.stack.push(current.getTag());
                                                        current = parser.lexer.scan();
                                                        // verify stack and replace with FICHIER
                                                        int temp = parser.stack.pop();
                                                        if (temp == Tag.EOF) {
                                                            temp = parser.stack.pop();
                                                            if (temp == Tag.SYMBOL) {
                                                                temp = parser.stack.pop();
                                                                if (temp == Tag.BEGIN_INSTRUCTION) {
                                                                    temp = parser.stack.pop();
                                                                    if (temp == Tag.NT_PROCEDURE) {
                                                                        temp = parser.stack.pop();
                                                                        if (temp == Tag.SYMBOL) {
                                                                            temp = parser.stack.pop();
                                                                            if (temp == Tag.ID) {
                                                                                temp = parser.stack.pop();
                                                                                if (temp == Tag.SYMBOL) {
                                                                                    temp = parser.stack.pop();
                                                                                    if (temp == Tag.ID) {
                                                                                        temp = parser.stack.pop();
                                                                                        if (temp == Tag.USE) {
                                                                                            temp = parser.stack.pop();
                                                                                            if (temp == Tag.SYMBOL) {
                                                                                                temp = parser.stack.pop();
                                                                                                if (temp == Tag.ID) {
                                                                                                    temp = parser.stack.pop();
                                                                                                    if (temp == Tag.SYMBOL) {
                                                                                                        temp = parser.stack.pop();
                                                                                                        if (temp == Tag.ID) {
                                                                                                            temp = parser.stack.pop();
                                                                                                            if (temp == Tag.WITH) {
                                                                                                                parser.stack.push(Tag.FICHIER);
                                                                                                                parser.ast.buffer.pop();
                                                                                                            } else {
                                                                                                                throw new Error("Reduction/Stack error : expected <" + Tag.WITH + "> but found <" + temp + ">");
                                                                                                            }
                                                                                                        } else {
                                                                                                            throw new Error("Reduction/Stack error : expected <" + Tag.SYMBOL + "> but found <" + temp + ">");
                                                                                                        }
                                                                                                    } else {
                                                                                                        throw new Error("Reduction/Stack error : expected <" + Tag.ID + "> but found <" + temp + ">");
                                                                                                    }
                                                                                                } else {
                                                                                                    throw new Error("Reduction/Stack error : expected <" + Tag.ID + "> but found <" + temp + ">");
                                                                                                }
                                                                                            } else {
                                                                                                throw new Error("Reduction/Stack error : expected <" + Tag.SYMBOL + "> but found <" + temp + ">");
                                                                                            }
                                                                                        } else {
                                                                                            throw new Error("Reduction/Stack error : expected <" + Tag.USE + "> but found <" + temp + ">");
                                                                                        }
                                                                                    } else {
                                                                                        throw new Error("Reduction/Stack error : expected <" + Tag.ID + "> but found <" + temp + ">");
                                                                                    }
                                                                                } else {
                                                                                    throw new Error("Reduction/Stack error : expected <" + Tag.SYMBOL + "> but found <" + temp + ">");
                                                                                }
                                                                            } else {
                                                                                throw new Error("Reduction/Stack error : expected <" + Tag.ID + "> but found <" + temp + ">");
                                                                            }
                                                                        } else {
                                                                            throw new Error("Reduction/Stack error : expected <" + Tag.SYMBOL + "> but found <" + temp + ">");
                                                                        }
                                                                    } else {
                                                                        throw new Error("Reduction/Stack error : expected <" + Tag.NT_PROCEDURE + "> but found <" + temp + ">");
                                                                    }
                                                                } else {
                                                                    throw new Error("Reduction/Stack error : expected <" + Tag.BEGIN_INSTRUCTION + "> but found <" + temp + ">");
                                                                }
                                                            } else {
                                                                throw new Error("Reduction/Stack error : expected <" + Tag.SYMBOL + "> but found <" + temp + ">");
                                                            }
                                                        } else {
                                                            throw new Error("Reduction/Stack error : expected <" + Tag.EOF + "> but found <" + current.getTag() + ">");
                                                        }
                                                    } else {
                                                        throw new Error("Error line " + parser.lexer.getLine() + " : expected <" + Tag.EOF + " 'EOF'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
                                                    }
                                                } else {
                                                    throw new Error("Error line " + parser.lexer.getLine() + " : expected <" + Tag.SYMBOL + " ';'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");

                                                }
                                            } else {
                                                throw new Error("Error line " + parser.lexer.getLine() + " : expected <" + Tag.SYMBOL + " ';'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
                                            }
                                        } else {
                                            throw new Error("Error line " + parser.lexer.getLine() + " : expected <" + Tag.ID + " 'text_io'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
                                        }
                                    } else {
                                        throw new Error("Error line " + parser.lexer.getLine() + " : expected <" + Tag.SYMBOL + " '.'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
                                    }
                                } else {
                                    throw new Error("Error line " + parser.lexer.getLine() + " : expected <" + Tag.ID + " 'ada'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
                                }
                            } else {
                                throw new Error("Error line " + parser.lexer.getLine() + " : expected <" + Tag.USE + " 'USE'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
                            }
                        } else {
                            throw new Error("Error line " + parser.lexer.getLine() + " : expected <" + Tag.SYMBOL + " ';'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
                        }
                    } else {
                        throw new Error("Error line " + parser.lexer.getLine() + " : expected <" + Tag.ID + " 'text_io'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
                    }
                } else {
                    throw new Error("Error line " + parser.lexer.getLine() + " : expected <" + Tag.SYMBOL + " '.'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
                }
            } else {
                throw new Error("Error line " + parser.lexer.getLine() + " : expected <" + Tag.ID + " 'ada'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
            }
        }
        else {
            throw new Error("Error line " + parser.lexer.getLine() + " : expected <" + Tag.WITH + " 'WITH'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
        }
    }

    private void procedure() throws IOException {
        //PROCEDURE ::= procedure ident is END_PROCEDURE (lecture de procedure)
        if (current.getTag() == Tag.PROCEDURE) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            if (current.getTag() == Tag.ID) {
                // semantic functions
                parser.ast.addEdge(parser.ast.buffer.lastElement(), parser.ast.addNode(((Word)current).lexeme));
                parser.ast.addEdge(parser.ast.buffer.lastElement(), parser.ast.addNode("DECLARATIONS"));
                parser.ast.buffer.push(parser.ast.lastNode);
                // end semantic functions
                parser.stack.push(current.getTag());
                current = parser.lexer.scan();
                if (current.getTag() == Tag.IS) {
                    parser.stack.push(current.getTag());
                    current = parser.lexer.scan();
                    this.end_procedure();
                    int temp = parser.stack.pop();
                    if (temp == Tag.END_PROCEDURE) {
                        temp = parser.stack.pop();
                        if (temp == Tag.IS) {
                            temp = parser.stack.pop();
                            if (temp == Tag.ID) {
                                temp = parser.stack.pop();
                                if (temp == Tag.PROCEDURE) {
                                    parser.stack.push(Tag.NT_PROCEDURE);
                                    // semantic functions
                                    parser.ast.buffer.pop();
                                    // end semantic functions
                                } else {
                                    throw new Error("Reduction/Stack error : expected <" + Tag.PROCEDURE + "> but found <" + temp + ">");
                                }
                            } else {
                                throw new Error("Reduction/Stack error : expected <" + Tag.ID + "> but found <" + temp + ">");
                            }
                        } else {
                            throw new Error("Reduction/Stack error : expected <" + Tag.IS + "> but found <" + temp + ">");
                        }
                    } else {
                        throw new Error("Reduction/Stack error : expected <" + Tag.END_PROCEDURE + "> but found <" + temp + ">");
                    }
                } else {
                    throw new Error("Error line " + parser.lexer.getLine() + " : expected <" + Tag.IS + " 'IS'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
                }
            } else {
                throw new Error("Error line " + parser.lexer.getLine() + " : expected <" + Tag.ID + " 'ident'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
            }
        }
        else {
            throw new Error("Error line " + parser.lexer.getLine() + " : expected <" + Tag.PROCEDURE + " 'procedure'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
        }
    }

    private void end_procedure() throws IOException {
        //END_PROCEDURE ::= GENERATE_DECLARATIONS (lecture de procedure)
        //END_PROCEDURE ::= GENERATE_DECLARATIONS (lecture de ident)
        //END_PROCEDURE ::= ε (lecture de begin)
        //END_PROCEDURE ::= GENERATE_DECLARATIONS (lecture de type)
        //END_PROCEDURE ::= GENERATE_DECLARATIONS (lecture de function)
        if ((current.getTag() == Tag.PROCEDURE) || (current.getTag() == Tag.ID) || (current.getTag() == Tag.TYPE) || (current.getTag() == Tag.FUNCTION)) {
            this.generate_declarations();
            int temp = parser.stack.pop();
            if (temp == Tag.GENERATE_DECLARATIONS) {
                parser.stack.push(Tag.END_PROCEDURE);
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.GENERATE_DECLARATIONS + "> but found <" + temp + ">");
            }
        }
        else if (current.getTag() == Tag.BEGIN) {
            parser.stack.push(Tag.END_PROCEDURE);
        }
        else {
            throw new Error("Error line " + parser.lexer.getLine() + " : expected <" + Tag.PROCEDURE + " 'procedure'> or <" + Tag.ID + " 'ident'> or <" + Tag.TYPE + " 'type'> or <" + Tag.FUNCTION + " 'function'> or <" + Tag.BEGIN + " 'begin'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
        }
    }

    private void begin_instruction() throws IOException {
        //BEGIN_INSTRUCTION ::= begin GENERATE_INSTRUCTIONS end END_BEGIN_INSTRUCTION (lecture de begin)
        if (current.getTag() == Tag.BEGIN) {
            // semantic functions
            parser.ast.addEdge(parser.ast.buffer.lastElement(), parser.ast.addNode("INSTRUCTIONS"));
            parser.ast.buffer.push(parser.ast.lastNode);
            // end semantic functions
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.generate_instructions();
            if (current.getTag() == Tag.END) {
                parser.stack.push(current.getTag());
                current = parser.lexer.scan();
                this.end_begin_instruction();
                int temp = parser.stack.pop();
                if (temp == Tag.END_BEGIN_INSTRUCTION) {
                    temp = parser.stack.pop();
                    if (temp == Tag.END) {
                        temp = parser.stack.pop();
                        if (temp == Tag.GENERATE_INSTRUCTIONS) {
                            temp = parser.stack.pop();
                            if (temp == Tag.BEGIN) {
                                parser.stack.push(Tag.BEGIN_INSTRUCTION);
                            } else {
                                throw new Error("Reduction/Stack error : expected <" + Tag.BEGIN + "> but found <" + temp + ">");
                            }
                        } else {
                            throw new Error("Reduction/Stack error : expected <" + Tag.GENERATE_INSTRUCTIONS + "> but found <" + temp + ">");
                        }
                    } else {
                        throw new Error("Reduction/Stack error : expected <" + Tag.END + "> but found <" + temp + ">");
                    }
                } else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.END_BEGIN_INSTRUCTION + "> but found <" + temp + ">");
                }
            } else {
                throw new Error("Error line " + parser.lexer.getLine() + " : expected <" + Tag.END + " 'end'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
            }
        }
        else {
            throw new Error("Error line " + parser.lexer.getLine() + " : expected <" + Tag.BEGIN + " 'begin'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
        }
    }

    //END_BEGIN_INSTRUCTION
    private void end_begin_instruction() throws IOException {
        //END_BEGIN_INSTRUCTION ::= ε (lecture de ;)
        //END_BEGIN_INSTRUCTION ::= ident (lecture de ident)
        if ((current.getTag() == Tag.SYMBOL) && (current.getStringValue().equals(";"))) {
            parser.stack.push(Tag.END_BEGIN_INSTRUCTION);
        }
        else if (current.getTag() == Tag.ID) {
            // semantic function
            parser.ast.addEdge(parser.ast.buffer.lastElement(), parser.ast.addNode(((Word)current).getStringValue()));
            // end semantic function
            current = parser.lexer.scan();
            parser.stack.push(Tag.END_BEGIN_INSTRUCTION);

        }
        else {
            throw new Error("Error line " + parser.lexer.getLine() + " : expected <" + Tag.SYMBOL + " ';'> or <" + Tag.ID + " 'ident'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
        }
    }

    //GENERATE_DECLARATIONS
    private void generate_declarations() throws IOException {
        //GENERATE_DECLARATIONS ::= DECLARATION GENERATE_DECLARATIONS_FACTORISATION (lecture de procedure)
        //GENERATE_DECLARATIONS ::= DECLARATION GENERATE_DECLARATIONS_FACTORISATION (lecture de ident)
        //GENERATE_DECLARATIONS ::= DECLARATION GENERATE_DECLARATIONS_FACTORISATION (lecture de type)
        //GENERATE_DECLARATIONS ::= DECLARATION GENERATE_DECLARATIONS_FACTORISATION (lecture de function)
        if ((current.getTag() == Tag.PROCEDURE) || (current.getTag() == Tag.ID) || (current.getTag() == Tag.TYPE) || (current.getTag() == Tag.FUNCTION)) {
            this.declaration();
            this.generate_declarations_factorisation();
            int temp = parser.stack.pop();
            if (temp == Tag.GENERATE_DECLARATIONS_FACTORISATION) {
                temp = parser.stack.pop();
                if (temp == Tag.DECLARATION) {
                    parser.stack.push(Tag.GENERATE_DECLARATIONS);
                } else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.DECLARATION + "> but found <" + temp + ">");
                }
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.GENERATE_DECLARATIONS_FACTORISATION + "> but found <" + temp + ">");
            }
        }
        else {
            throw new Error("Error line " + parser.lexer.getLine() + " : expected <" + Tag.PROCEDURE + " 'procedure'> or <" + Tag.ID + " 'ident'> or <" + Tag.TYPE + " 'type'> or <" + Tag.FUNCTION + " 'function'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
        }
    }

    //GENERATE_DECLARATIONS_FACTORISATION
    private void generate_declarations_factorisation() throws IOException {
        //GENERATE_DECLARATIONS_FACTORISATION ::= GENERATE_DECLARATIONS (lecture de procedure)
        //GENERATE_DECLARATIONS_FACTORISATION ::= GENERATE_DECLARATIONS (lecture de ident)
        //GENERATE_DECLARATIONS_FACTORISATION ::= ε (lecture de begin)
        //GENERATE_DECLARATIONS_FACTORISATION ::= GENERATE_DECLARATIONS (lecture de type)
        //GENERATE_DECLARATIONS_FACTORISATION ::= GENERATE_DECLARATIONS (lecture de function)
        if ((current.getTag() == Tag.PROCEDURE) || (current.getTag() == Tag.ID) || (current.getTag() == Tag.TYPE) || (current.getTag() == Tag.FUNCTION)) {
            this.generate_declarations();
            int temp = parser.stack.pop();
            if (temp == Tag.GENERATE_DECLARATIONS) {
                parser.stack.push(Tag.GENERATE_DECLARATIONS_FACTORISATION);
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.GENERATE_DECLARATIONS + "> but found <" + temp + ">");
            }
        }
        else if (current.getTag() == Tag.BEGIN) {
            parser.stack.push(Tag.GENERATE_DECLARATIONS_FACTORISATION);
        }
        else {
            throw new Error("Error line " + parser.lexer.getLine() + " : expected <" + Tag.PROCEDURE + " 'procedure'> or <" + Tag.ID + " 'ident'> or <" + Tag.TYPE + " 'type'> or <" + Tag.FUNCTION + " 'function'> or <" + Tag.BEGIN + " 'begin'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
        }
    }

    //GENERATE_INSTRUCTIONS
    private void generate_instructions() throws IOException {
        //GENERATE_INSTRUCTIONS ::= INSTRUCTION GENERATE_INSTRUCTIONS_FACTORISATION (lecture de ident)
        //GENERATE_INSTRUCTIONS ::= INSTRUCTION GENERATE_INSTRUCTIONS_FACTORISATION (lecture de begin)
        //GENERATE_INSTRUCTIONS ::= INSTRUCTION GENERATE_INSTRUCTIONS_FACTORISATION (lecture de ( )
        //GENERATE_INSTRUCTIONS ::= INSTRUCTION GENERATE_INSTRUCTIONS_FACTORISATION (lecture de return)
        //GENERATE_INSTRUCTIONS ::= INSTRUCTION GENERATE_INSTRUCTIONS_FACTORISATION (lecture de new)
        //GENERATE_INSTRUCTIONS ::= INSTRUCTION GENERATE_INSTRUCTIONS_FACTORISATION (lecture de character'val)
        //GENERATE_INSTRUCTIONS ::= INSTRUCTION GENERATE_INSTRUCTIONS_FACTORISATION (lecture de not)
        //GENERATE_INSTRUCTIONS ::= INSTRUCTION GENERATE_INSTRUCTIONS_FACTORISATION (lecture de entier)
        //GENERATE_INSTRUCTIONS ::= INSTRUCTION GENERATE_INSTRUCTIONS_FACTORISATION (lecture de caractere)
        //GENERATE_INSTRUCTIONS ::= INSTRUCTION GENERATE_INSTRUCTIONS_FACTORISATION (lecture de true)
        //GENERATE_INSTRUCTIONS ::= INSTRUCTION GENERATE_INSTRUCTIONS_FACTORISATION (lecture de false)
        //GENERATE_INSTRUCTIONS ::= INSTRUCTION GENERATE_INSTRUCTIONS_FACTORISATION (lecture de null)
        //GENERATE_INSTRUCTIONS ::= INSTRUCTION GENERATE_INSTRUCTIONS_FACTORISATION (lecture de if)
        //GENERATE_INSTRUCTIONS ::= INSTRUCTION GENERATE_INSTRUCTIONS_FACTORISATION (lecture de for)
        //GENERATE_INSTRUCTIONS ::= INSTRUCTION GENERATE_INSTRUCTIONS_FACTORISATION (lecture de while)
        if ((current.getTag() == Tag.ID) || (current.getTag() == Tag.BEGIN) || ((current.getTag() == Tag.SYMBOL) && (current.getStringValue().equals("("))) || (current.getTag() == Tag.RETURN) || (current.getTag() == Tag.NEW) || (current.getTag() == Tag.CHARACTERVAL) || (current.getTag() == Tag.NOT) || (current.getTag() == Tag.NUMCONST) || (current.getTag() == Tag.CHAR) || (current.getTag() == Tag.TRUE) || (current.getTag() == Tag.FALSE) || (current.getTag() == Tag.NULL) || (current.getTag() == Tag.IF) || (current.getTag() == Tag.FOR) || (current.getTag() == Tag.WHILE)) {
            this.instruction();
            this.generate_instructions_factorisation();
            int temp = parser.stack.pop();
            if (temp == Tag.GENERATE_INSTRUCTIONS_FACTORISATION) {
                temp = parser.stack.pop();
                if (temp == Tag.INSTRUCTION) {
                    parser.stack.push(Tag.GENERATE_INSTRUCTIONS);
                } else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.INSTRUCTION + "> but found <" + temp + ">");
                }
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.GENERATE_INSTRUCTIONS_FACTORISATION + "> but found <" + temp + ">");
            }
        }
        else {
            throw new Error("Error line " + parser.lexer.getLine() + " : expected <" + Tag.ID + " 'ident'> or <" + Tag.BEGIN + " 'begin'> or <" + Tag.SYMBOL + " '('> or <" + Tag.RETURN + " 'return'> or <" + Tag.NEW + " 'new'> or <" + Tag.CHARACTERVAL + " 'character'val'> or <" + Tag.NOT + " 'not'> or <" + Tag.NUMCONST + " 'entier'> or <" + Tag.SYMBOL + " 'caractere'> or <" + Tag.TRUE + " 'true'> or <" + Tag.FALSE + " 'false'> or <" + Tag.NULL + " 'null'> or <" + Tag.IF + " 'if'> or <" + Tag.FOR + " 'for'> or <" + Tag.WHILE + " 'while'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
        }
    }

    //GENERATE_INSTRUCTIONS_FACTORISATION
    private void generate_instructions_factorisation() throws IOException {
        //GENERATE_INSTRUCTIONS_FACTORISATION ::= GENERATE_INSTRUCTIONS (lecture de ident)
        //GENERATE_INSTRUCTIONS_FACTORISATION ::= GENERATE_INSTRUCTIONS (lecture de begin)
        //GENERATE_INSTRUCTIONS_FACTORISATION ::= ε (lecture de end)
        //GENERATE_INSTRUCTIONS_FACTORISATION ::= GENERATE_INSTRUCTIONS (lecture de ( )
        //GENERATE_INSTRUCTIONS_FACTORISATION ::= GENERATE_INSTRUCTIONS (lecture de return)
        //GENERATE_INSTRUCTIONS_FACTORISATION ::= GENERATE_INSTRUCTIONS (lecture de new)
        //GENERATE_INSTRUCTIONS_FACTORISATION ::= GENERATE_INSTRUCTIONS (lecture de character'val)
        //GENERATE_INSTRUCTIONS_FACTORISATION ::= ε (lecture de else)
        //GENERATE_INSTRUCTIONS_FACTORISATION ::= GENERATE_INSTRUCTIONS (lecture de not)
        //GENERATE_INSTRUCTIONS_FACTORISATION ::= GENERATE_INSTRUCTIONS (lecture de true)
        //GENERATE_INSTRUCTIONS_FACTORISATION ::= GENERATE_INSTRUCTIONS (lecture de false)
        //GENERATE_INSTRUCTIONS_FACTORISATION ::= GENERATE_INSTRUCTIONS (lecture de null)
        //GENERATE_INSTRUCTIONS_FACTORISATION ::= GENERATE_INSTRUCTIONS (lecture de if)
        //GENERATE_INSTRUCTIONS_FACTORISATION ::= GENERATE_INSTRUCTIONS (lecture de for)
        //GENERATE_INSTRUCTIONS_FACTORISATION ::= GENERATE_INSTRUCTIONS (lecture de while)
        //GENERATE_INSTRUCTIONS_FACTORISATION ::= ε (lecture de elsif)
        if ((current.getTag() == Tag.ID) || (current.getTag() == Tag.BEGIN) || ((current.getTag() == Tag.SYMBOL) && (current.getStringValue().equals("("))) || (current.getTag() == Tag.RETURN) || (current.getTag() == Tag.NEW) || (current.getTag() == Tag.CHARACTERVAL) || (current.getTag() == Tag.NOT) || (current.getTag() == Tag.TRUE) || (current.getTag() == Tag.FALSE) || (current.getTag() == Tag.NULL) || (current.getTag() == Tag.IF) || (current.getTag() == Tag.FOR) || (current.getTag() == Tag.WHILE)) {
            this.generate_instructions();
            int temp = parser.stack.pop();
            if (temp == Tag.GENERATE_INSTRUCTIONS) {
                parser.stack.push(Tag.GENERATE_INSTRUCTIONS_FACTORISATION);
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.GENERATE_INSTRUCTIONS + "> but found <" + temp + ">");
            }
        }
        else if ((current.getTag() == Tag.END) || (current.getTag() == Tag.ELSE) || (current.getTag() == Tag.ELSIF)) {
            parser.stack.push(Tag.GENERATE_INSTRUCTIONS_FACTORISATION);
        }
        else {
            throw new Error("Error line " + parser.lexer.getLine() + " : expected <" + Tag.ID + " 'ident'> or <" + Tag.BEGIN + " 'begin'> or <" + Tag.SYMBOL + " '('> or <" + Tag.RETURN + " 'return'> or <" + Tag.NEW + " 'new'> or <" + Tag.CHARACTERVAL + " 'character'val'> or <" + Tag.NOT + " 'not'> or <" + Tag.TRUE + " 'true'> or <" + Tag.FALSE + " 'false'> or <" + Tag.NULL + " 'null'> or <" + Tag.IF + " 'if'> or <" + Tag.FOR + " 'for'> or <" + Tag.WHILE + " 'while'> or <" + Tag.SYMBOL + " 'else'> or <" + Tag.SYMBOL + " 'end'> or <" + Tag.SYMBOL + " 'elsif'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
        }
    }


    //DECLARATION
    private void declaration() throws IOException {
        //DECLARATION ::= procedure ident DECLARATION_PROCEDURE (lecture de procedure)
        //DECLARATION ::= GENERATE_IDENT : TYPE DECLARATION_WITH_EXPRESSION (lecture de ident)
        //DECLARATION ::= type ident DECLARATION_TYPE (lecture de type)
        //DECLARATION ::= function ident DECLARATION_FUNCTION (lecture de function)
        if (current.getTag() == Tag.PROCEDURE) {
            // semantic functions
            parser.ast.addEdge(parser.ast.buffer.lastElement(), parser.ast.addNode("PROCEDURE"));
            parser.ast.buffer.push(parser.ast.lastNode);
            // end semantic functions
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            if (current.getTag() == Tag.ID) {
                parser.stack.push(current.getTag());
                current = parser.lexer.scan();
                this.declaration_procedure();
                int temp = parser.stack.pop();
                if (temp == Tag.DECLARATION_PROCEDURE) {
                    temp = parser.stack.pop();
                    if (temp == Tag.ID) {
                        // semantic function
                        parser.ast.addEdge(parser.ast.buffer.lastElement(), parser.ast.addNode(((Word)current).lexeme));
                        // end semantic function
                        temp = parser.stack.pop();
                        if (temp == Tag.PROCEDURE) {
                            parser.stack.push(Tag.DECLARATION);
                        } else {
                            throw new Error("Reduction/Stack error : expected <" + Tag.PROCEDURE + "> but found <" + temp + ">");
                        }
                    } else {
                        throw new Error("Reduction/Stack error : expected <" + Tag.ID + "> but found <" + temp + ">");
                    }
                } else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.DECLARATION_PROCEDURE + "> but found <" + temp + ">");
                }
            } else {
                throw new Error("Error line " + parser.lexer.getLine() + " : expected <" + Tag.ID + " 'ident'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
            }
        }
        else if (current.getTag() == Tag.ID) {
            // semantic functions
            parser.ast.addEdge(parser.ast.buffer.lastElement(), parser.ast.addNode("VARIABLE"));
            parser.ast.buffer.push(parser.ast.lastNode);
            // end semantic functions
            this.generate_ident();
            if ((current.getTag() == Tag.SYMBOL) && (current.getStringValue().equals(":"))) {
                parser.stack.push(current.getTag());
                current = parser.lexer.scan();
                this.type();
                // semantic functions
                // int ident = parser.ast.buffer.pop();
                // int type = parser.ast.buffer.pop();
                // int newNode = parser.ast.addNode("VARIABLE");
                // parser.ast.addEdge(newNode, ident);
                // parser.ast.addEdge(newNode, type);
                // parser.ast.addEdge(parser.ast.buffer.lastElement(), newNode);
                // end semantic functions
                this.declaration_with_expression();
                int temp = parser.stack.pop();
                if (temp == Tag.DECLARATION_WITH_EXPRESSION) {
                    temp = parser.stack.pop();
                    if (temp == Tag.NT_TYPE) {
                        temp = parser.stack.pop();
                        if (temp == Tag.SYMBOL) {
                            temp = parser.stack.pop();
                            if (temp == Tag.GENERATE_IDENT) {
                                parser.stack.push(Tag.DECLARATION);
                            } else {
                                throw new Error("Reduction/Stack error : expected <" + Tag.GENERATE_IDENT + "> but found <" + temp + ">");
                            }
                        } else {
                            throw new Error("Reduction/Stack error : expected <" + Tag.SYMBOL + "> but found <" + temp + ">");
                        }
                    } else {
                        throw new Error("Reduction/Stack error : expected <" + Tag.NT_TYPE + "> but found <" + temp + ">");
                    }
                }
            }
        }
        else if (current.getTag() == Tag.TYPE) {
            // semantic functions
            parser.ast.addEdge(parser.ast.buffer.lastElement(), parser.ast.addNode("STRUCTURE"));
            parser.ast.buffer.push(parser.ast.lastNode);
            parser.stack.push(current.getTag());
            // end semantic functions
            current = parser.lexer.scan();
            if (current.getTag() == Tag.ID) {
                parser.ast.addEdge(parser.ast.buffer.lastElement(), parser.ast.addNode(((Word)current).lexeme));
                parser.stack.push(current.getTag());
                current = parser.lexer.scan();
                this.declaration_type();
                int temp = parser.stack.pop();
                if (temp == Tag.DECLARATION_TYPE) {
                    temp = parser.stack.pop();
                    if (temp == Tag.ID) {
                        temp = parser.stack.pop();
                        if (temp == Tag.TYPE) {
                            parser.stack.push(Tag.DECLARATION);
                        } else {
                            throw new Error("Reduction/Stack error : expected <" + Tag.TYPE + "> but found <" + temp + ">");
                        }
                    } else {
                        throw new Error("Reduction/Stack error : expected <" + Tag.ID + "> but found <" + temp + ">");
                    }
                } else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.DECLARATION_TYPE + "> but found <" + temp + ">");
                }
            } else {
                throw new Error("Error line " + parser.lexer.getLine() + " : expected <" + Tag.ID + " 'ident'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
            }
        }
        else if (current.getTag() == Tag.FUNCTION) {
            // semantic functions
            parser.ast.addEdge(parser.ast.buffer.lastElement(), parser.ast.addNode("FUNCTION"));
            parser.ast.buffer.push(parser.ast.lastNode);
            // end semantic functions
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            if (current.getTag() == Tag.ID) {
                parser.ast.addEdge(parser.ast.buffer.lastElement(), parser.ast.addNode(((Word)current).lexeme));
                parser.stack.push(current.getTag());
                current = parser.lexer.scan();
                this.declaration_function();
                int temp = parser.stack.pop();
                if (temp == Tag.DECLARATION_FUNCTION) {
                    temp = parser.stack.pop();
                    if (temp == Tag.ID) {
                        temp = parser.stack.pop();
                        if (temp == Tag.FUNCTION) {
                            parser.stack.push(Tag.DECLARATION);
                        } else {
                            throw new Error("Reduction/Stack error : expected <" + Tag.FUNCTION + "> but found <" + temp + ">");
                        }
                    } else {
                        throw new Error("Reduction/Stack error : expected <" + Tag.ID + "> but found <" + temp + ">");
                    }
                } else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.DECLARATION_FUNCTION + "> but found <" + temp + ">");
                }
            } else {
                throw new Error("Error line " + parser.lexer.getLine() + " : expected <" + Tag.ID + " 'ident'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
            }
        }
        else {
            throw new Error("Error line " + parser.lexer.getLine() + " : expected <" + Tag.PROCEDURE + " 'procedure'> or <" + Tag.ID + " 'ident'> or <" + Tag.TYPE + " 'type'> or <" + Tag.FUNCTION + " 'function'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
        }
    }

    //DECLARATION_TYPE
    private void declaration_type() throws IOException {
        //DECLARATION_TYPE ::= ; (lecture de ;)
        //DECLARATION_TYPE ::= is ACCESS_RECORD (lecture de is)
        if (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(";")) {
            current = parser.lexer.scan();
            parser.stack.push(Tag.DECLARATION_TYPE);
        }
        else if (current.getTag() == Tag.IS) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.access_record();
            int temp = parser.stack.pop();
            if (temp == Tag.ACCESS_RECORD) {
                temp = parser.stack.pop();
                if (temp == Tag.IS) {
                    parser.stack.push(Tag.DECLARATION_TYPE);
                    // semantic functions
                    parser.ast.buffer.pop();
                    // end semantic functions
                } else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.IS + "> but found <" + temp + ">");
                }
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.ACCESS_RECORD + "> but found <" + temp + ">");
            }
        }
        else {
            throw new Error("Error line " + parser.lexer.getLine() + " : expected <" + Tag.SYMBOL + " ';'> or <" + Tag.IS + " 'is'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
        }
    }

    //ACCESS_RECORD
    private void access_record() throws IOException {
        //ACCESS_RECORD ::= access ident ; (lecture de access)
        //ACCESS_RECORD ::= record GENERATE_CHAMPS end record ; (lecture de record)
        if (current.getTag() == Tag.ACCESS) {
            // semantic function
            parser.ast.addEdge(parser.ast.buffer.lastElement(), parser.ast.addNode("ACCESS"));
            parser.ast.buffer.push(parser.ast.lastNode);
            // end semantic function
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            if (current.getTag() == Tag.ID) {
                // semantic function
                parser.ast.addEdge(parser.ast.buffer.lastElement(), parser.ast.addNode(((Word)current).lexeme));
                // end semantic function
                parser.stack.push(current.getTag());
                current = parser.lexer.scan();
                if (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(";")) {
                    parser.stack.push(current.getTag());
                    current = parser.lexer.scan();
                    int temp = parser.stack.pop();
                    if (temp == Tag.SYMBOL) {
                        temp = parser.stack.pop();
                        if (temp == Tag.ID) {
                            temp = parser.stack.pop();
                            if (temp == Tag.ACCESS) {
                                parser.stack.push(Tag.ACCESS_RECORD);
                                // semantic function
                                parser.ast.buffer.pop();
                                // end semantic function
                            } else {
                                throw new Error("Reduction/Stack error : expected <" + Tag.ACCESS + "> but found <" + temp + ">");
                            }
                        } else {
                            throw new Error("Reduction/Stack error : expected <" + Tag.ID + "> but found <" + temp + ">");
                        }
                    } else {
                        throw new Error("Reduction/Stack error : expected <" + Tag.SYMBOL + "> but found <" + temp + ">");
                    }
                } else {
                    throw new Error("Error line " + parser.lexer.getLine() + " : expected <" + Tag.SYMBOL + " ';'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
                }
            } else {
                throw new Error("Error line " + parser.lexer.getLine() + " : expected <" + Tag.ID + " 'ident'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
            }
        }
        else if (current.getTag() == Tag.RECORD) {
            // semantic function
            parser.ast.addEdge(parser.ast.buffer.lastElement(), parser.ast.addNode("RECORD"));
            parser.ast.buffer.push(parser.ast.lastNode);
            // end semantic function
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.generate_champs();
            if (current.getTag() == Tag.END) {
                parser.stack.push(current.getTag());
                current = parser.lexer.scan();
                if (current.getTag() == Tag.RECORD) {
                    parser.stack.push(current.getTag());
                    current = parser.lexer.scan();
                    if (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(";")) {
                        parser.stack.push(current.getTag());
                        current = parser.lexer.scan();
                        int temp = parser.stack.pop();
                        if (temp == Tag.SYMBOL) {
                            temp = parser.stack.pop();
                            if (temp == Tag.RECORD) {
                                temp = parser.stack.pop();
                                if (temp == Tag.END) {
                                    temp = parser.stack.pop();
                                    if (temp == Tag.GENERATE_CHAMPS) {
                                        temp = parser.stack.pop();
                                        if (temp == Tag.RECORD) {
                                            parser.stack.push(Tag.ACCESS_RECORD);
                                            // semantic function
                                            parser.ast.buffer.pop();
                                            // end semantic function
                                        } else {
                                            throw new Error("Reduction/Stack error : expected <" + Tag.RECORD + "> but found <" + temp + ">");
                                        }
                                    } else {
                                        throw new Error("Reduction/Stack error : expected <" + Tag.GENERATE_CHAMPS + "> but found <" + temp + ">");
                                    }
                                } else {
                                    throw new Error("Reduction/Stack error : expected <" + Tag.END + "> but found <" + temp + ">");
                                }
                            } else {
                                throw new Error("Reduction/Stack error : expected <" + Tag.RECORD + "> but found <" + temp + ">");
                            }
                        } else {
                            throw new Error("Reduction/Stack error : expected <" + Tag.SYMBOL + "> but found <" + temp + ">");
                        }
                    } else {
                        throw new Error("Error line " + parser.lexer.getLine() + " : expected <" + Tag.SYMBOL + " ';'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
                    }
                } else {
                    throw new Error("Error line " + parser.lexer.getLine() + " : expected <" + Tag.RECORD + " 'record'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
                }
            } else {
                throw new Error("Error line " + parser.lexer.getLine() + " : expected <" + Tag.END + " 'end'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
            }
        }
        else {
            throw new Error("Error line " + parser.lexer.getLine() + " : expected <" + Tag.ACCESS + " 'access'> or <" + Tag.RECORD + " 'record'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
        }
    }

    //DECLARATION_WITH_EXPRESSION
    private void declaration_with_expression() throws IOException {
        //DECLARATION_WITH_EXPRESSION ::= ; (lecture de ;)
        //DECLARATION_WITH_EXPRESSION ::= := UNARY EXPRESSION ; (lecture de := )
        if (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(";")) {
            parser.stack.push(Tag.DECLARATION_WITH_EXPRESSION);
            current = parser.lexer.scan();
            // semantic functions
            parser.ast.buffer.pop();
            // end semantic functions
        }
        else if (current.getTag() == Tag.ASSIGNMENT) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.unary();
            this.expression();
            // semantic functions
            int expr = parser.ast.buffer.pop();
            parser.ast.addEdge(parser.ast.buffer.lastElement(), expr);
            parser.ast.buffer.pop();
            // end semantic functions
            if (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(";")) {
                parser.stack.push(current.getTag());
                current = parser.lexer.scan();
                int temp = parser.stack.pop();
                if (temp == Tag.SYMBOL) {
                    temp = parser.stack.pop();
                    if (temp == Tag.EXPRESSION) {
                        temp = parser.stack.pop();
                        if (temp == Tag.UNARY) {
                            temp = parser.stack.pop();
                            if (temp == Tag.ASSIGNMENT) {
                                parser.stack.push(Tag.DECLARATION_WITH_EXPRESSION);


                            } else {
                                throw new Error("Reduction/Stack error : expected <" + Tag.ASSIGNMENT + "> but found <" + temp + ">");
                            }
                        } else {
                            throw new Error("Reduction/Stack error : expected <" + Tag.UNARY + "> but found <" + temp + ">");
                        }
                    } else {
                        throw new Error("Reduction/Stack error : expected <" + Tag.SYMBOL + "> but found <" + temp + ">");
                    }
                } else {
                    throw new Error("Error line " + parser.lexer.getLine() + " : expected <" + Tag.SYMBOL + " ';'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
                }
            } else {
                throw new Error("Error line " + parser.lexer.getLine() + " : expected <" + Tag.SYMBOL + " ';'> or <" + Tag.ASSIGNMENT + " ':='> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
            }
        }
        else {
            throw new Error("Error line " + parser.lexer.getLine() + " : expected <" + Tag.EXPRESSION + "> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
        }
    }

    //DECLARATION_PROCEDURE
    private void declaration_procedure() throws IOException{
        //DECLARATION_PROCEDURE ::= IS_DECLARATION BEGIN_INSTRUCTION ; (lecture de is)
        //DECLARATION_PROCEDURE ::= PARAMS IS_DECLARATION BEGIN_INSTRUCTION ; (lecture de ( )
        if (current.getTag() == Tag.IS) {
            this.is_declaration();
            this.begin_instruction();
            if (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(";")) {
                parser.stack.push(current.getTag());
                current = parser.lexer.scan();
                int temp = parser.stack.pop();
                if(temp == Tag.SYMBOL) {
                    temp = parser.stack.pop();
                    if(temp == Tag.BEGIN_INSTRUCTION) {
                        temp = parser.stack.pop();
                        if(temp == Tag.IS_DECLARATION) {
                            parser.stack.push(Tag.DECLARATION_PROCEDURE);
                            // semantic functions
                            parser.ast.buffer.pop();
                            // end semantic functions
                        }
                        else {
                            throw new Error("Reduction/Stack error : expected <"+Tag.IS_DECLARATION+"> but found <"+temp+">");
                        }
                    }
                    else {
                        throw new Error("Reduction/Stack error : expected <"+Tag.BEGIN_INSTRUCTION+"> but found <"+temp+">");
                    }
                }
                else {
                    throw new Error("Reduction/Stack error : expected <"+Tag.SYMBOL +"> but found <"+temp+">");
                }
            }
            else {
                throw new Error("Error line "+parser.lexer.getLine()+" : expected <"+Tag.SYMBOL +" ';'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
            }
        }
        else if (current.getTag() == Tag.SYMBOL && current.getStringValue().equals("(")){
            this.params();
            this.is_declaration();
            this.begin_instruction();
            if (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(";")) {
                parser.stack.push(current.getTag());
                current = parser.lexer.scan();
                int temp = parser.stack.pop();
                if(temp == Tag.SYMBOL) {
                    temp = parser.stack.pop();
                    if(temp == Tag.BEGIN_INSTRUCTION) {
                        temp = parser.stack.pop();
                        if(temp == Tag.IS_DECLARATION) {
                            temp = parser.stack.pop();
                            if(temp == Tag.PARAMS) {
                                parser.stack.push(Tag.DECLARATION_PROCEDURE);
                            }
                            else {
                                throw new Error("Reduction/Stack error : expected <"+Tag.PARAMS+"> but found <"+temp+">");
                            }
                        }
                        else {
                            throw new Error("Reduction/Stack error : expected <"+Tag.IS_DECLARATION+"> but found <"+temp+">");
                        }
                    }
                    else {
                        throw new Error("Reduction/Stack error : expected <"+Tag.BEGIN_INSTRUCTION+"> but found <"+temp+">");
                    }
                }
                else {
                    throw new Error("Reduction/Stack error : expected <"+Tag.SYMBOL +"> but found <"+temp+">");
                }
            }
            else {
                throw new Error("Error line "+parser.lexer.getLine()+" : expected <"+Tag.SYMBOL +" ';'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
            }
        }
        else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <"+Tag.IS+" 'is'> or <"+Tag.SYMBOL +" '('> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
        }
    }

    //DECLARATION_FUNCTION
    private void declaration_function() throws IOException{
        //DECLARATION_FUNCTION ::= PARAMS return TYPE IS_DECLARATION BEGIN_INSTRUCTION ; (lecture de ( )
        //DECLARATION_FUNCTION ::= return TYPE IS_DECLARATION BEGIN_INSTRUCTION ; (lecture de return)
        if(current.getTag() == Tag.SYMBOL && current.getStringValue().equals("(")){
            this.params();
            if(current.getTag() == Tag.RETURN){
                parser.stack.push(current.getTag());
                current = parser.lexer.scan();
                // semantic functions
                parser.ast.addEdge(parser.ast.buffer.lastElement(), parser.ast.addNode("RETURN_TYPE"));
                parser.ast.buffer.push(parser.ast.lastNode);
                // end semantic functions
                this.type();
                // semantic functions
                parser.ast.buffer.pop();
                // end semantic functions
                this.is_declaration();
                this.begin_instruction();
                if(current.getTag() == Tag.SYMBOL && current.getStringValue().equals(";")) {
                    parser.stack.push(current.getTag());
                    current = parser.lexer.scan();
                    int temp = parser.stack.pop();
                    if(temp == Tag.SYMBOL) {
                        temp = parser.stack.pop();
                        if(temp == Tag.BEGIN_INSTRUCTION) {
                            temp = parser.stack.pop();
                            if(temp == Tag.IS_DECLARATION) {
                                temp = parser.stack.pop();
                                if(temp == Tag.NT_TYPE) {
                                    temp = parser.stack.pop();
                                    if(temp == Tag.RETURN) {
                                        temp = parser.stack.pop();
                                        if(temp == Tag.PARAMS) {
                                            parser.stack.push(Tag.DECLARATION_FUNCTION);
                                            // semantic functions
                                            parser.ast.buffer.pop();
                                            // end semantic functions
                                        }
                                        else {
                                            throw new Error("Reduction/Stack error : expected <"+Tag.PARAMS+"> but found <"+temp+">");
                                        }
                                    }
                                    else {
                                        throw new Error("Reduction/Stack error : expected <"+Tag.RETURN+"> but found <"+temp+">");
                                    }
                                }
                                else {
                                    throw new Error("Reduction/Stack error : expected <"+Tag.NT_TYPE+"> but found <"+temp+">");
                                }
                            }
                            else {
                                throw new Error("Reduction/Stack error : expected <"+Tag.IS_DECLARATION+"> but found <"+temp+">");
                            }
                        }
                        else {
                            throw new Error("Reduction/Stack error : expected <"+Tag.BEGIN_INSTRUCTION+"> but found <"+temp+">");
                        }
                    }
                    else {
                        throw new Error("Reduction/Stack error : expected <"+Tag.SYMBOL +"> but found <"+temp+">");
                    }
                }
                else {
                    throw new Error("Error line "+parser.lexer.getLine()+" : expected <"+Tag.SYMBOL +" ';'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
                }
            }
        }
        else if (current.getTag() == Tag.RETURN) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.type();
            this.is_declaration();
            this.begin_instruction();
            if (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(";")) {
                parser.stack.push(current.getTag());
                current = parser.lexer.scan();
                int temp = parser.stack.pop();
                if(temp == Tag.SYMBOL) {
                    temp = parser.stack.pop();
                    if(temp == Tag.BEGIN_INSTRUCTION) {
                        temp = parser.stack.pop();
                        if(temp == Tag.IS_DECLARATION) {
                            temp = parser.stack.pop();
                            if(temp == Tag.NT_TYPE) {
                                temp = parser.stack.pop();
                                if(temp == Tag.RETURN) {
                                    parser.stack.push(Tag.DECLARATION_FUNCTION);
                                    // semantic functions
                                    parser.ast.buffer.pop();
                                    // end semantic functions
                                }
                                else {
                                    throw new Error("Reduction/Stack error : expected <"+Tag.RETURN+"> but found <"+temp+">");
                                }
                            }
                            else {
                                throw new Error("Reduction/Stack error : expected <"+Tag.NT_TYPE+"> but found <"+temp+">");
                            }
                        }
                        else {
                            throw new Error("Reduction/Stack error : expected <"+Tag.IS_DECLARATION+"> but found <"+temp+">");
                        }
                    }
                    else {
                        throw new Error("Reduction/Stack error : expected <"+Tag.BEGIN_INSTRUCTION+"> but found <"+temp+">");
                    }
                }
                else {
                    throw new Error("Reduction/Stack error : expected <"+Tag.SYMBOL +"> but found <"+temp+">");
                }
            }
        }
        else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <"+Tag.SYMBOL +" '('> or <"+Tag.RETURN+" 'return'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
        }
    }

    //GENERATE_IDENT
    private void generate_ident() throws IOException{
        //GENERATE_IDENT ::= ident END_GENERATE_IDENT (lecture de ident)
        if (current.getTag() == Tag.ID) {
            // semantic functions
            parser.ast.addEdge(parser.ast.buffer.lastElement(), parser.ast.addNode(((Word)current).lexeme));
            // end semantic functions
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.end_generate_ident();
            int temp = parser.stack.pop();
            if(temp == Tag.END_GENERATE_IDENT) {
                temp = parser.stack.pop();
                if(temp == Tag.ID) {
                    parser.stack.push(Tag.GENERATE_IDENT);
                }
                else {
                    throw new Error("Reduction/Stack error : expected <"+Tag.ID+"> but found <"+temp+">");
                }
            }
            else {
                throw new Error("Reduction/Stack error : expected <"+Tag.END_GENERATE_IDENT+"> but found <"+temp+">");
            }
        }
        else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <"+Tag.ID+" 'ident'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
        }
    }

    //END_GENERATE_IDENT
    private void end_generate_ident() throws IOException{
        //END_GENERATE_IDENT ::= ε (lecture de : )
        //END_GENERATE_IDENT ::= , GENERATE_IDENT (lecture de , )
        if (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(":")) {
            parser.stack.push(Tag.END_GENERATE_IDENT);
        }
        else if (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(",")) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.generate_ident();
            int temp = parser.stack.pop();
            if(temp == Tag.GENERATE_IDENT) {
                temp = parser.stack.pop();
                if(temp == Tag.SYMBOL) {
                    parser.stack.push(Tag.END_GENERATE_IDENT);
                }
                else {
                    throw new Error("Reduction/Stack error : expected <"+Tag.SYMBOL +"> but found <"+temp+">");
                }
            }
            else {
                throw new Error("Reduction/Stack error : expected <"+Tag.GENERATE_IDENT+"> but found <"+temp+">");
            }
        }
        else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <"+Tag.SYMBOL +" ':'> or <"+Tag.SYMBOL +" ','> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
        }
    }

    //IS_DECLARATION
    private void is_declaration() throws IOException{
        //IS_DECLARATION ::= is IS_DECLARATION_FACTORISATION (lecture de is)
        if (current.getTag() == Tag.IS) {
            // semantic functions
            // int return_type = parser.ast.buffer.pop();
            // parser.ast.addEdge(parser.ast.buffer.lastElement(), parser.ast.addNode("RETURN_TYPE"));
            parser.ast.addEdge(parser.ast.buffer.lastElement(), parser.ast.addNode("DECLARATIONS"));
            parser.ast.buffer.push(parser.ast.lastNode);
            // end semantic functions
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.is_declaration_factorisation();
            int temp = parser.stack.pop();
            if(temp == Tag.IS_DECLARATION_FACTORISATION) {
                temp = parser.stack.pop();
                if(temp == Tag.IS) {
                    parser.stack.push(Tag.IS_DECLARATION);
                    parser.ast.buffer.pop();
                }
                else {
                    throw new Error("Reduction/Stack error : expected <"+Tag.IS+"> but found <"+temp+">");
                }
            }
            else {
                throw new Error("Reduction/Stack error : expected <"+Tag.IS_DECLARATION_FACTORISATION+"> but found <"+temp+">");
            }
        }
        else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <"+Tag.IS+" 'is'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
        }
    }

    //IS_DECLARATION_FACTORISATION
    private void is_declaration_factorisation() throws IOException{
        //IS_DECLARATION_FACTORISATION ::= GENERATE_DECLARATIONS (lecture de procedure)
        //IS_DECLARATION_FACTORISATION ::= GENERATE_DECLARATIONS (lecture de ident)
        //IS_DECLARATION_FACTORISATION ::= ε (lecture de begin)
        //IS_DECLARATION_FACTORISATION ::= GENERATE_DECLARATIONS (lecture de type)
        //IS_DECLARATION_FACTORISATION ::= GENERATE_DECLARATIONS (lecture de function)
        if ((current.getTag() == Tag.PROCEDURE) || (current.getTag() == Tag.ID) || (current.getTag() == Tag.TYPE) || (current.getTag() == Tag.FUNCTION)) {
            this.generate_declarations();
            int temp = parser.stack.pop();
            if(temp == Tag.GENERATE_DECLARATIONS) {
                parser.stack.push(Tag.IS_DECLARATION_FACTORISATION);
            }
            else {
                throw new Error("Reduction/Stack error : expected <"+Tag.GENERATE_DECLARATIONS+"> but found <"+temp+">");
            }
        }
        else if(current.getTag() == Tag.BEGIN) {
            parser.stack.push(Tag.IS_DECLARATION_FACTORISATION);
        }
        else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <"+Tag.PROCEDURE+" 'procedure'> or <"+Tag.ID+" 'ident'> or <"+Tag.TYPE+" 'type'> or <"+Tag.FUNCTION+" 'function'> or <"+Tag.BEGIN+" 'begin'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
        }
    }

    //CHAMPS
    private void champs() throws IOException{
        // CHAMPS ::= GENERATE_IDENT : TYPE ; (lecture de ident)
        // semantic function
        parser.ast.addEdge(parser.ast.buffer.lastElement(), parser.ast.addNode("CHAMP"));
        parser.ast.buffer.push(parser.ast.lastNode);
        // end semantic function
        if (current.getTag() == Tag.ID){
            this.generate_ident();
            if (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(":")){
                parser.stack.push(current.getTag());
                current = parser.lexer.scan();
                this.type();
                if (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(";")){
                    parser.stack.push(current.getTag());
                    current = parser.lexer.scan();
                    int temp = parser.stack.pop();
                    if (temp == Tag.SYMBOL){
                        temp = parser.stack.pop();
                        if (temp == Tag.NT_TYPE){
                            temp = parser.stack.pop();
                            if (temp == Tag.SYMBOL){
                                temp = parser.stack.pop();
                                if (temp == Tag.GENERATE_IDENT){
                                    parser.stack.push(Tag.CHAMPS);
                                    // semantic function
                                    parser.ast.buffer.pop();
                                    // end semantic function
                                }
                                else {
                                    throw new Error("Reduction/Stack error : expected <"+Tag.GENERATE_IDENT+"> but found <"+temp+">");
                                }
                            }
                            else {
                                throw new Error("Reduction/Stack error : expected <"+Tag.SYMBOL +"> but found <"+temp+">");
                            }

                        }
                        else {
                            throw new Error("Reduction/Stack error : expected <"+Tag.NT_TYPE+"> but found <"+temp+">");
                        }
                    }
                    else {
                        throw new Error("Reduction/Stack error : expected <"+Tag.SYMBOL +"> but found <"+temp+">");
                    }
                }
                else {
                    throw new Error("Error line "+parser.lexer.getLine()+" : expected <"+Tag.SYMBOL +" ';'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
                }

            }
        }
        else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <"+Tag.ID+" 'ident'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
        }
    }

    //GENERATE_CHAMPS
    private void generate_champs() throws IOException{
        // GENERATE_CHAMPS GENERATE_CHAMPS ::= CHAMPS END_GENERATE_CHAMPS (lecture de ident)
        if (current.getTag() == Tag.ID){
            this.champs();
            this.end_generate_champs();
            int temp = parser.stack.pop();
            if (temp == Tag.END_GENERATE_CHAMPS) {
                temp = parser.stack.pop();
                if (temp == Tag.CHAMPS) {
                    parser.stack.push(Tag.GENERATE_CHAMPS);
                } else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.CHAMPS + "> but found <" + temp + ">");
                }
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.END_GENERATE_CHAMPS + "> but found <" + temp + ">");
            }
        }
        else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <"+Tag.ID+" 'ident'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
        }
    }

    //END_GENERATE_CHAMPS
    private void end_generate_champs() throws IOException{
        // END_GENERATE_CHAMPS ::= GENERATE_CHAMPS (lecture de ident)
        // END_GENERATE_CHAMPS ::= ε (lecture de end)
        if (current.getTag() == Tag.ID){
            this.generate_champs();
            int temp = parser.stack.pop();
            if (temp == Tag.GENERATE_CHAMPS){
                parser.stack.push(Tag.END_GENERATE_CHAMPS);
            }
            else {
                throw new Error("Reduction/Stack error : expected <"+Tag.GENERATE_CHAMPS+"> but found <"+temp+">");
            }
        }
        else if (current.getTag() == Tag.END){
            parser.stack.push(Tag.END_GENERATE_CHAMPS);
        }
        else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <"+Tag.ID+" 'ident'> or <"+Tag.END+" 'end'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
        }
    }

    //TYPE
    private void type() throws IOException{
        // TYPE ::= ident (lecture de ident)
        // TYPE ::= access ident (lecture de access)
        if (current.getTag() == Tag.ID){
            // semantic functions
            parser.ast.addEdge(parser.ast.buffer.lastElement(),parser.ast.addNode(((Word)current).lexeme));
            // end semantic functions
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            int temp = parser.stack.pop();
            if (temp == Tag.ID){
                parser.stack.push(Tag.NT_TYPE);
            }
            else {
                throw new Error("Reduction/Stack error : expected <"+Tag.ID+"> but found <"+temp+">");
            }
        }
        else if (current.getTag() == Tag.ACCESS){
            // semantic function
            // parser.ast.addEdge(parser.ast.buffer.lastElement(), parser.ast.addNode("TYPE_ACCESS"));
            // parser.ast.buffer.push(parser.ast.lastNode);
            // end semantic function
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            if (current.getTag() == Tag.ID){
                // semantic function
                parser.ast.addEdge(parser.ast.buffer.lastElement(), parser.ast.addNode(((Word)current).lexeme));
                // end semantic function
                parser.stack.push(current.getTag());
                current = parser.lexer.scan();
                int temp = parser.stack.pop();
                if (temp == Tag.ID){
                    temp = parser.stack.pop();
                    if (temp == Tag.ACCESS){
                        parser.stack.push(Tag.NT_TYPE);
                    }
                    else {
                        throw new Error("Reduction/Stack error : expected <"+Tag.ACCESS+"> but found <"+temp+">");
                    }
                }
                else {
                    throw new Error("Reduction/Stack error : expected <"+Tag.ID+"> but found <"+temp+">");
                }
            }
            else {
                throw new Error("Error line "+parser.lexer.getLine()+" : expected <"+Tag.ID+" 'ident'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
            }
        }
        else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <"+Tag.ID+" 'ident'> or <"+Tag.ACCESS+" 'access'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
        }
    }

    //PARAMS
    private void params() throws IOException{
        // PARAMS ::= ( PARAM (lecture de ( )
        if (current.getTag() == Tag.SYMBOL && current.getStringValue().equals("(")){
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.param();
            int temp = parser.stack.pop();
            if (temp == Tag.PARAM){
                temp = parser.stack.pop();
                if (temp == Tag.SYMBOL){
                    parser.stack.push(Tag.PARAMS);
                }
                else {
                    throw new Error("Reduction/Stack error : expected <"+Tag.SYMBOL+"> but found <"+temp+">");
                }
            }
            else {
                throw new Error("Reduction/Stack error : expected <"+Tag.PARAM+"> but found <"+temp+">");
            }
        }
        else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <"+Tag.SYMBOL+" '('> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
        }
    }

    //PARAM
    private void param() throws IOException{
        // PARAM ::= GENERATE_IDENT : TYPE_OR_MODE_TYPE_PARAM (lecture de ident)
        if (current.getTag() == Tag.ID){
            // semantic function
            parser.ast.addEdge(parser.ast.buffer.lastElement(), parser.ast.addNode("PARAM"));
            parser.ast.buffer.push(parser.ast.lastNode);
            // end semantic function
            this.generate_ident();
            if (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(":")){
                parser.stack.push(current.getTag());
                current = parser.lexer.scan();
                this.type_or_mode_type_param();
                int temp = parser.stack.pop();
                if (temp == Tag.TYPE_OR_MODE_TYPE_PARAM){
                    temp = parser.stack.pop();
                    if (temp == Tag.SYMBOL){
                        temp = parser.stack.pop();
                        if (temp == Tag.GENERATE_IDENT){
                            parser.stack.push(Tag.PARAM);
                        }
                        else {
                            throw new Error("Reduction/Stack error : expected <"+Tag.GENERATE_IDENT+"> but found <"+temp+">");
                        }
                    }
                    else {
                        throw new Error("Reduction/Stack error : expected <"+Tag.SYMBOL+"> but found <"+temp+">");
                    }
                }
                else {
                    throw new Error("Reduction/Stack error : expected <"+Tag.TYPE_OR_MODE_TYPE_PARAM+"> but found <"+temp+">");
                }
            }
            else {
                throw new Error("Error line "+parser.lexer.getLine()+" : expected <"+Tag.SYMBOL+" ':'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
            }
        }
        else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <"+Tag.ID+" 'ident'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
        }
    }

    //TYPE_OR_MODE_TYPE_PARAM
    private void type_or_mode_type_param() throws IOException{
        // TYPE_OR_MODE_TYPE_PARAM ::= TYPE END_PARAM (lecture de ident)
        // TYPE_OR_MODE_TYPE_PARAM ::= TYPE END_PARAM (lecture de access)
        // TYPE_OR_MODE_TYPE_PARAM ::= MODE TYPE END_PARAM (lecture de in)
        if (current.getTag() == Tag.ID){
            this.type();
            // semantic functions
            // int ident = parser.ast.buffer.pop();
            // int type = parser.ast.buffer.pop();
            // int newNode = parser.ast.addNode("PARAM");
            // parser.ast.addEdge(newNode, ident);
            // parser.ast.addEdge(newNode, type);
            // parser.ast.addEdge(parser.ast.buffer.lastElement(), newNode);
            // end semantic functions
            this.end_param();
            int temp = parser.stack.pop();
            if (temp == Tag.END_PARAM){
                temp = parser.stack.pop();
                if (temp == Tag.NT_TYPE){
                    parser.stack.push(Tag.TYPE_OR_MODE_TYPE_PARAM);
                }
                else {
                    throw new Error("Reduction/Stack error : expected <"+Tag.NT_TYPE+"> but found <"+temp+">");
                }
            }
            else {
                throw new Error("Reduction/Stack error : expected <"+Tag.END_PARAM+"> but found <"+temp+">");
            }
        }
        else if (current.getTag() == Tag.ACCESS){
            this.type();
            this.end_param();
            int temp = parser.stack.pop();
            if (temp == Tag.END_PARAM){
                temp = parser.stack.pop();
                if (temp == Tag.NT_TYPE){
                    parser.stack.push(Tag.TYPE_OR_MODE_TYPE_PARAM);
                }
                else {
                    throw new Error("Reduction/Stack error : expected <"+Tag.NT_TYPE+"> but found <"+temp+">");
                }
            }
            else {
                throw new Error("Reduction/Stack error : expected <"+Tag.END_PARAM+"> but found <"+temp+">");
            }
        }
        else if (current.getTag() == Tag.IN){
            this.mode();
            this.type();
            this.end_param();
            int temp = parser.stack.pop();
            if (temp == Tag.END_PARAM){
                temp = parser.stack.pop();
                if (temp == Tag.NT_TYPE){
                    temp = parser.stack.pop();
                    if (temp == Tag.MODE){
                        parser.stack.push(Tag.TYPE_OR_MODE_TYPE_PARAM);
                    }
                    else {
                        throw new Error("Reduction/Stack error : expected <"+Tag.MODE+"> but found <"+temp+">");
                    }
                }
                else {
                    throw new Error("Reduction/Stack error : expected <"+Tag.NT_TYPE+"> but found <"+temp+">");
                }
            }
            else {
                throw new Error("Reduction/Stack error : expected <"+Tag.END_PARAM+"> but found <"+temp+">");
            }
        }
        else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <"+Tag.ID+" 'ident'> or <"+Tag.ACCESS+" 'access'> or <"+Tag.IN+" 'in'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
        }
    }

    //END_PARAM
    private void end_param() throws IOException{
        // END_PARAM ::= ; PARAM (lecture de ;)
        // END_PARAM ::= ) (lecture de ) )
        // semantic function
        parser.ast.buffer.pop();
        // end semantic function
        if (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(";")){
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.param();
            int temp = parser.stack.pop();
            if (temp == Tag.PARAM){
                temp = parser.stack.pop();
                if (temp == Tag.SYMBOL){
                    parser.stack.push(Tag.END_PARAM);
                }
                else {
                    throw new Error("Reduction/Stack error : expected <"+Tag.SYMBOL+"> but found <"+temp+">");
                }
            }
            else {
                throw new Error("Reduction/Stack error : expected <"+Tag.PARAM+"> but found <"+temp+">");
            }
        }
        else if (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(")")){
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            int temp = parser.stack.pop();
            if (temp == Tag.SYMBOL){
                parser.stack.push(Tag.END_PARAM);
            }
            else {
                throw new Error("Reduction/Stack error : expected <"+Tag.SYMBOL+"> but found <"+temp+">");
            }
        }
        else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <"+Tag.SYMBOL+" ';'> or <"+Tag.SYMBOL+" ')'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
        }
    }

    //MODE
    private void mode() throws IOException{
        // MODE ::= in OUT_OR_NOT (lecture de in)
        if (current.getTag() == Tag.IN){
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.out_or_not();
            int temp = parser.stack.pop();
            if (temp == Tag.OUT_OR_NOT){
                temp = parser.stack.pop();
                if (temp == Tag.IN){
                    parser.stack.push(Tag.MODE);
                }
                else {
                    throw new Error("Reduction/Stack error : expected <"+Tag.IN+"> but found <"+temp+">");
                }
            }
            else {
                throw new Error("Reduction/Stack error : expected <"+Tag.OUT_OR_NOT+"> but found <"+temp+">");
            }
        }
        else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <"+Tag.IN+" 'in'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
        }
    }

    //OUT_OR_NOT
    private void out_or_not() throws IOException{
        // OUT_OR_NOT ::= ε (lecture de ident)
        // OUT_OR_NOT ::= ε (lecture de access)
        // OUT_OR_NOT ::= out (lecture de out)
        if ((current.getTag() == Tag.ID) || (current.getTag() == Tag.ACCESS)){
            parser.stack.push(Tag.OUT_OR_NOT);
        }
        else if (current.getTag() == Tag.OUT){
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            int temp = parser.stack.pop();
            if (temp == Tag.OUT){
                parser.stack.push(Tag.OUT_OR_NOT);
            }
            else {
                throw new Error("Reduction/Stack error : expected <"+Tag.OUT+"> but found <"+temp+">");
            }
        }
        else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <"+Tag.ID+" 'ident'> or <"+Tag.ACCESS+" 'access'> or <"+Tag.OUT+" 'out'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
        }
    }

    //EXPRESSION
    private void expression() throws IOException{
        // EXPRESSION ::= EXPRESSION_1 EXPRESSION_OR (lecture de ident)
        // EXPRESSION ::= EXPRESSION_1 EXPRESSION_OR (lecture de ( )
        // EXPRESSION ::= EXPRESSION_1 EXPRESSION_OR (lecture de not)
        // EXPRESSION ::= EXPRESSION_1 EXPRESSION_OR (lecture de entier)
        // EXPRESSION ::= EXPRESSION_1 EXPRESSION_OR (lecture de caractere)
        // EXPRESSION ::= EXPRESSION_1 EXPRESSION_OR (lecture de true)
        // EXPRESSION ::= EXPRESSION_1 EXPRESSION_OR (lecture de false)
        // EXPRESSION ::= EXPRESSION_1 EXPRESSION_OR (lecture de null)
        // EXPRESSION ::= new ident EXPRESSION_OR (lecture de new)
        // EXPRESSION ::= character’val ( UNARY EXPRESSION ) EXPRESSION_OR (lecture de character’val)
        if ((current.getTag() == Tag.ID) || (current.getTag() == Tag.SYMBOL && current.getStringValue().equals("(")) || (current.getTag() == Tag.NOT) || (current.getTag() == Tag.NUMCONST) || (current.getTag() == Tag.CHAR) || (current.getTag() == Tag.TRUE) || (current.getTag() == Tag.FALSE) || (current.getTag() == Tag.NULL)){
            this.expression_1();
            this.expression_or();
            int temp = parser.stack.pop();
            if (temp == Tag.EXPRESSION_OR){
                temp = parser.stack.pop();
                if (temp == Tag.EXPRESSION_1){
                    parser.stack.push(Tag.EXPRESSION);
                    // semantic function
//                    int newNode = parser.ast.buffer.pop();
//                    parser.ast.addEdge(parser.ast.buffer.lastElement(),newNode);
//                    parser.ast.buffer.pop();
                    // end semantic function
                }
                else {
                    throw new Error("Reduction/Stack error : expected <"+Tag.EXPRESSION_1+"> but found <"+temp+">");
                }
            }
            else {
                throw new Error("Reduction/Stack error : expected <"+Tag.EXPRESSION_OR+"> but found <"+temp+">");
            }
        }
        else if (current.getTag() == Tag.NEW){
            // semantic function
            parser.ast.addEdge(parser.ast.buffer.lastElement(), parser.ast.addNode("EXPRESSION_NEW"));
            parser.ast.buffer.push(parser.ast.lastNode);
            parser.stack.push(current.getTag());
            // end semantic function
            current = parser.lexer.scan();
            if (current.getTag() == Tag.ID){
                parser.ast.addEdge(parser.ast.buffer.lastElement(), parser.ast.addNode(((Word)current).lexeme));
                parser.stack.push(current.getTag());
                current = parser.lexer.scan();
                this.expression_or();
                int temp = parser.stack.pop();
                if (temp == Tag.EXPRESSION_OR){
                    temp = parser.stack.pop();
                    if (temp == Tag.ID){
                        temp = parser.stack.pop();
                        if (temp == Tag.NEW){
                            parser.stack.push(Tag.EXPRESSION);
                            parser.ast.buffer.pop();
                        }
                        else {
                            throw new Error("Reduction/Stack error : expected <"+Tag.SYMBOL+"> but found <"+temp+">");
                        }
                    }
                    else {
                        throw new Error("Reduction/Stack error : expected <"+Tag.ID+"> but found <"+temp+">");
                    }
                }
                else {
                    throw new Error("Reduction/Stack error : expected <"+Tag.EXPRESSION_OR+"> but found <"+temp+">");
                }
            }
            else {
                throw new Error("Error line "+parser.lexer.getLine()+" : expected <"+Tag.ID+" 'ident'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
            }
        }
        else if (current.getTag() == Tag.CHARACTERVAL){
            // semantic function
            parser.ast.addEdge(parser.ast.buffer.lastElement(), parser.ast.addNode("EXPRESSION_CHAR_VAL"));
            parser.ast.buffer.push(parser.ast.lastNode);
            // end semantic function
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            if (current.getTag() == Tag.SYMBOL && current.getStringValue().equals("(")){
                parser.stack.push(current.getTag());
                current = parser.lexer.scan();
                this.unary();
                this.expression();
                if (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(")")){
                    parser.stack.push(current.getTag());
                    current = parser.lexer.scan();
                    this.expression_or();
                    int temp = parser.stack.pop();
                    if (temp == Tag.EXPRESSION_OR) {
                        temp = parser.stack.pop();
                        if (temp == Tag.SYMBOL) {
                            temp = parser.stack.pop();
                            if (temp == Tag.EXPRESSION) {
                                temp = parser.stack.pop();
                                if (temp == Tag.UNARY) {
                                    temp = parser.stack.pop();
                                    if (temp == Tag.SYMBOL) {
                                        temp = parser.stack.pop();
                                        if (temp == Tag.CHARACTERVAL) {
                                            parser.stack.push(Tag.EXPRESSION);
                                            // semantic function
                                            parser.ast.buffer.pop();
                                            // end semantic function
                                        } else {
                                            throw new Error("Reduction/Stack error : expected <" + Tag.CHARACTERVAL + "> but found <" + temp + ">");
                                        }
                                    } else {
                                        throw new Error("Reduction/Stack error : expected <" + Tag.SYMBOL + "> but found <" + temp + ">");
                                    }
                                } else {
                                    throw new Error("Reduction/Stack error : expected <" + Tag.UNARY + "> but found <" + temp + ">");
                                }
                            } else {
                                throw new Error("Reduction/Stack error : expected <" + Tag.EXPRESSION + "> but found <" + temp + ">");
                            }
                        } else {
                            throw new Error("Reduction/Stack error : expected <" + Tag.SYMBOL + "> but found <" + temp + ">");
                        }
                    } else {
                            throw new Error("Reduction/Stack error : expected <" + Tag.EXPRESSION_OR + "> but found <" + temp + ">");
                    }
                } else {
                    throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.SYMBOL + " ')'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
                }
            } else {
                throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.SYMBOL + " '('> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
            }
        }
        else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <"+Tag.ID+" 'ident'> or <"+Tag.SYMBOL+" '('> or <"+Tag.NOT+" 'not'> or <"+Tag.NUMCONST+" 'entier'> or <"+Tag.CHAR+" 'caractere'> or <"+Tag.TRUE+" 'true'> or <"+Tag.FALSE+" 'false'> or <"+Tag.NULL+" 'null'> or <"+Tag.NEW+" 'new'> or <"+Tag.CHARACTERVAL+" 'character_val'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
        }
    }

    //EXPRESSION_OR
    private void expression_or() throws IOException{
        // EXPRESSION_OR ::= ε (lecture de ; )
        // EXPRESSION_OR ::= ε (lecture de ) )
        // EXPRESSION_OR ::= ε (lecture de , )
        // EXPRESSION_OR ::= ε (lecture de then)
        // EXPRESSION_OR ::= ε (lecture de loop)
        // EXPRESSION_OR ::= ε (lecture de .. )
        // EXPRESSION_OR ::= or UNARY EXPRESSION_ELSE (lecture de or)
        if ((current.getTag() == Tag.SYMBOL && current.getStringValue().equals(";")) || (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(")")) || (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(",")) || (current.getTag() == Tag.THEN) || (current.getTag() == Tag.LOOP) || (current.getTag() == Tag.DOUBLEPOINT)){
            parser.stack.push(Tag.EXPRESSION_OR);
        }
        else if (current.getTag() == Tag.OR){
            parser.ast.addEdge(parser.ast.buffer.lastElement(), parser.ast.addNode("OR"));
            parser.ast.buffer.push(parser.ast.lastNode);
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.unary();
            this.expression_else();
            int temp = parser.stack.pop();
            if (temp == Tag.EXPRESSION_ELSE){
                temp = parser.stack.pop();
                if (temp == Tag.UNARY){
                    temp = parser.stack.pop();
                    if (temp == Tag.OR){
                        parser.stack.push(Tag.EXPRESSION_OR);
                        parser.ast.buffer.pop();
                    }
                    else {
                        throw new Error("Reduction/Stack error : expected <"+Tag.OR+"> but found <"+temp+">");
                    }
                }
                else {
                    throw new Error("Reduction/Stack error : expected <"+Tag.UNARY+"> but found <"+temp+">");
                }
            }
            else {
                throw new Error("Reduction/Stack error : expected <"+Tag.EXPRESSION_ELSE+"> but found <"+temp+">");
            }
        }
        else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <"+Tag.SYMBOL+" ';'> or <"+Tag.SYMBOL+" ')'> or <"+Tag.SYMBOL+" ','> or <"+Tag.THEN+" 'then'> or <"+Tag.LOOP+" 'loop'> or <"+Tag.DOUBLEPOINT+" '..'> or <"+Tag.OR+" 'or'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
        }
    }

    //EXPRESSION_ELSE
    private void expression_else() throws IOException {
        // EXPRESSION_ELSE ::= EXPRESSION_1 EXPRESSION_OR (lecture de ident)
        // EXPRESSION_ELSE ::= EXPRESSION_1 EXPRESSION_OR (lecture de ( )
        // EXPRESSION_ELSE ::= EXPRESSION_1 EXPRESSION_OR (lecture de not)
        // EXPRESSION_ELSE ::= EXPRESSION_1 EXPRESSION_OR (lecture de entier)
        // EXPRESSION_ELSE ::= EXPRESSION_1 EXPRESSION_OR (lecture de caractere)
        // EXPRESSION_ELSE ::= EXPRESSION_1 EXPRESSION_OR (lecture de true)
        // EXPRESSION_ELSE ::= EXPRESSION_1 EXPRESSION_OR (lecture de false)
        // EXPRESSION_ELSE ::= EXPRESSION_1 EXPRESSION_OR (lecture de null)
        // EXPRESSION_ELSE ::= else UNARY EXPRESSION_1 EXPRESSION_OR (lecture de else)
        if ((current.getTag() == Tag.ID) || (current.getTag() == Tag.SYMBOL && current.getStringValue().equals("(")) || (current.getTag() == Tag.NOT) || (current.getTag() == Tag.NUMCONST) || (current.getTag() == Tag.CHAR) || (current.getTag() == Tag.TRUE) || (current.getTag() == Tag.FALSE) || (current.getTag() == Tag.NULL)) {
            this.expression_1();
            this.expression_or();
            int temp = parser.stack.pop();
            if (temp == Tag.EXPRESSION_OR) {
                temp = parser.stack.pop();
                if (temp == Tag.EXPRESSION_1) {
                    parser.stack.push(Tag.EXPRESSION_ELSE);
                } else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.EXPRESSION_1 + "> but found <" + temp + ">");
                }
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.EXPRESSION_OR + "> but found <" + temp + ">");
            }
        }
        else if (current.getTag() == Tag.ELSE) {
            parser.ast.addEdge(parser.ast.buffer.lastElement(), parser.ast.addNode("OR_ELSE"));
            parser.ast.buffer.push(parser.ast.lastNode);
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.unary();
            this.expression_1();
            this.expression_or();
            int temp = parser.stack.pop();
            if (temp == Tag.EXPRESSION_OR) {
                temp = parser.stack.pop();
                if (temp == Tag.EXPRESSION_1) {
                    temp = parser.stack.pop();
                    if (temp == Tag.UNARY) {
                        temp = parser.stack.pop();
                        if (temp == Tag.ELSE) {
                            parser.stack.push(Tag.EXPRESSION_ELSE);
                            parser.ast.buffer.pop();
                        } else {
                            throw new Error("Reduction/Stack error : expected <" + Tag.ELSE + "> but found <" + temp + ">");
                        }
                    } else {
                        throw new Error("Reduction/Stack error : expected <" + Tag.UNARY + "> but found <" + temp + ">");
                    }
                } else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.EXPRESSION_1 + "> but found <" + temp + ">");
                }
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.EXPRESSION_OR + "> but found <" + temp + ">");
            }
        }
        else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.ID + " 'ident'> or <" + Tag.SYMBOL + " '('> or <" + Tag.NOT + " 'not'> or <" + Tag.NUMCONST + " 'entier'> or <" + Tag.CHAR + " 'caractere'> or <" + Tag.TRUE + " 'true'> or <" + Tag.FALSE + " 'false'> or <" + Tag.NULL + " 'null'> or <" + Tag.ELSE + " 'else'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
        }
    }

    //EXPRESSION_1
    private void expression_1() throws IOException{
        // EXPRESSION_1 ::= EXPRESSION_NOT EXPRESSION_AND (lecture de ident)
        // EXPRESSION_1 ::= EXPRESSION_NOT EXPRESSION_AND (lecture de ( )
        // EXPRESSION_1 ::= EXPRESSION_NOT EXPRESSION_AND (lecture de not)
        // EXPRESSION_1 ::= EXPRESSION_NOT EXPRESSION_AND (lecture de entier)
        // EXPRESSION_1 ::= EXPRESSION_NOT EXPRESSION_AND (lecture de caractere)
        // EXPRESSION_1 ::= EXPRESSION_NOT EXPRESSION_AND (lecture de true)
        // EXPRESSION_1 ::= EXPRESSION_NOT EXPRESSION_AND (lecture de false)
        // EXPRESSION_1 ::= EXPRESSION_NOT EXPRESSION_AND (lecture de null)
        if ((current.getTag() == Tag.ID) || (current.getTag() == Tag.SYMBOL && current.getStringValue().equals("(")) || (current.getTag() == Tag.NOT) || (current.getTag() == Tag.NUMCONST) || (current.getTag() == Tag.CHAR) || (current.getTag() == Tag.TRUE) || (current.getTag() == Tag.FALSE) || (current.getTag() == Tag.NULL)){
            this.expression_not();
            this.expression_and();
            int temp = parser.stack.pop();
            if (temp == Tag.EXPRESSION_AND){
                temp = parser.stack.pop();
                if (temp == Tag.EXPRESSION_NOT){
                    parser.stack.push(Tag.EXPRESSION_1);
                }
                else {
                    throw new Error("Reduction/Stack error : expected <"+Tag.EXPRESSION_NOT+"> but found <"+temp+">");
                }
            }
            else {
                throw new Error("Reduction/Stack error : expected <"+Tag.EXPRESSION_AND+"> but found <"+temp+">");
            }
        }
        else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <"+Tag.ID+" 'ident'> or <"+Tag.SYMBOL+" '('> or <"+Tag.NOT+" 'not'> or <"+Tag.NUMCONST+" 'entier'> or <"+Tag.CHAR+" 'caractere'> or <"+Tag.TRUE+" 'true'> or <"+Tag.FALSE+" 'false'> or <"+Tag.NULL+" 'null'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
        }

    }

    //EXPRESSION_AND
    private void expression_and() throws IOException{
        // EXPRESSION_AND ::= ε (lecture de ; )
        // EXPRESSION_AND ::= ε (lecture de ) )
        // EXPRESSION_AND ::= ε (lecture de , )
        // EXPRESSION_AND ::= ε (lecture de or)
        // EXPRESSION_AND ::= ε (lecture de then)
        // EXPRESSION_AND ::= ε (lecture de loop)
        // EXPRESSION_AND ::= ε (lecture de .. )
        // EXPRESSION_AND ::= and UNARY EXPRESSION_THEN (lecture de and)
        if ((current.getTag() == Tag.SYMBOL && current.getStringValue().equals(";")) || (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(")")) || (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(",")) || (current.getTag() == Tag.OR) || (current.getTag() == Tag.THEN) || (current.getTag() == Tag.LOOP) || (current.getTag() == Tag.DOUBLEPOINT)){
            parser.stack.push(Tag.EXPRESSION_AND);
        }
        else if (current.getTag() == Tag.AND){
            parser.ast.addEdge(parser.ast.buffer.lastElement(), parser.ast.addNode("AND"));
            parser.ast.buffer.push(parser.ast.lastNode);
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.unary();
            this.expression_then();
            int temp = parser.stack.pop();
            if (temp == Tag.EXPRESSION_THEN){
                temp = parser.stack.pop();
                if (temp == Tag.UNARY){
                    temp = parser.stack.pop();
                    if (temp == Tag.AND){
                        parser.stack.push(Tag.EXPRESSION_AND);
                        parser.ast.buffer.pop();
                    }
                    else {
                        throw new Error("Reduction/Stack error : expected <"+Tag.AND+"> but found <"+temp+">");
                    }
                }
                else {
                    throw new Error("Reduction/Stack error : expected <"+Tag.UNARY+"> but found <"+temp+">");
                }
            }
            else {
                throw new Error("Reduction/Stack error : expected <"+Tag.EXPRESSION_THEN+"> but found <"+temp+">");
            }
        }
        else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <"+Tag.SYMBOL+" ';'> or <"+Tag.SYMBOL+" ')'> or <"+Tag.SYMBOL+" ','> or <"+Tag.OR+" 'or'> or <"+Tag.THEN+" 'then'> or <"+Tag.LOOP+" 'loop'> or <"+Tag.DOUBLEPOINT+" '..'> or <"+Tag.AND+" 'and'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
        }
    }

    //EXPRESSION_THEN
    private void expression_then() throws IOException{
        // EXPRESSION_THEN ::= EXPRESSION_NOT EXPRESSION_AND (lecture de ident)
        // EXPRESSION_THEN ::= EXPRESSION_NOT EXPRESSION_AND (lecture de ( )
        // EXPRESSION_THEN ::= then UNARY EXPRESSION_NOT EXPRESSION_AND (lecture de then)
        // EXPRESSION_THEN ::= EXPRESSION_NOT EXPRESSION_AND (lecture de not)
        // EXPRESSION_THEN ::= EXPRESSION_NOT EXPRESSION_AND (lecture de entier)
        // EXPRESSION_THEN ::= EXPRESSION_NOT EXPRESSION_AND (lecture de caractere)
        // EXPRESSION_THEN ::= EXPRESSION_NOT EXPRESSION_AND (lecture de true)
        // EXPRESSION_THEN ::= EXPRESSION_NOT EXPRESSION_AND (lecture de false)
        // EXPRESSION_THEN ::= EXPRESSION_NOT EXPRESSION_AND (lecture de null)
        if ((current.getTag() == Tag.ID) || (current.getTag() == Tag.SYMBOL && current.getStringValue().equals("(")) || (current.getTag() == Tag.NOT) || (current.getTag() == Tag.NUMCONST) || (current.getTag() == Tag.CHAR) || (current.getTag() == Tag.TRUE) || (current.getTag() == Tag.FALSE) || (current.getTag() == Tag.NULL)){
            this.expression_not();
            this.expression_and();
            int temp = parser.stack.pop();
            if (temp == Tag.EXPRESSION_AND){
                temp = parser.stack.pop();
                if (temp == Tag.EXPRESSION_NOT){
                    parser.stack.push(Tag.EXPRESSION_THEN);
                }
                else {
                    throw new Error("Reduction/Stack error : expected <"+Tag.EXPRESSION_NOT+"> but found <"+temp+">");
                }
            }
            else {
                throw new Error("Reduction/Stack error : expected <"+Tag.EXPRESSION_AND+"> but found <"+temp+">");
            }
        }
        else if (current.getTag() == Tag.THEN){
            parser.ast.addEdge(parser.ast.buffer.lastElement(), parser.ast.addNode("THEN"));
            parser.ast.buffer.push(parser.ast.lastNode);
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.unary();
            this.expression_not();
            this.expression_and();
            int temp = parser.stack.pop();
            if (temp == Tag.EXPRESSION_AND){
                temp = parser.stack.pop();
                if (temp == Tag.EXPRESSION_NOT){
                    temp = parser.stack.pop();
                    if (temp == Tag.UNARY){
                        temp = parser.stack.pop();
                        if (temp == Tag.THEN){
                            parser.stack.push(Tag.EXPRESSION_THEN);
                            parser.ast.buffer.pop();
                        }
                        else {
                            throw new Error("Reduction/Stack error : expected <"+Tag.THEN+"> but found <"+temp+">");
                        }
                    }
                    else {
                        throw new Error("Reduction/Stack error : expected <"+Tag.UNARY+"> but found <"+temp+">");
                    }
                }
                else {
                    throw new Error("Reduction/Stack error : expected <"+Tag.EXPRESSION_NOT+"> but found <"+temp+">");
                }
            }
            else {
                throw new Error("Reduction/Stack error : expected <"+Tag.EXPRESSION_AND+"> but found <"+temp+">");
            }
        }
        else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.ID + " 'ident'> or <" + Tag.SYMBOL + " '('> or <" + Tag.NOT + " 'not'> or <" + Tag.NUMCONST + " 'entier'> or <" + Tag.CHAR + " 'caractere'> or <" + Tag.TRUE + " 'true'> or <" + Tag.FALSE + " 'false'> or <" + Tag.NULL + " 'null'> or <" + Tag.THEN + " 'then'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
        }
    }

    //EXPRESSION_NOT
    private void expression_not() throws IOException{
        // EXPRESSION_NOT ::= EXPRESSION_3 EXPRESSION_EQUALS (lecture de ident)
        // EXPRESSION_NOT ::= EXPRESSION_3 EXPRESSION_EQUALS (lecture de ( )
        // EXPRESSION_NOT ::= EXPRESSION_3 EXPRESSION_EQUALS (lecture de entier)
        // EXPRESSION_NOT ::= EXPRESSION_3 EXPRESSION_EQUALS (lecture de caractere)
        // EXPRESSION_NOT ::= EXPRESSION_3 EXPRESSION_EQUALS (lecture de true)
        // EXPRESSION_NOT ::= EXPRESSION_3 EXPRESSION_EQUALS (lecture de false)
        // EXPRESSION_NOT ::= EXPRESSION_3 EXPRESSION_EQUALS (lecture de null)
        // EXPRESSION_NOT ::= not UNARY EXPRESSION_NOT (lecture de not)
        if ((current.getTag() == Tag.ID) || (current.getTag() == Tag.SYMBOL && current.getStringValue().equals("(")) || (current.getTag() == Tag.NUMCONST) || (current.getTag() == Tag.CHAR) || (current.getTag() == Tag.TRUE) || (current.getTag() == Tag.FALSE) || (current.getTag() == Tag.NULL)){
            this.expression_3();
            this.expression_equals();
            int temp = parser.stack.pop();
            if (temp == Tag.EXPRESSION_EQUALS){
                temp = parser.stack.pop();
                if (temp == Tag.EXPRESSION_3){
                    parser.stack.push(Tag.EXPRESSION_NOT);
                }
                else {
                    throw new Error("Reduction/Stack error : expected <"+Tag.EXPRESSION_3+"> but found <"+temp+">");
                }
            }
            else {
                throw new Error("Reduction/Stack error : expected <"+Tag.EXPRESSION_EQUALS+"> but found <"+temp+">");
            }
        }
        else if (current.getTag() == Tag.NOT){
            parser.ast.addEdge(parser.ast.buffer.lastElement(), parser.ast.addNode("NOT"));
            parser.ast.buffer.push(parser.ast.lastNode);
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.unary();
            this.expression_not();
            int temp = parser.stack.pop();
            if (temp == Tag.EXPRESSION_NOT){
                temp = parser.stack.pop();
                if (temp == Tag.UNARY){
                    temp = parser.stack.pop();
                    if (temp == Tag.NOT){
                        parser.stack.push(Tag.EXPRESSION_NOT);
                        parser.ast.buffer.pop();
                    }
                    else {
                        throw new Error("Reduction/Stack error : expected <"+Tag.NOT+"> but found <"+temp+">");
                    }
                }
                else {
                    throw new Error("Reduction/Stack error : expected <"+Tag.UNARY+"> but found <"+temp+">");
                }
            }
            else {
                throw new Error("Reduction/Stack error : expected <"+Tag.EXPRESSION_NOT+"> but found <"+temp+">");
            }
        }
        else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.ID + " 'ident'> or <" + Tag.SYMBOL + " '('> or <" + Tag.NUMCONST + " 'entier'> or <" + Tag.CHAR + " 'caractere'> or <" + Tag.TRUE + " 'true'> or <" + Tag.FALSE + " 'false'> or <" + Tag.NULL + " 'null'> or <" + Tag.NOT + " 'not'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
        }
    }

    //EXPRESSION_3
    private void expression_3() throws IOException{
        // EXPRESSION_3 ::= EXPRESSION_4 EXPRESSION_EQUALS (lecture de ident)
        // EXPRESSION_3 ::= EXPRESSION_4 EXPRESSION_EQUALS (lecture de ( )
        // EXPRESSION_3 ::= EXPRESSION_4 EXPRESSION_EQUALS (lecture de entier)
        // EXPRESSION_3 ::= EXPRESSION_4 EXPRESSION_EQUALS (lecture de caractere)
        // EXPRESSION_3 ::= EXPRESSION_4 EXPRESSION_EQUALS (lecture de true)
        // EXPRESSION_3 ::= EXPRESSION_4 EXPRESSION_EQUALS (lecture de false)
        // EXPRESSION_3 ::= EXPRESSION_4 EXPRESSION_EQUALS (lecture de null)
        if ((current.getTag() == Tag.ID) || (current.getTag() == Tag.SYMBOL && current.getStringValue().equals("(")) || (current.getTag() == Tag.NUMCONST) || (current.getTag() == Tag.CHAR) || (current.getTag() == Tag.TRUE) || (current.getTag() == Tag.FALSE) || (current.getTag() == Tag.NULL)){
            this.expression_4();
            this.expression_equals();
            int temp = parser.stack.pop();
            if (temp == Tag.EXPRESSION_EQUALS){
                temp = parser.stack.pop();
                if (temp == Tag.EXPRESSION_4){
                    parser.stack.push(Tag.EXPRESSION_3);
                }
                else {
                    throw new Error("Reduction/Stack error : expected <"+Tag.EXPRESSION_4+"> but found <"+temp+">");
                }
            }
            else {
                throw new Error("Reduction/Stack error : expected <"+Tag.EXPRESSION_EQUALS+"> but found <"+temp+">");
            }
        }
        else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.ID + " 'ident'> or <" + Tag.SYMBOL + " '('> or <" + Tag.NUMCONST + " 'entier'> or <" + Tag.CHAR + " 'caractere'> or <" + Tag.TRUE + " 'true'> or <" + Tag.FALSE + " 'false'> or <" + Tag.NULL + " 'null'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
        }
    }

    //EXPRESSION_EQUALS
    private void expression_equals() throws IOException{
        // EXPRESSION_EQUALS ::= ε (lecture de ; )
        // EXPRESSION_EQUALS ::= ε (lecture de ) )
        // EXPRESSION_EQUALS ::= ε (lecture de , )
        // EXPRESSION_EQUALS ::= ε (lecture de or)
        // EXPRESSION_EQUALS ::= ε (lecture de and)
        // EXPRESSION_EQUALS ::= ε (lecture de then)
        // EXPRESSION_EQUALS ::= ε (lecture de loop)
        // EXPRESSION_EQUALS ::= ε (lecture de .. )
        // EXPRESSION_EQUALS ::= = UNARY EXPRESSION_4 EXPRESSION_EQUALS (lecture de =)
        // EXPRESSION_EQUALS ::= /= UNARY EXPRESSION_4 EXPRESSION_EQUALS (lecture de /=)
        if ((current.getTag() == Tag.SYMBOL && current.getStringValue().equals(";")) || (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(")")) || (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(",")) || (current.getTag() == Tag.OR) || (current.getTag() == Tag.AND) || (current.getTag() == Tag.THEN) || (current.getTag() == Tag.LOOP) || (current.getTag() == Tag.DOUBLEPOINT)){
            parser.stack.push(Tag.EXPRESSION_EQUALS);
        }
        else if (current.getTag() == Tag.SYMBOL && current.getStringValue().equals("=")){
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.unary();
            this.expression_4();
            // semantic function
            int right = parser.ast.buffer.pop();
            int left = parser.ast.buffer.pop();
            int newNode = parser.ast.addNode("=");
            parser.ast.addEdge(newNode,right);
            parser.ast.addEdge(newNode,left);
            parser.ast.buffer.push(newNode);
            // end semantic function
            this.expression_equals();
            int temp = parser.stack.pop();
            if (temp == Tag.EXPRESSION_EQUALS){
                temp = parser.stack.pop();
                if (temp == Tag.EXPRESSION_4){
                    temp = parser.stack.pop();
                    if (temp == Tag.UNARY){
                        temp = parser.stack.pop();
                        if (temp == Tag.SYMBOL){
                            parser.stack.push(Tag.EXPRESSION_EQUALS);
                        }
                        else {
                            throw new Error("Reduction/Stack error : expected <"+Tag.SYMBOL+"> but found <"+temp+">");
                        }
                    }
                    else {
                        throw new Error("Reduction/Stack error : expected <"+Tag.UNARY+"> but found <"+temp+">");
                    }
                }
                else {
                    throw new Error("Reduction/Stack error : expected <"+Tag.EXPRESSION_4+"> but found <"+temp+">");
                }
            }
            else {
                throw new Error("Reduction/Stack error : expected <"+Tag.EXPRESSION_EQUALS+"> but found <"+temp+">");
            }
        }
        else if (current.getTag() == Tag.DIFFERENT) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.unary();
            this.expression_4();
            // semantic function
            int right = parser.ast.buffer.pop();
            int left = parser.ast.buffer.pop();
            int newNode = parser.ast.addNode("/=");
            parser.ast.addEdge(newNode,right);
            parser.ast.addEdge(newNode,left);
            parser.ast.buffer.push(newNode);
            // end semantic function
            this.expression_equals();
            int temp = parser.stack.pop();
            if (temp == Tag.EXPRESSION_EQUALS) {
                temp = parser.stack.pop();
                if (temp == Tag.EXPRESSION_4) {
                    temp = parser.stack.pop();
                    if (temp == Tag.UNARY) {
                        temp = parser.stack.pop();
                        if (temp == Tag.DIFFERENT) {
                            parser.stack.push(Tag.EXPRESSION_EQUALS);
                        } else {
                            throw new Error("Reduction/Stack error : expected <" + Tag.DIFFERENT + "> but found <" + temp + ">");
                        }
                    } else {
                        throw new Error("Reduction/Stack error : expected <" + Tag.UNARY + "> but found <" + temp + ">");
                    }
                } else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.EXPRESSION_4 + "> but found <" + temp + ">");
                }
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.EXPRESSION_EQUALS + "> but found <" + temp + ">");
            }
        } else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <"+Tag.SYMBOL+" ';'> or <"+Tag.SYMBOL+" ')'> or <"+Tag.SYMBOL+" ','> or <"+Tag.OR+" 'or'> or <"+Tag.AND+" 'and'> or <"+Tag.THEN+" 'then'> or <"+Tag.LOOP+" 'loop'> or <"+Tag.DOUBLEPOINT+" '..'> or <"+Tag.SYMBOL+" '='> or <"+Tag.DIFFERENT+" '/='> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
        }

    }

    //EXPRESSION_4
    private void expression_4() throws IOException{
        // EXPRESSION_4 ::= EXPRESSION_5 EXPRESSION_COMPARAISON (lecture de ident)
        // EXPRESSION_4 ::= EXPRESSION_5 EXPRESSION_COMPARAISON (lecture de ( )
        // EXPRESSION_4 ::= EXPRESSION_5 EXPRESSION_COMPARAISON (lecture de entier)
        // EXPRESSION_4 ::= EXPRESSION_5 EXPRESSION_COMPARAISON (lecture de caractere)
        // EXPRESSION_4 ::= EXPRESSION_5 EXPRESSION_COMPARAISON (lecture de true)
        // EXPRESSION_4 ::= EXPRESSION_5 EXPRESSION_COMPARAISON (lecture de false)
        // EXPRESSION_4 ::= EXPRESSION_5 EXPRESSION_COMPARAISON (lecture de null)
        if ((current.getTag() == Tag.ID) || (current.getTag() == Tag.SYMBOL && current.getStringValue().equals("(")) || (current.getTag() == Tag.NUMCONST) || (current.getTag() == Tag.CHAR) || (current.getTag() == Tag.TRUE) || (current.getTag() == Tag.FALSE) || (current.getTag() == Tag.NULL)){
            this.expression_5();
            this.expression_comparaison();
            int temp = parser.stack.pop();
            if (temp == Tag.EXPRESSION_COMPARAISON){
                temp = parser.stack.pop();
                if (temp == Tag.EXPRESSION_5){
                    parser.stack.push(Tag.EXPRESSION_4);
                }
                else {
                    throw new Error("Reduction/Stack error : expected <"+Tag.EXPRESSION_5+"> but found <"+temp+">");
                }
            }
            else {
                throw new Error("Reduction/Stack error : expected <"+Tag.EXPRESSION_COMPARAISON+"> but found <"+temp+">");
            }
        }
        else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.ID + " 'ident'> or <" + Tag.SYMBOL + " '('> or <" + Tag.NUMCONST + " 'entier'> or <" + Tag.CHAR + " 'caractere'> or <" + Tag.TRUE + " 'true'> or <" + Tag.FALSE + " 'false'> or <" + Tag.NULL + " 'null'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
        }
    }

    //EXPRESSION_COMPARAISON
    private void expression_comparaison() throws IOException{
        // EXPRESSION_COMPARAISON ::= ε (lecture de ; )
        // EXPRESSION_COMPARAISON ::= ε (lecture de ) )
        // EXPRESSION_COMPARAISON ::= ε (lecture de , )
        // EXPRESSION_COMPARAISON ::= ε (lecture de or)
        // EXPRESSION_COMPARAISON ::= ε (lecture de and)
        // EXPRESSION_COMPARAISON ::= ε (lecture de then)
        // EXPRESSION_COMPARAISON ::= ε (lecture de = )
        // EXPRESSION_COMPARAISON ::= ε (lecture de /= )
        // EXPRESSION_COMPARAISON ::= ε (lecture de loop)
        // EXPRESSION_COMPARAISON ::= ε (lecture de .. )
        // EXPRESSION_COMPARAISON ::= > UNARY EXPRESSION_5 EXPRESSION_COMPARAISON (lecture de >)
        // EXPRESSION_COMPARAISON ::= >= UNARY EXPRESSION_5 EXPRESSION_COMPARAISON (lecture de >=)
        // EXPRESSION_COMPARAISON ::= < UNARY EXPRESSION_5 EXPRESSION_COMPARAISON (lecture de <)
        // EXPRESSION_COMPARAISON ::= <= UNARY EXPRESSION_5 EXPRESSION_COMPARAISON (lecture de <=)
        if ((current.getTag() == Tag.SYMBOL && current.getStringValue().equals(";")) || (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(")")) || (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(",")) || (current.getTag() == Tag.OR) || (current.getTag() == Tag.AND) || (current.getTag() == Tag.THEN) || (current.getTag() == Tag.SYMBOL && current.getStringValue().equals("=")) || (current.getTag() == Tag.DIFFERENT) || (current.getTag() == Tag.LOOP) || (current.getTag() == Tag.DOUBLEPOINT)){
            parser.stack.push(Tag.EXPRESSION_COMPARAISON);
        }
        else if (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(">")){
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.unary();
            this.expression_5();
            // semantic function
            int right = parser.ast.buffer.pop();
            int left = parser.ast.buffer.pop();
            int newNode = parser.ast.addNode(">");
            parser.ast.addEdge(newNode,right);
            parser.ast.addEdge(newNode,left);
            parser.ast.buffer.push(newNode);
            // end semantic function
            this.expression_comparaison();
            int temp = parser.stack.pop();
            if (temp == Tag.EXPRESSION_COMPARAISON){
                temp = parser.stack.pop();
                if (temp == Tag.EXPRESSION_5){
                    temp = parser.stack.pop();
                    if (temp == Tag.UNARY){
                        temp = parser.stack.pop();
                        if (temp == Tag.SYMBOL){
                            parser.stack.push(Tag.EXPRESSION_COMPARAISON);
                        }
                        else {
                            throw new Error("Reduction/Stack error : expected <"+Tag.SYMBOL+"> but found <"+temp+">");
                        }
                    }
                    else {
                        throw new Error("Reduction/Stack error : expected <"+Tag.UNARY+"> but found <"+temp+">");
                    }
                }
                else {
                    throw new Error("Reduction/Stack error : expected <"+Tag.EXPRESSION_5+"> but found <"+temp+">");
                }
            }
            else {
                throw new Error("Reduction/Stack error : expected <"+Tag.EXPRESSION_COMPARAISON+"> but found <"+temp+">");
            }
        }
        else if (current.getTag() == Tag.GEQ) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.unary();
            this.expression_5();
            // semantic function
            int right = parser.ast.buffer.pop();
            int left = parser.ast.buffer.pop();
            int newNode = parser.ast.addNode(">=");
            parser.ast.addEdge(newNode,right);
            parser.ast.addEdge(newNode,left);
            parser.ast.buffer.push(newNode);
            // end semantic function
            this.expression_comparaison();
            int temp = parser.stack.pop();
            if (temp == Tag.EXPRESSION_COMPARAISON) {
                temp = parser.stack.pop();
                if (temp == Tag.EXPRESSION_5) {
                    temp = parser.stack.pop();
                    if (temp == Tag.UNARY) {
                        temp = parser.stack.pop();
                        if (temp == Tag.GEQ) {
                            parser.stack.push(Tag.EXPRESSION_COMPARAISON);
                        } else {
                            throw new Error("Reduction/Stack error : expected <" + Tag.SYMBOL + "> but found <" + temp + ">");
                        }
                    } else {
                        throw new Error("Reduction/Stack error : expected <" + Tag.UNARY + "> but found <" + temp + ">");
                    }
                } else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.EXPRESSION_5 + "> but found <" + temp + ">");
                }
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.EXPRESSION_COMPARAISON + "> but found <" + temp + ">");
            }
        }
        else if (current.getTag() == Tag.SYMBOL && current.getStringValue().equals("<")){
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.unary();
            this.expression_5();
            // semantic function
            int right = parser.ast.buffer.pop();
            int left = parser.ast.buffer.pop();
            int newNode = parser.ast.addNode("<");
            parser.ast.addEdge(newNode,right);
            parser.ast.addEdge(newNode,left);
            parser.ast.buffer.push(newNode);
            // end semantic function
            this.expression_comparaison();
            int temp = parser.stack.pop();
            if (temp == Tag.EXPRESSION_COMPARAISON){
                temp = parser.stack.pop();
                if (temp == Tag.EXPRESSION_5){
                    temp = parser.stack.pop();
                    if (temp == Tag.UNARY){
                        temp = parser.stack.pop();
                        if (temp == Tag.SYMBOL){
                            parser.stack.push(Tag.EXPRESSION_COMPARAISON);
                        }
                        else {
                            throw new Error("Reduction/Stack error : expected <"+Tag.SYMBOL+"> but found <"+temp+">");
                        }
                    }
                    else {
                        throw new Error("Reduction/Stack error : expected <"+Tag.UNARY+"> but found <"+temp+">");
                    }
                }
                else {
                    throw new Error("Reduction/Stack error : expected <"+Tag.EXPRESSION_5+"> but found <"+temp+">");
                }
            }
            else {
                throw new Error("Reduction/Stack error : expected <"+Tag.EXPRESSION_COMPARAISON+"> but found <"+temp+">");
            }
        }
        else if (current.getTag() == Tag.LEQ) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.unary();
            this.expression_5();
            // semantic function
            int right = parser.ast.buffer.pop();
            int left = parser.ast.buffer.pop();
            int newNode = parser.ast.addNode("<=");
            parser.ast.addEdge(newNode,right);
            parser.ast.addEdge(newNode,left);
            parser.ast.buffer.push(newNode);
            // end semantic function
            this.expression_comparaison();
            int temp = parser.stack.pop();
            if (temp == Tag.EXPRESSION_COMPARAISON) {
                temp = parser.stack.pop();
                if (temp == Tag.EXPRESSION_5) {
                    temp = parser.stack.pop();
                    if (temp == Tag.UNARY) {
                        temp = parser.stack.pop();
                        if (temp == Tag.LEQ) {
                            parser.stack.push(Tag.EXPRESSION_COMPARAISON);
                        } else {
                            throw new Error("Reduction/Stack error : expected <" + Tag.SYMBOL + "> but found <" + temp + ">");
                        }
                    } else {
                        throw new Error("Reduction/Stack error : expected <" + Tag.UNARY + "> but found <" + temp + ">");
                    }
                } else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.EXPRESSION_5 + "> but found <" + temp + ">");
                }
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.EXPRESSION_COMPARAISON + "> but found <" + temp + ">");
            }
        } else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <"+Tag.SYMBOL+" ';'> or <"+Tag.SYMBOL+" ')'> or <"+Tag.SYMBOL+" ','> or <"+Tag.OR+" 'or'> or <"+Tag.AND+" 'and'> or <"+Tag.THEN+" 'then'> or <"+Tag.SYMBOL+" '='> or <"+Tag.DIFFERENT+" '/='> or <"+Tag.LOOP+" 'loop'> or <"+Tag.DOUBLEPOINT+" '..'> or <"+Tag.SYMBOL+" '>'> or <"+Tag.GEQ+" '>='> or <"+Tag.SYMBOL+" '<'> or <"+Tag.LEQ+" '<='> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
        }
    }

    //EXPRESSION_5
    private void expression_5() throws IOException{
        // EXPRESSION_5 ::= EXPRESSION_6 EXPRESSION_PLUS_MOINS (lecture de ident)
        // EXPRESSION_5 ::= EXPRESSION_6 EXPRESSION_PLUS_MOINS (lecture de ( )
        // EXPRESSION_5 ::= EXPRESSION_6 EXPRESSION_PLUS_MOINS (lecture de entier)
        // EXPRESSION_5 ::= EXPRESSION_6 EXPRESSION_PLUS_MOINS (lecture de caractere)
        // EXPRESSION_5 ::= EXPRESSION_6 EXPRESSION_PLUS_MOINS (lecture de true)
        // EXPRESSION_5 ::= EXPRESSION_6 EXPRESSION_PLUS_MOINS (lecture de false)
        // EXPRESSION_5 ::= EXPRESSION_6 EXPRESSION_PLUS_MOINS (lecture de null)
        if ((current.getTag() == Tag.ID) || (current.getTag() == Tag.SYMBOL && current.getStringValue().equals("(")) || (current.getTag() == Tag.NUMCONST) || (current.getTag() == Tag.CHAR) || (current.getTag() == Tag.TRUE) || (current.getTag() == Tag.FALSE) || (current.getTag() == Tag.NULL)){
            this.expression_6();
            this.expression_plus_moins();
            int temp = parser.stack.pop();
            if (temp == Tag.EXPRESSION_PLUS_MOINS){
                temp = parser.stack.pop();
                if (temp == Tag.EXPRESSION_6){
                    parser.stack.push(Tag.EXPRESSION_5);
                }
                else {
                    throw new Error("Reduction/Stack error : expected <"+Tag.EXPRESSION_6+"> but found <"+temp+">");
                }
            }
            else {
                throw new Error("Reduction/Stack error : expected <"+Tag.EXPRESSION_PLUS_MOINS+"> but found <"+temp+">");
            }
        }
        else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.ID + " 'ident'> or <" + Tag.SYMBOL + " '('> or <" + Tag.NUMCONST + " 'entier'> or <" + Tag.CHAR + " 'caractere'> or <" + Tag.TRUE + " 'true'> or <" + Tag.FALSE + " 'false'> or <" + Tag.NULL + " 'null'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
        }
    }

    //EXPRESSION_PLUS_MOINS
    private void expression_plus_moins() throws IOException{
        // EXPRESSION_PLUS_MOINS ::= ε (lecture de ; )
        // EXPRESSION_PLUS_MOINS ::= ε (lecture de ) )
        // EXPRESSION_PLUS_MOINS ::= ε (lecture de , )
        // EXPRESSION_PLUS_MOINS ::= ε (lecture de or)
        // EXPRESSION_PLUS_MOINS ::= ε (lecture de and)
        // EXPRESSION_PLUS_MOINS ::= ε (lecture de then)
        // EXPRESSION_PLUS_MOINS ::= ε (lecture de = )
        // EXPRESSION_PLUS_MOINS ::= ε (lecture de /= )
        // EXPRESSION_PLUS_MOINS ::= ε (lecture de > )
        // EXPRESSION_PLUS_MOINS ::= ε (lecture de >= )
        // EXPRESSION_PLUS_MOINS ::= ε (lecture de < )
        // EXPRESSION_PLUS_MOINS ::= ε (lecture de <= )
        // EXPRESSION_PLUS_MOINS ::= ε (lecture de loop)
        // EXPRESSION_PLUS_MOINS ::= ε (lecture de .. )
        // EXPRESSION_PLUS_MOINS ::= + UNARY EXPRESSION_6 EXPRESSION_PLUS_MOINS (lecture de +)
        // EXPRESSION_PLUS_MOINS ::= - UNARY EXPRESSION_6 EXPRESSION_PLUS_MOINS (lecture de -)

        if ((current.getTag() == Tag.SYMBOL && current.getStringValue().equals(";")) || (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(")")) || (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(",")) || (current.getTag() == Tag.OR) || (current.getTag() == Tag.AND) || (current.getTag() == Tag.THEN) || (current.getTag() == Tag.SYMBOL && current.getStringValue().equals("=")) || (current.getTag() == Tag.DIFFERENT) || (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(">")) || (current.getTag() == Tag.GEQ) || (current.getTag() == Tag.SYMBOL && current.getStringValue().equals("<")) || (current.getTag() == Tag.LEQ) || (current.getTag() == Tag.LOOP) || (current.getTag() == Tag.DOUBLEPOINT)){
            parser.stack.push(Tag.EXPRESSION_PLUS_MOINS);

        }
        else if (current.getTag() == Tag.SYMBOL && current.getStringValue().equals("+")) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.unary();
            this.expression_6();
            // semantic function
            int right = parser.ast.buffer.pop();
            int left = parser.ast.buffer.pop();
            int newNode = parser.ast.addNode("+");
            parser.ast.addEdge(newNode,left);
            parser.ast.addEdge(newNode,right);
            parser.ast.buffer.push(newNode);
            // end semantic function
            this.expression_plus_moins();
            int temp = parser.stack.pop();
            if (temp == Tag.EXPRESSION_PLUS_MOINS) {
                temp = parser.stack.pop();
                if (temp == Tag.EXPRESSION_6) {
                    temp = parser.stack.pop();
                    if (temp == Tag.UNARY) {
                        temp = parser.stack.pop();
                        if (temp == Tag.SYMBOL) {

                            parser.stack.push(Tag.EXPRESSION_PLUS_MOINS);
                        } else {
                            throw new Error("Reduction/Stack error : expected <" + Tag.SYMBOL + "> but found <" + temp + ">");
                        }
                    } else {
                        throw new Error("Reduction/Stack error : expected <" + Tag.UNARY + "> but found <" + temp + ">");
                    }
                } else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.EXPRESSION_6 + "> but found <" + temp + ">");
                }
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.EXPRESSION_PLUS_MOINS + "> but found <" + temp + ">");
            }
        }
        else if (current.getTag() == Tag.SYMBOL && current.getStringValue().equals("-")){
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.unary();
            this.expression_6();
            // semantic function
            int left = parser.ast.buffer.pop();
            int right = parser.ast.buffer.pop();
            int newNode = parser.ast.addNode("-");
            parser.ast.addEdge(newNode,left);
            parser.ast.addEdge(newNode,right);
            parser.ast.buffer.push(newNode);
            // end semantic function
            this.expression_plus_moins();
            int temp = parser.stack.pop();
            if (temp == Tag.EXPRESSION_PLUS_MOINS) {
                temp = parser.stack.pop();
                if (temp == Tag.EXPRESSION_6) {
                    temp = parser.stack.pop();
                    if (temp == Tag.UNARY) {
                        temp = parser.stack.pop();
                        if (temp == Tag.SYMBOL) {

                            parser.stack.push(Tag.EXPRESSION_PLUS_MOINS);
                        } else {
                            throw new Error("Reduction/Stack error : expected <" + Tag.SYMBOL + "> but found <" + temp + ">");
                        }
                    } else {
                        throw new Error("Reduction/Stack error : expected <" + Tag.UNARY + "> but found <" + temp + ">");
                    }
                } else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.EXPRESSION_6 + "> but found <" + temp + ">");
                }
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.EXPRESSION_PLUS_MOINS + "> but found <" + temp + ">");
            }
        } else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <"+Tag.SYMBOL+" ';'> or <"+Tag.SYMBOL+" ')'> or <"+Tag.SYMBOL+" ','> or <"+Tag.OR+" 'or'> or <"+Tag.AND+" 'and'> or <"+Tag.THEN+" 'then'> or <"+Tag.SYMBOL+" '='> or <"+Tag.DIFFERENT+" '/='> or <"+Tag.SYMBOL+" '>'> or <"+Tag.GEQ+" '>='> or <"+Tag.SYMBOL+" '<'> or <"+Tag.LEQ+" '<='> or <"+Tag.LOOP+" 'loop'> or <"+Tag.DOUBLEPOINT+" '..'> or <"+Tag.SYMBOL+" '+'> or <"+Tag.SYMBOL+" '-'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
        }

    }

    //EXPRESSION_6
    private void expression_6() throws IOException{
        // EXPRESSION_6 ::= EXPRESSION_7 EXPRESSION_MULT_DIV (lecture de ident)
        // EXPRESSION_6 ::= EXPRESSION_7 EXPRESSION_MULT_DIV (lecture de ( )
        // EXPRESSION_6 ::= EXPRESSION_7 EXPRESSION_MULT_DIV (lecture de entier)
        // EXPRESSION_6 ::= EXPRESSION_7 EXPRESSION_MULT_DIV (lecture de caractere)
        // EXPRESSION_6 ::= EXPRESSION_7 EXPRESSION_MULT_DIV (lecture de true)
        // EXPRESSION_6 ::= EXPRESSION_7 EXPRESSION_MULT_DIV (lecture de false)
        // EXPRESSION_6 ::= EXPRESSION_7 EXPRESSION_MULT_DIV (lecture de null)
        if ((current.getTag() == Tag.ID) || (current.getTag() == Tag.SYMBOL && current.getStringValue().equals("(")) || (current.getTag() == Tag.NUMCONST) || (current.getTag() == Tag.CHAR) || (current.getTag() == Tag.TRUE) || (current.getTag() == Tag.FALSE) || (current.getTag() == Tag.NULL)){
            this.expression_7();
            this.expression_mult_div();
            int temp = parser.stack.pop();
            if (temp == Tag.EXPRESSION_MULT_DIV){
                temp = parser.stack.pop();
                if (temp == Tag.EXPRESSION_7){
                    parser.stack.push(Tag.EXPRESSION_6);
                }
                else {
                    throw new Error("Reduction/Stack error : expected <"+Tag.EXPRESSION_7+"> but found <"+temp+">");
                }
            }
            else {
                throw new Error("Reduction/Stack error : expected <"+Tag.EXPRESSION_MULT_DIV+"> but found <"+temp+">");
            }
        }
        else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.ID + " 'ident'> or <" + Tag.SYMBOL + " '('> or <" + Tag.NUMCONST + " 'entier'> or <" + Tag.CHAR + " 'caractere'> or <" + Tag.TRUE + " 'true'> or <" + Tag.FALSE + " 'false'> or <" + Tag.NULL + " 'null'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
        }
    }

    //EXPRESSION_MULT_DIV
    private void expression_mult_div() throws IOException{
        // EXPRESSION_MULT_DIV ::= ε (lecture de ; )
        // EXPRESSION_MULT_DIV ::= ε (lecture de ) )
        // EXPRESSION_MULT_DIV ::= ε (lecture de , )
        // EXPRESSION_MULT_DIV ::= ε (lecture de or)
        // EXPRESSION_MULT_DIV ::= ε (lecture de and)
        // EXPRESSION_MULT_DIV ::= ε (lecture de then)
        // EXPRESSION_MULT_DIV ::= ε (lecture de = )
        // EXPRESSION_MULT_DIV ::= ε (lecture de /= )
        // EXPRESSION_MULT_DIV ::= ε (lecture de > )
        // EXPRESSION_MULT_DIV ::= ε (lecture de >= )
        // EXPRESSION_MULT_DIV ::= ε (lecture de < )
        // EXPRESSION_MULT_DIV ::= ε (lecture de <= )
        // EXPRESSION_MULT_DIV ::= ε (lecture de + )
        // EXPRESSION_MULT_DIV ::= ε (lecture de - )
        // EXPRESSION_MULT_DIV ::= ε (lecture de loop)
        // EXPRESSION_MULT_DIV ::= ε (lecture de .. )
        // EXPRESSION_MULT_DIV ::= * UNARY EXPRESSION_7 EXPRESSION_MULT_DIV (lecture de *)
        // EXPRESSION_MULT_DIV ::= / UNARY EXPRESSION_7 EXPRESSION_MULT_DIV (lecture de /)
        // EXPRESSION_MULT_DIV ::= rem UNARY EXPRESSION_7 EXPRESSION_MULT_DIV (lecture de rem)
        if ((current.getTag() == Tag.SYMBOL && current.getStringValue().equals(";")) || (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(")")) || (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(",")) || (current.getTag() == Tag.OR) || (current.getTag() == Tag.AND) || (current.getTag() == Tag.THEN) || (current.getTag() == Tag.SYMBOL && current.getStringValue().equals("=")) || (current.getTag() == Tag.DIFFERENT) || (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(">")) || (current.getTag() == Tag.GEQ) || (current.getTag() == Tag.SYMBOL && current.getStringValue().equals("<")) || (current.getTag() == Tag.LEQ) || (current.getTag() == Tag.SYMBOL && current.getStringValue().equals("+")) || (current.getTag() == Tag.SYMBOL && current.getStringValue().equals("-")) || (current.getTag() == Tag.LOOP) || (current.getTag() == Tag.DOUBLEPOINT)){
            parser.stack.push(Tag.EXPRESSION_MULT_DIV);
        }
        else if (current.getTag() == Tag.SYMBOL && current.getStringValue().equals("*")){
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.unary();
            this.expression_7();
            // semantic function
            int right = parser.ast.buffer.pop();
            int left = parser.ast.buffer.pop();
            int newNode = parser.ast.addNode("*");
            parser.ast.addEdge(newNode,right);
            parser.ast.addEdge(newNode,left);
            parser.ast.buffer.push(newNode);
            // end semantic function
            this.expression_mult_div();
            int temp = parser.stack.pop();
            if (temp == Tag.EXPRESSION_MULT_DIV) {
                temp = parser.stack.pop();
                if (temp == Tag.EXPRESSION_7) {
                    temp = parser.stack.pop();
                    if (temp == Tag.UNARY) {
                        temp = parser.stack.pop();
                        if (temp == Tag.SYMBOL) {
                            parser.stack.push(Tag.EXPRESSION_MULT_DIV);
                        } else {
                            throw new Error("Reduction/Stack error : expected <" + Tag.SYMBOL + "> but found <" + temp + ">");
                        }
                    } else {
                        throw new Error("Reduction/Stack error : expected <" + Tag.UNARY + "> but found <" + temp + ">");
                    }
                } else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.EXPRESSION_7 + "> but found <" + temp + ">");
                }
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.EXPRESSION_MULT_DIV + "> but found <" + temp + ">");
            }
        }
        else if (current.getTag() == Tag.SYMBOL && current.getStringValue().equals("/")){
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.unary();
            this.expression_7();
            // semantic function
            int right = parser.ast.buffer.pop();
            int left = parser.ast.buffer.pop();
            int newNode = parser.ast.addNode("/");
            parser.ast.addEdge(newNode,right);
            parser.ast.addEdge(newNode,left);
            parser.ast.buffer.push(newNode);
            // end semantic function
            this.expression_mult_div();
            int temp = parser.stack.pop();
            if (temp == Tag.EXPRESSION_MULT_DIV) {
                temp = parser.stack.pop();
                if (temp == Tag.EXPRESSION_7) {
                    temp = parser.stack.pop();
                    if (temp == Tag.UNARY) {
                        temp = parser.stack.pop();
                        if (temp == Tag.SYMBOL) {
                            parser.stack.push(Tag.EXPRESSION_MULT_DIV);
                        } else {
                            throw new Error("Reduction/Stack error : expected <" + Tag.SYMBOL + "> but found <" + temp + ">");
                        }
                    } else {
                        throw new Error("Reduction/Stack error : expected <" + Tag.UNARY + "> but found <" + temp + ">");
                    }
                } else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.EXPRESSION_7 + "> but found <" + temp + ">");
                }
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.EXPRESSION_MULT_DIV + "> but found <" + temp + ">");
            }
        }
        else if (current.getTag() == Tag.REM) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.unary();
            this.expression_7();
            // semantic function
            int right = parser.ast.buffer.pop();
            int left = parser.ast.buffer.pop();
            int newNode = parser.ast.addNode("rem");
            parser.ast.addEdge(newNode,right);
            parser.ast.addEdge(newNode,left);
            parser.ast.buffer.push(newNode);
            // end semantic function
            this.expression_mult_div();
            int temp = parser.stack.pop();
            if (temp == Tag.EXPRESSION_MULT_DIV) {
                temp = parser.stack.pop();
                if (temp == Tag.EXPRESSION_7) {
                    temp = parser.stack.pop();
                    if (temp == Tag.UNARY) {
                        temp = parser.stack.pop();
                        if (temp == Tag.REM) {
                            parser.stack.push(Tag.EXPRESSION_MULT_DIV);
                        } else {
                            throw new Error("Reduction/Stack error : expected <" + Tag.REM + "> but found <" + temp + ">");
                        }
                    } else {
                        throw new Error("Reduction/Stack error : expected <" + Tag.UNARY + "> but found <" + temp + ">");
                    }
                } else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.EXPRESSION_7 + "> but found <" + temp + ">");
                }
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.EXPRESSION_MULT_DIV + "> but found <" + temp + ">");
            }
        } else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <"+Tag.SYMBOL+" ';'> or <"+Tag.SYMBOL+" ')'> or <"+Tag.SYMBOL+" ','> or <"+Tag.OR+" 'or'> or <"+Tag.AND+" 'and'> or <"+Tag.THEN+" 'then'> or <"+Tag.SYMBOL+" '='> or <"+Tag.DIFFERENT+" '/='> or <"+Tag.SYMBOL+" '>'> or <"+Tag.GEQ+" '>='> or <"+Tag.SYMBOL+" '<'> or <"+Tag.LEQ+" '<='> or <"+Tag.SYMBOL+" '+'> or <"+Tag.SYMBOL+" '-'> or <"+Tag.LOOP+" 'loop'> or <"+Tag.DOUBLEPOINT+" '..'> or <"+Tag.REM+" 'rem'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
        }
    }

    //EXPRESSION_7
    private void expression_7() throws IOException{
        // EXPRESSION_7 ::= EXPRESSION_ATOMS EXPRESSION_ACCESS_IDENT (lecture de ident)
        // EXPRESSION_7 ::= EXPRESSION_ATOMS EXPRESSION_ACCESS_IDENT (lecture de ( )
        // EXPRESSION_7 ::= EXPRESSION_ATOMS EXPRESSION_ACCESS_IDENT (lecture de entier)
        // EXPRESSION_7 ::= EXPRESSION_ATOMS EXPRESSION_ACCESS_IDENT (lecture de caractere)
        // EXPRESSION_7 ::= EXPRESSION_ATOMS EXPRESSION_ACCESS_IDENT (lecture de true)
        // EXPRESSION_7 ::= EXPRESSION_ATOMS EXPRESSION_ACCESS_IDENT (lecture de false)
        // EXPRESSION_7 ::= EXPRESSION_ATOMS EXPRESSION_ACCESS_IDENT (lecture de null)
        if ((current.getTag() == Tag.ID) || (current.getTag() == Tag.SYMBOL && current.getStringValue().equals("(")) || (current.getTag() == Tag.NUMCONST) || (current.getTag() == Tag.CHAR) || (current.getTag() == Tag.TRUE) || (current.getTag() == Tag.FALSE) || (current.getTag() == Tag.NULL)){
            this.expression_atoms();
            this.expression_access_ident();
            int temp = parser.stack.pop();
            if (temp == Tag.EXPRESSION_ACCESS_IDENT){
                temp = parser.stack.pop();
                if (temp == Tag.EXPRESSION_ATOMS){
                    parser.stack.push(Tag.EXPRESSION_7);
                }
                else {
                    throw new Error("Reduction/Stack error : expected <"+Tag.EXPRESSION_ATOMS+"> but found <"+temp+">");
                }
            }
            else {
                throw new Error("Reduction/Stack error : expected <"+Tag.EXPRESSION_ACCESS_IDENT+"> but found <"+temp+">");
            }
        }
        else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.ID + " 'ident'> or <" + Tag.SYMBOL + " '('> or <" + Tag.NUMCONST + " 'entier'> or <" + Tag.CHAR + " 'caractere'> or <" + Tag.TRUE + " 'true'> or <" + Tag.FALSE + " 'false'> or <" + Tag.NULL + " 'null'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
        }
    }

    //EXPRESSION_ACCESS_IDENT
    private void expression_access_ident() throws IOException{
        // EXPRESSION_ACCESS_IDENT ::= ε (lecture de ; )
        // EXPRESSION_ACCESS_IDENT ::= ε (lecture de := )
        // EXPRESSION_ACCESS_IDENT ::= ε (lecture de ) )
        // EXPRESSION_ACCESS_IDENT ::= ε (lecture de , )
        // EXPRESSION_ACCESS_IDENT ::= ε (lecture de or)
        // EXPRESSION_ACCESS_IDENT ::= ε (lecture de and)
        // EXPRESSION_ACCESS_IDENT ::= ε (lecture de then)
        // EXPRESSION_ACCESS_IDENT ::= ε (lecture de = )
        // EXPRESSION_ACCESS_IDENT ::= ε (lecture de /= )
        // EXPRESSION_ACCESS_IDENT ::= ε (lecture de > )
        // EXPRESSION_ACCESS_IDENT ::= ε (lecture de >= )
        // EXPRESSION_ACCESS_IDENT ::= ε (lecture de < )
        // EXPRESSION_ACCESS_IDENT ::= ε (lecture de <= )
        // EXPRESSION_ACCESS_IDENT ::= ε (lecture de + )
        // EXPRESSION_ACCESS_IDENT ::= ε (lecture de - )
        // EXPRESSION_ACCESS_IDENT ::= ε (lecture de * )
        // EXPRESSION_ACCESS_IDENT ::= ε (lecture de / )
        // EXPRESSION_ACCESS_IDENT ::= ε (lecture de rem )
        // EXPRESSION_ACCESS_IDENT ::= ε (lecture de loop)
        // EXPRESSION_ACCESS_IDENT ::= ε (lecture de .. )
        // EXPRESSION_ACCESS_IDENT ::= . ident EXPRESSION_ACCESS_IDENT (lecture de .)
        if ((current.getTag() == Tag.SYMBOL && current.getStringValue().equals(";")) || (current.getTag() == Tag.ASSIGNMENT) || (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(")")) || (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(",")) || (current.getTag() == Tag.OR) || (current.getTag() == Tag.AND) || (current.getTag() == Tag.THEN) || (current.getTag() == Tag.SYMBOL && current.getStringValue().equals("=")) || (current.getTag() == Tag.DIFFERENT) || (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(">")) || (current.getTag() == Tag.GEQ) || (current.getTag() == Tag.SYMBOL && current.getStringValue().equals("<")) || (current.getTag() == Tag.LEQ) || (current.getTag() == Tag.SYMBOL && current.getStringValue().equals("+")) || (current.getTag() == Tag.SYMBOL && current.getStringValue().equals("-")) || (current.getTag() == Tag.SYMBOL && current.getStringValue().equals("*")) || (current.getTag() == Tag.SYMBOL && current.getStringValue().equals("/")) || (current.getTag() == Tag.REM) || (current.getTag() == Tag.LOOP) || (current.getTag() == Tag.DOUBLEPOINT)){
            parser.stack.push(Tag.EXPRESSION_ACCESS_IDENT);
        }
        else if (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(".")){
            parser.ast.addEdge(parser.ast.buffer.lastElement(), parser.ast.addNode("ACCESS_IDENT"));
            parser.ast.buffer.push(parser.ast.lastNode);
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            if (current.getTag() == Tag.ID) {
                parser.ast.addEdge(parser.ast.buffer.lastElement(), parser.ast.addNode(((Word)current).getStringValue()));
                parser.stack.push(current.getTag());
                current = parser.lexer.scan();
                this.expression_access_ident();
                int temp = parser.stack.pop();
                if (temp == Tag.EXPRESSION_ACCESS_IDENT) {
                    temp = parser.stack.pop();
                    if (temp == Tag.ID) {
                        temp = parser.stack.pop();
                        if (temp == Tag.SYMBOL) {
                            parser.stack.push(Tag.EXPRESSION_ACCESS_IDENT);
                            parser.ast.buffer.pop();
                        } else {
                            throw new Error("Reduction/Stack error : expected <" + Tag.SYMBOL + "> but found <" + temp + ">");
                        }
                    } else {
                        throw new Error("Reduction/Stack error : expected <" + Tag.ID + "> but found <" + temp + ">");
                    }
                } else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.EXPRESSION_ACCESS_IDENT + "> but found <" + temp + ">");
                }
            } else {
                throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.ID + " 'ident'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
            }
        }
        else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <"+Tag.SYMBOL+" ';'> or <"+Tag.ASSIGNMENT+" ':='> or <"+Tag.SYMBOL+" ')'> or <"+Tag.SYMBOL+" ','> or <"+Tag.OR+" 'or'> or <"+Tag.AND+" 'and'> or <"+Tag.THEN+" 'then'> or <"+Tag.SYMBOL+" '='> or <"+Tag.DIFFERENT+" '/='> or <"+Tag.SYMBOL+" '>'> or <"+Tag.GEQ+" '>='> or <"+Tag.SYMBOL+" '<'> or <"+Tag.LEQ+" '<='> or <"+Tag.SYMBOL+" '+'> or <"+Tag.SYMBOL+" '-'> or <"+Tag.SYMBOL+" '*'> or <"+Tag.SYMBOL+" '/'> or <"+Tag.REM+" 'rem'> or <"+Tag.LOOP+" 'loop'> or <"+Tag.DOUBLEPOINT+" '..'> or <"+Tag.SYMBOL+" '.'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
        }
    }

    //EXPRESSION_ATOMS
    private void expression_atoms() throws IOException{
        // EXPRESSION_ATOMS ::= ident START_NEW_EXPRESSION (lecture de ident)
        // EXPRESSION_ATOMS ::= ( EXPRESSION ) (lecture de ( )
        // EXPRESSION_ATOMS ::= entier (lecture de entier)
        // EXPRESSION_ATOMS ::= caractere (lecture de caractere)
        // EXPRESSION_ATOMS ::= true (lecture de true)
        // EXPRESSION_ATOMS ::= false (lecture de false)
        // EXPRESSION_ATOMS ::= null (lecture de null)
        if (current.getTag() == Tag.ID){
//            parser.ast.addEdge(parser.ast.buffer.lastElement(), parser.ast.addNode("IDENT_EXPRESSION"));
//            parser.ast.buffer.push(parser.ast.lastNode);
//            parser.ast.addEdge(parser.ast.buffer.lastElement(), parser.ast.addNode(((Word)current).getStringValue()));
            // semantic function
            int moins = parser.ast.buffer.pop();
            int ident = parser.ast.addNode(((Word)current).getStringValue());
            int tmp = ident;
            for (int i = 0; i < -moins; i++) {
                tmp = parser.ast.addNode("UNARY");
                parser.ast.addEdge(tmp,ident);
            }
            parser.ast.buffer.push(tmp);
            // end semantic function
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.start_new_expression();
            int temp = parser.stack.pop();
            if (temp == Tag.START_NEW_EXPRESSION){
                temp = parser.stack.pop();
                if (temp == Tag.ID){
                    parser.stack.push(Tag.EXPRESSION_ATOMS);
//                    parser.ast.buffer.pop();
                }
                else {
                    throw new Error("Reduction/Stack error : expected <"+Tag.ID+"> but found <"+temp+">");
                }
            }
            else {
                throw new Error("Reduction/Stack error : expected <"+Tag.START_NEW_EXPRESSION+"> but found <"+temp+">");
            }
        }
        else if (current.getTag() == Tag.SYMBOL && current.getStringValue().equals("(")) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.expression();
            if (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(")")) {
                parser.stack.push(current.getTag());
                current = parser.lexer.scan();
                int temp = parser.stack.pop();
                if (temp == Tag.SYMBOL) {
                    temp = parser.stack.pop();
                    if (temp == Tag.EXPRESSION) {
                        temp = parser.stack.pop();
                        if (temp == Tag.SYMBOL) {
                            parser.stack.push(Tag.EXPRESSION_ATOMS);
                        } else {
                            throw new Error("Reduction/Stack error : expected <" + Tag.SYMBOL + "> but found <" + temp + ">");
                        }
                    } else {
                        throw new Error("Reduction/Stack error : expected <" + Tag.EXPRESSION + "> but found <" + temp + ">");
                    }
                } else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.SYMBOL + "> but found <" + temp + ">");
                }
            } else {
                throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.SYMBOL + " ')'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
            }
        }
        else if (current.getTag() == Tag.NUMCONST) {
            // semantic function
            int moins = parser.ast.buffer.pop();
            int ident = parser.ast.addNode(((Num)current).getStringValue());
            int tmp = ident;
            for (int i = 0; i < -moins; i++) {
                tmp = parser.ast.addNode("UNARY");
                parser.ast.addEdge(tmp,ident);
            }
            parser.ast.buffer.push(tmp);
            // end semantic function
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            int temp = parser.stack.pop();
            if (temp == Tag.NUMCONST) {
                parser.stack.push(Tag.EXPRESSION_ATOMS);
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.NUMCONST + "> but found <" + temp + ">");
            }
        }
        else if (current.getTag() == Tag.CHAR) {
            // semantic function
            parser.ast.buffer.push(parser.ast.addNode(((Char)current).getStringValue()));
            // end semantic function
            current = parser.lexer.scan();
            int temp = parser.stack.pop();
            if (temp == Tag.CHAR) {
                parser.stack.push(Tag.EXPRESSION_ATOMS);
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.CHAR + "> but found <" + temp + ">");
            }
        }
        else if (current.getTag() == Tag.TRUE) {
            // semantic function
            parser.ast.buffer.push(parser.ast.addNode(((Word)current).getStringValue()));
            // end semantic function
            current = parser.lexer.scan();
            int temp = parser.stack.pop();
            if (temp == Tag.TRUE) {
                parser.stack.push(Tag.EXPRESSION_ATOMS);
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.TRUE + "> but found <" + temp + ">");
            }
        }
        else if (current.getTag() == Tag.FALSE) {
            // semantic function
            parser.ast.buffer.push(parser.ast.addNode(((Word)current).getStringValue()));
            // end semantic function
            current = parser.lexer.scan();
            int temp = parser.stack.pop();
            if (temp == Tag.FALSE) {
                parser.stack.push(Tag.EXPRESSION_ATOMS);
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.FALSE + "> but found <" + temp + ">");
            }
        }
        else if (current.getTag() == Tag.NULL) {
            // semantic function
            parser.ast.buffer.push(parser.ast.addNode(((Word)current).getStringValue()));
            // end semantic function
            current = parser.lexer.scan();
            int temp = parser.stack.pop();
            if (temp == Tag.NULL) {
                parser.stack.push(Tag.EXPRESSION_ATOMS);
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.NULL + "> but found <" + temp + ">");
            }
        }
        else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.ID + " 'ident'> or <" + Tag.SYMBOL + " '('> or <" + Tag.NUMCONST + " 'entier'> or <" + Tag.CHAR + " 'caractere'> or <" + Tag.TRUE + " 'true'> or <" + Tag.FALSE + " 'false'> or <" + Tag.NULL + " 'null'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
        }
    }

    //UNARY
    private void unary() throws IOException{
        // UNARY ::= ε (lecture de ident)
        // UNARY ::= ε (lecture de ( )
        // UNARY ::= ε (lecture de new)
        // UNARY ::= ε (lecture de character'val)
        // UNARY ::= ε (lecture de else)
        // UNARY ::= ε (lecture de then)
        // UNARY ::= ε (lecture de not)
        // UNARY ::= ε (lecture de entier)
        // UNARY ::= ε (lecture de caractere)
        // UNARY ::= ε (lecture de true)
        // UNARY ::= ε (lecture de false)
        // UNARY ::= ε (lecture de null)
        // UNARY ::= - UNARY (lecture de -)
        if ((current.getTag() == Tag.ID) || (current.getTag() == Tag.SYMBOL && current.getStringValue().equals("(")) || (current.getTag() == Tag.NEW) || (current.getTag() == Tag.CHARACTERVAL) || (current.getTag() == Tag.ELSE) || (current.getTag() == Tag.THEN) || (current.getTag() == Tag.NOT) || (current.getTag() == Tag.NUMCONST) || (current.getTag() == Tag.CHAR) || (current.getTag() == Tag.TRUE) || (current.getTag() == Tag.FALSE) || (current.getTag() == Tag.NULL)){
            // semantic functions
            if (parser.ast.buffer.lastElement() >= 0) {
                parser.ast.buffer.push(0);
            }
            // end semantic functions
            parser.stack.push(Tag.UNARY);
        }
        else if (current.getTag() == Tag.SYMBOL && current.getStringValue().equals("-")) {
            // semantic functions
            if (parser.ast.buffer.lastElement() >= 0) {
                parser.ast.buffer.push(-1);
            } else {
                parser.ast.buffer.push(parser.ast.buffer.pop() - 1);
            }
            // end semantic functions
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.unary();
            int temp = parser.stack.pop();
            if (temp == Tag.UNARY) {
                temp = parser.stack.pop();
                if (temp == Tag.SYMBOL) {
                    parser.stack.push(Tag.UNARY);
                } else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.SYMBOL + "> but found <" + temp + ">");
                }
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.UNARY + "> but found <" + temp + ">");
            }
        }
        else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <"+Tag.ID+" 'ident'> or <"+Tag.SYMBOL+" '('> or <"+Tag.NEW+" 'new'> or <"+Tag.CHARACTERVAL+" 'character'val'> or <"+Tag.ELSE+" 'else'> or <"+Tag.THEN+" 'then'> or <"+Tag.NOT+" 'not'> or <"+Tag.NUMCONST+" 'entier'> or <"+Tag.CHAR+" 'caractere'> or <"+Tag.TRUE+" 'true'> or <"+Tag.FALSE+" 'false'> or <"+Tag.NULL+" 'null'> or <"+Tag.SYMBOL+" '-'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
        }
    }

    //START_NEW_EXPRESSION
    private void start_new_expression() throws IOException{
        // START_NEW_EXPRESSION ::= ε (lecture de . )
        // START_NEW_EXPRESSION ::= ε (lecture de ; )
        // START_NEW_EXPRESSION ::= ε (lecture de ) )
        // START_NEW_EXPRESSION ::= ε (lecture de , )
        // START_NEW_EXPRESSION ::= ε (lecture de or)
        // START_NEW_EXPRESSION ::= ε (lecture de and)
        // START_NEW_EXPRESSION ::= ε (lecture de then)
        // START_NEW_EXPRESSION ::= ε (lecture de = )
        // START_NEW_EXPRESSION ::= ε (lecture de /= )
        // START_NEW_EXPRESSION ::= ε (lecture de > )
        // START_NEW_EXPRESSION ::= ε (lecture de >= )
        // START_NEW_EXPRESSION ::= ε (lecture de < )
        // START_NEW_EXPRESSION ::= ε (lecture de <= )
        // START_NEW_EXPRESSION ::= ε (lecture de + )
        // START_NEW_EXPRESSION ::= ε (lecture de - )
        // START_NEW_EXPRESSION ::= ε (lecture de * )
        // START_NEW_EXPRESSION ::= ε (lecture de / )
        // START_NEW_EXPRESSION ::= ε (lecture de rem )
        // START_NEW_EXPRESSION ::= ε (lecture de loop)
        // START_NEW_EXPRESSION ::= ε (lecture de .. )
        // START_NEW_EXPRESSION ::= ( UNARY GENERATE_EXPRESSION (lecture de ( )
        if ((current.getTag() == Tag.SYMBOL && current.getStringValue().equals(".")) || (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(";")) || (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(")")) || (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(",")) || (current.getTag() == Tag.OR) || (current.getTag() == Tag.AND) || (current.getTag() == Tag.THEN) || (current.getTag() == Tag.SYMBOL && current.getStringValue().equals("=")) || (current.getTag() == Tag.DIFFERENT) || (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(">")) || (current.getTag() == Tag.GEQ) || (current.getTag() == Tag.SYMBOL && current.getStringValue().equals("<")) || (current.getTag() == Tag.LEQ) || (current.getTag() == Tag.SYMBOL && current.getStringValue().equals("+")) || (current.getTag() == Tag.SYMBOL && current.getStringValue().equals("-")) || (current.getTag() == Tag.SYMBOL && current.getStringValue().equals("*")) || (current.getTag() == Tag.SYMBOL && current.getStringValue().equals("/")) || (current.getTag() == Tag.REM) || (current.getTag() == Tag.LOOP) || (current.getTag() == Tag.DOUBLEPOINT)){
            parser.stack.push(Tag.START_NEW_EXPRESSION);
        }
        else if (current.getTag() == Tag.SYMBOL && current.getStringValue().equals("(")) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.unary();
            this.generate_expression();
            int temp = parser.stack.pop();
            if (temp == Tag.GENERATE_EXPRESSION) {
                temp = parser.stack.pop();
                if (temp == Tag.UNARY) {
                    temp = parser.stack.pop();
                    if (temp == Tag.SYMBOL) {
                        parser.stack.push(Tag.START_NEW_EXPRESSION);
                    } else {
                        throw new Error("Reduction/Stack error : expected <" + Tag.SYMBOL + "> but found <" + temp + ">");
                    }
                } else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.UNARY + "> but found <" + temp + ">");
                }
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.GENERATE_EXPRESSION + "> but found <" + temp + ">");
            }
        }
        else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.SYMBOL + " '.'> or <" + Tag.SYMBOL + " ';'> or <" + Tag.SYMBOL + " ')'> or <" + Tag.SYMBOL + " ','> or <" + Tag.OR + " 'or'> or <" + Tag.AND + " 'and'> or <" + Tag.THEN + " 'then'> or <" + Tag.SYMBOL + " '='> or <" + Tag.DIFFERENT + " '/='> or <" + Tag.SYMBOL + " '>'> or <" + Tag.GEQ + " '>='> or <" + Tag.SYMBOL + " '<'> or <" + Tag.LEQ + " '<='> or <" + Tag.SYMBOL + " '+'> or <" + Tag.SYMBOL + " '-'> or <" + Tag.SYMBOL + " '*'> or <" + Tag.SYMBOL + " '/'> or <" + Tag.REM + " 'rem'> or <" + Tag.LOOP + " 'loop'> or <" + Tag.DOUBLEPOINT + " '..'> or <" + Tag.SYMBOL + " '('> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
        }
    }

    //GENERATE_EXPRESSION
    private void generate_expression() throws IOException{
        // GENERATE_EXPRESSION ::= EXPRESSION END_GENERATE_EXPRESSION (lecture de ident)
        // GENERATE_EXPRESSION ::= EXPRESSION END_GENERATE_EXPRESSION (lecture de ( )
        // GENERATE_EXPRESSION ::= EXPRESSION END_GENERATE_EXPRESSION (lecture de new)
        // GENERATE_EXPRESSION ::= EXPRESSION END_GENERATE_EXPRESSION (lecture de character'val)
        // GENERATE_EXPRESSION ::= EXPRESSION END_GENERATE_EXPRESSION (lecture de not)
        // GENERATE_EXPRESSION ::= EXPRESSION END_GENERATE_EXPRESSION (lecture de entier)
        // GENERATE_EXPRESSION ::= EXPRESSION END_GENERATE_EXPRESSION (lecture de caractere)
        // GENERATE_EXPRESSION ::= EXPRESSION END_GENERATE_EXPRESSION (lecture de true)
        // GENERATE_EXPRESSION ::= EXPRESSION END_GENERATE_EXPRESSION (lecture de false)
        // GENERATE_EXPRESSION ::= EXPRESSION END_GENERATE_EXPRESSION (lecture de null)
        if ((current.getTag() == Tag.ID) || (current.getTag() == Tag.SYMBOL && current.getStringValue().equals("(")) || (current.getTag() == Tag.NEW) || (current.getTag() == Tag.CHARACTERVAL) || (current.getTag() == Tag.NOT) || (current.getTag() == Tag.NUMCONST) || (current.getTag() == Tag.CHAR) || (current.getTag() == Tag.TRUE) || (current.getTag() == Tag.FALSE) || (current.getTag() == Tag.NULL)){
            this.expression();
            // semantic functions
            int expr = parser.ast.buffer.pop();
            parser.ast.addEdge(parser.ast.buffer.lastElement(), expr);
            // end semantic functions
            this.end_generate_expression();
            int temp = parser.stack.pop();
            if (temp == Tag.END_GENERATE_EXPRESSION){
                temp = parser.stack.pop();
                if (temp == Tag.EXPRESSION){
                    parser.stack.push(Tag.GENERATE_EXPRESSION);
                }
                else {
                    throw new Error("Reduction/Stack error : expected <"+Tag.EXPRESSION+"> but found <"+temp+">");
                }
            }
            else {
                throw new Error("Reduction/Stack error : expected <"+Tag.END_GENERATE_EXPRESSION+"> but found <"+temp+">");
            }
        }
        else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.ID + " 'ident'> or <" + Tag.SYMBOL + " '('> or <" + Tag.NEW + " 'new'> or <" + Tag.CHARACTERVAL + " 'character'val'> or <" + Tag.NOT + " 'not'> or <" + Tag.NUMCONST + " 'entier'> or <" + Tag.CHAR + " 'caractere'> or <" + Tag.TRUE + " 'true'> or <" + Tag.FALSE + " 'false'> or <" + Tag.NULL + " 'null'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
        }
    }

    //END_GENERATE_EXPRESSION
    private void end_generate_expression() throws IOException{
        // END_GENERATE_EXPRESSION ::= ) (lecture de ) )
        // END_GENERATE_EXPRESSION ::= , UNARY GENERATE_EXPRESSION (lecture de , )
        if (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(")")) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            int temp = parser.stack.pop();
            if (temp == Tag.SYMBOL) {
                parser.stack.push(Tag.END_GENERATE_EXPRESSION);
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.SYMBOL + "> but found <" + temp + ">");
            }
        }
        else if (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(",")) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.unary();
            this.generate_expression();
            int temp = parser.stack.pop();
            if (temp == Tag.GENERATE_EXPRESSION) {
                temp = parser.stack.pop();
                if (temp == Tag.UNARY) {
                    temp = parser.stack.pop();
                    if (temp == Tag.SYMBOL) {
                        parser.stack.push(Tag.END_GENERATE_EXPRESSION);
                    } else {
                        throw new Error("Reduction/Stack error : expected <" + Tag.SYMBOL + "> but found <" + temp + ">");
                    }
                } else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.UNARY + "> but found <" + temp + ">");
                }
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.GENERATE_EXPRESSION + "> but found <" + temp + ">");
            }
        }
        else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.SYMBOL + " ')'> or <" + Tag.SYMBOL + " ','> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
        }
    }

    //INSTRUCTION
    private void instruction() throws IOException{
        // INSTRUCTION ::= ident INSTRUCTION_IDENT_EXPRESSION (lecture de ident)
        // INSTRUCTION ::= begin GENERATE_INSTRUCTIONS end ; (lecture de begin)
        // INSTRUCTION ::= return END_RETURN (lecture de return)
        // INSTRUCTION ::= WI_EXPRESSION := UNARY EXPRESSION ; (lecture de ( )
        // INSTRUCTION ::= WI_EXPRESSION := UNARY EXPRESSION ; (lecture de new)
        // INSTRUCTION ::= WI_EXPRESSION := UNARY EXPRESSION ; (lecture de character'val)
        // INSTRUCTION ::= WI_EXPRESSION := UNARY EXPRESSION ; (lecture de not)
        // INSTRUCTION ::= WI_EXPRESSION := UNARY EXPRESSION ; (lecture de entier)
        // INSTRUCTION ::= WI_EXPRESSION := UNARY EXPRESSION ; (lecture de caractere)
        // INSTRUCTION ::= WI_EXPRESSION := UNARY EXPRESSION ; (lecture de true)
        // INSTRUCTION ::= WI_EXPRESSION := UNARY EXPRESSION ; (lecture de false)
        // INSTRUCTION ::= WI_EXPRESSION := UNARY EXPRESSION ; (lecture de null)
        // INSTRUCTION ::= if UNARY EXPRESSION then GENERATE_INSTRUCTIONS NEXT_IF (lecture de if)
        // INSTRUCTION ::= for ident in FOR_INSTRUCTION (lecture de for)
        // INSTRUCTION ::= while UNARY EXPRESSION loop GENERATE_INSTRUCTIONS end loop ; (lecture de while)
        if (current.getTag() == Tag.ID){
            // semantic function
            parser.ast.buffer.push(parser.ast.addNode(((Word)current).getStringValue()));
            // end semantic function
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.instruction_ident_expression();
            int temp = parser.stack.pop();
            if (temp == Tag.INSTRUCTION_IDENT_EXPRESSION){
                temp = parser.stack.pop();
                if (temp == Tag.ID) {
                    parser.stack.push(Tag.INSTRUCTION);
                }
                else {
                    throw new Error("Reduction/Stack error : expected <"+Tag.ID+"> but found <"+temp+">");
                }
            }
            else {
                throw new Error("Reduction/Stack error : expected <"+Tag.INSTRUCTION_IDENT_EXPRESSION+"> but found <"+temp+">");
            }
        }
        else if (current.getTag() == Tag.BEGIN) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            // semantic function
            parser.ast.buffer.push(parser.ast.addNode("BLOCK_INSTRCUTIONS"));
            // end semantic function
            this.generate_instructions();
            if (current.getTag() == Tag.END) {
                parser.stack.push(current.getTag());
                current = parser.lexer.scan();
                if (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(";")) {
                    parser.stack.push(current.getTag());
                    current = parser.lexer.scan();
                    int temp = parser.stack.pop();
                    if (temp == Tag.SYMBOL) {
                        temp = parser.stack.pop();
                        if (temp == Tag.END) {
                            temp = parser.stack.pop();
                            if (temp == Tag.GENERATE_INSTRUCTIONS) {
                                temp = parser.stack.pop();
                                if (temp == Tag.BEGIN) {
                                    parser.stack.push(Tag.INSTRUCTION);
                                    // semantic function
                                    parser.ast.buffer.pop();
                                    // end semantic function
                                } else {
                                    throw new Error("Reduction/Stack error : expected <" + Tag.BEGIN + "> but found <" + temp + ">");
                                }
                            } else {
                                throw new Error("Reduction/Stack error : expected <" + Tag.GENERATE_INSTRUCTIONS + "> but found <" + temp + ">");
                            }
                        } else {
                            throw new Error("Reduction/Stack error : expected <" + Tag.END + "> but found <" + temp + ">");
                        }
                    } else {
                        throw new Error("Reduction/Stack error : expected <" + Tag.SYMBOL + "> but found <" + temp + ">");
                    }
                } else {
                    throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.SYMBOL + " ';'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
                }
            } else {
                throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.END + " 'end'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
            }
        }
        else if (current.getTag() == Tag.RETURN) {
            // semantic functions
            parser.ast.addEdge(parser.ast.buffer.lastElement(), parser.ast.addNode("RETURN_EXPRESSION"));
            parser.ast.buffer.push(parser.ast.lastNode);
            // end semantic functions
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.end_return();
            // semantic functions
            int expr = parser.ast.buffer.pop();
            parser.ast.addEdge(parser.ast.buffer.lastElement(), expr);
            parser.ast.buffer.pop();
            // end semantic functions
            int temp = parser.stack.pop();
            if (temp == Tag.END_RETURN) {
                temp = parser.stack.pop();
                if (temp == Tag.RETURN) {
                    parser.stack.push(Tag.INSTRUCTION);
                    // semantic functions
                    parser.ast.buffer.pop();
                    // end semantic functions
                } else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.RETURN + "> but found <" + temp + ">");
                }
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.END_RETURN + "> but found <" + temp + ">");
            }
        }
        else if ((current.getTag() == Tag.SYMBOL && current.getStringValue().equals("(")) || (current.getTag() == Tag.NEW) || (current.getTag() == Tag.CHARACTERVAL) || (current.getTag() == Tag.NOT) || (current.getTag() == Tag.NUMCONST) || (current.getTag() == Tag.CHAR) || (current.getTag() == Tag.TRUE) || (current.getTag() == Tag.FALSE) || (current.getTag() == Tag.NULL)) {
            this.wi_expression();
            if (!this.checkIdent) {
                throw new Error("Error line "+parser.lexer.getLine()+" : expected '. ident' got ':='");
            }
            if (current.getTag() == Tag.ASSIGNMENT) {
                parser.stack.push(current.getTag());
                current = parser.lexer.scan();
                this.unary();
                this.expression();
                if (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(";")) {
                    parser.stack.push(current.getTag());
                    current = parser.lexer.scan();
                    int tmp = parser.stack.pop();
                    if (tmp == Tag.SYMBOL) {
                        tmp = parser.stack.pop();
                        if (tmp == Tag.EXPRESSION) {
                            tmp = parser.stack.pop();
                            if (tmp == Tag.UNARY) {
                                tmp = parser.stack.pop();
                                if (tmp == Tag.ASSIGNMENT) {
                                    tmp = parser.stack.pop();
                                    if (tmp == Tag.WI_EXPRESSION) {
                                        parser.stack.push(Tag.INSTRUCTION);
                                    } else {
                                        throw new Error("Reduction/Stack error : expected <" + Tag.WI_EXPRESSION + "> but found <" + tmp + ">");
                                    }
                                } else {
                                    throw new Error("Reduction/Stack error : expected <" + Tag.ASSIGNMENT + "> but found <" + tmp + ">");
                                }
                            } else {
                                throw new Error("Reduction/Stack error : expected <" + Tag.UNARY + "> but found <" + tmp + ">");
                            }
                        } else {
                            throw new Error("Reduction/Stack error : expected <" + Tag.EXPRESSION + "> but found <" + tmp + ">");
                        }
                    } else {
                        throw new Error("Reduction/Stack error : expected <" + Tag.SYMBOL + "> but found <" + tmp + ">");
                    }
                } else {
                    throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.SYMBOL + " ';'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
                }
            } else {
                throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.ASSIGNMENT + " ':='> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
            }
        }
        else if (current.getTag() == Tag.IF) {
            // semantic functions
            parser.ast.addEdge(parser.ast.buffer.lastElement(), parser.ast.addNode("IF"));
            parser.ast.buffer.push(parser.ast.lastNode);
            parser.ast.addEdge(parser.ast.buffer.lastElement(), parser.ast.addNode("CONDITION"));
            parser.ast.buffer.push(parser.ast.lastNode);
            // end functions
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.unary();
            this.expression();
            // semantic functions
            int expr = parser.ast.buffer.pop();
            parser.ast.addEdge(parser.ast.buffer.lastElement(), expr);
            parser.ast.buffer.pop();
            // end functions
            if (current.getTag() == Tag.THEN) {
                parser.stack.push(current.getTag());
                current = parser.lexer.scan();
                // semantic functions
                parser.ast.addEdge(parser.ast.buffer.lastElement(), parser.ast.addNode("INSTRUCTIONS"));
                parser.ast.buffer.push(parser.ast.lastNode);
                // end functions
                this.generate_instructions();
                System.out.println(parser.ast.buffer);
                // semantic functions
                parser.ast.buffer.pop();
                // end functions
                this.next_if();
                int temp = parser.stack.pop();
                if (temp == Tag.NEXT_IF) {
                    temp = parser.stack.pop();
                    if (temp == Tag.GENERATE_INSTRUCTIONS) {
                        temp = parser.stack.pop();
                        if (temp == Tag.THEN) {
                            temp = parser.stack.pop();
                            if (temp == Tag.EXPRESSION) {
                                temp = parser.stack.pop();
                                if (temp == Tag.UNARY) {
                                    temp = parser.stack.pop();
                                    if (temp == Tag.IF) {
                                        parser.stack.push(Tag.INSTRUCTION);
                                        // semantic functions
                                        parser.ast.buffer.pop();
                                        // end functions
                                    } else {
                                        throw new Error("Reduction/Stack error : expected <" + Tag.IF + "> but found <" + temp + ">");
                                    }
                                } else {
                                    throw new Error("Reduction/Stack error : expected <" + Tag.UNARY + "> but found <" + temp + ">");
                                }
                            } else {
                                throw new Error("Reduction/Stack error : expected <" + Tag.EXPRESSION + "> but found <" + temp + ">");
                            }
                        } else {
                            throw new Error("Reduction/Stack error : expected <" + Tag.THEN + "> but found <" + temp + ">");
                        }
                    } else {
                        throw new Error("Reduction/Stack error : expected <" + Tag.GENERATE_INSTRUCTIONS + "> but found <" + temp + ">");
                    }
                } else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.NEXT_IF + "> but found <" + temp + ">");
                }
            } else {
                throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.THEN + " 'then'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
            }
        }
        else if (current.getTag() == Tag.FOR) {
            // semantic functions
            parser.ast.addEdge(parser.ast.buffer.lastElement(), parser.ast.addNode("FOR"));
            parser.ast.buffer.push(parser.ast.lastNode);
            // end functions
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            if (current.getTag() == Tag.ID) {
                parser.stack.push(current.getTag());
                // semantic functions
                parser.ast.addEdge(parser.ast.buffer.lastElement(), parser.ast.addNode(((Word)current).getStringValue()));
                // end semantic functions
                current = parser.lexer.scan();
                if (current.getTag() == Tag.IN) {
                    parser.stack.push(current.getTag());
                    current = parser.lexer.scan();
                    this.for_instruction();
                    int temp = parser.stack.pop();
                    if (temp == Tag.FOR_INSTRUCTION) {
                        temp = parser.stack.pop();
                        if (temp == Tag.IN) {
                            temp = parser.stack.pop();
                            if (temp == Tag.ID) {
                                temp = parser.stack.pop();
                                if (temp == Tag.FOR) {
                                    parser.stack.push(Tag.INSTRUCTION);
                                } else {
                                    throw new Error("Reduction/Stack error : expected <" + Tag.FOR + "> but found <" + temp + ">");
                                }
                            } else {
                                throw new Error("Reduction/Stack error : expected <" + Tag.ID + "> but found <" + temp + ">");
                            }
                        } else {
                            throw new Error("Reduction/Stack error : expected <" + Tag.IN + "> but found <" + temp + ">");
                        }
                    } else {
                        throw new Error("Reduction/Stack error : expected <" + Tag.FOR_INSTRUCTION + "> but found <" + temp + ">");
                    }
                } else {
                    throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.IN + " 'in'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
                }
            } else {
                throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.ID + " 'ident'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
            }
        }
        else if (current.getTag() == Tag.WHILE) {
            // semantic functions
            parser.ast.addEdge(parser.ast.buffer.lastElement(), parser.ast.addNode("WHILE"));
            parser.ast.buffer.push(parser.ast.lastNode);
            // end functions
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.unary();
            this.expression();
            if (current.getTag() == Tag.LOOP) {
                parser.stack.push(current.getTag());
                current = parser.lexer.scan();
                this.generate_instructions();
                if (current.getTag() == Tag.END) {
                    parser.stack.push(current.getTag());
                    current = parser.lexer.scan();
                    if (current.getTag() == Tag.LOOP) {
                        parser.stack.push(current.getTag());
                        current = parser.lexer.scan();
                        if (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(";")) {
                            parser.stack.push(current.getTag());
                            current = parser.lexer.scan();
                            int temp = parser.stack.pop();
                            if (temp == Tag.SYMBOL) {
                                temp = parser.stack.pop();
                                if (temp == Tag.LOOP) {
                                    temp = parser.stack.pop();
                                    if (temp == Tag.END) {
                                        temp = parser.stack.pop();
                                        if (temp == Tag.GENERATE_INSTRUCTIONS) {
                                            temp = parser.stack.pop();
                                            if (temp == Tag.LOOP) {
                                                temp = parser.stack.pop();
                                                if (temp == Tag.EXPRESSION) {
                                                    temp = parser.stack.pop();
                                                    if (temp == Tag.UNARY) {
                                                        temp = parser.stack.pop();
                                                        if (temp == Tag.WHILE) {
                                                            parser.stack.push(Tag.INSTRUCTION);
                                                            // semantic functions
                                                            parser.ast.buffer.pop();
                                                            // end functions
                                                        } else {
                                                            throw new Error("Reduction/Stack error : expected <" + Tag.WHILE + "> but found <" + temp + ">");
                                                        }
                                                    } else {
                                                        throw new Error("Reduction/Stack error : expected <" + Tag.UNARY + "> but found <" + temp + ">");
                                                    }
                                                } else {
                                                    throw new Error("Reduction/Stack error : expected <" + Tag.EXPRESSION + "> but found <" + temp + ">");
                                                }
                                            } else {
                                                throw new Error("Reduction/Stack error : expected <" + Tag.LOOP + "> but found <" + temp + ">");
                                            }
                                        } else {
                                            throw new Error("Reduction/Stack error : expected <" + Tag.GENERATE_INSTRUCTIONS + "> but found <" + temp + ">");
                                        }
                                    } else {
                                        throw new Error("Reduction/Stack error : expected <" + Tag.END + "> but found <" + temp + ">");
                                    }
                                } else {
                                    throw new Error("Reduction/Stack error : expected <" + Tag.LOOP + "> but found <" + temp + ">");
                                }
                            } else {
                                throw new Error("Reduction/Stack error : expected <" + Tag.SYMBOL + "> but found <" + temp + ">");
                            }
                        } else {
                            throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.SYMBOL + " ';'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
                        }
                    } else {
                        throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.LOOP + " 'loop'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
                    }
                } else {
                    throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.END + " 'end'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
                }
            } else {
                throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.LOOP + " 'loop'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
            }
        } else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.ID + " 'ident'> or <" + Tag.BEGIN + " 'begin'> or <" + Tag.RETURN + " 'return'> or <" + Tag.SYMBOL + " '('> or <" + Tag.NEW + " 'new'> or <" + Tag.CHARACTERVAL + " 'character'val'> or <" + Tag.NOT + " 'not'> or <" + Tag.NUMCONST + " 'entier'> or <" + Tag.CHAR + " 'caractere'> or <" + Tag.TRUE + " 'true'> or <" + Tag.FALSE + " 'false'> or <" + Tag.NULL + " 'null'> or <" + Tag.IF + " 'if'> or <" + Tag.FOR + " 'for'> or <" + Tag.WHILE + " 'while'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
        }
    }

    //INSTRUCTION_IDENT_EXPRESSION
    private void instruction_ident_expression() throws IOException{
        // INSTRUCTION_IDENT_EXPRESSION ::= INSTRUCTION_IDENT_EXPRESSION1 (lecture de .)
        // INSTRUCTION_IDENT_EXPRESSION ::= INSTRUCTION_IDENT_EXPRESSION1 (lecture de ; )
        // INSTRUCTION_IDENT_EXPRESSION ::= INSTRUCTION_IDENT_EXPRESSION1 (lecture de :=)
        // INSTRUCTION_IDENT_EXPRESSION ::= INSTRUCTION_IDENT_EXPRESSION2 (lecture de ( )
        if ((current.getTag() == Tag.SYMBOL && current.getStringValue().equals(".")) || (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(";")) || (current.getTag() == Tag.ASSIGNMENT)){
            this.instruction_ident_expression1();
            int temp = parser.stack.pop();
            if (temp == Tag.INSTRUCTION_IDENT_EXPRESSION1){
                parser.stack.push(Tag.INSTRUCTION_IDENT_EXPRESSION);
            }
            else {
                throw new Error("Reduction/Stack error : expected <"+Tag.INSTRUCTION_IDENT_EXPRESSION1+"> but found <"+temp+">");
            }
        }
        else if (current.getTag() == Tag.SYMBOL && current.getStringValue().equals("(")) {
            this.instruction_ident_expression2();
            int temp = parser.stack.pop();
            if (temp == Tag.INSTRUCTION_IDENT_EXPRESSION2){
                parser.stack.push(Tag.INSTRUCTION_IDENT_EXPRESSION);
            }
            else {
                throw new Error("Reduction/Stack error : expected <"+Tag.INSTRUCTION_IDENT_EXPRESSION2+"> but found <"+temp+">");
            }
        }
        else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.SYMBOL + " '.'> or <" + Tag.SYMBOL + " ';'> or <" + Tag.ASSIGNMENT + " ':='> or <" + Tag.SYMBOL + " '('> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");

        }
    }

    private void instruction_ident_expression1() throws IOException{
        // INSTRUCTION_IDENT_EXPRESSION1 ::= . ident INSTRUCTION_IDENT_EXPRESSION1 (lecture de .)
        // INSTRUCTION_IDENT_EXPRESSION1 ::= ; (lecture de ; )
        // INSTRUCTION_IDENT_EXPRESSION1 ::= := UNARY EXPRESSION ; (lecture de :=)
        if (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(".")) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            if (current.getTag() == Tag.ID) {
                parser.stack.push(current.getTag());
                current = parser.lexer.scan();
                this.instruction_ident_expression1();
                int temp = parser.stack.pop();
                if (temp == Tag.INSTRUCTION_IDENT_EXPRESSION1) {
                    temp = parser.stack.pop();
                    if (temp == Tag.ID) {
                        temp = parser.stack.pop();
                        if (temp == Tag.SYMBOL) {
                            parser.stack.push(Tag.INSTRUCTION_IDENT_EXPRESSION1);
                        } else {
                            throw new Error("Reduction/Stack error : expected <" + Tag.SYMBOL + "> but found <" + temp + ">");
                        }
                    } else {
                        throw new Error("Reduction/Stack error : expected <" + Tag.ID + "> but found <" + temp + ">");
                    }
                } else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.INSTRUCTION_IDENT_EXPRESSION1 + "> but found <" + temp + ">");
                }
            }
        }
        else if (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(";")) {
            current = parser.lexer.scan();
            parser.stack.push(Tag.INSTRUCTION_IDENT_EXPRESSION1);
        }
        else if (current.getTag() == Tag.ASSIGNMENT) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.unary();
            this.expression();
            // semantic function
            int right = parser.ast.buffer.pop();
            int left = parser.ast.buffer.pop();
            int newNode = parser.ast.addNode(":=");
            parser.ast.addEdge(parser.ast.buffer.lastElement(), newNode);
            parser.ast.addEdge(newNode,left);
            parser.ast.addEdge(newNode,right);
            // end semantic function
            if (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(";")) {
                parser.stack.push(current.getTag());
                current = parser.lexer.scan();
                int temp = parser.stack.pop();
                if (temp == Tag.SYMBOL) {
                    temp = parser.stack.pop();
                    if (temp == Tag.EXPRESSION) {
                        temp = parser.stack.pop();
                        if (temp == Tag.UNARY) {
                            temp = parser.stack.pop();
                            if (temp == Tag.ASSIGNMENT) {
                                parser.stack.push(Tag.INSTRUCTION_IDENT_EXPRESSION1);
                            } else {
                                throw new Error("Reduction/Stack error : expected <" + Tag.ASSIGNMENT + "> but found <" + temp + ">");
                            }
                        } else {
                            throw new Error("Reduction/Stack error : expected <" + Tag.UNARY + "> but found <" + temp + ">");
                        }
                    } else {
                        throw new Error("Reduction/Stack error : expected <" + Tag.EXPRESSION + "> but found <" + temp + ">");
                    }
                }
                else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.SYMBOL + "> but found <" + temp + ">");
                }
            }
            else {
                throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.SYMBOL + " ';'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
            }
        }
        else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.SYMBOL + " '.'> or <" + Tag.SYMBOL + " ';'> or <" + Tag.ASSIGNMENT + " ':='> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
        }
    }

    private void instruction_ident_expression2() throws IOException{
        // INSTRUCTION_IDENT_EXPRESSION2 ::= ( UNARY GENERATE_EXPRESSION INSTRUCTION_IDENT_EXPRESSION1 (lecture de ( )
        if (current.getTag() == Tag.SYMBOL && current.getStringValue().equals("(")) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.unary();
            this.generate_expression();
            // semantic functions
            int expr = parser.ast.buffer.pop();
            parser.ast.addEdge(parser.ast.buffer.lastElement(), expr);
            // end functions
            this.instruction_ident_expression1();
            int temp = parser.stack.pop();
            if (temp == Tag.INSTRUCTION_IDENT_EXPRESSION1) {
                temp = parser.stack.pop();
                if (temp == Tag.GENERATE_EXPRESSION) {
                    temp = parser.stack.pop();
                    if (temp == Tag.UNARY) {
                        temp = parser.stack.pop();
                        if (temp == Tag.SYMBOL) {
                            parser.stack.push(Tag.INSTRUCTION_IDENT_EXPRESSION2);
                        } else {
                            throw new Error("Reduction/Stack error : expected <" + Tag.SYMBOL + "> but found <" + temp + ">");
                        }
                    } else {
                        throw new Error("Reduction/Stack error : expected <" + Tag.UNARY + "> but found <" + temp + ">");
                    }
                } else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.GENERATE_EXPRESSION + "> but found <" + temp + ">");
                }
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.INSTRUCTION_IDENT_EXPRESSION1 + "> but found <" + temp + ">");
            }
        }
        else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.SYMBOL + " '('> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
        }
    }

    //END_RETURN
    private void end_return() throws IOException{
        // END_RETURN ::= ; (lecture de ; )
        // END_RETURN ::= UNARY EXPRESSION ; (lecture de ident)
        // END_RETURN ::= UNARY EXPRESSION ; (lecture de ( )
        // END_RETURN ::= UNARY EXPRESSION ; (lecture de new)
        // END_RETURN ::= UNARY EXPRESSION ; (lecture de character'val)
        // END_RETURN ::= UNARY EXPRESSION ; (lecture de not)
        // END_RETURN ::= UNARY EXPRESSION ; (lecture de - )
        // END_RETURN ::= UNARY EXPRESSION ; (lecture de entier)
        // END_RETURN ::= UNARY EXPRESSION ; (lecture de caractere)
        // END_RETURN ::= UNARY EXPRESSION ; (lecture de true)
        // END_RETURN ::= UNARY EXPRESSION ; (lecture de false)
        // END_RETURN ::= UNARY EXPRESSION ; (lecture de null)
        if (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(";")) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            int temp = parser.stack.pop();
            if (temp == Tag.SYMBOL) {
                parser.stack.push(Tag.END_RETURN);
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.SYMBOL + "> but found <" + temp + ">");
            }
        }
        else if ((current.getTag() == Tag.ID) || (current.getTag() == Tag.SYMBOL && current.getStringValue().equals("(")) || (current.getTag() == Tag.NEW) || (current.getTag() == Tag.CHARACTERVAL) || (current.getTag() == Tag.NOT) || (current.getTag() == Tag.SYMBOL && current.getStringValue().equals("-")) || (current.getTag() == Tag.NUMCONST) || (current.getTag() == Tag.CHAR) || (current.getTag() == Tag.TRUE) || (current.getTag() == Tag.FALSE) || (current.getTag() == Tag.NULL)) {
            this.unary();
            this.expression();
            if (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(";")) {
                parser.stack.push(current.getTag());
                current = parser.lexer.scan();
                int temp = parser.stack.pop();
                if (temp == Tag.SYMBOL) {
                   temp = parser.stack.pop();
                     if (temp == Tag.EXPRESSION) {
                          temp = parser.stack.pop();
                          if (temp == Tag.UNARY) {
                            parser.stack.push(Tag.END_RETURN);
                          } else {
                            throw new Error("Reduction/Stack error : expected <" + Tag.UNARY + "> but found <" + temp + ">");
                          }
                     } else {
                          throw new Error("Reduction/Stack error : expected <" + Tag.EXPRESSION + "> but found <" + temp + ">");
                     }
                } else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.SYMBOL + "> but found <" + temp + ">");
                }
            } else {
                throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.SYMBOL + " ';'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
            }
        } else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.SYMBOL + " ';'> or <" + Tag.ID + " 'ident'> or <" + Tag.SYMBOL + " '('> or <" + Tag.NEW + " 'new'> or <" + Tag.CHARACTERVAL + " 'character'val'> or <" + Tag.NOT + " 'not'> or <" + Tag.SYMBOL + " '-'> or <" + Tag.NUMCONST + " 'entier'> or <" + Tag.CHAR + " 'caractere'> or <" + Tag.TRUE + " 'true'> or <" + Tag.FALSE + " 'false'> or <" + Tag.NULL + " 'null'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
        }
    }

    //FOR_INSTRUCTION
    private void for_instruction() throws IOException {
        // FOR_INSTRUCTION ::= UNARY EXPRESSION .. UNARY EXPRESSION loop GENERATE_INSTRUCTIONS end loop ; (lecture de ident)
        // FOR_INSTRUCTION ::= UNARY EXPRESSION .. UNARY EXPRESSION loop GENERATE_INSTRUCTIONS end loop ; (lecture de ( )
        // FOR_INSTRUCTION ::= UNARY EXPRESSION .. UNARY EXPRESSION loop GENERATE_INSTRUCTIONS end loop ; (lecture de new)
        // FOR_INSTRUCTION ::= UNARY EXPRESSION .. UNARY EXPRESSION loop GENERATE_INSTRUCTIONS end loop ; (lecture de character'val)
        // FOR_INSTRUCTION ::= UNARY EXPRESSION .. UNARY EXPRESSION loop GENERATE_INSTRUCTIONS end loop ; (lecture de not)
        // FOR_INSTRUCTION ::= UNARY EXPRESSION .. UNARY EXPRESSION loop GENERATE_INSTRUCTIONS end loop ; (lecture de - )
        // FOR_INSTRUCTION ::= UNARY EXPRESSION .. UNARY EXPRESSION loop GENERATE_INSTRUCTIONS end loop ; (lecture de entier)
        // FOR_INSTRUCTION ::= UNARY EXPRESSION .. UNARY EXPRESSION loop GENERATE_INSTRUCTIONS end loop ; (lecture de caractere)
        // FOR_INSTRUCTION ::= UNARY EXPRESSION .. UNARY EXPRESSION loop GENERATE_INSTRUCTIONS end loop ; (lecture de true)
        // FOR_INSTRUCTION ::= UNARY EXPRESSION .. UNARY EXPRESSION loop GENERATE_INSTRUCTIONS end loop ; (lecture de false)
        // FOR_INSTRUCTION ::= UNARY EXPRESSION .. UNARY EXPRESSION loop GENERATE_INSTRUCTIONS end loop ; (lecture de null)
        // FOR_INSTRUCTION ::= reverse UNARY EXPRESSION .. UNARY EXPRESSION loop GENERATE_INSTRUCTIONS end loop ; (lecture de reverse)
        if ((current.getTag() == Tag.ID) || (current.getTag() == Tag.SYMBOL && current.getStringValue().equals("(")) || (current.getTag() == Tag.NEW) || (current.getTag() == Tag.CHARACTERVAL) || (current.getTag() == Tag.NOT) || (current.getTag() == Tag.SYMBOL && current.getStringValue().equals("-")) || (current.getTag() == Tag.NUMCONST) || (current.getTag() == Tag.CHAR) || (current.getTag() == Tag.TRUE) || (current.getTag() == Tag.FALSE) || (current.getTag() == Tag.NULL)) {
            this.unary();
            this.expression();
            if (current.getTag() == Tag.DOUBLEPOINT) {
                parser.stack.push(current.getTag());
                current = parser.lexer.scan();
                this.unary();
                this.expression();
                if (current.getTag() == Tag.LOOP) {
                    parser.stack.push(current.getTag());
                    current = parser.lexer.scan();
                    this.generate_instructions();
                    if (current.getTag() == Tag.END) {
                        parser.stack.push(current.getTag());
                        current = parser.lexer.scan();
                        if (current.getTag() == Tag.LOOP) {
                            parser.stack.push(current.getTag());
                            current = parser.lexer.scan();
                            if (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(";")) {
                                parser.stack.push(current.getTag());
                                current = parser.lexer.scan();
                                int temp = parser.stack.pop();
                                if (temp == Tag.SYMBOL) {
                                    temp = parser.stack.pop();
                                    if (temp == Tag.LOOP) {
                                        temp = parser.stack.pop();
                                        if (temp == Tag.END) {
                                            temp = parser.stack.pop();
                                            if (temp == Tag.GENERATE_INSTRUCTIONS) {
                                                temp = parser.stack.pop();
                                                if (temp == Tag.LOOP) {
                                                    temp = parser.stack.pop();
                                                    if (temp == Tag.EXPRESSION) {
                                                        temp = parser.stack.pop();
                                                        if (temp == Tag.UNARY) {
                                                            temp = parser.stack.pop();
                                                            if (temp == Tag.DOUBLEPOINT) {
                                                                temp = parser.stack.pop();
                                                                if (temp == Tag.EXPRESSION) {
                                                                    temp = parser.stack.pop();
                                                                    if (temp == Tag.UNARY) {
                                                                        parser.stack.push(Tag.FOR_INSTRUCTION);
                                                                        // semantic functions
                                                                        parser.ast.buffer.pop();
                                                                        // end semantic functions
                                                                    } else {
                                                                        throw new Error("Reduction/Stack error : expected <" + Tag.UNARY + "> but found <" + temp + ">");
                                                                    }
                                                                } else {
                                                                    throw new Error("Reduction/Stack error : expected <" + Tag.EXPRESSION + "> but found <" + temp + ">");
                                                                }
                                                            } else {
                                                                throw new Error("Reduction/Stack error : expected <" + Tag.DOUBLEPOINT + "> but found <" + temp + ">");
                                                            }
                                                        } else {
                                                            throw new Error("Reduction/Stack error : expected <" + Tag.UNARY + "> but found <" + temp + ">");
                                                        }
                                                    } else {
                                                        throw new Error("Reduction/Stack error : expected <" + Tag.EXPRESSION + "> but found <" + temp + ">");
                                                    }
                                                } else {
                                                    throw new Error("Reduction/Stack error : expected <" + Tag.LOOP + "> but found <" + temp + ">");
                                                }
                                            } else {
                                                throw new Error("Reduction/Stack error : expected <" + Tag.GENERATE_INSTRUCTIONS + "> but found <" + temp + ">");
                                            }
                                        } else {
                                            throw new Error("Reduction/Stack error : expected <" + Tag.END + "> but found <" + temp + ">");
                                        }
                                    } else {
                                        throw new Error("Reduction/Stack error : expected <" + Tag.LOOP + "> but found <" + temp + ">");
                                    }
                                } else {
                                    throw new Error("Reduction/Stack error : expected <" + Tag.SYMBOL + "> but found <" + temp + ">");
                                }
                            } else {
                                throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.SYMBOL + " ';'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
                            }
                        } else {
                            throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.LOOP + " 'loop'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
                        }
                    } else {
                        throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.END + " 'end'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
                    }
                } else {
                    throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.LOOP + " 'loop'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
                }
            } else {
                throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.DOUBLEPOINT + " '..'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
            }
        }
        else if (current.getTag() == Tag.REVERSE) {
            // semantic functions
            parser.ast.addEdge(parser.ast.buffer.lastElement(), parser.ast.addNode("reverse"));
            // end semantic functions
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.unary();
            this.expression();
            if (current.getTag() == Tag.DOUBLEPOINT) {
                parser.stack.push(current.getTag());
                current = parser.lexer.scan();
                this.unary();
                this.expression();
                if (current.getTag() == Tag.LOOP) {
                    parser.stack.push(current.getTag());
                    current = parser.lexer.scan();
                    this.generate_instructions();
                    if (current.getTag() == Tag.END) {
                        parser.stack.push(current.getTag());
                        current = parser.lexer.scan();
                        if (current.getTag() == Tag.LOOP) {
                            parser.stack.push(current.getTag());
                            current = parser.lexer.scan();
                            if (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(";")) {
                                parser.stack.push(current.getTag());
                                current = parser.lexer.scan();
                                int temp = parser.stack.pop();
                                if (temp == Tag.SYMBOL) {
                                    temp = parser.stack.pop();
                                    if (temp == Tag.LOOP) {
                                        temp = parser.stack.pop();
                                        if (temp == Tag.END) {
                                            temp = parser.stack.pop();
                                            if (temp == Tag.GENERATE_INSTRUCTIONS) {
                                                temp = parser.stack.pop();
                                                if (temp == Tag.LOOP) {
                                                    temp = parser.stack.pop();
                                                    if (temp == Tag.EXPRESSION) {
                                                        temp = parser.stack.pop();
                                                        if (temp == Tag.UNARY) {
                                                            temp = parser.stack.pop();
                                                            if (temp == Tag.DOUBLEPOINT) {
                                                                temp = parser.stack.pop();
                                                                if (temp == Tag.EXPRESSION) {
                                                                    temp = parser.stack.pop();
                                                                    if (temp == Tag.UNARY) {
                                                                        temp = parser.stack.pop();
                                                                        if (temp == Tag.REVERSE) {
                                                                            parser.stack.push(Tag.FOR_INSTRUCTION);
                                                                            // semantic functions
                                                                            parser.ast.buffer.pop();
                                                                            // end semantic functions
                                                                        } else {
                                                                            throw new Error("Reduction/Stack error : expected <" + Tag.REVERSE + "> but found <" + temp + ">");
                                                                        }
                                                                    } else {
                                                                        throw new Error("Reduction/Stack error : expected <" + Tag.UNARY + "> but found <" + temp + ">");
                                                                    }
                                                                } else {
                                                                    throw new Error("Reduction/Stack error : expected <" + Tag.EXPRESSION + "> but found <" + temp + ">");
                                                                }
                                                            } else {
                                                                throw new Error("Reduction/Stack error : expected <" + Tag.DOUBLEPOINT + "> but found <" + temp + ">");
                                                            }
                                                        } else {
                                                            throw new Error("Reduction/Stack error : expected <" + Tag.UNARY + "> but found <" + temp + ">");
                                                        }
                                                    } else {
                                                        throw new Error("Reduction/Stack error : expected <" + Tag.EXPRESSION + "> but found <" + temp + ">");
                                                    }
                                                } else {
                                                    throw new Error("Reduction/Stack error : expected <" + Tag.LOOP + "> but found <" + temp + ">");
                                                }
                                            } else {
                                                throw new Error("Reduction/Stack error : expected <" + Tag.GENERATE_INSTRUCTIONS + "> but found <" + temp + ">");
                                            }
                                        } else {
                                            throw new Error("Reduction/Stack error : expected <" + Tag.END + "> but found <" + temp + ">");
                                        }
                                    } else {
                                        throw new Error("Reduction/Stack error : expected <" + Tag.LOOP + "> but found <" + temp + ">");
                                    }
                                } else {
                                    throw new Error("Reduction/Stack error : expected <" + Tag.SYMBOL + "> but found <" + temp + ">");
                                }
                            } else {
                                throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.SYMBOL + " ';'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
                            }
                        } else {
                            throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.LOOP + " 'loop'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
                        }
                    } else {
                        throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.END + " 'end'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
                    }
                } else {
                    throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.LOOP + " 'loop'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
                }
            } else {
                throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.DOUBLEPOINT + " '..'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
            }
        }
        else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.ID + " 'ident'> or <" + Tag.SYMBOL + " '('> or <" + Tag.NEW + " 'new'> or <" + Tag.CHARACTERVAL + " 'character'val'> or <" + Tag.NOT + " 'not'> or <" + Tag.SYMBOL + " '-'> or <" + Tag.NUMCONST + " 'entier'> or <" + Tag.CHAR + " 'caractere'> or <" + Tag.TRUE + " 'true'> or <" + Tag.FALSE + " 'false'> or <" + Tag.NULL + " 'null'> or <" + Tag.REVERSE + " 'reverse'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
        }
    }

    //NEXT_IF
    private void next_if() throws IOException{
        // NEXT_IF ::= end if ; (lecture de end)
        // NEXT_IF ::= else ELSE (lecture de else)
        // NEXT_IF ::= elsif ELSIF (lecture de elsif)
        if (current.getTag() == Tag.END) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            if (current.getTag() == Tag.IF) {
                parser.stack.push(current.getTag());
                current = parser.lexer.scan();
                if (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(";")) {
                    parser.stack.push(current.getTag());
                    current = parser.lexer.scan();
                    int temp = parser.stack.pop();
                    if (temp == Tag.SYMBOL) {
                        temp = parser.stack.pop();
                        if (temp == Tag.IF) {
                            temp = parser.stack.pop();
                            if (temp == Tag.END) {
                                parser.stack.push(Tag.NEXT_IF);
                            } else {
                                throw new Error("Reduction/Stack error : expected <" + Tag.END + "> but found <" + temp + ">");
                            }
                        } else {
                            throw new Error("Reduction/Stack error : expected <" + Tag.IF + "> but found <" + temp + ">");
                        }
                    } else {
                        throw new Error("Reduction/Stack error : expected <" + Tag.SYMBOL + "> but found <" + temp + ">");
                    }
                } else {
                    throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.SYMBOL + " ';'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
                }
            } else {
                throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.IF + " 'if'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
            }
        }
        else if (current.getTag() == Tag.ELSE) {
            // semantic functions
            parser.ast.addEdge(parser.ast.buffer.lastElement(), parser.ast.addNode("ELSE"));
            parser.ast.buffer.push(parser.ast.lastNode);
            // end semantic functions
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.nt_else();
            int temp = parser.stack.pop();
            if (temp == Tag.NT_ELSE) {
                temp = parser.stack.pop();
                if (temp == Tag.ELSE) {
                    parser.stack.push(Tag.NEXT_IF);
                    // semantic functions
                    parser.ast.buffer.pop();
                    // end semantic functions
                } else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.ELSE + "> but found <" + temp + ">");
                }
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.NT_ELSE + "> but found <" + temp + ">");
            }
        }
        else if (current.getTag() == Tag.ELSIF) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.elsif();
            int temp = parser.stack.pop();
            if (temp == Tag.NT_ELSIF) {
                temp = parser.stack.pop();
                if (temp == Tag.ELSIF) {
                    parser.stack.push(Tag.NEXT_IF);
                } else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.NT_ELSIF + "> but found <" + temp + ">");
                }
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.ELSIF + "> but found <" + temp + ">");
            }
        }
        else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.END + " 'end'> or <" + Tag.ELSE + " 'else'> or <" + Tag.ELSIF + " 'elsif'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
        }
    }

    //ELSE
    private void nt_else() throws IOException{
        // ELSE ::= GENERATE_INSTRUCTIONS end if ; (lecture de ident)
        // ELSE ::= GENERATE_INSTRUCTIONS end if ; (lecture de begin)
        // ELSE ::= GENERATE_INSTRUCTIONS end if ; (lecture de ( )
        // ELSE ::= GENERATE_INSTRUCTIONS end if ; (lecture de return)
        // ELSE ::= GENERATE_INSTRUCTIONS end if ; (lecture de new)
        // ELSE ::= GENERATE_INSTRUCTIONS end if ; (lecture de character'val)
        // ELSE ::= GENERATE_INSTRUCTIONS end if ; (lecture de not)
        // ELSE ::= GENERATE_INSTRUCTIONS end if ; (lecture de entier)
        // ELSE ::= GENERATE_INSTRUCTIONS end if ; (lecture de caractere)
        // ELSE ::= GENERATE_INSTRUCTIONS end if ; (lecture de true)
        // ELSE ::= GENERATE_INSTRUCTIONS end if ; (lecture de false)
        // ELSE ::= GENERATE_INSTRUCTIONS end if ; (lecture de null)
        // ELSE ::= GENERATE_INSTRUCTIONS end if ; (lecture de if)
        // ELSE ::= GENERATE_INSTRUCTIONS end if ; (lecture de for)
        // ELSE ::= GENERATE_INSTRUCTIONS end if ; (lecture de while)
        if ((current.getTag() == Tag.ID) || (current.getTag() == Tag.BEGIN) || (current.getTag() == Tag.SYMBOL && current.getStringValue().equals("(")) || (current.getTag() == Tag.RETURN) || (current.getTag() == Tag.NEW) || (current.getTag() == Tag.CHARACTERVAL) || (current.getTag() == Tag.NOT) || (current.getTag() == Tag.NUMCONST) || (current.getTag() == Tag.CHAR) || (current.getTag() == Tag.TRUE) || (current.getTag() == Tag.FALSE) || (current.getTag() == Tag.NULL) || (current.getTag() == Tag.IF) || (current.getTag() == Tag.FOR) || (current.getTag() == Tag.WHILE)) {
            this.generate_instructions();
            if (current.getTag() == Tag.END) {
                parser.stack.push(current.getTag());
                current = parser.lexer.scan();
                if (current.getTag() == Tag.IF) {
                    parser.stack.push(current.getTag());
                    current = parser.lexer.scan();
                    if (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(";")) {
                        parser.stack.push(current.getTag());
                        current = parser.lexer.scan();
                        int temp = parser.stack.pop();
                        if (temp == Tag.SYMBOL) {
                            temp = parser.stack.pop();
                            if (temp == Tag.IF) {
                                temp = parser.stack.pop();
                                if (temp == Tag.END) {
                                    temp = parser.stack.pop();
                                    if (temp == Tag.GENERATE_INSTRUCTIONS) {
                                        parser.stack.push(Tag.NT_ELSE);
                                    } else {
                                        throw new Error("Reduction/Stack error : expected <" + Tag.GENERATE_INSTRUCTIONS + "> but found <" + temp + ">");
                                    }
                                } else {
                                    throw new Error("Reduction/Stack error : expected <" + Tag.END + "> but found <" + temp + ">");
                                }
                            } else {
                                throw new Error("Reduction/Stack error : expected <" + Tag.IF + "> but found <" + temp + ">");
                            }
                        } else {
                            throw new Error("Reduction/Stack error : expected <" + Tag.SYMBOL + "> but found <" + temp + ">");
                        }
                    } else {
                        throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.SYMBOL + " ';'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
                    }
                } else {
                    throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.IF + " 'if'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
                }
            } else {
                throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.END + " 'end'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
            }
        }
        else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.ID + " 'ident'> or <" + Tag.BEGIN + " 'begin'> or <" + Tag.SYMBOL + " '('> or <" + Tag.RETURN + " 'return'> or <" + Tag.NEW + " 'new'> or <" + Tag.CHARACTERVAL + " 'character'val'> or <" + Tag.NOT + " 'not'> or <" + Tag.NUMCONST + " 'entier'> or <" + Tag.CHAR + " 'caractere'> or <" + Tag.TRUE + " 'true'> or <" + Tag.FALSE + " 'false'> or <" + Tag.NULL + " 'null'> or <" + Tag.IF + " 'if'> or <" + Tag.FOR + " 'for'> or <" + Tag.WHILE + " 'while'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
        }
    }

    //ELSIF
    private void elsif() throws IOException{
        //ELSIF ::= UNARY EXPRESSION then GENERATE_INSTRUCTIONS END_ELSIF (lecture de ident)
        //ELSIF ::= UNARY EXPRESSION then GENERATE_INSTRUCTIONS END_ELSIF (lecture de ()
        //ELSIF ::= UNARY EXPRESSION then GENERATE_INSTRUCTIONS END_ELSIF (lecture de new)
        //ELSIF ::= UNARY EXPRESSION then GENERATE_INSTRUCTIONS END_ELSIF (lecture de character'val)
        //ELSIF ::= UNARY EXPRESSION then GENERATE_INSTRUCTIONS END_ELSIF (lecture de not)
        //ELSIF ::= UNARY EXPRESSION then GENERATE_INSTRUCTIONS END_ELSIF (lecture de -)
        //ELSIF ::= UNARY EXPRESSION then GENERATE_INSTRUCTIONS END_ELSIF (lecture de entier)
        //ELSIF ::= UNARY EXPRESSION then GENERATE_INSTRUCTIONS END_ELSIF (lecture de caractere)
        //ELSIF ::= UNARY EXPRESSION then GENERATE_INSTRUCTIONS END_ELSIF (lecture de true)
        //ELSIF ::= UNARY EXPRESSION then GENERATE_INSTRUCTIONS END_ELSIF (lecture de false)
        //ELSIF ::= UNARY EXPRESSION then GENERATE_INSTRUCTIONS END_ELSIF (lecture de null)
        if ((current.getTag() == Tag.ID) || (current.getTag() == Tag.SYMBOL && Objects.equals(current.getStringValue(), "(")) || (current.getTag() == Tag.NEW) || (current.getTag() == Tag.CHARACTERVAL) || (current.getTag() == Tag.NOT) || (current.getTag() == Tag.SYMBOL && Objects.equals(current.getStringValue(), "-")) || (current.getTag() == Tag.NUMCONST) || (current.getTag() == Tag.CHAR) || (current.getTag() == Tag.TRUE) || (current.getTag() == Tag.FALSE) || (current.getTag() == Tag.NULL)) {
            // semantic functions
            parser.ast.addEdge(parser.ast.buffer.lastElement(), parser.ast.addNode("ELSIF"));
            parser.ast.buffer.push(parser.ast.lastNode);
            // end semantic functions
            this.unary();
            this.expression();
            if (current.getTag() == Tag.THEN) {
                parser.stack.push(current.getTag());
                current = parser.lexer.scan();
                this.generate_instructions();
                this.end_elsif();
                int temp = parser.stack.pop();
                if(temp == Tag.END_ELSIF) {
                    temp = parser.stack.pop();
                    if(temp == Tag.GENERATE_INSTRUCTIONS) {
                        temp = parser.stack.pop();
                        if(temp == Tag.THEN) {
                            temp = parser.stack.pop();
                            if(temp == Tag.EXPRESSION) {
                                temp = parser.stack.pop();
                                if(temp == Tag.UNARY) {
                                    parser.stack.push(Tag.NT_ELSIF);
                                }
                                else {
                                    throw new Error("Reduction/Stack error : expected <"+Tag.UNARY+"> but found <"+temp+">");
                                }
                            }
                            else {
                                throw new Error("Reduction/Stack error : expected <"+Tag.EXPRESSION+"> but found <"+temp+">");
                            }
                        }
                        else {
                            throw new Error("Reduction/Stack error : expected <"+Tag.THEN+"> but found <"+temp+">");
                        }
                    }
                    else {
                        throw new Error("Reduction/Stack error : expected <"+Tag.GENERATE_INSTRUCTIONS+"> but found <"+temp+">");
                    }
                }
                else {
                    throw new Error("Reduction/Stack error : expected <"+Tag.END_ELSIF+"> but found <"+temp+">");
                }
            }
            else {
                throw new Error("Error line "+parser.lexer.getLine()+" : expected <"+Tag.THEN+" 'then'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
            }
        }
        else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <"+Tag.ID+" 'ident'> or <"+Tag.SYMBOL +" '('> or <"+Tag.NEW+" 'new'> or <"+Tag.CHARACTERVAL+" 'character'val'> or <"+Tag.NOT+" 'not'> or <"+Tag.SYMBOL +" '-'> or <"+Tag.NUMCONST+" 'entier'> or <"+Tag.CHAR+" 'caractere'> or <"+Tag.TRUE+" 'true'> or <"+Tag.FALSE+" 'false'> or <"+Tag.NULL+" 'null'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
        }
    }

    //END_ELSIF
    private void end_elsif() throws IOException{
        //END_ELSIF ::= end if ; (lecture de end)
        //END_ELSIF ::= else ELSE (lecture de else)
        //END_ELSIF ::= elsif ELSIF (lecture de elsif)
        // semantic functions
        parser.ast.buffer.pop();
        // end semantic functions
        if (current.getTag() == Tag.END) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            if (current.getTag() == Tag.IF) {
                parser.stack.push(current.getTag());
                current = parser.lexer.scan();
                if (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(";")) {
                    parser.stack.push(current.getTag());
                    current = parser.lexer.scan();
                    int temp = parser.stack.pop();
                    if(temp == Tag.SYMBOL) {
                        temp = parser.stack.pop();
                        if(temp == Tag.IF) {
                            temp = parser.stack.pop();
                            if(temp == Tag.END) {
                                parser.stack.push(Tag.END_ELSIF);
                            }
                            else {
                                throw new Error("Reduction/Stack error : expected <"+Tag.END+"> but found <"+temp+">");
                            }
                        }
                        else {
                            throw new Error("Reduction/Stack error : expected <"+Tag.IF+"> but found <"+temp+">");
                        }
                    }
                    else {
                        throw new Error("Reduction/Stack error : expected <"+Tag.SYMBOL +"> but found <"+temp+">");
                    }
                }
                else {
                    throw new Error("Error line "+parser.lexer.getLine()+" : expected <"+Tag.SYMBOL +" ';'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
                }
            }
            else {
                throw new Error("Error line "+parser.lexer.getLine()+" : expected <"+Tag.IF+" 'if'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
            }
        }
        else if (current.getTag() == Tag.ELSE) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.nt_else();
            int temp = parser.stack.pop();
            if(temp == Tag.NT_ELSE) {
                temp = parser.stack.pop();
                if(temp == Tag.ELSE) {
                    parser.stack.push(Tag.END_ELSIF);
                }
                else {
                    throw new Error("Reduction/Stack error : expected <"+Tag.ELSE+"> but found <"+temp+">");
                }
            }
            else {
                throw new Error("Reduction/Stack error : expected <"+Tag.NT_ELSE+"> but found <"+temp+">");
            }
        }
        else if (current.getTag() == Tag.ELSIF) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.elsif();
            int temp = parser.stack.pop();
            if(temp == Tag.NT_ELSIF) {
                temp = parser.stack.pop();
                if(temp == Tag.ELSIF) {
                    parser.stack.push(Tag.END_ELSIF);
                }
                else {
                    throw new Error("Reduction/Stack error : expected <"+Tag.ELSIF+"> but found <"+temp+">");
                }
            }
            else {
                throw new Error("Reduction/Stack error : expected <"+Tag.NT_ELSIF+"> but found <"+temp+">");
            }
        }
        else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <"+Tag.END+" 'end'> or <"+Tag.ELSE+" 'else'> or <"+Tag.ELSIF+" 'elsif'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
        }
    }

    //WI_EXPRESSION
    private void wi_expression() throws IOException {
        //WI_EXPRESSION ::= WI_EXPRESSION_1 WI_EXPRESSION_OR (lecture de ()
        //WI_EXPRESSION ::= new ident WI_EXPRESSION_OR (lecture de new)
        //WI_EXPRESSION ::= character’val ( UNARY EXPRESSION ) WI_EXPRESSION_OR (lecture de character’val)
        //WI_EXPRESSION ::= WI_EXPRESSION_1 WI_EXPRESSION_OR (lecture de not)
        //WI_EXPRESSION ::= WI_EXPRESSION_1 WI_EXPRESSION_OR (lecture de entier)
        //WI_EXPRESSION ::= WI_EXPRESSION_1 WI_EXPRESSION_OR (lecture de caractere)
        //WI_EXPRESSION ::= WI_EXPRESSION_1 WI_EXPRESSION_OR (lecture de true)
        //WI_EXPRESSION ::= WI_EXPRESSION_1 WI_EXPRESSION_OR (lecture de false)
        //WI_EXPRESSION ::= WI_EXPRESSION_1 WI_EXPRESSION_OR (lecture de null)
        if ((current.getTag() == Tag.SYMBOL && Objects.equals(current.getStringValue(), "(")) || (current.getTag() == Tag.NOT) || (current.getTag() == Tag.NUMCONST) || (current.getTag() == Tag.CHAR) || (current.getTag() == Tag.TRUE) || (current.getTag() == Tag.FALSE) || (current.getTag() == Tag.NULL)) {
            this.wi_expression_1();
            this.wi_expression_or();
            int temp = parser.stack.pop();
            if (temp == Tag.WI_EXPRESSION_OR) {
                temp = parser.stack.pop();
                if (temp == Tag.WI_EXPRESSION_1) {
                    parser.stack.push(Tag.WI_EXPRESSION);
                } else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.WI_EXPRESSION_1 + "> but found <" + temp + ">");
                }
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.WI_EXPRESSION_OR + "> but found <" + temp + ">");
            }
        }
        else if (current.getTag() == Tag.NEW) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.checkIdent = false;
            if (current.getTag() == Tag.ID) {
                parser.stack.push(current.getTag());
                current = parser.lexer.scan();
                this.checkIdent = false;
                this.wi_expression_or();
                int temp = parser.stack.pop();
                if (temp == Tag.WI_EXPRESSION_OR) {
                    temp = parser.stack.pop();
                    if (temp == Tag.ID) {
                        temp = parser.stack.pop();
                        if (temp == Tag.NEW) {
                            parser.stack.push(Tag.WI_EXPRESSION);
                        } else {
                            throw new Error("Reduction/Stack error : expected <" + Tag.NEW + "> but found <" + temp + ">");
                        }
                    } else {
                        throw new Error("Reduction/Stack error : expected <" + Tag.ID + "> but found <" + temp + ">");
                    }
                } else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.WI_EXPRESSION_OR + "> but found <" + temp + ">");
                }
            } else {
                throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.ID + " 'ident'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
            }
        }
        else if (current.getTag() == Tag.CHARACTERVAL) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.checkIdent = false;
            if (current.getTag() == Tag.SYMBOL && current.getStringValue().equals("(")) {
                parser.stack.push(current.getTag());
                current = parser.lexer.scan();
                this.checkIdent = false;
                this.unary();
                this.expression();
                if (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(")")) {
                    parser.stack.push(current.getTag());
                    current = parser.lexer.scan();
                    this.checkIdent = false;
                    this.wi_expression_or();
                    int temp = parser.stack.pop();
                    if (temp == Tag.WI_EXPRESSION_OR) {
                        temp = parser.stack.pop();
                        if (temp == Tag.SYMBOL) {
                            temp = parser.stack.pop();
                            if (temp == Tag.EXPRESSION) {
                                temp = parser.stack.pop();
                                if (temp == Tag.UNARY) {
                                    temp = parser.stack.pop();
                                    if (temp == Tag.SYMBOL) {
                                        temp = parser.stack.pop();
                                        if (temp == Tag.CHARACTERVAL) {
                                            parser.stack.push(Tag.WI_EXPRESSION);
                                        } else {
                                            throw new Error("Reduction/Stack error : expected <" + Tag.CHARACTERVAL + "> but found <" + temp + ">");
                                        }
                                    } else {
                                        throw new Error("Reduction/Stack error : expected <" + Tag.SYMBOL + "> but found <" + temp + ">");
                                    }
                                } else {
                                    throw new Error("Reduction/Stack error : expected <" + Tag.UNARY + "> but found <" + temp + ">");
                                }
                            } else {
                                throw new Error("Reduction/Stack error : expected <" + Tag.EXPRESSION + "> but found <" + temp + ">");
                            }
                        } else {
                            throw new Error("Reduction/Stack error : expected <" + Tag.SYMBOL + "> but found <" + temp + ">");
                        }
                    } else {
                        throw new Error("Reduction/Stack error : expected <" + Tag.WI_EXPRESSION_OR + "> but found <" + temp + ">");
                    }
                } else {
                    throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.SYMBOL + " ')'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
                }
            }
        }
        else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.OR + " 'or'> or <" + Tag.NOT + " 'not'> or <" + Tag.NUMCONST + " 'entier'> or <" + Tag.CHAR + " 'caractere'> or <" + Tag.TRUE + " 'true'> or <" + Tag.FALSE + " 'false'> or <" + Tag.NULL + " 'null'> or <" + Tag.NEW + " 'new'> or <" + Tag.CHARACTERVAL + " 'character'val'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
        }
    }

    //WI_EXPRESSION_OR
    private void wi_expression_or() throws IOException{
        //WI_EXPRESSION_OR ::= ε (lecture de :=)
        //WI_EXPRESSION_OR ::= or UNARY WI_EXPRESSION_ELSE (lecture de or)
        if (current.getTag() == Tag.ASSIGNMENT) {
            parser.stack.push(Tag.WI_EXPRESSION_OR);
        }
        else if (current.getTag() == Tag.OR) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.checkIdent = false;
            this.unary();
            this.wi_expression_else();
            int temp = parser.stack.pop();
            if(temp == Tag.WI_EXPRESSION_ELSE) {
                temp = parser.stack.pop();
                if(temp == Tag.UNARY) {
                    temp = parser.stack.pop();
                    if(temp == Tag.OR) {
                        parser.stack.push(Tag.WI_EXPRESSION_OR);
                    }
                    else {
                        throw new Error("Reduction/Stack error : expected <"+Tag.OR+"> but found <"+temp+">");
                    }
                }
                else {
                    throw new Error("Reduction/Stack error : expected <"+Tag.UNARY+"> but found <"+temp+">");
                }
            }
            else {
                throw new Error("Reduction/Stack error : expected <"+Tag.WI_EXPRESSION_ELSE+"> but found <"+temp+">");
            }
        }
        else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <"+Tag.ASSIGNMENT+" ':='> or <"+Tag.OR+" 'or'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
        }
    }

    //WI_EXPRESSION_ELSE
    private void wi_expression_else() throws IOException{
        //WI_EXPRESSION_ELSE ::= WI_EXPRESSION_1 WI_EXPRESSION_OR (lecture de ()
        //WI_EXPRESSION_ELSE ::= else UNARY WI_EXPRESSION_1 WI_EXPRESSION_OR (lecture de else)
        //WI_EXPRESSION_ELSE ::= WI_EXPRESSION_1 WI_EXPRESSION_OR (lecture de not)
        //WI_EXPRESSION_ELSE ::= WI_EXPRESSION_1 WI_EXPRESSION_OR (lecture de entier)
        //WI_EXPRESSION_ELSE ::= WI_EXPRESSION_1 WI_EXPRESSION_OR (lecture de caractere)
        //WI_EXPRESSION_ELSE ::= WI_EXPRESSION_1 WI_EXPRESSION_OR (lecture de true)
        //WI_EXPRESSION_ELSE ::= WI_EXPRESSION_1 WI_EXPRESSION_OR (lecture de false)
        //WI_EXPRESSION_ELSE ::= WI_EXPRESSION_1 WI_EXPRESSION_OR (lecture de null)
        if ((current.getTag() == Tag.SYMBOL && Objects.equals(current.getStringValue(), "(")) || (current.getTag() == Tag.NOT) || (current.getTag() == Tag.NUMCONST) || (current.getTag() == Tag.CHAR) || (current.getTag() == Tag.TRUE) || (current.getTag() == Tag.FALSE) || (current.getTag() == Tag.NULL)) {
            this.wi_expression_1();
            this.wi_expression_or();
            int temp = parser.stack.pop();
            if (temp == Tag.WI_EXPRESSION_OR) {
                temp = parser.stack.pop();
                if (temp == Tag.WI_EXPRESSION_1) {
                    parser.stack.push(Tag.WI_EXPRESSION_ELSE);
                } else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.WI_EXPRESSION_1 + "> but found <" + temp + ">");
                }
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.WI_EXPRESSION_OR + "> but found <" + temp + ">");
            }
        }
        else if (current.getTag() == Tag.ELSE) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.checkIdent = false;
            this.unary();
            this.wi_expression_1();
            this.wi_expression_or();
            int temp = parser.stack.pop();
            if (temp == Tag.WI_EXPRESSION_OR) {
                temp = parser.stack.pop();
                if (temp == Tag.WI_EXPRESSION_1) {
                    temp = parser.stack.pop();
                    if (temp == Tag.UNARY) {
                        temp = parser.stack.pop();
                        if (temp == Tag.ELSE) {
                            parser.stack.push(Tag.WI_EXPRESSION_ELSE);
                        } else {
                            throw new Error("Reduction/Stack error : expected <" + Tag.ELSE + "> but found <" + temp + ">");
                        }
                    } else {
                        throw new Error("Reduction/Stack error : expected <" + Tag.UNARY + "> but found <" + temp + ">");
                    }
                } else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.WI_EXPRESSION_1 + "> but found <" + temp + ">");
                }
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.WI_EXPRESSION_OR + "> but found <" + temp + ">");
            }
        }
        else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.OR + " 'or'> or <" + Tag.NOT + " 'not'> or <" + Tag.NUMCONST + " 'entier'> or <" + Tag.CHAR + " 'caractere'> or <" + Tag.TRUE + " 'true'> or <" + Tag.FALSE + " 'false'> or <" + Tag.NULL + " 'null'> or <" + Tag.ELSE + " 'else'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
        }
    }

    //WI_EXPRESSION_1
    private void wi_expression_1() throws IOException{
        //WI_EXPRESSION_1 ::= WI_EXPRESSION_NOT WI_EXPRESSION_AND (lecture de ()
        //WI_EXPRESSION_1 ::= WI_EXPRESSION_NOT WI_EXPRESSION_AND (lecture de not)
        //WI_EXPRESSION_1 ::= WI_EXPRESSION_NOT WI_EXPRESSION_AND (lecture de entier)
        //WI_EXPRESSION_1 ::= WI_EXPRESSION_NOT WI_EXPRESSION_AND (lecture de caractere)
        //WI_EXPRESSION_1 ::= WI_EXPRESSION_NOT WI_EXPRESSION_AND (lecture de true)
        //WI_EXPRESSION_1 ::= WI_EXPRESSION_NOT WI_EXPRESSION_AND (lecture de false)
        //WI_EXPRESSION_1 ::= WI_EXPRESSION_NOT WI_EXPRESSION_AND (lecture de null)
        if ((current.getTag() == Tag.SYMBOL && Objects.equals(current.getStringValue(), "(")) || (current.getTag() == Tag.NOT) || (current.getTag() == Tag.NUMCONST) || (current.getTag() == Tag.CHAR) || (current.getTag() == Tag.TRUE) || (current.getTag() == Tag.FALSE) || (current.getTag() == Tag.NULL)) {
            this.wi_expression_not();
            this.wi_expression_and();
            int temp = parser.stack.pop();
            if (temp == Tag.WI_EXPRESSION_AND) {
                temp = parser.stack.pop();
                if (temp == Tag.WI_EXPRESSION_NOT) {
                    parser.stack.push(Tag.WI_EXPRESSION_1);
                } else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.WI_EXPRESSION_NOT + "> but found <" + temp + ">");
                }
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.WI_EXPRESSION_AND + "> but found <" + temp + ">");
            }
        }
        else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.NOT + " 'not'> or <" + Tag.NUMCONST + " 'entier'> or <" + Tag.CHAR + " 'caractere'> or <" + Tag.TRUE + " 'true'> or <" + Tag.FALSE + " 'false'> or <" + Tag.NULL + " 'null'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
        }
    }

    //WI_EXPRESSION_AND
    private void wi_expression_and() throws IOException{
        //WI_EXPRESSION_AND ::= ε (lecture de :=)
        //WI_EXPRESSION_AND ::= ε (lecture de or)
        //WI_EXPRESSION_AND ::= and UNARY WI_EXPRESSION_THEN (lecture de and)
        if (current.getTag() == Tag.ASSIGNMENT) {
            parser.stack.push(Tag.WI_EXPRESSION_AND);
        }
        else if (current.getTag() == Tag.OR) {
            parser.stack.push(Tag.WI_EXPRESSION_AND);
        }
        else if (current.getTag() == Tag.AND) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.checkIdent = false;
            this.unary();
            this.wi_expression_then();
            int temp = parser.stack.pop();
            if(temp == Tag.WI_EXPRESSION_THEN) {
                temp = parser.stack.pop();
                if(temp == Tag.UNARY) {
                    temp = parser.stack.pop();
                    if(temp == Tag.AND) {
                        parser.stack.push(Tag.WI_EXPRESSION_AND);
                    }
                    else {
                        throw new Error("Reduction/Stack error : expected <"+Tag.AND+"> but found <"+temp+">");
                    }
                }
                else {
                    throw new Error("Reduction/Stack error : expected <"+Tag.UNARY+"> but found <"+temp+">");
                }
            }
            else {
                throw new Error("Reduction/Stack error : expected <"+Tag.WI_EXPRESSION_THEN+"> but found <"+temp+">");
            }
        }
        else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <"+Tag.ASSIGNMENT+" ':='> or <"+Tag.OR+" 'or'> or <"+Tag.AND+" 'and'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
        }
    }

    //WI_EXPRESSION_THEN
    private void wi_expression_then() throws IOException{
        //WI_EXPRESSION_THEN ::= WI_EXPRESSION_NOT WI_EXPRESSION_AND (lecture de ()
        //WI_EXPRESSION_THEN ::= then UNARY WI_EXPRESSION_NOT WI_EXPRESSION_AND (lecture de then)
        //WI_EXPRESSION_THEN ::= WI_EXPRESSION_NOT WI_EXPRESSION_AND (lecture de not)
        //WI_EXPRESSION_THEN ::= WI_EXPRESSION_NOT WI_EXPRESSION_AND (lecture de entier)
        //WI_EXPRESSION_THEN ::= WI_EXPRESSION_NOT WI_EXPRESSION_AND (lecture de caractere)
        //WI_EXPRESSION_THEN ::= WI_EXPRESSION_NOT WI_EXPRESSION_AND (lecture de true)
        //WI_EXPRESSION_THEN ::= WI_EXPRESSION_NOT WI_EXPRESSION_AND (lecture de false)
        //WI_EXPRESSION_THEN ::= WI_EXPRESSION_NOT WI_EXPRESSION_AND (lecture de null)
        if ((current.getTag() == Tag.SYMBOL && Objects.equals(current.getStringValue(), "(")) || (current.getTag() == Tag.NOT) || (current.getTag() == Tag.NUMCONST) || (current.getTag() == Tag.CHAR) || (current.getTag() == Tag.TRUE) || (current.getTag() == Tag.FALSE) || (current.getTag() == Tag.NULL)) {
            this.wi_expression_not();
            this.wi_expression_and();
            int temp = parser.stack.pop();
            if (temp == Tag.WI_EXPRESSION_AND) {
                temp = parser.stack.pop();
                if (temp == Tag.WI_EXPRESSION_NOT) {
                    parser.stack.push(Tag.WI_EXPRESSION_THEN);
                } else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.WI_EXPRESSION_NOT + "> but found <" + temp + ">");
                }
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.WI_EXPRESSION_AND + "> but found <" + temp + ">");
            }
        }
        else if (current.getTag() == Tag.THEN) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.checkIdent = false;
            this.unary();
            this.wi_expression_not();
            this.wi_expression_and();
            int temp = parser.stack.pop();
            if (temp == Tag.WI_EXPRESSION_AND) {
                temp = parser.stack.pop();
                if (temp == Tag.WI_EXPRESSION_NOT) {
                    temp = parser.stack.pop();
                    if (temp == Tag.UNARY) {
                        temp = parser.stack.pop();
                        if (temp == Tag.THEN) {
                            parser.stack.push(Tag.WI_EXPRESSION_THEN);
                        } else {
                            throw new Error("Reduction/Stack error : expected <" + Tag.THEN + "> but found <" + temp + ">");
                        }
                    } else {
                        throw new Error("Reduction/Stack error : expected <" + Tag.UNARY + "> but found <" + temp + ">");
                    }
                } else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.WI_EXPRESSION_NOT + "> but found <" + temp + ">");
                }
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.WI_EXPRESSION_AND + "> but found <" + temp + ">");
            }
        }
        else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.THEN + " 'then'> or <" + Tag.NOT + " 'not'> or <" + Tag.NUMCONST + " 'entier'> or <" + Tag.CHAR + " 'caractere'> or <" + Tag.TRUE + " 'true'> or <" + Tag.FALSE + " 'false'> or <" + Tag.NULL + " 'null'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
        }
    }

    //WI_EXPRESSION_NOT
    private void wi_expression_not() throws IOException{
        //WI_EXPRESSION_NOT ::= WI_EXPRESSION_3 (lecture de ()
        //WI_EXPRESSION_NOT ::= not UNARY WI_EXPRESSION_NOT (lecture de not)
        //WI_EXPRESSION_NOT ::= WI_EXPRESSION_3 (lecture de entier)
        //WI_EXPRESSION_NOT ::= WI_EXPRESSION_3 (lecture de caractere)
        //WI_EXPRESSION_NOT ::= WI_EXPRESSION_3 (lecture de true)
        //WI_EXPRESSION_NOT ::= WI_EXPRESSION_3 (lecture de false)
        //WI_EXPRESSION_NOT ::= WI_EXPRESSION_3 (lecture de null)
        if ((current.getTag() == Tag.SYMBOL && Objects.equals(current.getStringValue(), "(")) || (current.getTag() == Tag.NUMCONST) || (current.getTag() == Tag.CHAR) || (current.getTag() == Tag.TRUE) || (current.getTag() == Tag.FALSE) || (current.getTag() == Tag.NULL)) {
            this.wi_expression_3();
            int temp = parser.stack.pop();
            if (temp == Tag.WI_EXPRESSION_3) {
                parser.stack.push(Tag.WI_EXPRESSION_NOT);
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.WI_EXPRESSION_3 + "> but found <" + temp + ">");
            }
        }
        else if (current.getTag() == Tag.NOT) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.checkIdent = false;
            this.unary();
            this.wi_expression_not();
            int temp = parser.stack.pop();
            if (temp == Tag.WI_EXPRESSION_NOT) {
                temp = parser.stack.pop();
                if (temp == Tag.UNARY) {
                    temp = parser.stack.pop();
                    if (temp == Tag.NOT) {
                        parser.stack.push(Tag.WI_EXPRESSION_NOT);
                    } else {
                        throw new Error("Reduction/Stack error : expected <" + Tag.NOT + "> but found <" + temp + ">");
                    }
                } else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.UNARY + "> but found <" + temp + ">");
                }
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.WI_EXPRESSION_NOT + "> but found <" + temp + ">");
            }
        }
        else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.NOT + " 'not'> or <" + Tag.NUMCONST + " 'entier'> or <" + Tag.CHAR + " 'caractere'> or <" + Tag.TRUE + " 'true'> or <" + Tag.FALSE + " 'false'> or <" + Tag.NULL + " 'null'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
        }
    }

    //WI_EXPRESSION_3
    private void wi_expression_3() throws IOException{
        //WI_EXPRESSION_3 ::= WI_EXPRESSION_4 WI_EXPRESSION_EQUALS (lecture de ()
        //WI_EXPRESSION_3 ::= WI_EXPRESSION_4 WI_EXPRESSION_EQUALS (lecture de entier)
        //WI_EXPRESSION_3 ::= WI_EXPRESSION_4 WI_EXPRESSION_EQUALS (lecture de caractere)
        //WI_EXPRESSION_3 ::= WI_EXPRESSION_4 WI_EXPRESSION_EQUALS (lecture de true)
        //WI_EXPRESSION_3 ::= WI_EXPRESSION_4 WI_EXPRESSION_EQUALS (lecture de false)
        //WI_EXPRESSION_3 ::= WI_EXPRESSION_4 WI_EXPRESSION_EQUALS (lecture de null)
        if ((current.getTag() == Tag.SYMBOL && Objects.equals(current.getStringValue(), "(")) || (current.getTag() == Tag.NUMCONST) || (current.getTag() == Tag.CHAR) || (current.getTag() == Tag.TRUE) || (current.getTag() == Tag.FALSE) || (current.getTag() == Tag.NULL)) {
            this.wi_expression_4();
            this.wi_expression_equals();
            int temp = parser.stack.pop();
            if (temp == Tag.WI_EXPRESSION_EQUALS) {
                temp = parser.stack.pop();
                if (temp == Tag.WI_EXPRESSION_4) {
                    parser.stack.push(Tag.WI_EXPRESSION_3);
                } else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.WI_EXPRESSION_4 + "> but found <" + temp + ">");
                }
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.WI_EXPRESSION_EQUALS + "> but found <" + temp + ">");
            }
        }
        else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.NUMCONST + " 'entier'> or <" + Tag.CHAR + " 'caractere'> or <" + Tag.TRUE + " 'true'> or <" + Tag.FALSE + " 'false'> or <" + Tag.NULL + " 'null'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
        }
    }

    //WI_EXPRESSION_EQUALS
    private void wi_expression_equals() throws IOException {
        //WI_EXPRESSION_EQUALS ::= ε (lecture de :=)
        //WI_EXPRESSION_EQUALS ::= ε (lecture de or)
        //WI_EXPRESSION_EQUALS ::= ε (lecture de and)
        //WI_EXPRESSION_EQUALS ::= = UNARY WI_EXPRESSION_4 WI_EXPRESSION_EQUALS (lecture de =)
        //WI_EXPRESSION_EQUALS ::= /= UNARY WI_EXPRESSION_4 WI_EXPRESSION_EQUALS (lecture de /=)
        if ((current.getTag() == Tag.ASSIGNMENT) || (current.getTag() == Tag.OR) || (current.getTag() == Tag.AND)) {
            parser.stack.push(Tag.WI_EXPRESSION_EQUALS);
        }
        else if (current.getTag() == Tag.SYMBOL && Objects.equals(current.getStringValue(), "=")) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.checkIdent = false;
            this.unary();
            this.wi_expression_4();
            this.wi_expression_equals();
            int temp = parser.stack.pop();
            if (temp == Tag.WI_EXPRESSION_EQUALS) {
                temp = parser.stack.pop();
                if (temp == Tag.WI_EXPRESSION_4) {
                    temp = parser.stack.pop();
                    if (temp == Tag.UNARY) {
                        temp = parser.stack.pop();
                        if (temp == Tag.SYMBOL) {
                            parser.stack.push(Tag.WI_EXPRESSION_EQUALS);
                        } else {
                            throw new Error("Reduction/Stack error : expected <" + Tag.SYMBOL + "> but found <" + temp + ">");
                        }
                    } else {
                        throw new Error("Reduction/Stack error : expected <" + Tag.UNARY + "> but found <" + temp + ">");
                    }
                } else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.WI_EXPRESSION_4 + "> but found <" + temp + ">");
                }
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.WI_EXPRESSION_EQUALS + "> but found <" + temp + ">");
            }
        }
        else if (current.getTag() == Tag.DIFFERENT) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.checkIdent = false;
            this.unary();
            this.wi_expression_4();
            this.wi_expression_equals();
            int temp = parser.stack.pop();
            if (temp == Tag.WI_EXPRESSION_EQUALS) {
                temp = parser.stack.pop();
                if (temp == Tag.WI_EXPRESSION_4) {
                    temp = parser.stack.pop();
                    if (temp == Tag.UNARY) {
                        temp = parser.stack.pop();
                        if (temp == Tag.DIFFERENT) {
                            parser.stack.push(Tag.WI_EXPRESSION_EQUALS);
                        } else {
                            throw new Error("Reduction/Stack error : expected <" + Tag.DIFFERENT + "> but found <" + temp + ">");
                        }
                    } else {
                        throw new Error("Reduction/Stack error : expected <" + Tag.UNARY + "> but found <" + temp + ">");
                    }
                } else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.WI_EXPRESSION_4 + "> but found <" + temp + ">");
                }
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.WI_EXPRESSION_EQUALS + "> but found <" + temp + ">");
            }
        }
        else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.ASSIGNMENT + " ':='> or <" + Tag.OR + " 'or'> or <" + Tag.AND + " 'and'> or <" + Tag.SYMBOL + " '='> or <" + Tag.DIFFERENT + " '/='> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
        }
    }

    //WI_EXPRESSION_4
    private void wi_expression_4() throws IOException{
        //WI_EXPRESSION_4 ::= WI_EXPRESSION_5 WI_EXPRESSION_COMPARAISON (lecture de ()
        //WI_EXPRESSION_4 ::= WI_EXPRESSION_5 WI_EXPRESSION_COMPARAISON (lecture de entier)
        //WI_EXPRESSION_4 ::= WI_EXPRESSION_5 WI_EXPRESSION_COMPARAISON (lecture de caractere)
        //WI_EXPRESSION_4 ::= WI_EXPRESSION_5 WI_EXPRESSION_COMPARAISON (lecture de true)
        //WI_EXPRESSION_4 ::= WI_EXPRESSION_5 WI_EXPRESSION_COMPARAISON (lecture de false)
        //WI_EXPRESSION_4 ::= WI_EXPRESSION_5 WI_EXPRESSION_COMPARAISON (lecture de null)
        if ((current.getTag() == Tag.SYMBOL && Objects.equals(current.getStringValue(), "(")) || (current.getTag() == Tag.NUMCONST) || (current.getTag() == Tag.CHAR) || (current.getTag() == Tag.TRUE) || (current.getTag() == Tag.FALSE) || (current.getTag() == Tag.NULL)) {
            this.wi_expression_5();
            this.wi_expression_comparaison();
            int temp = parser.stack.pop();
            if (temp == Tag.WI_EXPRESSION_COMPARAISON) {
                temp = parser.stack.pop();
                if (temp == Tag.WI_EXPRESSION_5) {
                    parser.stack.push(Tag.WI_EXPRESSION_4);
                } else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.WI_EXPRESSION_5 + "> but found <" + temp + ">");
                }
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.WI_EXPRESSION_COMPARAISON + "> but found <" + temp + ">");
            }
        }
        else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.NUMCONST + " 'entier'> or <" + Tag.CHAR + " 'caractere'> or <" + Tag.TRUE + " 'true'> or <" + Tag.FALSE + " 'false'> or <" + Tag.NULL + " 'null'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
        }
    }

    //WI_EXPRESSION_COMPARAISON
    private void wi_expression_comparaison() throws IOException {
        //WI_EXPRESSION_COMPARAISON ::= ε (lecture de :=)
        //WI_EXPRESSION_COMPARAISON ::= ε (lecture de or)
        //WI_EXPRESSION_COMPARAISON ::= ε (lecture de and)
        //WI_EXPRESSION_COMPARAISON ::= ε (lecture de =)
        //WI_EXPRESSION_COMPARAISON ::= ε (lecture de /=)
        //WI_EXPRESSION_COMPARAISON ::= > UNARY WI_EXPRESSION_5 WI_EXPRESSION_COMPARAISON (lecture de >)
        //WI_EXPRESSION_COMPARAISON ::= >= UNARY WI_EXPRESSION_5 WI_EXPRESSION_COMPARAISON (lecture de >=)
        //WI_EXPRESSION_COMPARAISON ::= < UNARY WI_EXPRESSION_5 WI_EXPRESSION_COMPARAISON (lecture de <)
        //WI_EXPRESSION_COMPARAISON ::= <= UNARY WI_EXPRESSION_5 WI_EXPRESSION_COMPARAISON (lecture de <=)
        if ((current.getTag() == Tag.ASSIGNMENT) || (current.getTag() == Tag.OR) || (current.getTag() == Tag.AND) || (current.getTag() == Tag.SYMBOL && Objects.equals(current.getStringValue(), "=")) || (current.getTag() == Tag.DIFFERENT)) {
            parser.stack.push(Tag.WI_EXPRESSION_COMPARAISON);
        }
        else if (current.getTag() == Tag.SYMBOL && Objects.equals(current.getStringValue(), ">")) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.checkIdent = false;
            this.unary();
            this.wi_expression_5();
            this.wi_expression_comparaison();
            int temp = parser.stack.pop();
            if (temp == Tag.WI_EXPRESSION_COMPARAISON) {
                temp = parser.stack.pop();
                if (temp == Tag.WI_EXPRESSION_5) {
                    temp = parser.stack.pop();
                    if (temp == Tag.UNARY) {
                        temp = parser.stack.pop();
                        if (temp == Tag.SYMBOL) {
                            parser.stack.push(Tag.WI_EXPRESSION_COMPARAISON);
                        } else {
                            throw new Error("Reduction/Stack error : expected <" + Tag.SYMBOL + "> but found <" + temp + ">");
                        }
                    } else {
                        throw new Error("Reduction/Stack error : expected <" + Tag.UNARY + "> but found <" + temp + ">");
                    }
                } else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.WI_EXPRESSION_5 + "> but found <" + temp + ">");
                }
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.WI_EXPRESSION_COMPARAISON + "> but found <" + temp + ">");
            }
        }
        else if (current.getTag() == Tag.GEQ) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.checkIdent = false;
            this.unary();
            this.wi_expression_5();
            this.wi_expression_comparaison();
            int temp = parser.stack.pop();
            if (temp == Tag.WI_EXPRESSION_COMPARAISON) {
                temp = parser.stack.pop();
                if (temp == Tag.WI_EXPRESSION_5) {
                    temp = parser.stack.pop();
                    if (temp == Tag.UNARY) {
                        temp = parser.stack.pop();
                        if (temp == Tag.GEQ) {
                            parser.stack.push(Tag.WI_EXPRESSION_COMPARAISON);
                        } else {
                            throw new Error("Reduction/Stack error : expected <" + Tag.GEQ + "> but found <" + temp + ">");
                        }
                    } else {
                        throw new Error("Reduction/Stack error : expected <" + Tag.UNARY + "> but found <" + temp + ">");
                    }
                } else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.WI_EXPRESSION_5 + "> but found <" + temp + ">");
                }
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.WI_EXPRESSION_COMPARAISON + "> but found <" + temp + ">");
            }
        }
        else if (current.getTag() == Tag.SYMBOL && Objects.equals(current.getStringValue(), "<")) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.checkIdent = false;
            this.unary();
            this.wi_expression_5();
            this.wi_expression_comparaison();
            int temp = parser.stack.pop();
            if (temp == Tag.WI_EXPRESSION_COMPARAISON) {
                temp = parser.stack.pop();
                if (temp == Tag.WI_EXPRESSION_5) {
                    temp = parser.stack.pop();
                    if (temp == Tag.UNARY) {
                        temp = parser.stack.pop();
                        if (temp == Tag.SYMBOL) {
                            parser.stack.push(Tag.WI_EXPRESSION_COMPARAISON);
                        } else {
                            throw new Error("Reduction/Stack error : expected <" + Tag.SYMBOL + "> but found <" + temp + ">");
                        }
                    } else {
                        throw new Error("Reduction/Stack error : expected <" + Tag.UNARY + "> but found <" + temp + ">");
                    }
                } else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.WI_EXPRESSION_5 + "> but found <" + temp + ">");
                }
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.WI_EXPRESSION_COMPARAISON + "> but found <" + temp + ">");
            }
        }
        else if (current.getTag() == Tag.LEQ) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.checkIdent = false;
            this.unary();
            this.wi_expression_5();
            this.wi_expression_comparaison();
            int temp = parser.stack.pop();
            if (temp == Tag.WI_EXPRESSION_COMPARAISON) {
                temp = parser.stack.pop();
                if (temp == Tag.WI_EXPRESSION_5) {
                    temp = parser.stack.pop();
                    if (temp == Tag.UNARY) {
                        temp = parser.stack.pop();
                        if (temp == Tag.LEQ) {
                            parser.stack.push(Tag.WI_EXPRESSION_COMPARAISON);
                        } else {
                            throw new Error("Reduction/Stack error : expected <" + Tag.LEQ + "> but found <" + temp + ">");
                        }
                    } else {
                        throw new Error("Reduction/Stack error : expected <" + Tag.UNARY + "> but found <" + temp + ">");
                    }
                } else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.WI_EXPRESSION_5 + "> but found <" + temp + ">");
                }
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.WI_EXPRESSION_COMPARAISON + "> but found <" + temp + ">");
            }
        }
        else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.ASSIGNMENT + " ':='> or <" + Tag.OR + " 'or'> or <" + Tag.AND + " 'and'> or <" + Tag.SYMBOL + " '='> or <" + Tag.DIFFERENT + " '/='> or <" + Tag.SYMBOL + " '>'> or <" + Tag.GEQ + " '>='> or <" + Tag.SYMBOL + " '<'> or <" + Tag.LEQ + " '<='> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
        }
    }

    //WI_EXPRESSION_5
    private void wi_expression_5() throws IOException{
        //WI_EXPRESSION_5 ::= WI_EXPRESSION_6 WI_EXPRESSION_PLUS_MOINS (lecture de ()
        //WI_EXPRESSION_5 ::= WI_EXPRESSION_6 WI_EXPRESSION_PLUS_MOINS (lecture de entier)
        //WI_EXPRESSION_5 ::= WI_EXPRESSION_6 WI_EXPRESSION_PLUS_MOINS (lecture de caractere)
        //WI_EXPRESSION_5 ::= WI_EXPRESSION_6 WI_EXPRESSION_PLUS_MOINS (lecture de true)
        //WI_EXPRESSION_5 ::= WI_EXPRESSION_6 WI_EXPRESSION_PLUS_MOINS (lecture de false)
        //WI_EXPRESSION_5 ::= WI_EXPRESSION_6 WI_EXPRESSION_PLUS_MOINS (lecture de null)
        if ((current.getTag() == Tag.SYMBOL && Objects.equals(current.getStringValue(), "(")) || (current.getTag() == Tag.NUMCONST) || (current.getTag() == Tag.CHAR) || (current.getTag() == Tag.TRUE) || (current.getTag() == Tag.FALSE) || (current.getTag() == Tag.NULL)) {
            this.wi_expression_6();
            this.wi_expression_plus_moins();
            int temp = parser.stack.pop();
            if (temp == Tag.WI_EXPRESSION_PLUS_MOINS) {
                temp = parser.stack.pop();
                if (temp == Tag.WI_EXPRESSION_6) {
                    parser.stack.push(Tag.WI_EXPRESSION_5);
                } else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.WI_EXPRESSION_6 + "> but found <" + temp + ">");
                }
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.WI_EXPRESSION_PLUS_MOINS + "> but found <" + temp + ">");
            }
        }
        else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.NUMCONST + " 'entier'> or <" + Tag.CHAR + " 'caractere'> or <" + Tag.TRUE + " 'true'> or <" + Tag.FALSE + " 'false'> or <" + Tag.NULL + " 'null'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
        }
    }

    //WI_EXPRESSION_PLUS_MOINS
    private void wi_expression_plus_moins() throws IOException{
        //WI_EXPRESSION_PLUS_MOINS ::= ε (lecture de :=)
        //WI_EXPRESSION_PLUS_MOINS ::= ε (lecture de or)
        //WI_EXPRESSION_PLUS_MOINS ::= ε (lecture de and)
        //WI_EXPRESSION_PLUS_MOINS ::= ε (lecture de =)
        //WI_EXPRESSION_PLUS_MOINS ::= ε (lecture de /=)
        //WI_EXPRESSION_PLUS_MOINS ::= ε (lecture de >)
        //WI_EXPRESSION_PLUS_MOINS ::= ε (lecture de >=)
        //WI_EXPRESSION_PLUS_MOINS ::= ε (lecture de <)
        //WI_EXPRESSION_PLUS_MOINS ::= ε (lecture de <=)
        //WI_EXPRESSION_PLUS_MOINS ::= + UNARY WI_EXPRESSION_6 WI_EXPRESSION_PLUS_MOINS (lecture de +)
        //WI_EXPRESSION_PLUS_MOINS ::= - UNARY WI_EXPRESSION_6 WI_EXPRESSION_PLUS_MOINS (lecture de -)
        if ((current.getTag() == Tag.ASSIGNMENT) || (current.getTag() == Tag.OR) || (current.getTag() == Tag.AND) || (current.getTag() == Tag.SYMBOL && Objects.equals(current.getStringValue(), "=")) || (current.getTag() == Tag.DIFFERENT) || (current.getTag() == Tag.SYMBOL && Objects.equals(current.getStringValue(), ">")) || (current.getTag() == Tag.GEQ) || (current.getTag() == Tag.SYMBOL && Objects.equals(current.getStringValue(), "<")) || (current.getTag() == Tag.LEQ)) {
            parser.stack.push(Tag.WI_EXPRESSION_PLUS_MOINS);
        }
        else if (current.getTag() == Tag.SYMBOL && Objects.equals(current.getStringValue(), "+")) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.checkIdent = false;
            this.unary();
            this.wi_expression_6();
            this.wi_expression_plus_moins();
            int temp = parser.stack.pop();
            if (temp == Tag.WI_EXPRESSION_PLUS_MOINS) {
                temp = parser.stack.pop();
                if (temp == Tag.WI_EXPRESSION_6) {
                    temp = parser.stack.pop();
                    if (temp == Tag.UNARY) {
                        temp = parser.stack.pop();
                        if (temp == Tag.SYMBOL) {
                            parser.stack.push(Tag.WI_EXPRESSION_PLUS_MOINS);
                        } else {
                            throw new Error("Reduction/Stack error : expected <" + Tag.SYMBOL + "> but found <" + temp + ">");
                        }
                    } else {
                        throw new Error("Reduction/Stack error : expected <" + Tag.UNARY + "> but found <" + temp + ">");
                    }
                } else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.WI_EXPRESSION_6 + "> but found <" + temp + ">");
                }
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.WI_EXPRESSION_PLUS_MOINS + "> but found <" + temp + ">");
            }
        }
        else if (current.getTag() == Tag.SYMBOL && Objects.equals(current.getStringValue(), "-")) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.checkIdent = false;
            this.unary();
            this.wi_expression_6();
            this.wi_expression_plus_moins();
            int temp = parser.stack.pop();
            if (temp == Tag.WI_EXPRESSION_PLUS_MOINS) {
                temp = parser.stack.pop();
                if (temp == Tag.WI_EXPRESSION_6) {
                    temp = parser.stack.pop();
                    if (temp == Tag.UNARY) {
                        temp = parser.stack.pop();
                        if (temp == Tag.SYMBOL) {
                            parser.stack.push(Tag.WI_EXPRESSION_PLUS_MOINS);
                        } else {
                            throw new Error("Reduction/Stack error : expected <" + Tag.SYMBOL + "> but found <" + temp + ">");
                        }
                    } else {
                        throw new Error("Reduction/Stack error : expected <" + Tag.UNARY + "> but found <" + temp + ">");
                    }
                } else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.WI_EXPRESSION_6 + "> but found <" + temp + ">");
                }
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.WI_EXPRESSION_PLUS_MOINS + "> but found <" + temp + ">");
            }
        }
        else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.ASSIGNMENT + " ':='> or <" + Tag.OR + " 'or'> or <" + Tag.AND + " 'and'> or <" + Tag.SYMBOL + " '='> or <" + Tag.DIFFERENT + " '/='> or <" + Tag.SYMBOL + " '>'> or <" + Tag.GEQ + " '>='> or <" + Tag.SYMBOL + " '<'> or <" + Tag.LEQ + " '<='> or <" + Tag.SYMBOL + " '+'> or <" + Tag.SYMBOL + " '-'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
        }
    }

    //WI_EXPRESSION_6
    private void wi_expression_6() throws IOException{
        //WI_EXPRESSION_6 ::= WI_EXPRESSION_ACCES_IDENT WI_EXPRESSION_MUL_DIV (lecture de ()
        //WI_EXPRESSION_6 ::= WI_EXPRESSION_ACCES_IDENT WI_EXPRESSION_MUL_DIV (lecture de entier)
        //WI_EXPRESSION_6 ::= WI_EXPRESSION_ACCES_IDENT WI_EXPRESSION_MUL_DIV (lecture de caractere)
        //WI_EXPRESSION_6 ::= WI_EXPRESSION_ACCES_IDENT WI_EXPRESSION_MUL_DIV (lecture de true)
        //WI_EXPRESSION_6 ::= WI_EXPRESSION_ACCES_IDENT WI_EXPRESSION_MUL_DIV (lecture de false)
        //WI_EXPRESSION_6 ::= WI_EXPRESSION_ACCES_IDENT WI_EXPRESSION_MUL_DIV (lecture de null)
        if ((current.getTag() == Tag.SYMBOL && Objects.equals(current.getStringValue(), "(")) || (current.getTag() == Tag.NUMCONST) || (current.getTag() == Tag.CHAR) || (current.getTag() == Tag.TRUE) || (current.getTag() == Tag.FALSE) || (current.getTag() == Tag.NULL)) {
            this.wi_expression_acces_ident();
            this.wi_expression_mul_div();
            int temp = parser.stack.pop();
            if (temp == Tag.WI_EXPRESSION_MUL_DIV) {
                temp = parser.stack.pop();
                if (temp == Tag.WI_EXPRESSION_ACCES_IDENT) {
                    parser.stack.push(Tag.WI_EXPRESSION_6);
                } else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.WI_EXPRESSION_ACCES_IDENT + "> but found <" + temp + ">");
                }
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.WI_EXPRESSION_MUL_DIV + "> but found <" + temp + ">");
            }
        }
        else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.NUMCONST + " 'entier'> or <" + Tag.CHAR + " 'caractere'> or <" + Tag.TRUE + " 'true'> or <" + Tag.FALSE + " 'false'> or <" + Tag.NULL + " 'null'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
        }
    }

    //WI_EXPRESSION_MUL_DIV
    private void wi_expression_mul_div() throws IOException{
        //WI_EXPRESSION_MUL_DIV ::= ε (lecture de :=)
        //WI_EXPRESSION_MUL_DIV ::= ε (lecture de or)
        //WI_EXPRESSION_MUL_DIV ::= ε (lecture de and)
        //WI_EXPRESSION_MUL_DIV ::= ε (lecture de =)
        //WI_EXPRESSION_MUL_DIV ::= ε (lecture de /=)
        //WI_EXPRESSION_MUL_DIV ::= ε (lecture de >)
        //WI_EXPRESSION_MUL_DIV ::= ε (lecture de >=)
        //WI_EXPRESSION_MUL_DIV ::= ε (lecture de <)
        //WI_EXPRESSION_MUL_DIV ::= ε (lecture de <=)
        //WI_EXPRESSION_MUL_DIV ::= ε (lecture de +)
        //WI_EXPRESSION_MUL_DIV ::= ε (lecture de -)
        //WI_EXPRESSION_MUL_DIV ::= * UNARY WI_EXPRESSION_ACCES_IDENT WI_EXPRESSION_MUL_DIV (lecture de *)
        //WI_EXPRESSION_MUL_DIV ::= / UNARY WI_EXPRESSION_ACCES_IDENT WI_EXPRESSION_MUL_DIV (lecture de /)
        //WI_EXPRESSION_MUL_DIV ::= rem UNARY WI_EXPRESSION_ACCES_IDENT WI_EXPRESSION_MUL_DIV (lecture de rem)
        if ((current.getTag() == Tag.ASSIGNMENT) || (current.getTag() == Tag.OR) || (current.getTag() == Tag.AND) || (current.getTag() == Tag.SYMBOL && Objects.equals(current.getStringValue(), "=")) || (current.getTag() == Tag.DIFFERENT) || (current.getTag() == Tag.SYMBOL && Objects.equals(current.getStringValue(), ">")) || (current.getTag() == Tag.GEQ) || (current.getTag() == Tag.SYMBOL && Objects.equals(current.getStringValue(), "<")) || (current.getTag() == Tag.LEQ) || (current.getTag() == Tag.SYMBOL && Objects.equals(current.getStringValue(), "+")) || (current.getTag() == Tag.SYMBOL && Objects.equals(current.getStringValue(), "-"))) {
            parser.stack.push(Tag.WI_EXPRESSION_MUL_DIV);
        }
        else if (current.getTag() == Tag.SYMBOL && Objects.equals(current.getStringValue(), "*")) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.checkIdent = false;
            this.unary();
            this.wi_expression_acces_ident();
            this.wi_expression_mul_div();
            int temp = parser.stack.pop();
            if (temp == Tag.WI_EXPRESSION_MUL_DIV) {
                temp = parser.stack.pop();
                if (temp == Tag.WI_EXPRESSION_ACCES_IDENT) {
                    temp = parser.stack.pop();
                    if (temp == Tag.UNARY) {
                        temp = parser.stack.pop();
                        if (temp == Tag.SYMBOL) {
                            parser.stack.push(Tag.WI_EXPRESSION_MUL_DIV);
                        } else {
                            throw new Error("Reduction/Stack error : expected <" + Tag.SYMBOL + "> but found <" + temp + ">");
                        }
                    } else {
                        throw new Error("Reduction/Stack error : expected <" + Tag.UNARY + "> but found <" + temp + ">");
                    }
                } else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.WI_EXPRESSION_ACCES_IDENT + "> but found <" + temp + ">");
                }
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.WI_EXPRESSION_MUL_DIV + "> but found <" + temp + ">");
            }
        }
        else if (current.getTag() == Tag.SYMBOL && Objects.equals(current.getStringValue(), "/")) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.checkIdent = false;
            this.unary();
            this.wi_expression_acces_ident();
            this.wi_expression_mul_div();
            int temp = parser.stack.pop();
            if (temp == Tag.WI_EXPRESSION_MUL_DIV) {
                temp = parser.stack.pop();
                if (temp == Tag.WI_EXPRESSION_ACCES_IDENT) {
                    temp = parser.stack.pop();
                    if (temp == Tag.UNARY) {
                        temp = parser.stack.pop();
                        if (temp == Tag.SYMBOL) {
                            parser.stack.push(Tag.WI_EXPRESSION_MUL_DIV);
                        } else {
                            throw new Error("Reduction/Stack error : expected <" + Tag.SYMBOL + "> but found <" + temp + ">");
                        }
                    } else {
                        throw new Error("Reduction/Stack error : expected <" + Tag.UNARY + "> but found <" + temp + ">");
                    }
                } else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.WI_EXPRESSION_ACCES_IDENT + "> but found <" + temp + ">");
                }
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.WI_EXPRESSION_MUL_DIV + "> but found <" + temp + ">");
            }
        }
        else if (current.getTag() == Tag.REM) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.checkIdent = false;
            this.unary();
            this.wi_expression_acces_ident();
            this.wi_expression_mul_div();
            int temp = parser.stack.pop();
            if (temp == Tag.WI_EXPRESSION_MUL_DIV) {
                temp = parser.stack.pop();
                if (temp == Tag.WI_EXPRESSION_ACCES_IDENT) {
                    temp = parser.stack.pop();
                    if (temp == Tag.UNARY) {
                        temp = parser.stack.pop();
                        if (temp == Tag.REM) {
                            parser.stack.push(Tag.WI_EXPRESSION_MUL_DIV);
                        } else {
                            throw new Error("Reduction/Stack error : expected <" + Tag.REM + "> but found <" + temp + ">");
                        }
                    } else {
                        throw new Error("Reduction/Stack error : expected <" + Tag.UNARY + "> but found <" + temp + ">");
                    }
                } else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.WI_EXPRESSION_ACCES_IDENT + "> but found <" + temp + ">");
                }
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.WI_EXPRESSION_MUL_DIV + "> but found <" + temp + ">");
            }
        }
        else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.ASSIGNMENT + " ':='> or <" + Tag.OR + " 'or'> or <" + Tag.AND + " 'and'> or <" + Tag.SYMBOL + " '='> or <" + Tag.DIFFERENT + " '/='> or <" + Tag.SYMBOL + " '>'> or <" + Tag.GEQ + " '>='> or <" + Tag.SYMBOL + " '<'> or <" + Tag.LEQ + " '<='> or <" + Tag.SYMBOL + " '+'> or <" + Tag.SYMBOL + " '-'> or <" + Tag.SYMBOL + " '*'> or <" + Tag.SYMBOL + " '/'> or <" + Tag.REM + " 'rem'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
        }
    }

    //WI_EXPRESSION_ACCES_IDENT
    private void wi_expression_acces_ident() throws IOException{
        //WI_EXPRESSION_ACCES_IDENT ::= WI_EXPRESSION_ATOMS EXPRESSION_ACCESS_IDENT (lecture de ()
        //WI_EXPRESSION_ACCES_IDENT ::= WI_EXPRESSION_ATOMS EXPRESSION_ACCESS_IDENT (lecture de entier)
        //WI_EXPRESSION_ACCES_IDENT ::= WI_EXPRESSION_ATOMS EXPRESSION_ACCESS_IDENT (lecture de caractere)
        //WI_EXPRESSION_ACCES_IDENT ::= WI_EXPRESSION_ATOMS EXPRESSION_ACCESS_IDENT (lecture de true)
        //WI_EXPRESSION_ACCES_IDENT ::= WI_EXPRESSION_ATOMS EXPRESSION_ACCESS_IDENT (lecture de false)
        //WI_EXPRESSION_ACCES_IDENT ::= WI_EXPRESSION_ATOMS EXPRESSION_ACCESS_IDENT (lecture de null)
        if ((current.getTag() == Tag.SYMBOL && Objects.equals(current.getStringValue(), "(")) || (current.getTag() == Tag.NUMCONST) || (current.getTag() == Tag.CHAR) || (current.getTag() == Tag.TRUE) || (current.getTag() == Tag.FALSE) || (current.getTag() == Tag.NULL)) {
            this.wi_expression_atoms();
            if (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(".")) {
                this.checkIdent = true;
            }
            this.expression_access_ident();
            int temp = parser.stack.pop();
            if (temp == Tag.EXPRESSION_ACCESS_IDENT) {
                temp = parser.stack.pop();
                if (temp == Tag.WI_EXPRESSION_ATOMS) {
                    parser.stack.push(Tag.WI_EXPRESSION_ACCES_IDENT);
                } else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.WI_EXPRESSION_ATOMS + "> but found <" + temp + ">");
                }
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.EXPRESSION_ACCESS_IDENT + "> but found <" + temp + ">");
            }
        }
        else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.NUMCONST + " 'entier'> or <" + Tag.CHAR + " 'caractere'> or <" + Tag.TRUE + " 'true'> or <" + Tag.FALSE + " 'false'> or <" + Tag.NULL + " 'null'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
        }
    }

    //WI_EXPRESSION_ATOMS
    private void wi_expression_atoms() throws IOException {
        //WI_EXPRESSION_ATOMS ::= ( UNARY GENERATE_EXPRESSION (lecture de ()
        //WI_EXPRESSION_ATOMS ::= entier (lecture de entier)
        //WI_EXPRESSION_ATOMS ::= caractere (lecture de caractere)
        //WI_EXPRESSION_ATOMS ::= true (lecture de true)
        //WI_EXPRESSION_ATOMS ::= false (lecture de false)
        //WI_EXPRESSION_ATOMS ::= null (lecture de null)
        if (current.getTag() == Tag.SYMBOL && Objects.equals(current.getStringValue(), "(")) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.checkIdent = false;
            this.unary();
            this.generate_expression();
            int temp = parser.stack.pop();
            if (temp == Tag.GENERATE_EXPRESSION) {
                temp = parser.stack.pop();
                if (temp == Tag.UNARY) {
                    temp = parser.stack.pop();
                    if (temp == Tag.SYMBOL) {
                        parser.stack.push(Tag.WI_EXPRESSION_ATOMS);
                    } else {
                        throw new Error("Reduction/Stack error : expected <" + Tag.SYMBOL + "> but found <" + temp + ">");
                    }
                } else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.UNARY + "> but found <" + temp + ">");
                }
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.GENERATE_EXPRESSION + "> but found <" + temp + ">");
            }
        }
        else if (current.getTag() == Tag.NUMCONST) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.checkIdent = false;
            int temp = parser.stack.pop();
            if (temp == Tag.NUMCONST) {
                parser.stack.push(Tag.WI_EXPRESSION_ATOMS);
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.NUMCONST + "> but found <" + temp + ">");
            }
        }
        else if (current.getTag() == Tag.CHAR) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.checkIdent = false;
            int temp = parser.stack.pop();
            if (temp == Tag.CHAR) {
                parser.stack.push(Tag.WI_EXPRESSION_ATOMS);
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.CHAR + "> but found <" + temp + ">");
            }
        }
        else if (current.getTag() == Tag.TRUE) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.checkIdent = false;
            int temp = parser.stack.pop();
            if (temp == Tag.TRUE) {
                parser.stack.push(Tag.WI_EXPRESSION_ATOMS);
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.TRUE + "> but found <" + temp + ">");
            }
        }
        else if (current.getTag() == Tag.FALSE) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.checkIdent = false;
            int temp = parser.stack.pop();
            if (temp == Tag.FALSE) {
                parser.stack.push(Tag.WI_EXPRESSION_ATOMS);
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.FALSE + "> but found <" + temp + ">");
            }
        }
        else if (current.getTag() == Tag.NULL) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.checkIdent = false;
            int temp = parser.stack.pop();
            if (temp == Tag.NULL) {
                parser.stack.push(Tag.WI_EXPRESSION_ATOMS);
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.NULL + "> but found <" + temp + ">");
            }
        }
        else {
            throw new Error("Error line "+parser.lexer.getLine()+" : expected <" + Tag.SYMBOL + " '('> or <" + Tag.NUMCONST + " 'entier'> or <" + Tag.CHAR + " 'caractere'> or <" + Tag.TRUE + " 'true'> or <" + Tag.FALSE + " 'false'> or <" + Tag.NULL + " 'null'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
        }
    }
}
