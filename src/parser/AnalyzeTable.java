package parser;

import lexer.Lexer;
import lexer.Tag;
import lexer.Token;

import java.io.IOException;

public class AnalyzeTable {

    public Parser parser;
    public Token current;
    
    public AnalyzeTable(Parser parser) throws IOException {
        this.parser = parser;
    }

    public void analyze() throws IOException {
        current = parser.lexer.scan();
        this.ficher();
    }

    private void ficher() throws IOException {
        //FICHIER ::= with Ada . Text_IO ; use Ada . Text_IO ; PROCEDURE BEGIN_INSTRUCTION ; EOF
        if (current.getTag() == Tag.WITH) {
            parser.stack.push(current.getTag());
            current = parser.lexer.scan();
            if ((current.getTag() == Tag.ID) && (current.getStringValue().equals("ada"))) {
                parser.stack.push(current.getTag());
                current = parser.lexer.scan();
                if ((current.getTag() == Tag.CHARCONST) && (current.getStringValue().equals("."))){
                    parser.stack.push(current.getTag());
                    current = parser.lexer.scan();
                    if ((current.getTag() == Tag.ID) && (current.getStringValue().equals("text_io"))) {
                        parser.stack.push(current.getTag());
                        current = parser.lexer.scan();
                        if((current.getTag() == Tag.CHARCONST) && (current.getStringValue().equals(";"))) {
                            parser.stack.push(current.getTag());
                            current = parser.lexer.scan();
                            if(current.getTag() == Tag.USE) {
                                parser.stack.push(current.getTag());
                                current = parser.lexer.scan();
                                if ((current.getTag() == Tag.ID) && (current.getStringValue().equals("ada"))) {
                                    parser.stack.push(current.getTag());
                                    current = parser.lexer.scan();
                                    if ((current.getTag() == Tag.CHARCONST) && (current.getStringValue().equals("."))){
                                        parser.stack.push(current.getTag());
                                        current = parser.lexer.scan();
                                        if ((current.getTag() == Tag.ID) && (current.getStringValue().equals("text_io"))) {
                                            parser.stack.push(current.getTag());
                                            current = parser.lexer.scan();
                                            if((current.getTag() == Tag.CHARCONST) && (current.getStringValue().equals(";"))) {
                                                parser.stack.push(current.getTag());
                                                current = parser.lexer.scan();
                                                this.procedure();
                                                this.begin_instruction();
                                                if((current.getTag() == Tag.CHARCONST) && (current.getStringValue().equals(";"))) {
                                                    parser.stack.push(current.getTag());
                                                    current = parser.lexer.scan();
                                                    if(current.getTag() == Tag.EOF) {
                                                        parser.stack.push(current.getTag());
                                                        current = parser.lexer.scan();
                                                        // verify stack and replace with FICHIER
                                                        int temp = parser.stack.pop();
                                                        if (temp == Tag.EOF) {
                                                            temp = parser.stack.pop();
                                                            if(temp == Tag.CHARCONST){
                                                                temp = parser.stack.pop();
                                                                if(temp == Tag.BEGIN_INSTRUCTION) {
                                                                    temp = parser.stack.pop();
                                                                    if(temp == Tag.NT_PROCEDURE) {
                                                                        temp = parser.stack.pop();
                                                                        if(temp == Tag.CHARCONST) {
                                                                            temp = parser.stack.pop();
                                                                            if(temp == Tag.ID) {
                                                                                temp = parser.stack.pop();
                                                                                if(temp == Tag.CHARCONST) {
                                                                                    temp = parser.stack.pop();
                                                                                    if(temp == Tag.ID) {
                                                                                        temp = parser.stack.pop();
                                                                                        if(temp == Tag.USE) {
                                                                                            temp = parser.stack.pop();
                                                                                            if(temp == Tag.CHARCONST) {
                                                                                                temp = parser.stack.pop();
                                                                                                if(temp == Tag.ID) {
                                                                                                    temp = parser.stack.pop();
                                                                                                    if(temp == Tag.CHARCONST) {
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
                                                                                                            throw new Error("Reduction/Stack error : expected <"+Tag.CHARCONST+"> but found <"+temp+">");
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
                                                                                                throw new Error("Reduction/Stack error : expected <"+Tag.CHARCONST+"> but found <"+temp+">");
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
                                                                                    throw new Error("Reduction/Stack error : expected <"+Tag.CHARCONST+"> but found <"+temp+">");
                                                                                }
                                                                            }
                                                                            else {
                                                                                throw new Error("Reduction/Stack error : expected <"+Tag.ID+"> but found <"+temp+">");
                                                                            }
                                                                        }
                                                                        else {
                                                                            throw new Error("Reduction/Stack error : expected <"+Tag.CHARCONST+"> but found <"+temp+">");
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
                                                                throw new Error("Reduction/Stack error : expected <"+Tag.CHARCONST+"> but found <"+temp+">");
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
                                                    throw new Error("Error : expected <"+Tag.CHARCONST+" ';'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");

                                                }
                                            }
                                            else {
                                                throw new Error("Error : expected <"+Tag.CHARCONST+" ';'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
                                            }
                                        }
                                        else {
                                            throw new Error("Error : expected <"+Tag.ID+" 'text_io'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
                                        }
                                    }
                                    else {
                                        throw new Error("Error : expected <"+Tag.CHARCONST+" '.'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
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
                            throw new Error("Error : expected <"+Tag.CHARCONST+" ';'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
                        }
                    }
                    else {
                        throw new Error("Error : expected <"+Tag.ID+" 'text_io'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
                    }
                }
                else {
                    throw new Error("Error : expected <"+Tag.CHARCONST+" '.'> but found <"+current.getTag()+" '"+current.getStringValue()+"'>");
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
        //PROCEDURE ::= procedure ident is END_PROCEDURE
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
        return;
    }

    //GENERATE_DECLARATIONS
    private void generate_declarations() throws IOException{
        return;
    }

    //GENERATE_DECLARATIONS_FACTORISATION
    private void generate_declarations_factorisation() throws IOException{
        return;
    }

    //GENERATE_INSTRUCTIONS
    private void generate_instructions() throws IOException{
        return;
    }

    //GENERATE_INSTRUCTIONS_FACTORISATION
    private void generate_instructions_factorisation() throws IOException{
        return;
    }

    //DECLARATION
    private void declaration() throws IOException{
        return;
    }

    //DECLARATION_TYPE
    private void declaration_type() throws IOException{
        return;
    }

    //ACCESS_RECORD
    private void access_record() throws IOException{
        return;
    }

    //DECLARATION_WITH_EXPRESSION
    private void declaration_with_expression() throws IOException{
        return;
    }

    //DECLARATION_PROCEDURE
    private void declaration_procedure() throws IOException{
        return;
    }

    //DECLARATION_FUNCTION
    private void declaration_function() throws IOException{
        return;
    }

    //GENERATE_IDENT
    private void generate_ident() throws IOException{
        return;
    }

    //END_GENERATE_IDENT
    private void end_generate_ident() throws IOException{
        return;
    }

    //IS_DECLARATION
    private void is_declaration() throws IOException{
        return;
    }

    //IS_DECLARATION_FACTORISATION
    private void is_declaration_factorisation() throws IOException{
        return;
    }

    //CHAMPS
    private void champs() throws IOException{
        return;
    }

    //GENERATE_CHAMPS
    private void generate_champs() throws IOException{
        return;
    }

    //END_GENERATE_CHAMPS
    private void end_generate_champs() throws IOException{
        return;
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
