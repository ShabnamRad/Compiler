package Parser;

import Scanner.LexicalAnalyzer;
import Scanner.Token;

import java.util.HashSet;

public class SyntaxAnalyzer {
    private LexicalAnalyzer lexicalAnalyzer;
    private Grammar grammar = Grammar.getInstance();

    public SyntaxAnalyzer(LexicalAnalyzer lexicalAnalyzer) {
        this.lexicalAnalyzer = lexicalAnalyzer;
        if(program())
            System.out.println("input parsed successfully");
        else
            System.out.println("parsing encountered an error");
    }

    private boolean program() {
        int state = 0;
        while (true) {
            Token token = lexicalAnalyzer.getToken();
            String tokenName = token.getTokenName();
            switch (state) {
                case 0:
                    if (grammar.first(new Token(null, "declaration-list", "NONTERMINAL")).contains(token)) {
                        lexicalAnalyzer.setRepeatToken();
                        if (declaration_list())
                            state = 1;
                        else
                            return false;
                    }
                    else
                        return false;
                    break;
                case 1:
                    switch (tokenName) {
                        case "EOF":
                            state = 2;
                            break;
                        default:
                            return false;
                    }
                    break;
                case 2:
                    return true;
                default:
                    return false;
            }
        }
    }

    private boolean declaration_list() {
        Token diagramNonTerminal = new Token(null, "declaration-list", "NONTERMINAL");
        HashSet<Token> follow = grammar.follow(diagramNonTerminal);
        int state = 0;
        while (true) {
            Token token = lexicalAnalyzer.getToken();
            String tokenName = token.getTokenName();
            switch (state) {
                case 0:
                    switch (tokenName) {
                        case "int":
                        case "void":
                            state = 1;
                            break;
                        default:
                            if (follow.contains(token)) {
                                state = 6;
                                lexicalAnalyzer.setRepeatToken();
                            }
                            else
                                return false;
                    }
                    break;
                case 1:
                    switch (tokenName) {
                        case "ID":
                            state = 2;
                            break;
                        default:
                            return false;
                    }
                    break;
                case 2:
                    switch (tokenName) {
                        case ";":
                            state = 0;
                            break;
                        case "[":
                            state = 3;
                            break;
                        case "(":
                            state = 7;
                            break;
                        default:
                            return false;
                    }
                    break;
                case 3:
                    switch (tokenName) {
                        case "NUM":
                            state = 4;
                            break;
                        default:
                            return false;
                    }
                    break;
                case 4:
                    switch (tokenName) {
                        case "]":
                            state = 5;
                            break;
                        default:
                            return false;
                    }
                    break;
                case 5:
                    switch (tokenName) {
                        case ";":
                            state = 0;
                            break;
                        default:
                            return false;
                    }
                    break;
                case 6:
                    return true;
                case 7:
                    if (grammar.first(new Token(null, "params", "NONTERMINAL")).contains(token)) {
                        lexicalAnalyzer.setRepeatToken();
                        if (params()) {
                            state = 8;
                        }
                        else
                            return false;
                    }
                    else
                        return false;
                    break;
                case 8:
                    switch (tokenName) {
                        case ")":
                            state = 9;
                            break;
                        default:
                            return false;
                    }
                    break;
                case 9:
                    switch (tokenName) {
                        case "{":
                            state = 10;
                            break;
                        default:
                            return false;
                    }
                    break;
                case 10:
                    if (grammar.first(new Token(null, "declaration-list", "NONTERMINAL")).contains(token)) {
                        lexicalAnalyzer.setRepeatToken();
                        if (declaration_list()) {
                            state = 12;
                        }
                        else
                            return false;
                    }
                    else
                        return false;
                    break;
                case 11:
                    if (grammar.first(new Token(null, "statement-list", "NONTERMINAL")).contains(token)) {
                        lexicalAnalyzer.setRepeatToken();
                        if (statement_list()) {
                            state = 0;
                        }
                        else
                            return false;
                    }
                    else
                        return false;
                    break;
                case 12:
                    switch (tokenName) {
                        case "}":
                            state = 11;
                            break;
                        default:
                            return false;
                    }
                    break;
            }
        }
    }

