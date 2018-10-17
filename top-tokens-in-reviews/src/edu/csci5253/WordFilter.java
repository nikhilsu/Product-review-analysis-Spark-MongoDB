package edu.csci5253;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;

class WordFilter implements Serializable {
    private final HashSet<String> stopWords;

    WordFilter(List<String> stopWords) {
        this.stopWords = new HashSet<>();
        stopWords.forEach(word -> {
            String onlyAlphas = getOnlyAlphas(word);
            if (!onlyAlphas.isEmpty())
                this.stopWords.add(onlyAlphas.toLowerCase());
        });
    }

    String denoise(String word) {
        String alphaOnlyChars = getOnlyAlphas(word).toLowerCase();
        return alphaOnlyChars.isEmpty() || this.stopWords.contains(alphaOnlyChars) ? null : alphaOnlyChars;
    }

    private String getOnlyAlphas(String word) {
        return word.replaceAll("[^a-zA-Z]", "");
    }
}
