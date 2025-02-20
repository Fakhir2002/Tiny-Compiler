import java.util.*;

class DFA {
    State startState;
    Set<State> finalStates;
    Set<State> allStates;
    Map<State, Map<Character, State>> transitionTable;
    Map<State, String> finalStateCategories;

    public DFA(State startState, Set<State> finalStates, Set<State> allStates,
               Map<State, Map<Character, State>> transitionTable, Map<State, String> finalStateCategories) {
        this.startState = startState;
        this.finalStates = finalStates;
        this.allStates = allStates;
        this.transitionTable = transitionTable;
        this.finalStateCategories = finalStateCategories;
    }

    public String getTokenType(State state) {
        return finalStateCategories.getOrDefault(state, "Unknown");
    }

    public void printTransitionTable(String input) {
        System.out.println("DFA State Transition Table for "+input+":");
        System.out.println("---------------------------------");
        System.out.println("State      Input      Next State");
        System.out.println("---------------------------------");

        for (Map.Entry<State, Map<Character, State>> entry : transitionTable.entrySet()) {
            State state = entry.getKey();
            for (Map.Entry<Character, State> transition : entry.getValue().entrySet()) {
                System.out.printf("%-10s %-10s %-10s\n", state.id, transition.getKey(), transition.getValue().id);
            }
        }
    }
}
