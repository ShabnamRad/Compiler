package Parser;

import Scanner.LexicalAnalyzer;
import Scanner.Token;

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

    private boolean program(){return true;}
    private boolean declaration_list(){return true;}
    private boolean params(){return true;}
    private boolean statement(){return true;}
    private boolean expression(){return true;}
    private boolean term(){return true;}
    private boolean factor(){return true;}
    private boolean statement_list(){return true;}

    private boolean traverse(Diagram diagram) {
        State curr = diagram.getStartState();
        while(!curr.isEndState()) {
            boolean found = false;
            Token token = lexicalAnalyzer.getToken();
            for (Edge edge : curr.getChildren().keySet()) {
                if(edge.isDiagram()) {
                    Diagram candidateDiagram = edge.getDiagram();
                    Token diagramNonTerminal = grammar.getDiagramNonTerminal(candidateDiagram);
                    if (grammar.first(diagramNonTerminal).contains(token)) {
                        lexicalAnalyzer.returnLastChar();
                        if (!traverse(candidateDiagram))
                            return false;
                        curr = curr.getChildren().get(edge);
                        found = true;
                        break;
                    }
                } else if(edge.getToken().equals(new Token("\\eps"))) {
                    //TODO: check for t in follow
                    if(false) {
                        curr = curr.getChildren().get(edge);
                        found = true;
                        break;
                    }
                } else if (edge.getToken().equals(token)) {
                    curr = curr.getChildren().get(edge);
                    found = true;
                    break;
                }
            }
            if(!found)
                return false;
        }
        return true;
    }
}
