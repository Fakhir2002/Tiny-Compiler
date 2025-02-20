import java.util.*;

class State {
    int id;
    boolean isFinal;  // Marking final state
    Map<Character, List<State>> transitions;  // Transitions on characters
    List<State> epsilonTransitions;  // Epsilon transitions

    public State(int id) {
        this.id = id;
        this.isFinal = false; // Default to non-final
        this.transitions = new HashMap<>();
        this.epsilonTransitions = new ArrayList<>();
    }

    public State(int id, boolean isFinal) { // Overloaded constructor
        this.id = id;
        this.isFinal = isFinal;
        this.transitions = new HashMap<>();
        this.epsilonTransitions = new ArrayList<>();
    }

    public void addTransition(char symbol, State nextState) {
        transitions.putIfAbsent(symbol, new ArrayList<>());
        transitions.get(symbol).add(nextState);
    }

    public void addEpsilonTransition(State nextState) {
        epsilonTransitions.add(nextState);
    }
}
