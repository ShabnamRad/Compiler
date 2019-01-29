package SymbolTable;

import Scanner.Token;

import java.util.ArrayList;

/**
 * Created by Parham on 1/24/2019.
 */
public final class SymbolTable {

    private static ArrayList<Token> sym_tab = new ArrayList<>();
    public static int scope = 0;
    public static void add(Token t) { sym_tab.add(t); }
    public static Token get(String s) {
        for (Token t : sym_tab) {
            if (t.match(s) && t.getScope() == scope) return t;
        }
        throw new Error(String.valueOf(scope));
    }
    public static void show() {
        System.out.println();
        for (Token t :
                sym_tab) {
            System.out.println(t.getLexeme() + " " + t.getTokenName() + " " + t.getType() + " " + t.getScope());
        }
        System.out.println(sym_tab.size());
        System.out.println();
    }
}

