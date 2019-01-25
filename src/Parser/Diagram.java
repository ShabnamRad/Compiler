package Parser;

import Scanner.LexicalAnalyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shabnam on 1/24/2019.
 */
class Diagram {
    private ArrayList<State> states;
    private State startState;

    Diagram() {
        states = new ArrayList<>();
        startState = null;
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

    public boolean traverse(LexicalAnalyzer lexicalAnalyzer) {
        State curr = this.startState;
        while(!curr.isEndState()) {
            boolean found = false;
            String tokenName = lexicalAnalyzer.getToken().getTokenName();
            for (Edge edge : curr.getChildren().keySet()) {
                if(edge.isDiagram()) {
                    Diagram candidateDiagram = edge.getDiagram();
                    //TODO: check for t in First
                    if (false) {
                        if (!candidateDiagram.traverse(lexicalAnalyzer))
                            return false;
                        curr = curr.getChildren().get(edge);
                        found = true;
                        break;
                    }
                } else if(edge.getTokenName().equals("epsilon")) {
                    //TODO: check for t in follow
                    if(false) {
                        curr = curr.getChildren().get(edge);
                        found = true;
                        break;
                    }
                } else if (edge.getTokenName().equals(tokenName)) {
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
    private String tokenName;
    private Diagram diagram;

    Edge(String tokenName) {
        isDiagram = false;
        this.tokenName = tokenName;
    }

    Edge(Diagram diagram) {
        isDiagram = true;
        this.diagram = diagram;
    }

    boolean isDiagram() {
        return isDiagram;
    }

    String getTokenName() {
        return tokenName;
    }

    Diagram getDiagram() {
        return diagram;
    }
}