import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parser {
    private final List<List<PartOfSpeech>> allowedRules = new ArrayList<>();

    public Parser() {
        // Subject-Verb-Object (SVO) Variations
        allowedRules.add(Arrays.asList(PartOfSpeech.PRONOUN, PartOfSpeech.VERB, PartOfSpeech.NOUN));
        allowedRules.add(Arrays.asList(PartOfSpeech.PRONOUN, PartOfSpeech.VERB, PartOfSpeech.NOUN, PartOfSpeech.ADJECTIVE));
        allowedRules.add(Arrays.asList(PartOfSpeech.PRONOUN, PartOfSpeech.VERB, PartOfSpeech.NOUN, PartOfSpeech.DETERMINER));
        allowedRules.add(Arrays.asList(PartOfSpeech.NOUN, PartOfSpeech.VERB, PartOfSpeech.NOUN));

        // Determiner & Noun Sequences
        allowedRules.add(Arrays.asList(PartOfSpeech.DETERMINER, PartOfSpeech.NOUN, PartOfSpeech.VERB, PartOfSpeech.NOUN));
        allowedRules.add(Arrays.asList(PartOfSpeech.PRONOUN, PartOfSpeech.VERB, PartOfSpeech.DETERMINER, PartOfSpeech.NOUN));
    }

    public void validate(List<Token> tokens) {
        List<PartOfSpeech> inputSequence = new ArrayList<>();
        for (Token t : tokens) {
            inputSequence.add(t.getPos());
        }

        if (allowedRules.contains(inputSequence)) {
            return;
        }

        System.out.println("\n[SYNTAX ERROR]: Invalid sentence structure.");
        System.out.print("Detected Word Sequence: ");
        for (int i = 0; i < inputSequence.size(); i++) {
            System.out.print(inputSequence.get(i));
            if (i < inputSequence.size() - 1) System.out.print(" -> ");
        }
        System.out.println("\nThis exact structural layout violates the grammatical constraints.");
        System.exit(1);
    }
}