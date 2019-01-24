import Scanner.Analyzer;

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

        Analyzer lexicalAnalyzer = new Analyzer(new File(args[0]));
    }
}
