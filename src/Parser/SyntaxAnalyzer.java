package Parser;

import Scanner.LexicalAnalyzer;

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
        Diagram program = new Diagram();
        Diagram declaration_list = new Diagram();
        Diagram params = new Diagram();
        Diagram param = new Diagram();
        Diagram statement_list = new Diagram();
        Diagram statement = new Diagram();
        Diagram expression = new Diagram();
        Diagram var = new Diagram();
        Diagram term = new Diagram();
        Diagram factor = new Diagram();
        transitionDigrams = new ArrayList<>(Arrays.asList(program, declaration_list, params, param, statement_list,
                statement, expression, var, term, factor));

        //Constructing Diagrams
        State state = new State(true);
        program.addState(state);
        Map<Edge, State> children = new HashMap<>();
        children.put(new Edge("EOF"), state);
        state = new State(children);
        program.addState(state);
        children = new HashMap<>();
        children.put(new Edge(declaration_list), state);
        state = new State(children);
        program.setStartState(state);

        state = new State(true);
        declaration_list.addState(state);
        children = new HashMap<>();
        children.put(new Edge("epsilon"), state);
        state = new State(children);
        declaration_list.setStartState(state);
        children = new HashMap<>();
        children.put(new Edge("epsilon"), state);
        state = new State(children);
        declaration_list.addState(state);
        children = new HashMap<>();
        children.put(new Edge("semi-colon"), state);
        state = new State(children);
        declaration_list.addState(state);
        children.put(new Edge("semi-colon"), declaration_list.getStates().get(2));
        state = new State(children);
        declaration_list.addState(state);
    }
}
