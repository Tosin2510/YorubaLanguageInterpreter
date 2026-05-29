import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Lexer {
    private final Map<String, Token> dictionary = new HashMap<>();
    private final Map<String, PartOfSpeech> posMap = new HashMap<>();

    public Lexer() {
        posMap.put("noun", PartOfSpeech.NOUN);
        posMap.put("verb", PartOfSpeech.VERB);
        posMap.put("pron", PartOfSpeech.PRONOUN);
        posMap.put("pronoun", PartOfSpeech.PRONOUN);
        posMap.put("adj", PartOfSpeech.ADJECTIVE);
        posMap.put("adjective", PartOfSpeech.ADJECTIVE);
        posMap.put("det", PartOfSpeech.DETERMINER);
        posMap.put("determiner", PartOfSpeech.DETERMINER);
        posMap.put("character", PartOfSpeech.PRONOUN);
    }

    public void loadDataset(String filePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                try {
                    JsonObject jsonObj = JsonParser.parseString(line.trim()).getAsJsonObject();

                    if (!jsonObj.has("word") || !jsonObj.has("pos")) continue;

                    String word = jsonObj.get("word").getAsString().trim();
                    String rawPos = jsonObj.get("pos").getAsString().trim().toLowerCase();
                    if (word.isEmpty()) continue;

                    if (posMap.containsKey(rawPos)) {
                        PartOfSpeech assignedPos = posMap.get(rawPos);
                        String phonetic = extractIPA(jsonObj);
                        // Normalize word: strip off diacritics
                        word = Normalizer.normalize(word.toLowerCase(), Normalizer.Form.NFD)
                                .replaceAll("\\p{M}", "");
                        dictionary.put(word, new Token(word, assignedPos, phonetic));
                    }
                } catch (Exception e) {
                    continue;
                }
            }
        }
    }

    private String extractIPA(JsonObject obj) {
        if (obj.has("sounds")) {
            JsonElement soundsElement = obj.get("sounds");
            if (soundsElement != null && soundsElement.isJsonArray()) {
                JsonArray soundsArray = soundsElement.getAsJsonArray();
                for (JsonElement element : soundsArray) {
                    if (element.isJsonObject()) {
                        JsonObject soundObj = element.getAsJsonObject();
                        if (soundObj.has("ipa")) {
                            return soundObj.get("ipa").getAsString();
                        }
                    }
                }
            }
        }
        return "N/A";
    }

    public List<Token> tokenize(String sentence) {
        List<Token> matchedTokens = new ArrayList<>();
        String[] rawWords = sentence.trim().split("\\s+");

        for (String rawWord : rawWords) {
            String cleanWord = rawWord.replaceAll("[.,?!]", "").toLowerCase();

            if (dictionary.containsKey(cleanWord)) {
                matchedTokens.add(dictionary.get(cleanWord));
            } else {
                System.out.println("\n[LEXICAL ERROR]: The word '" + rawWord + "' is not recognized.");
                System.exit(1);
            }
        }
        return matchedTokens;
    }
}