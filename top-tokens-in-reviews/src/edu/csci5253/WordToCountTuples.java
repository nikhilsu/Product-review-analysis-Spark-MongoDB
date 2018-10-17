package edu.csci5253;

import scala.Tuple2;

import java.util.List;

class WordToCountTuples {
    private final List<Tuple2<String, Integer>> wordToCountTuples;

    WordToCountTuples(List<Tuple2<String, Integer>> wordToCountTuples) {
        this.wordToCountTuples = wordToCountTuples;
    }

    @Override
    public String toString() {
        return wordToCountTuples.stream()
                                .map(wordToCount -> wordToCount._1 + "\t" + wordToCount._2)
                                .reduce((str1, str2) -> str1 + "\n" + str2).orElse("");
    }
}
