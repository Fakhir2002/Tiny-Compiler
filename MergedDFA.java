import java.util.*;

class MergedDFA {
    public DFA mergeDFA(List<DFA> dfaList) {
        Set<State> allStates = new HashSet<>();
        Set<State> finalStates = new HashSet<>();
        Map<State, Map<Character, State>> transitionTable = new HashMap<>();
        State mergedStart = new State(0, false); // Unified start state

        Map<State, State> stateMapping = new HashMap<>();
        int stateCounter = 1;

        // Map final states to token categories
        Map<State, String> finalStateCategories = new HashMap<>();

        for (DFA dfa : dfaList) {
            Map<State, State> localStateMapping = new HashMap<>();

            // Create unique states for each DFA
            for (State state : dfa.allStates) {
                State newState = new State(stateCounter++, state.isFinal);
                allStates.add(newState);
                localStateMapping.put(state, newState);
                stateMapping.put(state, newState);
            }

            // Assign final states of this DFA a token type
            for (State finalState : dfa.finalStates) {
                String tokenType = dfa.getTokenType(dfa.startState); // Retrieve from actual DFA states

                if (!tokenType.equals("Unknown")) {
                    finalStateCategories.put(localStateMapping.get(finalState), tokenType);
                }
            }

            // Ensure merged start state transitions correctly
            transitionTable.putIfAbsent(mergedStart, new HashMap<>());
            for (Map.Entry<Character, State> entry : dfa.transitionTable.get(dfa.startState).entrySet()) {
                char input = entry.getKey();
                State nextState = localStateMapping.get(entry.getValue());
                transitionTable.get(mergedStart).put(input, nextState);
            }

            // Copy all transitions
            for (State state : dfa.allStates) {
                State mappedState = localStateMapping.get(state);
                transitionTable.putIfAbsent(mappedState, new HashMap<>());

                for (Map.Entry<Character, State> entry : dfa.transitionTable.getOrDefault(state, new HashMap<>()).entrySet()) {
                    transitionTable.get(mappedState).put(entry.getKey(), localStateMapping.get(entry.getValue()));
                }
            }
        }

        // Add merged start state
        allStates.add(mergedStart);
        transitionTable.putIfAbsent(mergedStart, new HashMap<>());

        return new DFA(mergedStart, finalStates, allStates, transitionTable, finalStateCategories);
    }
}
