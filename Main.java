import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Read the source code from input.chikni
        String inputCode = readFile("input.urdu");

        if (inputCode == null) {
            System.out.println("Error: Could not read input.urdu file.");
            return;
        }

        ErrorHandler errorHandler = new ErrorHandler(); // Initialize error handler

        SymbolTable symbolTable = new SymbolTable(errorHandler); // Initialize Symbol Table

        // Define regex patterns for different token types
        String identifiers = "[a-z]+";
        String integers = "[0-9]+";
        String ops = "\\+|/|-|\\*|%|\\^|=";
        String singlelinecomment = "~.*";
        String multilinecomment = "~~.*~~";
        String decimal = "[0-9]+\\.[0-9]{1,5}";

        // Convert regex to NFA
        RegexToNFA converter = new RegexToNFA();
        NFA nfa1 = converter.createNFAFromRegex(identifiers);
        NFA nfa2 = converter.createNFAFromRegex(integers);
        NFA nfa3 = converter.createNFAFromRegex(ops);
        NFA nfa4 = converter.createNFAFromRegex(singlelinecomment);
        NFA nfa5 = converter.createNFAFromRegex(multilinecomment);
        NFA nfa6 = converter.createNFAFromRegex(decimal);

        //nfa1.printTransitionTable("identifiers");
        //nfa2.printTransitionTable("integers");
        //nfa3.printTransitionTable("operators");
        //nfa4.printTransitionTable("singlelinecomment");
        //nfa5.printTransitionTable("multilinecomment");
        //nfa6.printTransitionTable("decimal");

        // Convert NFA to DFA
        NFAtoDFA nfaToDfaConverter = new NFAtoDFA();
        DFA dfa1 = nfaToDfaConverter.convertNFAtoDFA(nfa1);
        DFA dfa2 = nfaToDfaConverter.convertNFAtoDFA(nfa2);
        DFA dfa3 = nfaToDfaConverter.convertNFAtoDFA(nfa3);
        DFA dfa4 = nfaToDfaConverter.convertNFAtoDFA(nfa4);
        DFA dfa5 = nfaToDfaConverter.convertNFAtoDFA(nfa5);
        DFA dfa6 = nfaToDfaConverter.convertNFAtoDFA(nfa6);

        //dfa1.printTransitionTable("identifiers");
        //dfa2.printTransitionTable("integers");
        //dfa3.printTransitionTable("operators");
        //dfa4.printTransitionTable("singlelinecomment");
        //dfa5.printTransitionTable("multilinecomment");
        //dfa6.printTransitionTable("decimal");

        // Merge DFAs into one
        MergedDFA mergedDfaConverter = new MergedDFA();
        List<DFA> dfaList = List.of(dfa1, dfa2, dfa3, dfa4, dfa5, dfa6);
        DFA mergedDFA = mergedDfaConverter.mergeDFA(dfaList);

        // Print the final merged DFA transition table
        mergedDFA.printTransitionTable("MERGED TABLE");

        // Tokenize the input code from file
        DFAScanner scanner = new DFAScanner(mergedDFA, errorHandler);
        List<TokenInfo> tokens = scanner.tokenize(inputCode);

        // Print tokens
        for (TokenInfo token : tokens) {
            System.out.println(token);
        }

        // Print total token count
        System.out.println("\nTotal Tokens Found: " + scanner.getTokenCount());

        // Process Symbol Table
        SymbolTableProcessor symbolProcessor = new SymbolTableProcessor(symbolTable,errorHandler);
        symbolProcessor.processTokens(tokens);

        // Print Symbol Table
        symbolTable.printTable();

        // Print Errors (if any)
        errorHandler.printErrors();
    }

    // Function to read a file and return its content as a string
    private static String readFile(String filename) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + filename);
            return null;
        }
        return content.toString();
    }
}
