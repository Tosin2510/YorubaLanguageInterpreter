import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // 1. Force the console output stream to use standard UTF-8 bytes
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));

        System.out.println("    YORUBA INTERPRETER v1.0    ");

        Lexer lexer = new Lexer();
        try {
            lexer.loadDataset("kaikki.org-dictionary-Yoruba.jsonl");
            
            Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);
            System.out.print("\nEnter Yoruba sentence: ");
            String input = scanner.nextLine();
            
            List<Token> tokens = lexer.tokenize(input);
            System.out.println("\nLexical Analysis Passed!");
            System.out.println("\n--- PHONETIC PRONUNCIATION ---");
            
            // 2. SAFE PRINTING LOOP
            for (Token t : tokens) {
                // Safely clean up the text ONLY for display purposes
                String displayWord = safeTerminalString(t.getWord());
                String displayPhonetic = safeTerminalString(t.getPhonetic());
                
                System.out.println("  " + displayWord + " -> " + displayPhonetic);
            }
            scanner.close();
            
        } catch (Exception e) {
            System.out.println("\n[ERROR]: " + e.getMessage());
        }
    }

    // Helper method to keep your console completely clear of ß╗ì symbols
    private static String safeTerminalString(String input) {
        if (input == null) return "";
        // Separate letters from accents
        String decomposed = Normalizer.normalize(input, Normalizer.Form.NFD);
        // Remove diacritical marks
        String noDiacritics = decomposed.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        // Only allow basic terminal-safe characters to print on your Windows screen
        return noDiacritics.replaceAll("[^a-zA-Z\\s\\.]", "").toLowerCase().trim();
    }
}