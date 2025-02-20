import java.util.*;

class DFAScanner {
    private DFA dfa;
    private Set<String> keywords;
    private Set<String> operators;
    private int tokenCount;
    private ErrorHandler errorHandler; //  Added ErrorHandler

    public DFAScanner(DFA dfa, ErrorHandler errorHandler) { //  Pass ErrorHandler in constructor
        this.dfa = dfa;
        this.errorHandler = errorHandler;
        this.tokenCount = 0;

        // Define language keywords
        keywords = new HashSet<>(Arrays.asList(
            "sachai", "sahi", "jhoot", "adad", "nukta", "harf", "jumla", "duniyawala", "muqarrar", "gharwala"
        ));

        // Define language operators
        operators = new HashSet<>(Arrays.asList("+", "-", "*", "/", "%", "=", "^"));
    }

    public List<TokenInfo> tokenize(String input) {
        List<TokenInfo> tokens = new ArrayList<>();
        StringBuilder currentToken = new StringBuilder();
        State currentState = dfa.startState;
        int lineNumber = 1;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            // **Track line numbers**
            if (c == '\n') {
                lineNumber++;
                continue;
            }

            // **Handle Multi-line Comments (`~~ ... ~~`)**
            if (c == '~' && i + 1 < input.length() && input.charAt(i + 1) == '~') {
                int start = i;
                i += 2;
                while (i + 1 < input.length() && !(input.charAt(i) == '~' && input.charAt(i + 1) == '~')) {
                    if (input.charAt(i) == '\n') lineNumber++;
                    i++;
                }
                if (i + 1 < input.length()) {
                    i++; // Move past the closing `~~`
                }
                tokens.add(new TokenInfo("Multi-line Comment", input.substring(start, i + 1), lineNumber));
                tokenCount++;
                continue;
            }

            // **Handle Single-line Comments (`~...`)**
            if (c == '~') {
                int start = i;
                while (i < input.length() && input.charAt(i) != '\n') {
                    i++;
                }
                tokens.add(new TokenInfo("Single-line Comment", input.substring(start, i), lineNumber));
                tokenCount++;
                lineNumber++;  //  Ensure line number is updated
                continue;
            }

            // **Ignore extra spaces**
            if (Character.isWhitespace(c) && currentToken.length() == 0) {
                continue;
            }

            // **Handle Character (`'a'`)**
            if (c == '\'' && i + 2 < input.length() && input.charAt(i + 2) == '\'') {
                tokens.add(new TokenInfo("Character", input.substring(i, i + 3), lineNumber));
                tokenCount++;
                i += 2;
                continue;
            }

            // **Handle String (`"hello"`)**
            if (c == '"') {
                int start = i;
                i++;
                while (i < input.length() && input.charAt(i) != '"') {
                    i++;
                }
                if (i < input.length()) {
                    tokens.add(new TokenInfo("String", input.substring(start, i + 1), lineNumber));
                    tokenCount++;
                } else {
                    errorHandler.reportError(lineNumber, "Unclosed string literal.");
                }
                continue;
            }

            // **Check DFA transition for the current character**
            if (dfa.transitionTable.containsKey(currentState) && dfa.transitionTable.get(currentState).containsKey(c)) {
                currentState = dfa.transitionTable.get(currentState).get(c);
                currentToken.append(c);
            } else {
                if (currentToken.length() > 0) {
                    // ✅ Check if a number follows an identifier or keyword
                    if (Character.isDigit(c)) {
                        errorHandler.reportError(lineNumber, "Numbers are not allowed in identifiers or keywords: " + currentToken.toString() + c);
                        currentToken.setLength(0); // Clear token
                        continue; // Move to next character
                    }

                    classifyToken(tokens, currentToken.toString(), currentState, lineNumber);
                    tokenCount++;
                    currentToken.setLength(0);
                }

                currentState = dfa.startState;

                // **Check if a new identifier starts**
                if (Character.isLetter(c)) {
                    currentToken.append(c);
                    currentState = dfa.startState;
                    continue;
                }

                // **Operators are standalone tokens**
                if (operators.contains(String.valueOf(c))) {
                    tokens.add(new TokenInfo("Operator", String.valueOf(c), lineNumber));
                    tokenCount++;
                }
            }
        }

        if (currentToken.length() > 0) {
            classifyToken(tokens, currentToken.toString(), currentState, lineNumber);
            tokenCount++;
        }

        return tokens;
    }

    public void classifyToken(List<TokenInfo> tokens, String token, State finalState, int lineNumber) {
        if (token.isEmpty()) return;
    
        //  If the token contains uppercase letters, report as a full word
        if (!token.equals(token.toLowerCase())) {
            errorHandler.reportError(lineNumber, "Uppercase letters are not allowed: " + token);
            return;
        }

        
    
        //  Recognize keywords, identifiers, numbers, and operators correctly
        if (keywords.contains(token)) {
            tokens.add(new TokenInfo("Keyword", token, lineNumber));
        } else if (token.matches("[a-z]+")) {
            tokens.add(new TokenInfo("Identifier", token, lineNumber));
        } else if (token.matches("[0-9]+")) {
            tokens.add(new TokenInfo("Integer", token, lineNumber));
        } else if (token.matches("[0-9]+\\.[0-9]{1,5}")) {
            tokens.add(new TokenInfo("Decimal", token, lineNumber));
        } else if (operators.contains(token)) {
            tokens.add(new TokenInfo("Operator", token, lineNumber));
        } else {
            tokens.add(new TokenInfo("Error", "Unknown Token: " + token, lineNumber));
        }
    }
    

    public int getTokenCount() {
        return tokenCount;
    }
}
