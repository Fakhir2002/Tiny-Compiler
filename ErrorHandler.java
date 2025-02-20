import java.util.ArrayList;
import java.util.List;

class ErrorHandler {
    private List<String> errors;

    public ErrorHandler() {
        this.errors = new ArrayList<>();
    }

    //  Add a new error with line number
    public void reportError(int lineNumber, String message) {
        errors.add("Line " + lineNumber + ": " + message);
    }

    //  Print all errors
    public void printErrors() {
        if (errors.isEmpty()) {
            System.out.println("\n No errors found!");
        } else {
            System.out.println("\n---  Error Report ---");
            for (String error : errors) {
                System.out.println(error);
            }
        }
    }

    // ✅ Check if errors exist
    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    // ✅ Get total error count
    public int getErrorCount() {
        return errors.size();
    }
}
