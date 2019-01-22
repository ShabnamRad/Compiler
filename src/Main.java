import Scanner.LexicalAnalyzer;
import Scanner.Token;

/**
 * Created by Shabnam on 1/21/2019
 */
public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("No file to compile");
            return;
        }

        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(args[0]);
        Token token;
        while((token = lexicalAnalyzer.getNextToken()) != null) {
            System.out.println("tokenID: " + token.getTokenID() + " lexeme: " + token.getLexeme());
        }
    }
}
