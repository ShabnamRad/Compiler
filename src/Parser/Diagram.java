package Parser;

import java.util.ArrayList;

/**
 * Created by Shabnam on 1/24/2019.
 */
public class Diagram {
    private ArrayList<State> states;
    private State startState;

    public ArrayList<State> getStates() {
        return states;
    }

    public void addState(State state) {
        this.states.add(state);
    }

    public State getStartState() {
        return startState;
    }

    public void setStartState(State startState) {
        this.startState = startState;
        this.startState.setAsStartState();
        this.states.add(this.startState);
    }
}

class State {
    private int stateNumber;
    private ArrayList<State> children;
    private boolean startState;
    private boolean endState;

    public State() {
    }

    public State(int stateNumber, ArrayList<State> children) {
        this.stateNumber = stateNumber;
        this.children = children;
    }

    public int getStateNumber() {
        return stateNumber;
    }

    public void setStateNumber(int stateNumber) {
        this.stateNumber = stateNumber;
    }

    public ArrayList<State> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<State> children) {
        this.children = children;
    }

    public boolean isStartState() {
        return startState;
    }

    void setAsStartState() {
        this.startState = true;
    }

    public boolean isEndState() {
        return endState;
    }

    public void setEndState(boolean endState) {
        this.endState = endState;
    }
}