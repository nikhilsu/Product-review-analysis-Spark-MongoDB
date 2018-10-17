package edu.csci5253;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import scala.Serializable;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Objects;

import static edu.csci5253.Constants.Config;

class SparkWordCountMapReduce implements Serializable {
    private final WordFilter wordFilter;
    private String SPLIT_REGEX = "[ \t\n\r\f]";

    SparkWordCountMapReduce(WordFilter wordFilter) {
        this.wordFilter = wordFilter;
    }

    private JavaPairRDD<String, Integer> reduceWordToCount(JavaPairRDD<String, Integer> wordToOneRDD) {
        return wordToOneRDD.reduceByKey((count1, count2) -> count1 + count2)
                           .coalesce(1);
    }

    private JavaPairRDD<String, Integer> mapWordsToOne(JavaRDD<String> textFile) {
        JavaRDD<String> wordsRDD = textFile.flatMap((FlatMapFunction<String, String>) line ->
                                                    Arrays.asList(line.split(SPLIT_REGEX)).iterator())
                                           .map(wordFilter::denoise)
                                           .filter(Objects::nonNull);

        return wordsRDD.mapToPair(token -> new Tuple2<>(token, 1));
    }

    WordToCountTuples fetchTopNWords(JavaRDD<String> textFile) {
        JavaPairRDD<String, Integer> wordToOneRDD = mapWordsToOne(textFile);
        JavaPairRDD<String, Integer> wordToCountRDD = reduceWordToCount(wordToOneRDD);
        return new WordToCountTuples(wordToCountRDD.takeOrdered(Config.TOP_N, new TokenToCountComparator()));
    }
}
