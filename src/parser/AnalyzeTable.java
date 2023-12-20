package parser;

import lexer.Tag;
import lexer.Token;

import java.io.IOException;
import java.util.Objects;

public class AnalyzeTable {

    public Parser parser;
    public Token current;
    
    public AnalyzeTable(Parser parser) {
        this.parser = parser;
    }

    public void analyze() throws IOException {
        current = parser.lexer.scan();
        this.ficher();
    }

    private void ficher() throws IOException {
        //FICHIER ::= with Ada . Text_IO ; use Ada . Text_IO ; PROCEDURE BEGIN_INSTRUCTION ; EOF (lecture de with)
        if (current.getTag() == Tag.WITH) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            if ((current.getTag() == Tag.ID) && (current.getStringValue().equals("ada"))) {
                parser.stack.push(current.getTag());
                current = parser.lexer.scan();
                if ((current.getTag() == Tag.SYMBOL) && (current.getStringValue().equals("."))){
                    parser.stack.push(current.getTag());
                    current = parser.lexer.scan();
                    if ((current.getTag() == Tag.ID) && (current.getStringValue().equals("text_io"))) {
                        parser.stack.push(current.getTag());
                        current = parser.lexer.scan();
                        if((current.getTag() == Tag.SYMBOL) && (current.getStringValue().equals(";"))) {
                            parser.stack.push(current.getTag());
                            current = parser.lexer.scan();
                            if(current.getTag() == Tag.USE) {
                                parser.stack.push(current.getTag());
                                current = parser.lexer.scan();
                                if ((current.getTag() == Tag.ID) && (current.getStringValue().equals("ada"))) {
                                    parser.stack.push(current.getTag());
                                    current = parser.lexer.scan();
                                    if ((current.getTag() == Tag.SYMBOL) && (current.getStringValue().equals("."))){
                                        parser.stack.push(current.getTag());
                                        current = parser.lexer.scan();
                                        if ((current.getTag() == Tag.ID) && (current.getStringValue().equals("text_io"))) {
                                            parser.stack.push(current.getTag());
                                            current = parser.lexer.scan();
                                            if((current.getTag() == Tag.SYMBOL) && (current.getStringValue().equals(";"))) {
                                                parser.stack.push(current.getTag());
                                                current = parser.lexer.scan();
                                                this.procedure();
                                                this.begin_instruction();
                                                if((current.getTag() == Tag.SYMBOL) && (current.getStringValue().equals(";"))) {
                                                    parser.stack.push(current.getTag());
                                                    current = parser.lexer.scan();
                                                    if(current.getTag() == Tag.EOF) {
                                                        parser.stack.push(current.getTag());
                                                        current = parser.lexer.scan();
                                                        // verify stack and replace with FICHIER
                                                        int temp = parser.stack.pop();
                                                        if (temp == Tag.EOF) {
                                                            temp = parser.stack.pop();
                                                            if(temp == Tag.SYMBOL){
                                                                temp = parser.stack.pop();
                                                                if(temp == Tag.BEGIN_INSTRUCTION) {
                                                                    temp = parser.stack.pop();
                                                                    if(temp == Tag.NT_PROCEDURE) {
                                                                        temp = parser.stack.pop();
                                                                        if(temp == Tag.SYMBOL) {
                                                                            temp = parser.stack.pop();
                                                                            if(temp == Tag.ID) {
                                                                                temp = parser.stack.pop();
                                                                                if(temp == Tag.SYMBOL) {
                                                                                    temp = parser.stack.pop();
                                                                                    if(temp == Tag.ID) {
                                                                                        temp = parser.stack.pop();
                                                                                        if(temp == Tag.USE) {
                                                                                            temp = parser.stack.pop();
                                                                                            if(temp == Tag.SYMBOL) {
                                                                                                temp = parser.stack.pop();
                                                                                                if(temp == Tag.ID) {
                                                                                                    temp = parser.stack.pop();
                                                                                                    if(temp == Tag.SYMBOL) {
                                                                                                        temp = parser.stack.pop();
                                                                                                        if(temp == Tag.ID) {
                                                                                                            temp = parser.stack.pop();
                                                                                                            if(temp == Tag.WITH) {
                                                                                                                parser.stack.push(Tag.FICHIER);
                                                                                                                return;
                                                                                                            }
                                                                                                            else {
                                                                                                                throw new Error("Reduction/Stack error : expected <"+Tag.WITH+"> but found <"+temp+">");
                                                                                                            }
                                                                                                        }
                                                                                                        else {
                                                                                                            throw new Error("Reduction/Stack error : expected <"+Tag.SYMBOL +"> but found <"+temp+">");
                                                                                                        }
                                                                                                    }
                                                                                                    else {
                                                                                                        throw new Error("Reduction/Stack error : expected <"+Tag.ID+"> but found <"+temp+">");
                                                                                                    }
                                                                                                }
                                                                                                else {
                                                                                                    throw new Error("Reduction/Stack error : expected <"+Tag.ID+"> but found <"+temp+">");
                                                                                                }
                                                                                            }
                                                                                            else {
                                                                                                throw new Error("Reduction/Stack error : expected <"+Tag.SYMBOL +"> but found <"+temp+">");
                                                                                            }
                                                                                        }
                                                                                        else {
                                                                                            throw new Error("Reduction/Stack error : expected <"+Tag.USE+"> but found <"+temp+">");
                                                                                        }
                                                                                    }
                                                                                    else {
                                                                                        throw new Error("Reduction/Stack error : expected <"+Tag.ID+"> but found <"+temp+">");
                                                                                    }
                                                                                }
                                                                                else {
                                                                                    throw new Error("Reduction/Stack error : expected <"+Tag.SYMBOL +"> but found <"+temp+">");
                                                                                }
                                                                            }
                                                                            else {
                                                                                throw new Error("Reduction/Stack error : expected <"+Tag.ID+"> but found <"+temp+">");
                                                                            }
                                                                        }
                                                                        else {
                                                                            throw new Error("Reduction/Stack error : expected <"+Tag.SYMBOL +"> but found <"+temp+">");
                                                                        }
                                                                    }
                                                                    else {
                                                                        throw new Error("Reduction/Stack error : expected <"+Tag.NT_PROCEDURE+"> but found <"+temp+">");
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
                                                            throw new Error("Reduction/Stack error : expected <"+Tag.EOF+"> but found <"+current.getTag()+">");
                                                        }
                                                    }
                                                    else {
                                                        throw new Error("Error : expected <"+Tag.EOF+" 'EOF'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
                                                    }
                                                }
                                                else {
                                                    throw new Error("Error : expected <"+Tag.SYMBOL +" ';'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");

                                                }
                                            }
                                            else {
                                                throw new Error("Error : expected <"+Tag.SYMBOL +" ';'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
                                            }
                                        }
                                        else {
                                            throw new Error("Error : expected <"+Tag.ID+" 'text_io'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
                                        }
                                    }
                                    else {
                                        throw new Error("Error : expected <"+Tag.SYMBOL +" '.'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
                                    }
                                }
                                else {
                                    throw new Error("Error : expected <"+Tag.ID+" 'ada'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
                                }
                            }
                            else {
                                throw new Error("Error : expected <"+Tag.USE+" 'USE'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
                            }
                        }
                        else {
                            throw new Error("Error : expected <"+Tag.SYMBOL +" ';'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
                        }
                    }
                    else {
                        throw new Error("Error : expected <"+Tag.ID+" 'text_io'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
                    }
                }
                else {
                    throw new Error("Error : expected <"+Tag.SYMBOL +" '.'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
                }
            }
            else {
                throw new Error("Error : expected <"+Tag.ID+" 'ada'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
            }
        }
        else {
            throw new Error("Error : expected <"+Tag.WITH+" 'WITH'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
        }
    }

    private void procedure() throws IOException {
        //PROCEDURE ::= procedure ident is END_PROCEDURE (lecture de procedure)
        if (current.getTag() == Tag.PROCEDURE) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            if (current.getTag() == Tag.ID) {
                parser.stack.push(current.getTag());
                current = parser.lexer.scan();
                if (current.getTag() == Tag.IS) {
                    parser.stack.push(current.getTag());
                    current = parser.lexer.scan();
                    this.end_procedure();
                    int temp = parser.stack.pop();
                    if (temp == Tag.END_PROCEDURE) {
                        temp = parser.stack.pop();
                        if(temp == Tag.IS) {
                            temp = parser.stack.pop();
                            if(temp == Tag.ID) {
                                temp = parser.stack.pop();
                                if(temp == Tag.PROCEDURE) {
                                    parser.stack.push(Tag.NT_PROCEDURE);
                                    return;
                                }
                                else {
                                    throw new Error("Reduction/Stack error : expected <"+Tag.PROCEDURE+"> but found <"+temp+">");
                                }
                            }
                            else {
                                throw new Error("Reduction/Stack error : expected <"+Tag.ID+"> but found <"+temp+">");
                            }
                        }
                        else {
                            throw new Error("Reduction/Stack error : expected <"+Tag.IS+"> but found <"+temp+">");
                        }
                    }
                    else {
                        throw new Error("Reduction/Stack error : expected <"+Tag.END_PROCEDURE+"> but found <"+temp+">");
                    }
                }
                else {
                    throw new Error("Error : expected <"+Tag.IS+" 'IS'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
                }
            }
            else {
                throw new Error("Error : expected <"+Tag.ID+" 'ident'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
            }
        }
        else {
            throw new Error("Error : expected <"+Tag.PROCEDURE+" 'procedure'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
        }
    }

    private void end_procedure() throws IOException{
        //END_PROCEDURE ::= GENERATE_DECLARATIONS (lecture de procedure)
        //END_PROCEDURE ::= GENERATE_DECLARATIONS (lecture de ident)
        //END_PROCEDURE ::= ε (lecture de begin)
        //END_PROCEDURE ::= GENERATE_DECLARATIONS (lecture de type)
        //END_PROCEDURE ::= GENERATE_DECLARATIONS (lecture de function)
        if((current.getTag() == Tag.PROCEDURE) || (current.getTag() == Tag.ID) || (current.getTag() == Tag.TYPE) || (current.getTag() == Tag.FUNCTION)) {
            this.generate_declarations();
            int temp = parser.stack.pop();
            if(temp == Tag.GENERATE_DECLARATIONS) {
                parser.stack.push(Tag.END_PROCEDURE);
                return;
            }
            else {
                throw new Error("Reduction/Stack error : expected <"+Tag.GENERATE_DECLARATIONS+"> but found <"+temp+">");
            }
        }
        else if(current.getTag() == Tag.BEGIN) {
            parser.stack.push(Tag.END_PROCEDURE);
            return;
        }
        else {
            throw new Error("Error : expected <"+Tag.PROCEDURE+" 'procedure'> or <"+Tag.ID+" 'ident'> or <"+Tag.TYPE+" 'type'> or <"+Tag.FUNCTION+" 'function'> or <"+Tag.BEGIN+" 'begin'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
        }
    }

    private void begin_instruction() throws IOException{
        //BEGIN_INSTRUCTION ::= begin GENERATE_INSTRUCTIONS end END_BEGIN_INSTRUCTION (lecture de begin)
        if(current.getTag() == Tag.BEGIN) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.generate_instructions();
            if(current.getTag() == Tag.END) {
                parser.stack.push(current.getTag());
                current = parser.lexer.scan();
                this.end_begin_instruction();
                int temp = parser.stack.pop();
                if(temp == Tag.END_BEGIN_INSTRUCTION) {
                    temp = parser.stack.pop();
                    if(temp == Tag.END) {
                        temp = parser.stack.pop();
                        if(temp == Tag.GENERATE_INSTRUCTIONS) {
                            temp = parser.stack.pop();
                            if(temp == Tag.BEGIN) {
                                parser.stack.push(Tag.BEGIN_INSTRUCTION);
                                return;
                            }
                            else {
                                throw new Error("Reduction/Stack error : expected <"+Tag.BEGIN+"> but found <"+temp+">");
                            }
                        }
                        else {
                            throw new Error("Reduction/Stack error : expected <"+Tag.GENERATE_INSTRUCTIONS+"> but found <"+temp+">");
                        }
                    }
                    else {
                        throw new Error("Reduction/Stack error : expected <"+Tag.END+"> but found <"+temp+">");
                    }
                }
                else {
                    throw new Error("Reduction/Stack error : expected <"+Tag.END_BEGIN_INSTRUCTION+"> but found <"+temp+">");
                }
            }
            else {
                throw new Error("Error : expected <"+Tag.END+" 'end'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
            }
        }
        else {
            throw new Error("Error : expected <"+Tag.BEGIN+" 'begin'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
        }
    }

    //END_BEGIN_INSTRUCTION
    private void end_begin_instruction() throws IOException{
        //END_BEGIN_INSTRUCTION ::= ε (lecture de ;)
        //END_BEGIN_INSTRUCTION ::= ident (lecture de ident)
        if((current.getTag() == Tag.SYMBOL) && (current.getStringValue().equals(";"))) {
            parser.stack.push(Tag.END_BEGIN_INSTRUCTION);
            return;
        }
        else if(current.getTag() == Tag.ID) {
            current = parser.lexer.scan();
            parser.stack.push(Tag.END_BEGIN_INSTRUCTION);
            return;
        }
        else {
            throw new Error("Error : expected <"+Tag.SYMBOL +" ';'> or <"+Tag.ID+" 'ident'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
        }
    }

    //GENERATE_DECLARATIONS
    private void generate_declarations() throws IOException{
        //GENERATE_DECLARATIONS ::= DECLARATION GENERATE_DECLARATIONS_FACTORISATION (lecture de procedure)
        //GENERATE_DECLARATIONS ::= DECLARATION GENERATE_DECLARATIONS_FACTORISATION (lecture de ident)
        //GENERATE_DECLARATIONS ::= DECLARATION GENERATE_DECLARATIONS_FACTORISATION (lecture de type)
        //GENERATE_DECLARATIONS ::= DECLARATION GENERATE_DECLARATIONS_FACTORISATION (lecture de function)
        if ((current.getTag() == Tag.PROCEDURE) || (current.getTag() == Tag.ID) || (current.getTag() == Tag.TYPE) || (current.getTag() == Tag.FUNCTION)) {
            this.declaration();
            this.generate_declarations_factorisation();
            int temp = parser.stack.pop();
            if(temp == Tag.GENERATE_DECLARATIONS_FACTORISATION) {
                temp = parser.stack.pop();
                if(temp == Tag.DECLARATION) {
                    parser.stack.push(Tag.GENERATE_DECLARATIONS);
                    return;
                }
                else {
                    throw new Error("Reduction/Stack error : expected <"+Tag.DECLARATION+"> but found <"+temp+">");
                }
            }
            else {
                throw new Error("Reduction/Stack error : expected <"+Tag.GENERATE_DECLARATIONS_FACTORISATION+"> but found <"+temp+">");
            }
        }
        else {
            throw new Error("Error : expected <"+Tag.PROCEDURE+" 'procedure'> or <"+Tag.ID+" 'ident'> or <"+Tag.TYPE+" 'type'> or <"+Tag.FUNCTION+" 'function'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
        }
    }

    //GENERATE_DECLARATIONS_FACTORISATION
    private void generate_declarations_factorisation() throws IOException{
        //GENERATE_DECLARATIONS_FACTORISATION ::= GENERATE_DECLARATIONS (lecture de procedure)
        //GENERATE_DECLARATIONS_FACTORISATION ::= GENERATE_DECLARATIONS (lecture de ident)
        //GENERATE_DECLARATIONS_FACTORISATION ::= ε (lecture de begin)
        //GENERATE_DECLARATIONS_FACTORISATION ::= GENERATE_DECLARATIONS (lecture de type)
        //GENERATE_DECLARATIONS_FACTORISATION ::= GENERATE_DECLARATIONS (lecture de function)
        if ((current.getTag() == Tag.PROCEDURE) || (current.getTag() == Tag.ID) || (current.getTag() == Tag.TYPE) || (current.getTag() == Tag.FUNCTION)) {
            this.generate_declarations();
            int temp = parser.stack.pop();
            if(temp == Tag.GENERATE_DECLARATIONS) {
                parser.stack.push(Tag.GENERATE_DECLARATIONS_FACTORISATION);
                return;
            }
            else {
                throw new Error("Reduction/Stack error : expected <"+Tag.GENERATE_DECLARATIONS+"> but found <"+temp+">");
            }
        }
        else if(current.getTag() == Tag.BEGIN) {
            parser.stack.push(Tag.GENERATE_DECLARATIONS_FACTORISATION);
            return;
        }
        else {
            throw new Error("Error : expected <"+Tag.PROCEDURE+" 'procedure'> or <"+Tag.ID+" 'ident'> or <"+Tag.TYPE+" 'type'> or <"+Tag.FUNCTION+" 'function'> or <"+Tag.BEGIN+" 'begin'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
        }
    }

    //GENERATE_INSTRUCTIONS
    private void generate_instructions() throws IOException{
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
        if ((current.getTag() == Tag.ID) || (current.getTag() == Tag.BEGIN) || ((current.getTag() == Tag.SYMBOL) && (current.getStringValue().equals("("))) || (current.getTag() == Tag.RETURN) || (current.getTag() == Tag.NEW) || (current.getTag() == Tag.CHARACTERVAL) || (current.getTag() == Tag.NOT) || (current.getTag() == Tag.NUMCONST) || (current.getTag() == Tag.SYMBOL) || (current.getTag() == Tag.TRUE) || (current.getTag() == Tag.FALSE) || (current.getTag() == Tag.NULL) || (current.getTag() == Tag.IF) || (current.getTag() == Tag.FOR) || (current.getTag() == Tag.WHILE)) {
            this.instruction();
            this.generate_instructions_factorisation();
            int temp = parser.stack.pop();
            if(temp == Tag.GENERATE_INSTRUCTIONS_FACTORISATION) {
                temp = parser.stack.pop();
                if(temp == Tag.INSTRUCTION) {
                    parser.stack.push(Tag.GENERATE_INSTRUCTIONS);
                    return;
                }
                else {
                    throw new Error("Reduction/Stack error : expected <"+Tag.INSTRUCTION+"> but found <"+temp+">");
                }
            }
            else {
                throw new Error("Reduction/Stack error : expected <"+Tag.GENERATE_INSTRUCTIONS_FACTORISATION+"> but found <"+temp+">");
            }
        }
        else {
            throw new Error("Error : expected <"+Tag.ID+" 'ident'> or <"+Tag.BEGIN+" 'begin'> or <"+Tag.SYMBOL +" '('> or <"+Tag.RETURN+" 'return'> or <"+Tag.NEW+" 'new'> or <"+Tag.CHARACTERVAL+" 'character'val'> or <"+Tag.NOT+" 'not'> or <"+Tag.NUMCONST+" 'entier'> or <"+Tag.SYMBOL +" 'caractere'> or <"+Tag.TRUE+" 'true'> or <"+Tag.FALSE+" 'false'> or <"+Tag.NULL+" 'null'> or <"+Tag.IF+" 'if'> or <"+Tag.FOR+" 'for'> or <"+Tag.WHILE+" 'while'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
        }
    }

    //GENERATE_INSTRUCTIONS_FACTORISATION
    private void generate_instructions_factorisation() throws IOException{
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
            if(temp == Tag.GENERATE_INSTRUCTIONS) {
                parser.stack.push(Tag.GENERATE_INSTRUCTIONS_FACTORISATION);
                return;
            }
            else {
                throw new Error("Reduction/Stack error : expected <"+Tag.GENERATE_INSTRUCTIONS+"> but found <"+temp+">");
            }
        }
        else if((current.getTag() == Tag.SYMBOL) && (current.getStringValue().equals("else"))) {
            parser.stack.push(Tag.GENERATE_INSTRUCTIONS_FACTORISATION);
            return;
        }
        else if((current.getTag() == Tag.SYMBOL) && (current.getStringValue().equals("end"))) {
            parser.stack.push(Tag.GENERATE_INSTRUCTIONS_FACTORISATION);
            return;
        }
        else if((current.getTag() == Tag.SYMBOL) && (current.getStringValue().equals("elsif"))) {
            parser.stack.push(Tag.GENERATE_INSTRUCTIONS_FACTORISATION);
            return;
        }
        else {
            throw new Error("Error : expected <" + Tag.ID + " 'ident'> or <" + Tag.BEGIN + " 'begin'> or <" + Tag.SYMBOL + " '('> or <" + Tag.RETURN + " 'return'> or <" + Tag.NEW + " 'new'> or <" + Tag.CHARACTERVAL + " 'character'val'> or <" + Tag.NOT + " 'not'> or <" + Tag.TRUE + " 'true'> or <" + Tag.FALSE + " 'false'> or <" + Tag.NULL + " 'null'> or <" + Tag.IF + " 'if'> or <" + Tag.FOR + " 'for'> or <" + Tag.WHILE + " 'while'> or <" + Tag.SYMBOL + " 'else'> or <" + Tag.SYMBOL + " 'end'> or <" + Tag.SYMBOL + " 'elsif'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
        }
    }


    //DECLARATION
    private void declaration() throws IOException{
        //DECLARATION ::= procedure ident DECLARATION_PROCEDURE (lecture de procedure)
        //DECLARATION ::= GENERATE_IDENT : TYPE DECLARATION_WITH_EXPRESSION (lecture de ident)
        //DECLARATION ::= type ident DECLARATION_TYPE (lecture de type)
        //DECLARATION ::= function ident DECLARATION_FUNCTION (lecture de function)
        if(current.getTag() == Tag.PROCEDURE) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            if(current.getTag() == Tag.ID) {
                parser.stack.push(current.getTag());
                current = parser.lexer.scan();
                this.declaration_procedure();
                int temp = parser.stack.pop();
                if(temp == Tag.DECLARATION_PROCEDURE) {
                    temp = parser.stack.pop();
                    if(temp == Tag.ID) {
                        temp = parser.stack.pop();
                        if(temp == Tag.PROCEDURE) {
                            parser.stack.push(Tag.DECLARATION);
                            return;
                        }
                        else {
                            throw new Error("Reduction/Stack error : expected <"+Tag.PROCEDURE+"> but found <"+temp+">");
                        }
                    }
                    else {
                        throw new Error("Reduction/Stack error : expected <"+Tag.ID+"> but found <"+temp+">");
                    }
                }
                else {
                    throw new Error("Reduction/Stack error : expected <"+Tag.DECLARATION_PROCEDURE+"> but found <"+temp+">");
                }
            }
            else {
                throw new Error("Error : expected <"+Tag.ID+" 'ident'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
            }
        }
        else if(current.getTag() == Tag.ID) {
            this.generate_ident();
            if ((current.getTag() == Tag.SYMBOL) && (current.getStringValue().equals(":"))) {
                parser.stack.push(current.getTag());
                current = parser.lexer.scan();
                this.type();
                this.declaration_with_expression();
                int temp = parser.stack.pop();
                if (temp == Tag.DECLARATION_WITH_EXPRESSION) {
                    temp = parser.stack.pop();
                    if (temp == Tag.TYPE) {
                        temp = parser.stack.pop();
                        if (temp == Tag.SYMBOL) {
                            temp = parser.stack.pop();
                            if (temp == Tag.ID) {
                                parser.stack.push(Tag.DECLARATION);
                                return;
                            } else {
                                throw new Error("Reduction/Stack error : expected <" + Tag.ID + "> but found <" + temp + ">");
                            }
                        } else {
                            throw new Error("Reduction/Stack error : expected <" + Tag.SYMBOL + "> but found <" + temp + ">");
                        }
                    } else {
                        throw new Error("Reduction/Stack error : expected <" + Tag.TYPE + "> but found <" + temp + ">");
                    }
                }
            }
        }
        else if(current.getTag() == Tag.TYPE){
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            if(current.getTag() == Tag.ID) {
                parser.stack.push(current.getTag());
                current = parser.lexer.scan();
                this.declaration_type();
                int temp = parser.stack.pop();
                if(temp == Tag.DECLARATION_TYPE) {
                    temp = parser.stack.pop();
                    if(temp == Tag.ID) {
                        temp = parser.stack.pop();
                        if(temp == Tag.TYPE) {
                            parser.stack.push(Tag.DECLARATION);
                            return;
                        }
                        else {
                            throw new Error("Reduction/Stack error : expected <"+Tag.TYPE+"> but found <"+temp+">");
                        }
                    }
                    else {
                        throw new Error("Reduction/Stack error : expected <"+Tag.ID+"> but found <"+temp+">");
                    }
                }
                else {
                    throw new Error("Reduction/Stack error : expected <"+Tag.DECLARATION_TYPE+"> but found <"+temp+">");
                }
            }
            else {
                throw new Error("Error : expected <"+Tag.ID+" 'ident'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
            }
        }
        else if(current.getTag() == Tag.FUNCTION) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            if(current.getTag() == Tag.ID) {
                parser.stack.push(current.getTag());
                current = parser.lexer.scan();
                this.declaration_function();
                int temp = parser.stack.pop();
                if(temp == Tag.DECLARATION_FUNCTION) {
                    temp = parser.stack.pop();
                    if(temp == Tag.ID) {
                        temp = parser.stack.pop();
                        if(temp == Tag.FUNCTION) {
                            parser.stack.push(Tag.DECLARATION);
                            return;
                        }
                        else {
                            throw new Error("Reduction/Stack error : expected <"+Tag.FUNCTION+"> but found <"+temp+">");
                        }
                    }
                    else {
                        throw new Error("Reduction/Stack error : expected <"+Tag.ID+"> but found <"+temp+">");
                    }
                }
                else {
                    throw new Error("Reduction/Stack error : expected <"+Tag.DECLARATION_FUNCTION+"> but found <"+temp+">");
                }
            }
            else {
                throw new Error("Error : expected <" + Tag.ID + " 'ident'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
            }
        }
        else {
            throw new Error("Error : expected <"+Tag.PROCEDURE+" 'procedure'> or <"+Tag.ID+" 'ident'> or <"+Tag.TYPE+" 'type'> or <"+Tag.FUNCTION+" 'function'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
        }
    }

    //DECLARATION_TYPE
    private void declaration_type() throws IOException{
        //DECLARATION_TYPE ::= ; (lecture de ;)
        //DECLARATION_TYPE ::= is ACCESS_RECORD (lecture de is)
        if (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(";")) {
            current = parser.lexer.scan();
            parser.stack.push(Tag.DECLARATION_TYPE);
            return;
        }
        else if(current.getTag() == Tag.IS) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.access_record();
            int temp = parser.stack.pop();
            if(temp == Tag.ACCESS_RECORD) {
                temp = parser.stack.pop();
                if(temp == Tag.IS) {
                    parser.stack.push(Tag.DECLARATION_TYPE);
                    return;
                }
                else {
                    throw new Error("Reduction/Stack error : expected <"+Tag.IS+"> but found <"+temp+">");
                }
            }
            else {
                throw new Error("Reduction/Stack error : expected <"+Tag.ACCESS_RECORD+"> but found <"+temp+">");
            }
        }
        else {
            throw new Error("Error : expected <"+Tag.SYMBOL +" ';'> or <"+Tag.IS+" 'is'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
        }
    }

    //ACCESS_RECORD
    private void access_record() throws IOException{
        //ACCESS_RECORD ::= access ident ; (lecture de access)
        //ACCESS_RECORD ::= record GENERATE_CHAMPS end record ; (lecture de record)
        if (current.getTag() == Tag.ACCESS) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            if (current.getTag() == Tag.ID) {
                parser.stack.push(current.getTag());
                current = parser.lexer.scan();
                if (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(";")) {
                    parser.stack.push(current.getTag());
                    current = parser.lexer.scan();
                    int temp = parser.stack.pop();
                    if(temp == Tag.SYMBOL) {
                        temp = parser.stack.pop();
                        if(temp == Tag.ID) {
                            temp = parser.stack.pop();
                            if(temp == Tag.ACCESS) {
                                parser.stack.push(Tag.ACCESS_RECORD);
                                return;
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
                        throw new Error("Reduction/Stack error : expected <"+Tag.SYMBOL +"> but found <"+temp+">");
                    }
                }
                else {
                    throw new Error("Error : expected <"+Tag.SYMBOL +" ';'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
                }
            }
            else {
                throw new Error("Error : expected <"+Tag.ID+" 'ident'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
            }
        }
        else if(current.getTag() == Tag.RECORD) {
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
                        if(temp == Tag.SYMBOL) {
                            temp = parser.stack.pop();
                            if(temp == Tag.RECORD) {
                                temp = parser.stack.pop();
                                if(temp == Tag.END) {
                                    temp = parser.stack.pop();
                                    if(temp == Tag.GENERATE_CHAMPS) {
                                        temp = parser.stack.pop();
                                        if(temp == Tag.RECORD) {
                                            parser.stack.push(Tag.ACCESS_RECORD);
                                            return;
                                        }
                                        else {
                                            throw new Error("Reduction/Stack error : expected <"+Tag.RECORD+"> but found <"+temp+">");
                                        }
                                    }
                                    else {
                                        throw new Error("Reduction/Stack error : expected <"+Tag.GENERATE_CHAMPS+"> but found <"+temp+">");
                                    }
                                }
                                else {
                                    throw new Error("Reduction/Stack error : expected <"+Tag.END+"> but found <"+temp+">");
                                }
                            }
                            else {
                                throw new Error("Reduction/Stack error : expected <"+Tag.RECORD+"> but found <"+temp+">");
                            }
                        }
                        else {
                            throw new Error("Reduction/Stack error : expected <"+Tag.SYMBOL +"> but found <"+temp+">");
                        }
                    }
                    else {
                        throw new Error("Error : expected <"+Tag.SYMBOL +" ';'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
                    }
                }
                else {
                    throw new Error("Error : expected <"+Tag.RECORD+" 'record'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
                }
            }
            else {
                throw new Error("Error : expected <"+Tag.END+" 'end'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
            }
        }
        else {
            throw new Error("Error : expected <"+Tag.ACCESS+" 'access'> or <"+Tag.RECORD+" 'record'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
        }
    }

    //DECLARATION_WITH_EXPRESSION
    private void declaration_with_expression() throws IOException{
        //DECLARATION_WITH_EXPRESSION ::= ; (lecture de ;)
        //DECLARATION_WITH_EXPRESSION ::= ( := UNARY EXPRESSION ) ; (lecture de ( )
        if (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(";")) {
            current = parser.lexer.scan();
            parser.stack.push(Tag.DECLARATION_WITH_EXPRESSION);
            return;
        }
        else if (current.getTag() == Tag.SYMBOL && current.getStringValue().equals("(")) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            if (current.getTag() == Tag.ASSIGNMENT && current.getStringValue().equals(":=")) {
                parser.stack.push(current.getTag());
                current = parser.lexer.scan();
                this.unary();
                this.expression();
                if (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(")")) {
                    parser.stack.push(current.getTag());
                    current = parser.lexer.scan();
                    if (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(";")) {
                        parser.stack.push(current.getTag());
                        current = parser.lexer.scan();
                        int temp = parser.stack.pop();
                        if(temp == Tag.SYMBOL) {
                            temp = parser.stack.pop();
                            if(temp == Tag.SYMBOL) {
                                temp = parser.stack.pop();
                                if(temp == Tag.EXPRESSION) {
                                    temp = parser.stack.pop();
                                    if(temp == Tag.UNARY) {
                                        temp = parser.stack.pop();
                                        if(temp == Tag.ASSIGNMENT) {
                                            temp = parser.stack.pop();
                                            if(temp == Tag.SYMBOL) {
                                                parser.stack.push(Tag.DECLARATION_WITH_EXPRESSION);
                                                return;
                                            }
                                            else {
                                                throw new Error("Reduction/Stack error : expected <"+Tag.SYMBOL +"> but found <"+temp+">");
                                            }
                                        }
                                        else {
                                            throw new Error("Reduction/Stack error : expected <"+Tag.SYMBOL +"> but found <"+temp+">");
                                        }
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
                                throw new Error("Reduction/Stack error : expected <"+Tag.SYMBOL +"> but found <"+temp+">");
                            }
                        }
                        else {
                            throw new Error("Reduction/Stack error : expected <"+Tag.SYMBOL +"> but found <"+temp+">");
                        }
                    }
                    else {
                        throw new Error("Error : expected <"+Tag.SYMBOL +" ';'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
                    }
                }
                else {
                    throw new Error("Error : expected <" + Tag.SYMBOL + " ')'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
                }
            }
        }
        else {
            throw new Error("Error : expected <"+Tag.SYMBOL +" ';'> or <"+Tag.SYMBOL +" ':='> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
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
                            return;
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
                throw new Error("Error : expected <"+Tag.SYMBOL +" ';'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
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
                                return;
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
                throw new Error("Error : expected <"+Tag.SYMBOL +" ';'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
            }
        }
        else {
            throw new Error("Error : expected <"+Tag.IS+" 'is'> or <"+Tag.SYMBOL +" '('> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
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
                this.type();
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
                                if(temp == Tag.TYPE) {
                                    temp = parser.stack.pop();
                                    if(temp == Tag.RETURN) {
                                        temp = parser.stack.pop();
                                        if(temp == Tag.PARAMS) {
                                            parser.stack.push(Tag.DECLARATION_FUNCTION);
                                            return;
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
                                    throw new Error("Reduction/Stack error : expected <"+Tag.TYPE+"> but found <"+temp+">");
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
                    throw new Error("Error : expected <"+Tag.SYMBOL +" ';'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
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
                            if(temp == Tag.TYPE) {
                                temp = parser.stack.pop();
                                if(temp == Tag.RETURN) {
                                    parser.stack.push(Tag.DECLARATION_FUNCTION);
                                    return;
                                }
                                else {
                                    throw new Error("Reduction/Stack error : expected <"+Tag.RETURN+"> but found <"+temp+">");
                                }
                            }
                            else {
                                throw new Error("Reduction/Stack error : expected <"+Tag.TYPE+"> but found <"+temp+">");
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
            throw new Error("Error : expected <"+Tag.SYMBOL +" '('> or <"+Tag.RETURN+" 'return'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
        }
    }

    //GENERATE_IDENT
    private void generate_ident() throws IOException{
        //GENERATE_IDENT ::= ident END_GENERATE_IDENT (lecture de ident)
        if(current.getTag() == Tag.ID) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.end_generate_ident();
            int temp = parser.stack.pop();
            if(temp == Tag.END_GENERATE_IDENT) {
                temp = parser.stack.pop();
                if(temp == Tag.ID) {
                    parser.stack.push(Tag.GENERATE_IDENT);
                    return;
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
            throw new Error("Error : expected <"+Tag.ID+" 'ident'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
        }
    }

    //END_GENERATE_IDENT
    private void end_generate_ident() throws IOException{
        //END_GENERATE_IDENT ::= ε (lecture de : )
        //END_GENERATE_IDENT ::= , GENERATE_IDENT (lecture de , )
        if (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(":")) {
            parser.stack.push(Tag.END_GENERATE_IDENT);
            return;
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
                    return;
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
            throw new Error("Error : expected <"+Tag.SYMBOL +" ':'> or <"+Tag.SYMBOL +" ','> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
        }
    }

    //IS_DECLARATION
    private void is_declaration() throws IOException{
        //IS_DECLARATION ::= is IS_DECLARATION_FACTORISATION (lecture de is)
        if (current.getTag() == Tag.IS) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.is_declaration_factorisation();
            int temp = parser.stack.pop();
            if(temp == Tag.IS_DECLARATION_FACTORISATION) {
                temp = parser.stack.pop();
                if(temp == Tag.IS) {
                    parser.stack.push(Tag.IS_DECLARATION);
                    return;
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
            throw new Error("Error : expected <"+Tag.IS+" 'is'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
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
                return;
            }
            else {
                throw new Error("Reduction/Stack error : expected <"+Tag.GENERATE_DECLARATIONS+"> but found <"+temp+">");
            }
        }
        else if(current.getTag() == Tag.BEGIN) {
            parser.stack.push(Tag.IS_DECLARATION_FACTORISATION);
            return;
        }
        else {
            throw new Error("Error : expected <"+Tag.PROCEDURE+" 'procedure'> or <"+Tag.ID+" 'ident'> or <"+Tag.TYPE+" 'type'> or <"+Tag.FUNCTION+" 'function'> or <"+Tag.BEGIN+" 'begin'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
        }
    }

    //CHAMPS
    private void champs() throws IOException{
        // CHAMPS ::= GENERATE_IDENT : TYPE ; (lecture de ident)
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
                                    return;
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
                    throw new Error("Error : expected <"+Tag.SYMBOL +" ';'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
                }

            }
        }
    }

    //GENERATE_CHAMPS
    private void generate_champs() throws IOException{
        // GENERATE_CHAMPS GENERATE_CHAMPS ::= CHAMPS END_GENERATE_CHAMPS (lecture de ident)
        if (current.getTag() == Tag.ID){
            this.champs();
            if (current.getTag() == Tag.END_GENERATE_CHAMPS) {
                int temp = parser.stack.pop();
                if (temp == Tag.END_GENERATE_CHAMPS) {
                    parser.stack.push(Tag.GENERATE_CHAMPS);
                }
                else {
                    throw new Error("Reduction/Stack error : expected <"+Tag.END_GENERATE_CHAMPS+"> but found <"+temp+">");
                }
            }
            else {
                throw new Error("Error : expected <"+Tag.END_GENERATE_CHAMPS+" 'end'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
            }
        }
        return;
    }

    //END_GENERATE_CHAMPS
    private void end_generate_champs() throws IOException{
        // END_GENERATE_CHAMPS ::= GENERATE_CHAMPS (lecture de ident)
        // END_GENERATE_CHAMPS ::= ε (lecture de end)
        if (current.getTag() == Tag.ID){
            this.generate_champs();
            if (current.getTag() == Tag.GENERATE_CHAMPS) {
                int temp = parser.stack.pop();
                if (temp == Tag.GENERATE_CHAMPS) {
                    parser.stack.push(Tag.END_GENERATE_CHAMPS);
                }
                else {
                    throw new Error("Reduction/Stack error : expected <"+Tag.GENERATE_CHAMPS+"> but found <"+temp+">");
                }
            }
            else {
                throw new Error("Error : expected <"+Tag.GENERATE_CHAMPS+" 'ident'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
            }
        }
        else if (current.getTag() == Tag.END){
            parser.stack.push(Tag.END_GENERATE_CHAMPS);
        }
        else {
            throw new Error("Error : expected <"+Tag.ID+" 'ident'> or <"+Tag.END+" 'end'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
        }
    }

    //TYPE
    private void type() throws IOException{
        return;
    }

    //PARAMS
    private void params() throws IOException{
        return;
    }

    //PARAM
    private void param() throws IOException{
        return;
    }

    //TYPE_OR_MODE_TYPE_PARAM
    private void type_or_mode_type_param() throws IOException{
        return;
    }

    //END_PARAM
    private void end_param() throws IOException{
        return;
    }

    //MODE
    private void mode() throws IOException{
        return;
    }

    //OUT_OR_NOT
    private void out_or_not() throws IOException{
        return;
    }

    //EXPRESSION
    private void expression() throws IOException{
        return;
    }

    //EXPRESSION_OR
    private void expression_or() throws IOException{
        return;
    }

    //EXPRESSION_ELSE
    private void expression_else() throws IOException{
        return;
    }

    //EXPRESSION_1
    private void expression_1() throws IOException{
        return;
    }

    //EXPRESSION_AND
    private void expression_and() throws IOException{
        return;
    }

    //EXPRESSION_THEN
    private void expression_then() throws IOException{
        return;
    }

    //EXPRESSION_NOT
    private void expression_not() throws IOException{
        return;
    }

    //EXPRESSION_3
    private void expression_3() throws IOException{
        return;
    }

    //EXPRESSION_EQUALS
    private void expression_equals() throws IOException{
        return;
    }

    //EXPRESSION_4
    private void expression_4() throws IOException{
        return;
    }

    //EXPRESSION_COMPARAISON
    private void expression_comparaison() throws IOException{
        return;
    }

    //EXPRESSION_5
    private void expression_5() throws IOException{
        return;
    }

    //EXPRESSION_PLUS_MOINS
    private void expression_plus_moins() throws IOException{
        return;
    }

    //EXPRESSION_6
    private void expression_6() throws IOException{
        return;
    }

    //EXPRESSION_MULT_DIV
    private void expression_mult_div() throws IOException{
        return;
    }

    //EXPRESSION_7
    private void expression_7() throws IOException{
        return;
    }

    //EXPRESSION_ACCESS_IDENT
    private void expression_access_ident() throws IOException{
        return;
    }

    //EXPRESSION_ATOMS
    private void expression_atoms() throws IOException{
        return;
    }

    //UNARY
    private void unary() throws IOException{
        return;
    }

    //START_NEW_EXPRESSION
    private void start_new_expression() throws IOException{
        return;
    }

    //GENERATE_EXPRESSION
    private void generate_expression() throws IOException{
        return;
    }

    //END_GENERATE_EXPRESSION
    private void end_generate_expression() throws IOException{
        return;
    }

    //INSTRUCTION
    private void instruction() throws IOException{
        return;
    }

    //INSTRUCTION_IDENT_EXPRESSION
    private void instruction_ident_expression() throws IOException{
        return;
    }

    //END_RETURN
    private void end_return() throws IOException{
        return;
    }

    //FOR_INSTRUCTION
    private void for_instruction() throws IOException{
        return;
    }

    //NEXT_IF
    private void next_if() throws IOException{
        return;
    }

    //ELSE
    private void nt_else() throws IOException{
        return;
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
                throw new Error("Error : expected <"+Tag.THEN+" 'then'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
            }
        } else{
            throw new Error("Error : expected <"+Tag.ID+" 'ident'> or <"+Tag.SYMBOL +" '('> or <"+Tag.NEW+" 'new'> or <"+Tag.CHARACTERVAL+" 'character'val'> or <"+Tag.NOT+" 'not'> or <"+Tag.SYMBOL +" '-'> or <"+Tag.NUMCONST+" 'entier'> or <"+Tag.CHAR+" 'caractere'> or <"+Tag.TRUE+" 'true'> or <"+Tag.FALSE+" 'false'> or <"+Tag.NULL+" 'null'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
        }
    }

    //END_ELSIF
    private void end_elsif() throws IOException{
        //END_ELSIF ::= end if ; (lecture de end)
        //END_ELSIF ::= else ELSE (lecture de else)
        //END_ELSIF ::= elsif ELSIF (lecture de elsif)
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
                                return;
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
                    throw new Error("Error : expected <"+Tag.SYMBOL +" ';'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
                }
            }
            else {
                throw new Error("Error : expected <"+Tag.IF+" 'if'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
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
            throw new Error("Error : expected <"+Tag.END+" 'end'> or <"+Tag.ELSE+" 'else'> or <"+Tag.ELSIF+" 'elsif'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
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
        if ((current.getTag() == Tag.OR) || (current.getTag() == Tag.NOT) || (current.getTag() == Tag.NUMCONST) || (current.getTag() == Tag.CHAR) || (current.getTag() == Tag.TRUE) || (current.getTag() == Tag.FALSE) || (current.getTag() == Tag.NULL)) {
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
        } else if (current.getTag() == Tag.NEW) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            if (current.getTag() == Tag.ID) {
                parser.stack.push(current.getTag());
                current = parser.lexer.scan();
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
                throw new Error("Error : expected <" + Tag.ID + " 'ident'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
            }
        } else if (current.getTag() == Tag.CHARACTERVAL) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            if (current.getTag() == Tag.SYMBOL && current.getStringValue().equals("(")) {
                parser.stack.push(current.getTag());
                current = parser.lexer.scan();
                this.unary();
                this.expression();
                if (current.getTag() == Tag.SYMBOL && current.getStringValue().equals(")")) {
                    parser.stack.push(current.getTag());
                    current = parser.lexer.scan();
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
                    throw new Error("Error : expected <" + Tag.SYMBOL + " ')'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
                }
            }
        } else {
            throw new Error("Error : expected <" + Tag.OR + " 'or'> or <" + Tag.NOT + " 'not'> or <" + Tag.NUMCONST + " 'entier'> or <" + Tag.CHAR + " 'caractere'> or <" + Tag.TRUE + " 'true'> or <" + Tag.FALSE + " 'false'> or <" + Tag.NULL + " 'null'> or <" + Tag.NEW + " 'new'> or <" + Tag.CHARACTERVAL + " 'character'val'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
        }
    }

    //WI_EXPRESSION_OR
    private void wi_expression_or() throws IOException{
        //WI_EXPRESSION_OR ::= ε (lecture de :=)
        //WI_EXPRESSION_OR ::= or UNARY WI_EXPRESSION_ELSE (lecture de or)
        if (current.getTag() == Tag.ASSIGNMENT) {
            parser.stack.push(Tag.WI_EXPRESSION_OR);
            return;
        }
        else if (current.getTag() == Tag.OR) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
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
            throw new Error("Error : expected <"+Tag.ASSIGNMENT+" ':='> or <"+Tag.OR+" 'or'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
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
        } else if (current.getTag() == Tag.ELSE) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
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
        } else {
            throw new Error("Error : expected <" + Tag.OR + " 'or'> or <" + Tag.NOT + " 'not'> or <" + Tag.NUMCONST + " 'entier'> or <" + Tag.CHAR + " 'caractere'> or <" + Tag.TRUE + " 'true'> or <" + Tag.FALSE + " 'false'> or <" + Tag.NULL + " 'null'> or <" + Tag.ELSE + " 'else'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
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
        } else {
            throw new Error("Error : expected <" + Tag.NOT + " 'not'> or <" + Tag.NUMCONST + " 'entier'> or <" + Tag.CHAR + " 'caractere'> or <" + Tag.TRUE + " 'true'> or <" + Tag.FALSE + " 'false'> or <" + Tag.NULL + " 'null'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
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
            throw new Error("Error : expected <"+Tag.ASSIGNMENT+" ':='> or <"+Tag.OR+" 'or'> or <"+Tag.AND+" 'and'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
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
        } else if (current.getTag() == Tag.THEN) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
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
        } else {
            throw new Error("Error : expected <" + Tag.THEN + " 'then'> or <" + Tag.NOT + " 'not'> or <" + Tag.NUMCONST + " 'entier'> or <" + Tag.CHAR + " 'caractere'> or <" + Tag.TRUE + " 'true'> or <" + Tag.FALSE + " 'false'> or <" + Tag.NULL + " 'null'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
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
        } else if (current.getTag() == Tag.NOT) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
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
        } else {
            throw new Error("Error : expected <" + Tag.NOT + " 'not'> or <" + Tag.NUMCONST + " 'entier'> or <" + Tag.CHAR + " 'caractere'> or <" + Tag.TRUE + " 'true'> or <" + Tag.FALSE + " 'false'> or <" + Tag.NULL + " 'null'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
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
        } else {
            throw new Error("Error : expected <" + Tag.NUMCONST + " 'entier'> or <" + Tag.CHAR + " 'caractere'> or <" + Tag.TRUE + " 'true'> or <" + Tag.FALSE + " 'false'> or <" + Tag.NULL + " 'null'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
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
        } else if (current.getTag() == Tag.SYMBOL && Objects.equals(current.getStringValue(), "=")) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
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
                        if (temp == Tag.SYMBOL && Objects.equals(current.getStringValue(), "=")) {
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
        } else if (current.getTag() == Tag.DIFFERENT) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
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
        } else {
            throw new Error("Error : expected <" + Tag.ASSIGNMENT + " ':='> or <" + Tag.OR + " 'or'> or <" + Tag.AND + " 'and'> or <" + Tag.SYMBOL + " '='> or <" + Tag.DIFFERENT + " '/='> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
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
        } else {
            throw new Error("Error : expected <" + Tag.NUMCONST + " 'entier'> or <" + Tag.CHAR + " 'caractere'> or <" + Tag.TRUE + " 'true'> or <" + Tag.FALSE + " 'false'> or <" + Tag.NULL + " 'null'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
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
        } else if (current.getTag() == Tag.SYMBOL && Objects.equals(current.getStringValue(), ">")) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
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
                        if (temp == Tag.SYMBOL && Objects.equals(current.getStringValue(), ">")) {
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
        } else if (current.getTag() == Tag.GEQ) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
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
        } else if (current.getTag() == Tag.SYMBOL && Objects.equals(current.getStringValue(), "<")) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
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
                        if (temp == Tag.SYMBOL && Objects.equals(current.getStringValue(), "<")) {
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
        } else if (current.getTag() == Tag.LEQ) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
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
        } else {
            throw new Error("Error : expected <" + Tag.ASSIGNMENT + " ':='> or <" + Tag.OR + " 'or'> or <" + Tag.AND + " 'and'> or <" + Tag.SYMBOL + " '='> or <" + Tag.DIFFERENT + " '/='> or <" + Tag.SYMBOL + " '>'> or <" + Tag.GEQ + " '>='> or <" + Tag.SYMBOL + " '<'> or <" + Tag.LEQ + " '<='> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
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
        } else {
            throw new Error("Error : expected <" + Tag.NUMCONST + " 'entier'> or <" + Tag.CHAR + " 'caractere'> or <" + Tag.TRUE + " 'true'> or <" + Tag.FALSE + " 'false'> or <" + Tag.NULL + " 'null'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
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
        } else if (current.getTag() == Tag.SYMBOL && Objects.equals(current.getStringValue(), "+")) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
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
                        if (temp == Tag.SYMBOL && Objects.equals(current.getStringValue(), "+")) {
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
        } else if (current.getTag() == Tag.SYMBOL && Objects.equals(current.getStringValue(), "-")) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
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
                        if (temp == Tag.SYMBOL && Objects.equals(current.getStringValue(), "-")) {
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
        } else {
            throw new Error("Error : expected <" + Tag.ASSIGNMENT + " ':='> or <" + Tag.OR + " 'or'> or <" + Tag.AND + " 'and'> or <" + Tag.SYMBOL + " '='> or <" + Tag.DIFFERENT + " '/='> or <" + Tag.SYMBOL + " '>'> or <" + Tag.GEQ + " '>='> or <" + Tag.SYMBOL + " '<'> or <" + Tag.LEQ + " '<='> or <" + Tag.SYMBOL + " '+'> or <" + Tag.SYMBOL + " '-'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
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
        } else {
            throw new Error("Error : expected <" + Tag.NUMCONST + " 'entier'> or <" + Tag.CHAR + " 'caractere'> or <" + Tag.TRUE + " 'true'> or <" + Tag.FALSE + " 'false'> or <" + Tag.NULL + " 'null'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
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
        } else if (current.getTag() == Tag.SYMBOL && Objects.equals(current.getStringValue(), "*")) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
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
                        if (temp == Tag.SYMBOL && Objects.equals(current.getStringValue(), "*")) {
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
        } else if (current.getTag() == Tag.SYMBOL && Objects.equals(current.getStringValue(), "/")) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
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
                        if (temp == Tag.SYMBOL && Objects.equals(current.getStringValue(), "/")) {
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
        } else if (current.getTag() == Tag.REM) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
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
        } else {
            throw new Error("Error : expected <" + Tag.NUMCONST + " 'entier'> or <" + Tag.CHAR + " 'caractere'> or <" + Tag.TRUE + " 'true'> or <" + Tag.FALSE + " 'false'> or <" + Tag.NULL + " 'null'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
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
            this.unary();
            this.generate_expression();
            int temp = parser.stack.pop();
            if (temp == Tag.GENERATE_EXPRESSION) {
                temp = parser.stack.pop();
                if (temp == Tag.UNARY) {
                    temp = parser.stack.pop();
                    if (temp == Tag.SYMBOL && Objects.equals(current.getStringValue(), "(")) {
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
        } else if (current.getTag() == Tag.NUMCONST) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.wi_expression_atoms();
            int temp = parser.stack.pop();
            if (temp == Tag.WI_EXPRESSION_ATOMS) {
                temp = parser.stack.pop();
                if (temp == Tag.NUMCONST) {
                    parser.stack.push(Tag.WI_EXPRESSION_ATOMS);
                } else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.NUMCONST + "> but found <" + temp + ">");
                }
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.WI_EXPRESSION_ATOMS + "> but found <" + temp + ">");
            }
        } else if (current.getTag() == Tag.CHAR) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.wi_expression_atoms();
            int temp = parser.stack.pop();
            if (temp == Tag.WI_EXPRESSION_ATOMS) {
                temp = parser.stack.pop();
                if (temp == Tag.CHAR) {
                    parser.stack.push(Tag.WI_EXPRESSION_ATOMS);
                } else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.CHAR + "> but found <" + temp + ">");
                }
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.WI_EXPRESSION_ATOMS + "> but found <" + temp + ">");
            }
        } else if (current.getTag() == Tag.TRUE) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.wi_expression_atoms();
            int temp = parser.stack.pop();
            if (temp == Tag.WI_EXPRESSION_ATOMS) {
                temp = parser.stack.pop();
                if (temp == Tag.TRUE) {
                    parser.stack.push(Tag.WI_EXPRESSION_ATOMS);
                } else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.TRUE + "> but found <" + temp + ">");
                }
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.WI_EXPRESSION_ATOMS + "> but found <" + temp + ">");
            }
        } else if (current.getTag() == Tag.FALSE) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.wi_expression_atoms();
            int temp = parser.stack.pop();
            if (temp == Tag.WI_EXPRESSION_ATOMS) {
                temp = parser.stack.pop();
                if (temp == Tag.FALSE) {
                    parser.stack.push(Tag.WI_EXPRESSION_ATOMS);
                } else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.FALSE + "> but found <" + temp + ">");
                }
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.WI_EXPRESSION_ATOMS + "> but found <" + temp + ">");
            }
        } else if (current.getTag() == Tag.NULL) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            this.wi_expression_atoms();
            int temp = parser.stack.pop();
            if (temp == Tag.WI_EXPRESSION_ATOMS) {
                temp = parser.stack.pop();
                if (temp == Tag.NULL) {
                    parser.stack.push(Tag.WI_EXPRESSION_ATOMS);
                } else {
                    throw new Error("Reduction/Stack error : expected <" + Tag.NULL + "> but found <" + temp + ">");
                }
            } else {
                throw new Error("Reduction/Stack error : expected <" + Tag.WI_EXPRESSION_ATOMS + "> but found <" + temp + ">");
            }
        } else {
            throw new Error("Error : expected <" + Tag.SYMBOL + " '('> or <" + Tag.NUMCONST + " 'entier'> or <" + Tag.CHAR + " 'caractere'> or <" + Tag.TRUE + " 'true'> or <" + Tag.FALSE + " 'false'> or <" + Tag.NULL + " 'null'> but found <" + current.getTag() + " '" + current.getStringValue() + "'>");
        }
    }
}
