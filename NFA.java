import java.util.*;

class NFA {
    State startState;
    Set<State> finalStates;
    Set<State> allStates;

    public NFA(State startState, Set<State> finalStates, Set<State> allStates) {
        this.startState = startState;
        this.finalStates = finalStates;
        this.allStates = allStates;
    }

    // Print the NFA state transition table
    public void printTransitionTable(String input) {
        System.out.println("\nNFA State Transition Table for "+input+":");
        System.out.println("---------------------------------");
        System.out.printf("%-10s %-10s %-10s\n", "State", "Input", "Next State");
        System.out.println("---------------------------------");

        for (State state : allStates) {
            // Print character-based transitions
            for (Map.Entry<Character, List<State>> entry : state.transitions.entrySet()) {
                for (State nextState : entry.getValue()) {
                    System.out.printf("%-10d %-10s %-10d\n", state.id, entry.getKey(), nextState.id);
                }
            }

            // Print epsilon transitions
            for (State epsState : state.epsilonTransitions) {
                System.out.printf("%-10d %-10s %-10d\n", state.id, "ε", epsState.id);
            }
        }
        System.out.println("---------------------------------");
    }
}
