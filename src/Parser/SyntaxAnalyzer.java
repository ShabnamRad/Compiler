package Parser;

import Scanner.LexicalAnalyzer;
import Scanner.Token;

import java.util.HashSet;

public class SyntaxAnalyzer {
    private LexicalAnalyzer lexicalAnalyzer;
    private Grammar grammar = Grammar.getInstance();

    public SyntaxAnalyzer(LexicalAnalyzer lexicalAnalyzer) {
        this.lexicalAnalyzer = lexicalAnalyzer;
        if (program())
            System.out.println("input parsed successfully");
        else
            System.out.println("parsing encountered an error");
    }

    private boolean program() {
        return true;
    }

    private boolean declaration_list() {
        return true;
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
        return true;
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
        Token diagramNonTerminal = new Token(null, "term", "NONTERMINAL");
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
        Token diagramNonTerminal = new Token(null, "term", "NONTERMINAL");
        HashSet<Token> follow = grammar.follow(diagramNonTerminal);
        int state = 0;
        while (true) {
            Token token = lexicalAnalyzer.getToken();
            switch (state) {
                case 0:
                    if (grammar.first(new Token("statement")).contains(token)) {
                        lexicalAnalyzer.setRepeatToken();
                        if (statement())
                            state = 1;
                        else
                            return false;
                    } else if (follow.contains(token)) {
                        state = 2;
                        lexicalAnalyzer.setRepeatToken();
                    } else
                        return false;
                    break;
                case 1:
                    if (grammar.first(new Token("statement")).contains(token)) {
                        lexicalAnalyzer.setRepeatToken();
                        if (statement())
                            state = 0;
                        else
                            return false;
                    } else if (follow.contains(token)) {
                        state = 2;
                        lexicalAnalyzer.setRepeatToken();
                    } else
                        return false;
                    break;
                case 2:
                    return true;
                default:
                    return false;
            }
        }
    }
}