    @SuppressWarnings("all")
    private boolean params() {
        Token diagramNonTerminal = new Token(null, "params", "NONTERMINAL");
        HashSet<Token> follow = grammar.follow(diagramNonTerminal);
        int state = 0;
        while (true) {
            Token token = lexicalAnalyzer.getToken();
            String tokenName = token.getTokenName();
            switch (state) {
                case 0:
                    switch (tokenName) {
                        case "int":
                            state = 1;
                            break;
                        case "void":
                            state = 6;
                            break;
                        default:
                            return false;
                    }
                    break;
                case 1:
                    switch (tokenName) {
                        case "ID":
                            state = 2;
                            break;
                        default:
                            return false;
                    }
                    break;
                case 2:
                    switch (tokenName) {
                        case "[":
                            state = 3;
                            break;
                        case ",":
                            state = 5;
                            break;
                        default:
                            if (follow.contains(token)) {
                                state = 7;
                                lexicalAnalyzer.setRepeatToken();
                            } else
                                return false;
                    }
                    break;
                case 3:
                    switch (tokenName) {
                        case "]":
                            state = 4;
                            break;
                        default:
                            return false;
                    }
                    break;
                case 4:
                    switch (tokenName) {
                        case ",":
                            state = 5;
                            break;
                        default:
                            if (follow.contains(token)) {
                                state = 7;
                                lexicalAnalyzer.setRepeatToken();
                            } else
                                return false;
                    }
                    break;
                case 5:
                    switch (tokenName) {
                        case "int":
                        case "void":
                            state = 1;
                            break;
                        default:
                            return false;
                    }
                    break;
                case 6:
                    switch (tokenName) {
                        case "ID":
                            state = 2;
                            break;
                        default:
                            if (follow.contains(token)) {
                                state = 7;
                                lexicalAnalyzer.setRepeatToken();
                            } else
                                return false;
                    }
                    break;
                case 7:
                    return true;
                default:
                    return false;
            }
        }
    }

