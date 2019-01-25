package Scanner;

import SymbolTable.SymbolTable;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;

/**
 * Created by Shabnam on 1/21/2019.
 */
public class LexicalAnalyzer {

    private java.util.Scanner sc;
    private String cline;
    private int index, lnum;
    private HashSet<Character> SpecialChars;

    public LexicalAnalyzer(File f) {
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
        index ++;
        if (cline == null || index > cline.length()) {
            if (sc.hasNextLine()) {
                cline = sc.nextLine() + ' ';
                index = 1;
                lnum ++;
            }
            else {
                System.out.println("end of input");
                return (char) -1;
            }
        }

        return cline.charAt(index - 1);
    }

    private void returnLastChar() {index --;}

    private int prev = -1;
    private char prevc = ' ';
    @SuppressWarnings("Duplicates")
    public Token getToken() {
        StringBuilder raw = new StringBuilder();
        boolean found = false;
        int current = -1; //-1: unknown/eof, 0: string, 1: number, 2: mixed, 3: special char, 4: relop, 5: comment
        boolean comment = false;
        int comment_state = 0;
        do {
            char c = getChar();
            if (c == (char) -1) {
                raw.append(c);
                found = true;
            }
            if (comment) {
                if (comment_state == 0) {
                    if (c == '*')
                        comment_state = 1;
                } else {
                    if (c == '/') {
                        comment = false;
                        current = -1;
                    }
                    comment_state = 0;
                }
            } else if (current == -1) {
                if (Character.isAlphabetic(c)) {
                    current = 0;
                    raw.append(c);
                } else if (Character.isDigit(c)) {
                    current = 1;
                    raw.append(c);
                } else if (c == '<' || c == '=') {
                    current = 4;
                    raw.append(c);
                } else if (c == '/') {
                    current = 5;
                } else if (SpecialChars.contains(c)) {
                    current = 3;
                    raw.append(c);
                    found = true;
                } else if (c == '-' || c == '+') {
                    if (prev == 0 || prev == 1 || prev == 2 || prevc == ')' || prevc == ']') {
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
                } else if (Character.isDigit(c)) {
                    current = 2;
                    raw.append(c);
                } else if (c == '<' || SpecialChars.contains(c)) {
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
                } else if (Character.isAlphabetic(c)) {
                    current = -1;
                    raw.delete(0, raw.length());
                    returnLastChar();
                    System.out.println("wrong input, identifier starts with number at input location line: " + lnum + " index:" + index);
                } else if (c == '<' || SpecialChars.contains(c)) {
                    returnLastChar();
                    found = true;
                } else if (Character.isWhitespace(c)) {
                    found = true;
                } else {
                    System.out.println("ignoring unrecognized char at input location line: " + lnum + " index:" + index);
                }
            } else if (current == 2) {
                if (Character.isAlphabetic(c) || Character.isDigit(c)) {
                    raw.append(c);
                } else if (c == '<' || SpecialChars.contains(c)) {
                    returnLastChar();
                    found = true;
                } else if (Character.isWhitespace(c)) {
                    found = true;
                } else {
                    System.out.println("ignoring unrecognized char at input location line: " + lnum + " index:" + index);
                }
            } else if (current == 4) {
                if (prevc == '=' && c == '=') {
                    raw.append(c);
                    found = true;
                } else if (prevc == '<') {
                    returnLastChar();
                    found = true;
                } else {
                    current = 3;
                    returnLastChar();
                    found = true;
                }
            } else {
                if (c == '*') {
                    comment = true;
                } else {
                    current = -1;
                    returnLastChar();
                    System.out.println("ignoring unrecognized char at input location line: " + lnum + " index:" + index);
                }
            }
        prevc = c;
        } while (!found);

        System.out.println(raw.toString());

        prev = current;
        return Tokenize(raw.toString(), current);
    }

    private Token Tokenize(String inp, int c) {
        try {
            return new Token(inp);
        } catch (Error e1) {
            try {
                return SymbolTable.get(inp);
            } catch (Error e2) {
                String name = c == 1? "NUM":"ID";
                String type = c == 1? "INT":"unknown";
                Token t = new Token(inp, name, type);
                System.out.println("\tnew identifier");
                SymbolTable.add(t);
                return t;
            }
        }
    }

    // some testing
    public static void main(String[] args) {
        LexicalAnalyzer a = new LexicalAnalyzer(new File("C:\\Users\\Shabnam\\Documents\\University\\Term 7\\Compiler\\Project\\test.c"));
        for (int i = 0; i < 600; i++) {
            a.getToken();
        }
    }

}
