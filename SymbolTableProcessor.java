import java.util.List;

class SymbolTableProcessor {
    private SymbolTable symbolTable;
    private ErrorHandler errorHandler;
    private String lastIdentifier = null; 
    private boolean awaitingAssignment = false;
    private String lastDeclaredType = null; 
    private boolean isConstant = false;
    private String currentScope = "Global"; //  Default scope is Global

    public SymbolTableProcessor(SymbolTable symbolTable, ErrorHandler errorHandler) {
        this.symbolTable = symbolTable;
        this.errorHandler = errorHandler;
    }

    public void processTokens(List<TokenInfo> tokens) {
        for (int i = 0; i < tokens.size(); i++) {
            TokenInfo token = tokens.get(i);
            String type = token.getType();
            String value = token.getValue();
            int lineNumber = token.getLineNumber();

            if (type.equals("Keyword")) {
                switch (value) {
                    case "adad":
                        lastDeclaredType = "Integer";
                        break;
                    case "nukta":
                        lastDeclaredType = "Decimal";
                        break;
                    case "sachai":
                        lastDeclaredType = "Boolean";
                        break;
                    case "harf":
                        lastDeclaredType = "Character";
                        break;
                    case "jumla":
                        lastDeclaredType = "String";
                        break;
                    case "duniyawala":
                        currentScope = "Global"; //  Assign Global Scope
                        break;
                    case "gharwala":
                        currentScope = "Local"; //  Assign Local Scope
                        break;
                    case "muqarrar": 
                        isConstant = true; //  Mark as constant
                        break;
                    case "sahi":
                    case "jhoot":
                        if (awaitingAssignment && lastIdentifier != null) {
                            String booleanValue = value.equals("sahi") ? "1" : "0"; 
                            symbolTable.updateValue(lastIdentifier, booleanValue, lineNumber);
                            lastIdentifier = null;
                            awaitingAssignment = false;
                        } else {
                            errorHandler.reportError(lineNumber, "Boolean value assigned without a declaration.");
                        }
                        break;
                }

            } else if (type.equals("Identifier")) {
                if (lastDeclaredType != null) {
                    if (isConstant && (i + 2 < tokens.size()) && tokens.get(i + 1).getValue().equals("=")) {
                        TokenInfo nextValue = tokens.get(i + 2);
                        if (nextValue.getType().equals("Integer") || nextValue.getType().equals("Decimal") ||
                            nextValue.getType().equals("Character") || nextValue.getType().equals("String") ||
                            nextValue.getType().equals("Boolean")) {
                            
                            symbolTable.addEntry(value, lastDeclaredType, currentScope, nextValue.getValue(), isConstant);
                            i += 2; 
                        } else {
                            errorHandler.reportError(lineNumber, "Constants must be assigned a valid value at declaration.");
                        }
                    } else {
                        symbolTable.addEntry(value, lastDeclaredType, currentScope, "", isConstant);
                    }
                    lastIdentifier = value;
                    lastDeclaredType = null;
                    isConstant = false;
                } else {
                    lastIdentifier = value;
                }
                awaitingAssignment = false;

            } else if (type.equals("Operator") && value.equals("=")) {
                if (lastIdentifier == null) {
                    errorHandler.reportError(lineNumber, "Assignment without a valid identifier.");
                }
                awaitingAssignment = true;

            } else if (awaitingAssignment) {
                if (type.equals("Integer") || type.equals("Decimal") || type.equals("Character") || type.equals("String") || type.equals("Boolean")) {
                    if (lastIdentifier != null) {
                        if (symbolTable.lookup(lastIdentifier).isConstant()) {
                            errorHandler.reportError(lineNumber, "Cannot modify constant '" + lastIdentifier + "'");
                        } else {
                            symbolTable.updateValue(lastIdentifier, value, lineNumber);
                        }
                        lastIdentifier = null;
                    } else {
                        errorHandler.reportError(lineNumber, "Assignment without a declared variable.");
                    }
                }
                awaitingAssignment = false;
            }
        }
    }
}
