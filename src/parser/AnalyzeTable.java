package parser;

import lexer.Tag;
import lexer.Token;

import java.io.IOException;

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
        return;
    }

    //END_ELSIF
    private void end_elsif() throws IOException{
        return;
    }

    //WI_EXPRESSION
    private void wi_expression() throws IOException{
        return;
    }

    //WI_EXPRESSION_OR
    private void wi_expression_or() throws IOException{
        return;
    }

    //WI_EXPRESSION_ELSE
    private void wi_expression_else() throws IOException{
        return;
    }

    //WI_EXPRESSION_1
    private void wi_expression_1() throws IOException{
        return;
    }

    //WI_EXPRESSION_AND
    private void wi_expression_and() throws IOException{
        return;
    }

    //WI_EXPRESSION_THEN
    private void wi_expression_then() throws IOException{
        return;
    }

    //WI_EXPRESSION_NOT
    private void wi_expression_not() throws IOException{
        return;
    }

    //WI_EXPRESSION_3
    private void wi_expression_3() throws IOException{
        return;
    }

    //WI_EXPRESSION_EQUALS
    private void wi_expression_equals() throws IOException{
        return;
    }

    //WI_EXPRESSION_4
    private void wi_expression_4() throws IOException{
        return;
    }

    //WI_EXPRESSION_COMPARAISON
    private void wi_expression_comparaison() throws IOException{
        return;
    }

    //WI_EXPRESSION_5
    private void wi_expression_5() throws IOException{
        return;
    }

    //WI_EXPRESSION_PLUS_MOINS
    private void wi_expression_plus_moins() throws IOException{
        return;
    }

    //WI_EXPRESSION_6
    private void wi_expression_6() throws IOException{
        return;
    }

    //WI_EXPRESSION_MUL_DIV
    private void wi_expression_mul_div() throws IOException{
        return;
    }

    //WI_EXPRESSION_ACCES_IDENT
    private void wi_expression_acces_ident() throws IOException{
        return;
    }

    //WI_EXPRESSION_ATOMS
    private void wi_expression_atoms() throws IOException{
        return;
    }
}
