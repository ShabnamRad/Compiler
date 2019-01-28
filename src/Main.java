import CodeGeneration.CodeGen;
import Parser.Grammar;
import Parser.SyntaxAnalyzer;
import Scanner.LexicalAnalyzer;

import java.io.File;

/**
 * Created by Shabnam on 1/21/2019.
 */
public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("No file to compile");
            return;
        }

        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(new File(args[0]));
        new SyntaxAnalyzer(lexicalAnalyzer);
        for (int i = 0; i < 25; i++) {
            System.out.print(i + "\t");
            System.out.println(CodeGen.ProgramBlock[i]);
        }
    }
}
