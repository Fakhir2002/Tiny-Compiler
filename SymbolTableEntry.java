class SymbolTableEntry {
    private String name;
    private String type;
    private String scope;
    private String value;
    private boolean isConstant;  // NEW: Flag for constants

    public SymbolTableEntry(String name, String type, String scope, String value, boolean isConstant) {
        this.name = name;
        this.type = type;
        this.scope = scope;
        this.value = value;
        this.isConstant = isConstant;
    }

    // Getters
    public String getName() { return name; }
    public String getType() { return type; }
    public String getScope() { return scope; }
    public String getValue() { return value; }
    public boolean isConstant() { return isConstant; }

    // Setter for value (only if not a constant)
    public void setValue(String newValue) {
        if (!isConstant) {
            this.value = newValue;
        } else {
            System.out.println("Error: Cannot modify constant " + name);
        }
    }

    @Override
    public String toString() {
        return String.format("%-15s %-10s %-10s %-15s %-10s", 
            name, type, scope, value, (isConstant ? "Constant" : "Variable"));
    }
}
