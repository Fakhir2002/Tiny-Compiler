import java.util.*;

class NFAtoDFA {
    public DFA convertNFAtoDFA(NFA nfa) {
        Map<Set<State>, State> dfaStatesMap = new HashMap<>();
        Queue<Set<State>> queue = new LinkedList<>();
        Set<State> allDFAStates = new HashSet<>();
        Set<State> finalDFAStates = new HashSet<>();
        Map<State, Map<Character, State>> transitionTable = new HashMap<>();
        Map<State, String> finalStateCategories = new HashMap<>(); // Store token categories

        // Compute ε-closure of the start state
        Set<State> startClosure = epsilonClosure(Set.of(nfa.startState));
        State dfaStart = new State(0, containsFinalState(startClosure, nfa.finalStates));
        dfaStatesMap.put(startClosure, dfaStart);
        allDFAStates.add(dfaStart);
        queue.add(startClosure);

        int stateCounter = 1;

        while (!queue.isEmpty()) {
            Set<State> currentSet = queue.poll();
            State currentDFAState = dfaStatesMap.get(currentSet);
            transitionTable.put(currentDFAState, new HashMap<>());

            for (char symbol : getAlphabet(nfa.allStates)) {
                Set<State> moveResult = move(currentSet, symbol);
                Set<State> epsilonClosureResult = epsilonClosure(moveResult);

                if (!epsilonClosureResult.isEmpty()) {
                    State newState;
                    if (!dfaStatesMap.containsKey(epsilonClosureResult)) {
                        newState = new State(stateCounter++, containsFinalState(epsilonClosureResult, nfa.finalStates));
                        dfaStatesMap.put(epsilonClosureResult, newState);
                        allDFAStates.add(newState);
                        queue.add(epsilonClosureResult);
                    } else {
                        newState = dfaStatesMap.get(epsilonClosureResult);
                    }
                    transitionTable.get(currentDFAState).put(symbol, newState);
                }
            }
        }

        // Assign final state categories for token classification
        for (Set<State> stateSet : dfaStatesMap.keySet()) {
            State dfaState = dfaStatesMap.get(stateSet);
            if (dfaState.isFinal) {
                finalDFAStates.add(dfaState);
                finalStateCategories.put(dfaState, "Final State"); // Default, replaced in MergedDFA
            }
        }

        return new DFA(dfaStart, finalDFAStates, allDFAStates, transitionTable, finalStateCategories);
    }

    private Set<State> epsilonClosure(Set<State> states) {
        Stack<State> stack = new Stack<>();
        Set<State> closure = new HashSet<>(states);

        for (State state : states) {
            stack.push(state);
        }

        while (!stack.isEmpty()) {
            State state = stack.pop();
            for (State epsState : state.epsilonTransitions) {
                if (!closure.contains(epsState)) {
                    closure.add(epsState);
                    stack.push(epsState);
                }
            }
        }
        return closure;
    }

    private Set<State> move(Set<State> states, char symbol) {
        Set<State> result = new HashSet<>();
        for (State state : states) {
            if (state.transitions.containsKey(symbol)) {
                result.addAll(state.transitions.get(symbol));
            }
        }
        return result;
    }

    private Set<Character> getAlphabet(Set<State> states) {
        Set<Character> alphabet = new HashSet<>();
        for (State state : states) {
            alphabet.addAll(state.transitions.keySet());
        }
        return alphabet;
    }

    private boolean containsFinalState(Set<State> states, Set<State> finalStates) {
        for (State state : states) {
            if (finalStates.contains(state)) return true;
        }
        return false;
    }
}
