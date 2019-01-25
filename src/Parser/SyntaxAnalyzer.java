package Parser;

import Scanner.LexicalAnalyzer;
import Scanner.Token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SyntaxAnalyzer {
    private ArrayList<Diagram> transitionDigrams;
    private LexicalAnalyzer lexicalAnalyzer;
    private Grammar grammar = Grammar.getInstance();

    public SyntaxAnalyzer(LexicalAnalyzer lexicalAnalyzer) {
        this.lexicalAnalyzer = lexicalAnalyzer;
        transitionDigrams = new ArrayList<>();
        Diagram program = new Diagram("program");
        Diagram declaration_list = new Diagram("declaration-list");
        Diagram params = new Diagram("params");
        Diagram param = new Diagram("param");
        Diagram statement_list = new Diagram("statement-list");
        Diagram statement = new Diagram("statement");
        Diagram expression = new Diagram("expression");
        Diagram var = new Diagram("var");
        Diagram term = new Diagram("term");
        Diagram factor = new Diagram("factor");
        transitionDigrams = new ArrayList<>(Arrays.asList(program, declaration_list, params, param, statement_list,
                statement, expression, var, term, factor));

        //Constructing Diagrams
        State state = new State(true);
        program.addState(state);
        Map<Edge, State> children = new HashMap<>();
        children.put(new Edge("\uFFFF"), state);
        state = new State(children);
        program.addState(state);
        children = new HashMap<>();
        children.put(new Edge(declaration_list), state);
        state = new State(children);
        program.setStartState(state);

        state = new State(true);
        declaration_list.addState(state);
        children = new HashMap<>();
        children.put(new Edge("\\eps"), state);
        state = new State(children);
        declaration_list.setStartState(state);
        children = new HashMap<>();
        children.put(new Edge("\\eps"), state);
        state = new State(children);
        declaration_list.addState(state);
        children = new HashMap<>();
        children.put(new Edge(";"), state);
        state = new State(children);
        declaration_list.addState(state);
        children.put(new Edge(";"), declaration_list.getStates().get(2));
        state = new State(children);
        declaration_list.addState(state);
    }

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
