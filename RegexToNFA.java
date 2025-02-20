import java.util.*;

class RegexToNFA {
    private int stateCounter = 0;

    private State createNewState(Set<State> allStates, boolean isFinal) {
        State newState = new State(stateCounter++, isFinal);
        allStates.add(newState);
        return newState;
    }

    public NFA createNFAFromSymbol(char symbol) {
        Set<State> allStates = new HashSet<>();
        State start = createNewState(allStates, false);
        State end = createNewState(allStates, true);
        start.addTransition(symbol, end);
        return new NFA(start, Set.of(end), allStates);
    }

    public NFA applyKleeneStar(NFA nfa) {
        Set<State> allStates = new HashSet<>(nfa.allStates);
        State start = createNewState(allStates, false);
        State end = createNewState(allStates, true);

        start.addEpsilonTransition(nfa.startState);
        start.addEpsilonTransition(end);

        for (State finalState : nfa.finalStates) {
            finalState.addEpsilonTransition(nfa.startState);
            finalState.addEpsilonTransition(end);
        }

        return new NFA(start, Set.of(end), allStates);
    }

    public NFA concatenate(NFA first, NFA second) {
        Set<State> allStates = new HashSet<>(first.allStates);
        allStates.addAll(second.allStates);

        for (State finalState : first.finalStates) {
            finalState.addEpsilonTransition(second.startState);
        }

        return new NFA(first.startState, second.finalStates, allStates);
    }

    public NFA union(NFA first, NFA second) {
        Set<State> allStates = new HashSet<>(first.allStates);
        allStates.addAll(second.allStates);

        State start = createNewState(allStates, false);
        State end = createNewState(allStates, true);

        start.addEpsilonTransition(first.startState);
        start.addEpsilonTransition(second.startState);

        for (State finalState : first.finalStates) {
            finalState.addEpsilonTransition(end);
        }
        for (State finalState : second.finalStates) {
            finalState.addEpsilonTransition(end);
        }

        return new NFA(start, Set.of(end), allStates);
    }

    public NFA createNFAForRange(char startChar, char endChar) {
        Set<State> allStates = new HashSet<>();
        State start = createNewState(allStates, false);
        State end = createNewState(allStates, true);

        for (char c = startChar; c <= endChar; c++) {
            start.addTransition(c, end);
        }

        end.addEpsilonTransition(start);
        return new NFA(start, Set.of(end), allStates);
    }

    public NFA createNFAForComment(char commentStarter) {
        Set<State> allStates = new HashSet<>();
        State start = createNewState(allStates, false);
        State inComment = createNewState(allStates, false);
        State end = createNewState(allStates, true);

        start.addTransition(commentStarter, inComment);
        for (char c = 32; c <= 126; c++) {
            if (c != '\n') inComment.addTransition(c, inComment);
        }
        inComment.addTransition('\n', end);

        return new NFA(start, Set.of(end), allStates);
    }

    public NFA createNFAForMultiLineComment() {
        Set<State> allStates = new HashSet<>();
        State start = createNewState(allStates, false);
        State firstTilde = createNewState(allStates, false);
        State secondTilde = createNewState(allStates, false);
        State inComment = createNewState(allStates, false);
        State closingFirstTilde = createNewState(allStates, false);
        State closingSecondTilde = createNewState(allStates, true);

        start.addTransition('~', firstTilde);
        firstTilde.addTransition('~', secondTilde);
        for (char c = 10; c <= 127; c++) {
            inComment.addTransition(c, inComment);
        }
        inComment.addTransition('~', closingFirstTilde);
        closingFirstTilde.addTransition('~', closingSecondTilde);

        return new NFA(start, Set.of(closingSecondTilde), allStates);
    }

    public NFA createNFAForDecimal() {
        Set<State> allStates = new HashSet<>();
        State start = createNewState(allStates, false);
        State intPart = createNewState(allStates, false);
        State dot = createNewState(allStates, false);
        State firstDec = createNewState(allStates, false);
        State secondDec = createNewState(allStates, false);
        State thirdDec = createNewState(allStates, false);
        State fourthDec = createNewState(allStates, false);
        State fifthDec = createNewState(allStates, false);
        State finalState = createNewState(allStates, true);
    
        // Integer part (at least one digit before the decimal point)
        for (char c = '0'; c <= '9'; c++) {
            start.addTransition(c, intPart);
            intPart.addTransition(c, intPart);
        }
    
        // Decimal point (mandatory)
        intPart.addTransition('.', dot);
    
        // At least one digit must follow the decimal point
        for (char c = '0'; c <= '9'; c++) {
            dot.addTransition(c, firstDec);
            firstDec.addTransition(c, secondDec);
            secondDec.addTransition(c, thirdDec);
            thirdDec.addTransition(c, fourthDec);
            fourthDec.addTransition(c, fifthDec);
            fifthDec.addTransition(c, fifthDec);
        }
    
        // Ensure at least one decimal digit is required
        firstDec.addEpsilonTransition(finalState);
        secondDec.addEpsilonTransition(finalState);
        thirdDec.addEpsilonTransition(finalState);
        fourthDec.addEpsilonTransition(finalState);
        fifthDec.addEpsilonTransition(finalState);
    
        return new NFA(start, Set.of(finalState), allStates);
    }
    

    public NFA createNFAFromRegex(String regex) {
        if (regex.length() == 3 && regex.charAt(2) == '*') {
            return createNFAForComment(regex.charAt(0)); 
        }

        if (regex.equals("~~.*~~")) { 
            return createNFAForMultiLineComment(); 
        }
        if(regex.equals("[0-9]+\\.[0-9]{1,5}")){
            return createNFAForDecimal();
        }
    
    
        Set<State> allStates = new HashSet<>();
        State start = createNewState(allStates,false);
        State end = createNewState(allStates,true);
        
        allStates.add(start);
        allStates.add(end);
    
        // Handle identifiers "[a-z]+" and numbers "[0-9]+"
        if (regex.length() >= 6 && regex.startsWith("[") && regex.endsWith("]+") && regex.charAt(2) == '-') {
            char startChar = regex.charAt(1);
            char endChar = regex.charAt(3);
    
            for (char c = startChar; c <= endChar; c++) {
                start.addTransition(c, end); // First character (0 -> 1)
            }
    
            end.addEpsilonTransition(start); // Loop back for "+"
        } 
        // Handle operators like "+|/|-|*|%|^|="
        else if (regex.contains("|")) {
            String[] operators = regex.split("\\|");
            for (String op : operators) {
                if (op.length() == 1) {
                    start.addTransition(op.charAt(0), end);
                }
            }
        }
        // Handle single character cases
        else if (regex.length() == 1) {
            start.addTransition(regex.charAt(0), end);
        } else {
            throw new IllegalArgumentException("Invalid regex format: " + regex);
        }
    
        return new NFA(start, Set.of(end), allStates);
    }
    


    
}
