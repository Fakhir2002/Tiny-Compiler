import java.util.*;

class SymbolTable {
    private HashMap<String, SymbolTableEntry> table;
    private ErrorHandler errorHandler;

    public SymbolTable(ErrorHandler errorHandler) {
        this.table = new HashMap<>();
        this.errorHandler = errorHandler;
    }

    // ** Add new variable/constant to symbol table **
    public boolean addEntry(String name, String type, String scope, String value, boolean isConstant) {
        if (table.containsKey(name)) {
            errorHandler.reportError(-1, "Duplicate declaration of '" + name + "'");
            return false;
        }
        SymbolTableEntry entry = new SymbolTableEntry(name, type, scope, value, isConstant);
        table.put(name, entry);
        return true;
    }

    // ** Update an existing variable's value (prevent updates for constants) **
    public void updateValue(String name, String newValue, int lineNumber) {
        SymbolTableEntry entry = table.get(name);
        if (entry != null) {
            if (entry.isConstant()) {
                errorHandler.reportError(lineNumber, "Cannot modify constant '" + name + "'");
            } else {
                entry.setValue(newValue);
            }
        } else {
            errorHandler.reportError(lineNumber, "Variable '" + name + "' is not declared before assignment.");
        }
    }

    // ** Lookup a variable or constant **
    public SymbolTableEntry lookup(String name) {
        return table.get(name);
    }

    // ** Print the symbol table **
    public void printTable() {
        System.out.println("\n--- Symbol Table ---");
        System.out.printf("%-15s %-10s %-10s %-15s %-10s\n", "Name", "Type", "Scope", "Value", "Kind");
        System.out.println("-----------------------------------------------------------------");
        for (SymbolTableEntry entry : table.values()) {
            System.out.println(entry);
        }
    }
}
