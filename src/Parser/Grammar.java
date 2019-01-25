package Parser;

import Scanner.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by Shabnam on 1/25/2019.
 */
public class Grammar {
    private static final Grammar instance = new Grammar();
    private final ArrayList<Production> productions = new ArrayList<>();
    private final Token[] nonTerminals = new Token[40];
    private Map<Token, HashSet<Token>> firsts = new HashMap<>();

    private Grammar() {
        nonTerminals[0] = new Token(null, "program", "NONTERMINAL");
        nonTerminals[1] = new Token(null, "declaration-list", "NONTERMINAL");
        nonTerminals[2] = new Token(null, "declaration", "NONTERMINAL");
        nonTerminals[3] = new Token(null, "var-declaration", "NONTERMINAL");
        nonTerminals[4] = new Token(null, "fun-declaration", "NONTERMINAL");
        nonTerminals[5] = new Token(null, "type-specifier", "NONTERMINAL");
        nonTerminals[6] = new Token(null, "params", "NONTERMINAL");
        nonTerminals[7] = new Token(null, "compound-stmt", "NONTERMINAL");
        nonTerminals[8] = new Token(null, "param-list", "NONTERMINAL");
        nonTerminals[9] = new Token(null, "param", "NONTERMINAL");
        nonTerminals[10] = new Token(null, "statement-list", "NONTERMINAL");
        nonTerminals[11] = new Token(null, "statement", "NONTERMINAL");
        nonTerminals[12] = new Token(null, "expression-stmt", "NONTERMINAL");
        nonTerminals[13] = new Token(null, "selection-stmt", "NONTERMINAL");
        nonTerminals[14] = new Token(null, "iteration-stmt", "NONTERMINAL");
        nonTerminals[15] = new Token(null, "return-stmt", "NONTERMINAL");
        nonTerminals[16] = new Token(null, "switch-stmt", "NONTERMINAL");
        nonTerminals[17] = new Token(null, "expression", "NONTERMINAL");
        nonTerminals[18] = new Token(null, "case-stmts", "NONTERMINAL");
        nonTerminals[19] = new Token(null, "case-stmt", "NONTERMINAL");
        nonTerminals[20] = new Token(null, "default-stmt", "NONTERMINAL");
        nonTerminals[21] = new Token(null, "var", "NONTERMINAL");
        nonTerminals[22] = new Token(null, "simple-expression", "NONTERMINAL");
        nonTerminals[23] = new Token(null, "additive-expression", "NONTERMINAL");
        nonTerminals[24] = new Token(null, "relop", "NONTERMINAL");
        nonTerminals[25] = new Token(null, "addop", "NONTERMINAL");
        nonTerminals[26] = new Token(null, "term", "NONTERMINAL");
        nonTerminals[27] = new Token(null, "factor", "NONTERMINAL");
        nonTerminals[28] = new Token(null, "call", "NONTERMINAL");
        nonTerminals[29] = new Token(null, "args", "NONTERMINAL");
        nonTerminals[30] = new Token(null, "arg-list", "NONTERMINAL");
        nonTerminals[31] = new Token(null, "num", "NONTERMINAL");
        nonTerminals[32] = new Token(null, "param-rest", "NONTERMINAL");
        nonTerminals[33] = new Token(null, "brackets", "NONTERMINAL");
        nonTerminals[34] = new Token(null, "expr-gen", "NONTERMINAL");
        nonTerminals[35] = new Token(null, "index", "NONTERMINAL");
        nonTerminals[36] = new Token(null, "add-expr-gen", "NONTERMINAL");
        nonTerminals[37] = new Token(null, "next-term", "NONTERMINAL");
        nonTerminals[38] = new Token(null, "next-factor", "NONTERMINAL");
        nonTerminals[39] = new Token(null, "arg-rest", "NONTERMINAL");
        productions.add(new Production(nonTerminals[0], new Token[]{nonTerminals[1], new Token("\uFFFF")}));
        productions.add(new Production(nonTerminals[1], new Token[]{nonTerminals[2], nonTerminals[1]}));
        productions.add(new Production(nonTerminals[1], new Token[]{new Token("\\eps")}));

    }

    static Grammar getInstance() {
        return instance;
    }

    private HashSet<Token> first(Token token) {
       if(firsts.containsKey(token))
           return firsts.get(token);
       HashSet<Token> res = new HashSet<>();
       if(!token.getType().equals("NONTERMINAL")) {
           res.add(token);
           firsts.put(token, res);
           return res;
       }
       ArrayList<Production> tokenProductions = new ArrayList<>();
       for(Production production: productions) {
           if(production.nonTerminal.getTokenName().equals(token.getTokenName()))
               tokenProductions.add(production);
       }
       for(Production production :tokenProductions) {
           if(production.definitions.length == 1 && production.definitions[0].getTokenName().equals("epsilon"))
               res.add(new Token("\\eps"));
           int index = 1;
           HashSet<Token> firstOfDef = first(production.definitions[0]);
           int len = production.definitions.length;
           while(firstOfDef.contains(new Token("\\eps")) && index < len) {
               firstOfDef.remove(new Token("\\eps"));
               res.addAll(firstOfDef);
               firstOfDef = first(production.definitions[index]);
               index++;
           }
           res.addAll(firstOfDef);
       }
       firsts.put(token, res);
       return res;
    }

    public HashSet<Token> follow(Token token) {
        HashSet<Token> res = new HashSet<>();
        return res;
    }

    @Override
    public String toString() {
        StringBuilder grammarString = new StringBuilder();
        grammarString.append("NonTerminals: \n");
        for (Token nonTerminal : nonTerminals) {
            if (nonTerminal != null) {
                grammarString.append(nonTerminal.getTokenName()).append("\n");
            }
        }
        grammarString.append("Grammar: \n");
        for (Production production : productions) {
            if (production != null) {
                grammarString.append(production.toString()).append("\n");
            }
        }
        return grammarString.toString();
    }

    public static void main(String[] args) {
        Grammar grammar = Grammar.getInstance();
        System.out.println(grammar);
        for(Token nonTerminal: grammar.nonTerminals) {
            if(nonTerminal == null)
                break;
            System.out.println(grammar.first(nonTerminal));
        }
    }
}

class Production {
    Token nonTerminal;
    Token[] definitions;

    Production(Token nonTerminal, Token[] definitions) {
        this.nonTerminal = nonTerminal;
        this.definitions = definitions;
    }

    @Override
    public String toString() {
        StringBuilder definitionString = new StringBuilder();
        for (Token token : definitions) {
            if (token != null) {
                definitionString.append(token.getTokenName()).append(" ");
            }
        }
        return nonTerminal.getTokenName() + " -> " + definitionString;
    }
}