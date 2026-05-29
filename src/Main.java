import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Force the console output stream to use standard UTF-8 bytes
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

            for (Token t : tokens) {
                String displayWord = t.getWord();
                String displayPhonetic = t.getPhonetic();

                System.out.println("  " + displayWord + " -> " + displayPhonetic);
            }
            scanner.close();

        } catch (Exception e) {
            System.out.println("\n[ERROR]: " + e.getMessage());
        }
    }
}