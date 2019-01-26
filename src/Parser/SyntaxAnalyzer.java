package Parser;

import Scanner.LexicalAnalyzer;
import Scanner.Token;

import java.util.HashSet;

public class SyntaxAnalyzer {
    private LexicalAnalyzer lexicalAnalyzer;
    private Grammar grammar = Grammar.getInstance();

    public SyntaxAnalyzer(LexicalAnalyzer lexicalAnalyzer) {
        this.lexicalAnalyzer = lexicalAnalyzer;


    }

    private boolean call(String functionName) {
        switch (functionName) {
            case "program":
                return program();
            case "declaration-list":
                return declaration_list();
            case "params":
                return params();
            case "statement":
                return statement();
            case "expression":
                return expression();
            case "term":
                return term();
            case "factor":
                return factor();
            case "statement-list":
                return statement_list();
            default:
                return false;
        }
    }

    private boolean program() {
        return true;
    }

    private boolean declaration_list() {
        return true;
    }

    @SuppressWarnings("Duplicates")
    private boolean params() {
        Token diagramNonTerminal = new Token(null, "params", "NONTERMINAL");
        HashSet<Token> follow = grammar.follow(diagramNonTerminal);
        int state = 0;
        whileLoop:
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
                            throw new Error("panic! edge not found");
                    }
                    break;
                case 1:
                    switch (tokenName) {
                        case "ID":
                            state = 2;
                            break;
                        default:
                            throw new Error("panic! edge not found");
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
                                throw new Error("panic! edge not found");
                    }
                    break;
                case 3:
                    switch (tokenName) {
                        case "]":
                            state = 4;
                            break;
                        default:
                            throw new Error("panic! edge not found");
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
                                throw new Error("panic! edge not found");
                    }
                    break;
                case 5:
                    switch (tokenName) {
                        case "int":
                        case "void":
                            state = 1;
                            break;
                        default:
                            throw new Error("panic! edge not found");
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
                                throw new Error("panic! edge not found"); //TODO: maybe return false?
                    }
                    break;
                case 7:
                    break whileLoop;
            }
        }
        return true;
    }

    private boolean statement() {
        return true;
    }

    private boolean expression() {
        return true;
    }

    private boolean term() {
        Token diagramNonTerminal = new Token(null, "term", "NONTERMINAL");
        HashSet<Token> follow = grammar.follow(diagramNonTerminal);
        int state = 0;
        whileLoop:
        while (true) {
            Token token = lexicalAnalyzer.getToken();
            String tokenName = token.getTokenName();
            switch (state) {
                case 0:
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
                    break whileLoop;
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
                default:
                    return false;
            }
        }
        return true;
    }

    private boolean factor() {
        return true;
    }

    private boolean statement_list() {
        return true;
    }

//    private boolean traverse(Diagram diagram) {
//        State curr = diagram.getStartState();
//        while (!curr.isEndState()) {
//            boolean found = false;
//            Token token = lexicalAnalyzer.getToken();
//            for (Edge edge : curr.getChildren().keySet()) {
//                if (edge.isDiagram()) {
//                    Diagram candidateDiagram = edge.getDiagram();
//                    Token diagramNonTerminal = grammar.getDiagramNonTerminal(candidateDiagram);
//                    if (grammar.first(diagramNonTerminal).contains(token)) {
//                        lexicalAnalyzer.returnLastChar();
//                        if (!traverse(candidateDiagram))
//                            return false;
//                        curr = curr.getChildren().get(edge);
//                        found = true;
//                        break;
//                    }
//                } else if (edge.getToken().equals(new Token("\\eps"))) {
//                    //TODO: check for t in follow
//                    if (false) {
//                        curr = curr.getChildren().get(edge);
//                        found = true;
//                        break;
//                    }
//                } else if (edge.getToken().equals(token)) {
//                    curr = curr.getChildren().get(edge);
//                    found = true;
//                    break;
//                }
//            }
//            if (!found)
//                return false;
//        }
//        return true;
//    }
}