    private boolean statement() {
        Token diagramNonTerminal = new Token(null, "expression", "NONTERMINAL");
        HashSet<Token> follow = grammar.follow(diagramNonTerminal);
        int state = 0;
        while (true) {
            Token token = lexicalAnalyzer.getToken();
            String tokenName = token.getTokenName();
            switch (state) {
                case 0:
                    switch (tokenName) {
                        case ";":
                            state = 2;
                            break;
                        case "continue":
                        case "break":
                            state = 1;
                            break;
                        case "if":
                            state = 3;
                            break;
                        case "while":
                            state = 9;
                            break;
                        case "return":
                            state = 13;
                            break;
                        case "switch":
                            state = 15;
                            break;
                        default:
                            if (grammar.first(new Token(null, "expression", "NONTERMINAL")).contains(token)) {
                                lexicalAnalyzer.setRepeatToken();
                                if (expression()) {
                                    state = 1;
                                }
                                else
                                    return false;
                            }
                            else
                                return false;
                            break;
                    }
                    break;
                case 1:
                case 14:
                    switch (tokenName) {
                        case ";":
                            state = 2;
                            break;
                        default:
                            return false;
                    }
                    break;
                case 2:
                    return true;
                case 3:
                    switch (tokenName) {
                        case "(":
                            state = 4;
                            break;
                        default:
                            return false;
                    }
                    break;
                case 4:
                    if (grammar.first(new Token(null, "expression", "NONTERMINAL")).contains(token)) {
                        lexicalAnalyzer.setRepeatToken();
                        if (expression()) {
                            state = 5;
                        }
                        else
                            return false;
                    }
                    else
                        return false;
                    break;
                case 5:
                    switch (tokenName) {
                        case ")":
                            state = 6;
                            break;
                        default:
                            return false;
                    }
                    break;
                case 6:
                    if (grammar.first(new Token(null, "statement", "NONTERMINAL")).contains(token)) {
                        lexicalAnalyzer.setRepeatToken();
                        if (statement()) {
                            state = 7;
                        }
                        else
                            return false;
                    }
                    else
                        return false;
                    break;
                case 7:
                    switch (tokenName) {
                        case "else":
                            state = 8;
                            break;
                        default:
                            return false;
                    }
                    break;
                case 8:
                case 12:
                    if (grammar.first(new Token(null, "statement", "NONTERMINAL")).contains(token)) {
                        lexicalAnalyzer.setRepeatToken();
                        if (statement()) {
                            state = 2;
                        }
                        else
                            return false;
                    }
                    else
                        return false;
                    break;
                case 9:
                    switch (tokenName) {
                        case "(":
                            state = 10;
                            break;
                        default:
                            return false;
                    }
                    break;
                case 10:
                    if (grammar.first(new Token(null, "expression", "NONTERMINAL")).contains(token)) {
                        lexicalAnalyzer.setRepeatToken();
                        if (expression()) {
                            state = 11;
                        }
                        else
                            return false;
                    }
                    else
                        return false;
                    break;
                case 11:
                    switch (tokenName) {
                        case ")":
                            state = 12;
                            break;
                        default:
                            return false;
                    }
                    break;
                case 13:
                    switch (tokenName) {
                        case ";":
                            state = 2;
                            break;
                        default:
                            if (grammar.first(new Token(null, "expression", "NONTERMINAL")).contains(token)) {
                                lexicalAnalyzer.setRepeatToken();
                                if (expression()) {
                                    state = 14;
                                }
                                else
                                    return false;
                            }
                            else
                                return false;
                            break;
                    }
                    break;
                case 15:
                    switch (tokenName) {
                        case "(":
                            state = 16;
                            break;
                        default:
                            return false;
                    }
                    break;
                case 16:
                    if (grammar.first(new Token(null, "expression", "NONTERMINAL")).contains(token)) {
                        lexicalAnalyzer.setRepeatToken();
                        if (expression()) {
                            state = 17;
                        }
                        else
                            return false;
                    }
                    else
                        return false;
                    break;
                case 17:
                    switch (tokenName) {
                        case ")":
                            state = 18;
                            break;
                        default:
                            return false;
                    }
                    break;
                case 18:
                    switch (tokenName) {
                        case "{":
                            state = 19;
                            break;
                        default:
                            return false;
                    }
                    break;
                case 19:
                    switch (tokenName) {
                        case "}":
                            state = 2;
                            break;
                        case "default":
                            state = 20;
                            break;
                        case "case":
                            state = 23;
                            break;
                        default:
                            return false;
                    }
                    break;
                case 20:
                    switch (tokenName) {
                        case ":":
                            state = 21;
                            break;
                        default:
                            return false;
                    }
                    break;
                case 21:
                    if (grammar.first(new Token(null, "statement-list", "NONTERMINAL")).contains(token)) {
                        lexicalAnalyzer.setRepeatToken();
                        if (statement_list()) {
                            state = 22;
                        }
                        else
                            return false;
                    }
                    else
                        return false;
                    break;
                case 22:
                    switch (tokenName) {
                        case "}":
                            state = 2;
                            break;
                        default:
                            return false;
                    }
                    break;
                case 23:
                    switch (tokenName) {
                        case "NUM":
                            state = 24;
                            break;
                        default:
                            return false;
                    }
                    break;
                case 24:
                    switch (tokenName) {
                        case ":":
                            state = 25;
                            break;
                        default:
                            return false;
                    }
                    break;
                case 25:
                    if (grammar.first(new Token(null, "statement-list", "NONTERMINAL")).contains(token)) {
                        lexicalAnalyzer.setRepeatToken();
                        if (statement_list()) {
                            state = 19;
                        }
                        else
                            return false;
                    }
                    else
                        return false;
                    break;
                default:
                    return false;
            }
        }
    }

