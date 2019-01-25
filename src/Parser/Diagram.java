package Parser;

import Scanner.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shabnam on 1/24/2019.
 */
class Diagram {
    private String name;
    private ArrayList<State> states;
    private State startState;

    Diagram(String name) {
        this.name = name;
        states = new ArrayList<>();
        startState = null;
    }

    public String getName() {
        return name;
    }

    void addState(State state) {
        this.states.add(state);
    }

    ArrayList<State> getStates() {
        return states;
    }

    void setStartState(State startState) {
        this.startState = startState;
        this.startState.setAsStartState();
        this.states.add(this.startState);
    }

    public State getStartState() {
        return startState;
    }
}

class State {
    private Map<Edge, State> children;
    private boolean startState = false;
    private boolean endState = false;

    State() {
        this.children = new HashMap<>();
    }

    State(boolean endState) {
        this.children = new HashMap<>();
        if(endState)
            this.setAsEndState();
    }

    State(Map<Edge, State> children) {
        this.children = children;
    }

    Map<Edge, State> getChildren() {
        return children;
    }

    public void addChildren(Edge edge, State state) {
        this.children.put(edge, state);
    }

    public boolean isStartState() {
        return startState;
    }

    void setAsStartState() {
        this.startState = true;
    }

    boolean isEndState() {
        return endState;
    }

    private void setAsEndState() {
        this.endState = true;
    }
}

class Edge {
    private boolean isDiagram;
    private Token token;
    private Diagram diagram;

    Edge(String tokenName) {
        isDiagram = false;
        if(tokenName.equals("ID"))
            this.token = new Token(null, tokenName, "unknown");
        else if(tokenName.equals("NUM"))
            this.token = new Token(null, tokenName, "INT");
        else
            this.token = new Token(tokenName);
    }

    Edge(Diagram diagram) {
        isDiagram = true;
        this.diagram = diagram;
    }

    boolean isDiagram() {
        return isDiagram;
    }

    Token getToken() {
        return token;
    }

    Diagram getDiagram() {
        return diagram;
    }
}