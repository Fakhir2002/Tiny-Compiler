class TokenInfo {
    private String type;  // Example: "Keyword", "Identifier", "Integer"
    private String value; // Actual token value
    private int lineNumber;

    public TokenInfo(String type, String value, int lineNumber) {
        this.type = type;
        this.value = value;
        this.lineNumber = lineNumber;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    @Override
    public String toString() {
        return type + ": " + value + " (Line " + lineNumber + ")";
    }
}