    @SuppressWarnings("all")
    private boolean expression() {
        Token diagramNonTerminal = new Token(null, "expression", "NONTERMINAL");
        HashSet<Token> follow = grammar.follow(diagramNonTerminal);
        int state = 0;
        while (true) {
            Token token = lexicalAnalyzer.getToken();
            String tokenName = token.getTokenName();
            switch (state) {
                case 0:
                    switch (tokenName) {
                        case "(":
                            state = 1;
                            break;
                        case "NUM":
                            state = 9;
                            break;
                        case "ID":
                            state = 4;
                            break;
                        default:
                            return false;
                    }
                    break;
                case 1:
                    if (grammar.first(new Token(null, "expression", "NONTERMINAL")).contains(token)) {
                        lexicalAnalyzer.setRepeatToken();
                        if (expression())
                            state = 2;
                        else
                            return false;
                    } else
                        return false;
                    break;
                case 2:
                    switch (tokenName) {
                        case ")":
                            state = 9;
                            break;
                        default:
                            return false;
                    }
                    break;
                case 3:
                    switch (tokenName) {
                        case ")":
                            state = 9;
                            break;
                        default:
                            if (grammar.first(new Token(null, "expression", "NONTERMINAL")).contains(token)) {
                                lexicalAnalyzer.setRepeatToken();
                                if (expression())
                                    state = 8;
                                else
                                    return false;
                            } else
                                return false;
                    }
                    break;
                case 4:
                    switch (tokenName) {
                        case "=":
                            state = 0;
                            break;
                        case "(":
                            state = 3;
                            break;
                        case "[":
                            state = 5;
                            break;
                        default:
                            return false;
                    }
                    break;
                case 5:
                    if (grammar.first(new Token("expression")).contains(token)) {
                        lexicalAnalyzer.setRepeatToken();
                        if (expression())
                            state = 6;
                        else
                            return false;
                    } else
                        return false;
                    break;
                case 6:
                    switch (tokenName) {
                        case "]":
                            state = 7;
                            break;
                        default:
                            return false;
                    }
                    break;
                case 7:
                    switch (tokenName) {
                        case "=":
                            state = 0;
                            break;
                        default:
                            return false;
                    }
                    break;
                case 8:
                    switch (tokenName) {
                        case ")":
                            state = 9;
                            break;
                        case ",":
                            state = 11;
                            break;
                        default:
                            return false;
                    }
                    break;
                case 9:
                    switch (tokenName) {
                        case "*":
                            state = 10;
                            break;
                        case "+":
                        case "-":
                            state = 16;
                            break;
                        default:
                            if (follow.contains(token)) {
                                state = 15;
                                lexicalAnalyzer.setRepeatToken();
                            } else
                                return false;
                    }
                    break;
                case 10:
                    if (grammar.first(new Token("factor")).contains(token)) {
                        lexicalAnalyzer.setRepeatToken();
                        if (factor())
                            state = 9;
                        else
                            return false;
                    } else
                        return false;
                    break;
                case 11:
                    if (grammar.first(new Token("expression")).contains(token)) {
                        lexicalAnalyzer.setRepeatToken();
                        if (expression())
                            state = 8;
                        else
                            return false;
                    } else
                        return false;
                    break;
                case 12:
                    switch (tokenName) {
                        case "<":
                        case "==":
                            state = 13;
                            break;
                        default:
                            if (follow.contains(token)) {
                                state = 15;
                                lexicalAnalyzer.setRepeatToken();
                            } else
                                return false;
                    }
                    break;
                case 13:
                    if (grammar.first(new Token("term")).contains(token)) {
                        lexicalAnalyzer.setRepeatToken();
                        if (term())
                            state = 14;
                        else
                            return false;
                    } else
                        return false;
                    break;
                case 14:
                    switch (tokenName) {
                        case "+":
                        case "-":
                            state = 17;
                            break;
                        default:
                            if (follow.contains(token)) {
                                state = 15;
                                lexicalAnalyzer.setRepeatToken();
                            } else
                                return false;
                    }
                    break;
                case 15:
                    return true;
                case 16:
                    if (grammar.first(new Token("term")).contains(token)) {
                        lexicalAnalyzer.setRepeatToken();
                        if (term())
                            state = 12;
                        else
                            return false;
                    } else
                        return false;
                    break;
                case 17:
                    if (grammar.first(new Token("term")).contains(token)) {
                        lexicalAnalyzer.setRepeatToken();
                        if (term())
                            state = 14;
                        else
                            return false;
                    } else
                        return false;
                    break;
                default:
                    return false;
            }
        }
    }

