import asm.CodeGenerator;
import asm.visual.Launcher;
import ast.SemanticAnalyzer;
import ast.SemanticException;
import lexer.Lexer;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import parser.Parser;

public class CanAda {
    @Option(name = "-h", aliases = "--help", usage = "Print this message")
    public boolean help;

    @Option(name = "-p", aliases = "--parse", usage = "Parse the input file")
    public boolean parse;

    @Option(name = "-a", aliases = "--ast", usage = "Generate the .png AST")
    public boolean ast;

    @Option(name = "-s", aliases ="--semantic", usage = "Run the semantic analysis")
    public boolean semantic;

    @Option(name = "-c", aliases = "--compile", usage = "Compile and run the input file")
    public boolean compile;

    @Argument(required = true, usage = "Input file", metaVar = "INPUT")
    public String input;

    public static void main(String[] args) throws Exception {
        System.out.println("╭──────────────────────────────╮");
        System.out.println("│ If you can do it, you \033[1mCanAda\033[0m │");
        System.out.println("╰──────────────────────────────╯\n");
        new CanAda().doMain(args);
    }

    public void doMain(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);
        if (help) {
            parser.printUsage(System.out);
            System.exit(0);
        }
        try {
            parser.parseArgument(args);
            if (parse || ast || semantic || compile) {
                // print current folder
                Lexer l = new Lexer(input);
                Parser p = new Parser(l);
                p.parse(ast);
                System.out.println("Parsing completed \033[32msuccessfully\033[0m");
                if (ast) {
                    System.out.println("  ├AST (.dot) generated at \033[4m" + l.getFileName() + "-ast.dot\033[0m");
                    System.out.println("  └AST (.svg) generated at \033[4m" + l.getFileName() + "-ast.svg\033[0m\n");
                } else {
                    System.out.println("  └AST (.dot) generated at \033[4m" + l.getFileName() + "-ast.dot\033[0m\n");
                }
                if (semantic) {
                    SemanticAnalyzer sa = new SemanticAnalyzer(p.getAst());
                    sa.analyze();
                    System.out.println("Semantic analysis completed \033[32msuccessfully\033[0m\n");
                }
                if (compile) {
                    SemanticAnalyzer sa = new SemanticAnalyzer(p.getAst());
                    CodeGenerator codeGenerator = new CodeGenerator(p.getAst().getFilename(), true, sa.getTds(), sa.getStack());
                    sa.setCodeGen(codeGenerator);
                    sa.analyze();
                    System.out.println("Semantic analysis completed \033[32msuccessfully\033[0m\n");
                    System.out.println("ASM code generated \033[32msuccessfully\033[0m");
                    System.out.println("  └ARM code (.s) generated at \033[4m" + l.getFileName() + "-output.s\033[0m\n");
                    Launcher.run(p.getAst().getFilename()+ "-output.s");
                }
            }
        } catch (org.kohsuke.args4j.CmdLineException e) {
            System.err.println(e.getMessage());
            parser.printUsage(System.err);
            System.exit(1);
        } catch (SemanticException e) {
            System.err.println("\nA \033[31msemantic error\033[0m occurred:");
            System.err.println(e.getMessage());
            System.exit(1);
        } catch (Exception | Error e) {
            System.err.println("\nAn \033[31merror occurred\033[0m:");
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
