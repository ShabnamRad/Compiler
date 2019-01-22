package Scanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;

/**
 * Created by Shabnam on 1/21/2019
 */
public class Analyzer {

    private java.util.Scanner sc;
    private String cline;
    private int index, lnum;
    private HashSet<Character> SpecialChars;

    public Analyzer(File f) {
        try {
            sc = new Scanner(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("\n\t\tScanner error - file not found");
        }
        cline = null;
        index = 0;
        lnum = 0;
        SpecialChars = new HashSet<>();
        SpecialChars.add(';');
        SpecialChars.add(':');
        SpecialChars.add(',');
        SpecialChars.add('*');
        SpecialChars.add('=');
        SpecialChars.add('(');
        SpecialChars.add(')');
        SpecialChars.add('[');
        SpecialChars.add(']');
        SpecialChars.add('{');
        SpecialChars.add('}');
    }

    private char getChar() {
        if (cline == null || index > cline.length()) {
            if (sc.hasNextLine()) {
                cline = sc.nextLine() + ' ';
                index = 0;
                lnum ++;
            }
            else {
                System.out.println("end of input");
                return ' ';
            }
        }
        return cline.charAt(index);
    }

    private void returnLastChar() {index --;}

    private int prev = -1;
    public Tokenizer getToken() {
        StringBuilder raw = new StringBuilder();
        boolean found = false;
        int current = -1; //-1: unknown, 0: string, 1: number, 2: mixed, 3: special char
        do {
            char c = getChar();
            if (current == -1) {
                if (Character.isAlphabetic(c)) {
                    current = 0;
                    raw.append(c);
                } else if (Character.isDigit(c)) {
                    current = 1;
                    raw.append(c);
                } else if (SpecialChars.contains(c)) {
                    current = 3;
                    raw.append(c);
                    found = true;
                } else if (c == '-' || c == '+') {
                    if (prev == 1 || prev == 2) {
                        raw.append(c);
                        found = true;
                    } else {
                        current = 1;
                        raw.append(c);
                    }
                } else if (!Character.isWhitespace(c)){
                    System.out.println("ignoring unrecognized char at input location line: " + lnum + " index:" + index);
                }
            } else if (current == 0) {
                if (Character.isAlphabetic(c)) {
                    raw.append(c);
                } else if ( Character.isDigit(c)){
                    current = 2;
                    raw.append(c);
                } else if (SpecialChars.contains(c)) {
                    returnLastChar();
                    found = true;
                } else if (Character.isWhitespace(c)) {
                    found = true;
                } else {
                    System.out.println("ignoring unrecognized char at input location line: " + lnum + " index:" + index);
                }
            } else if (current == 1) {
                if (Character.isDigit(c)) {
                    raw.append(c);
                } else if (SpecialChars.contains(c) || Character.isAlphabetic(c)) {
                    returnLastChar();
                    found = true;
                } else if (Character.isWhitespace(c)) {
                    found = true;
                } else {
                    System.out.println("ignoring unrecognized char at input location line: " + lnum + " index:" + index);
                }
            } else {
                if (Character.isAlphabetic(c) || Character.isDigit(c)) {
                    raw.append(c);
                } else if (SpecialChars.contains(c)) {
                    returnLastChar();
                    found = true;
                } else if (Character.isWhitespace(c)) {
                    found = true;
                } else {
                    System.out.println("ignoring unrecognized char at input location line: " + lnum + " index:" + index);
                }
            }
        } while (!found);

        prev = current;
        return new Tokenizer();
    }

    // some testing
    public static void main(String[] args) {
        //Analyzer a = new Analyzer()
        Scanner sc = null;
        try {
            sc = new Scanner(new File("C:\\Users\\parha\\Desktop\\shared\\to push\\singlesample\\cs_sim_strainSolver.py"));
            System.out.println(sc.nextLine());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