    @SuppressWarnings("all")
    private boolean term() {
        Token diagramNonTerminal = new Token(null, "term", "NONTERMINAL");
        HashSet<Token> follow = grammar.follow(diagramNonTerminal);
        int state = 0;
        while (true) {
            Token token = lexicalAnalyzer.getToken();
            String tokenName = token.getTokenName();
            switch (state) {
                case 0:
                case 3:
                    if (grammar.first(new Token("factor")).contains(token)) {
                        lexicalAnalyzer.setRepeatToken();
                        if (factor())
                            state = 1;
                        else
                            return false;
                    } else
                        return false;
                    break;
                case 1:
                    switch (tokenName) {
                        case "*":
                            state = 3;
                            break;
                        default:
                            if (follow.contains(token)) {
                                state = 2;
                                lexicalAnalyzer.setRepeatToken();
                            } else
                                return false;
                    }
                    break;
                case 2:
                    return true;
                default:
                    return false;
            }
        }
    }

    @SuppressWarnings("all")
    private boolean factor() {
        Token diagramNonTerminal = new Token(null, "factor", "NONTERMINAL");
        HashSet<Token> follow = grammar.follow(diagramNonTerminal);
        int state = 0;
        while (true) {
            Token token = lexicalAnalyzer.getToken();
            String tokenName = token.getTokenName();
            switch (state) {
                case 0:
                    switch (tokenName) {
                        case "(":
                            state = 1;
                            break;
                        case "NUM":
                            state = 3;
                            break;
                        case "ID":
                            state = 4;
                            break;
                        default:
                            return false;
                    }
                    break;
                case 1:
                    if (grammar.first(new Token("expression")).contains(token)) {
                        lexicalAnalyzer.setRepeatToken();
                        if (expression())
                            state = 2;
                        else
                            return false;
                    } else
                        return false;
                    break;
                case 2:
                    switch (tokenName) {
                        case ")":
                            state = 3;
                            break;
                        default:
                            return false;
                    }
                    break;
                case 3:
                    return true;
                case 4:
                    switch (tokenName) {
                        case "[":
                            state = 5;
                            break;
                        case "(":
                            state = 7;
                            break;
                        default:
                            if (follow.contains(token)) {
                                state = 3;
                                lexicalAnalyzer.setRepeatToken();
                            } else
                                return false;
                    }
                    break;
                case 5:
                    if (grammar.first(new Token("expression")).contains(token)) {
                        lexicalAnalyzer.setRepeatToken();
                        if (expression())
                            state = 6;
                        else
                            return false;
                    } else
                        return false;
                    break;
                case 6:
                    switch (tokenName) {
                        case "]":
                            state = 3;
                            break;
                        default:
                            return false;
                    }
                    break;
                case 7:
                    switch (tokenName) {
                        case ")":
                            state = 3;
                            break;
                        default:
                            if (grammar.first(new Token("expression")).contains(token)) {
                                lexicalAnalyzer.setRepeatToken();
                                if (expression())
                                    state = 8;
                                else
                                    return false;
                            } else
                                return false;
                    }
                    break;
                case 8:
                    switch (tokenName) {
                        case ",":
                            state = 9;
                            break;
                        case ")":
                            state = 3;
                            break;
                        default:
                            return false;
                    }
                    break;
                case 9:
                    if (grammar.first(new Token("expression")).contains(token)) {
                        lexicalAnalyzer.setRepeatToken();
                        if (expression())
                            state = 8;
                        else
                            return false;
                    } else
                        return false;
                    break;
                default:
                    return false;
            }
        }
    }

    private boolean statement_list() {
        return true;
    }
}
