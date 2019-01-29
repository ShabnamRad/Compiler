import CodeGeneration.CodeGen;
import Parser.Grammar;
import Parser.SyntaxAnalyzer;
import Scanner.LexicalAnalyzer;
import SymbolTable.SymbolTable;

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
        System.out.print((char)27 + "[30m");
        int i = 0;
        String[] PB = CodeGen.ProgramBlock;
        while(i < PB.length && PB[i] != null) {
            System.out.print(i + "\t");
            System.out.println(CodeGen.ProgramBlock[i]);
            i++;
        }

        System.out.print((char)27 + "[35m");
        SymbolTable.show();
    }
}
