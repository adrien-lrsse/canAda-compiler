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

    @Argument(required = true, usage = "Input file")
    public String input;

    public static void main(String[] args) throws Exception {
        System.out.println("--------------------------------");
        System.out.println("- If you can do it, you \033[1mCanAda\033[0m -");
        System.out.println("--------------------------------\n");
        new CanAda().doMain(args);
    }

    public void doMain(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
            if (help) {
                parser.printUsage(System.out);
                System.exit(0);
            }
            if (parse) {
                // print current folder
                Lexer l = new Lexer(input);
                Parser p = new Parser(l);
                p.parse(ast);
                System.out.println("Parsing completed \033[32msuccessfully\033[0m");
                System.out.println("AST (.dot) generated at \033[4m" + l.getFileName() + "-ast.dot\033[0m");
                if (ast) {
                    System.out.println("AST (.png) generated at \033[4m" + l.getFileName() + "-ast.svg\033[0m");
                }
            }
        } catch (org.kohsuke.args4j.CmdLineException e) {
            System.err.println(e.getMessage());
            parser.printUsage(System.err);
            System.exit(1);
        } catch (Exception e) {
            System.err.println("An \033[31merror occurred\033[0m:");
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
