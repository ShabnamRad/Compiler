package SymbolTable;

import Scanner.Token;

import java.util.ArrayList;

/**
 * Created by Parham on 1/24/2019.
 */
public final class SymbolTable {

    private static ArrayList<Token> sym_tab = new ArrayList<>();
    public static void add(Token t) { sym_tab.add(t); }
    public static Token get(String s) {
        for (Token t : sym_tab) {
            if (t.match(s)) return t;
        }
        throw new Error("Token not found");
    }
    public static void show() {
        for (Token t :
                sym_tab) {
            System.out.println(t.toString());
        }
        System.out.println(sym_tab.size());
    }
}

