package edu.csci5253;

import com.mongodb.spark.rdd.api.java.JavaMongoRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.bson.Document;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static edu.csci5253.Constants.Collections;
import static edu.csci5253.Constants.Config;

public class SparkMongoDriver {
    public static void main(String[] args) throws FileNotFoundException {
        SparkSession spark = SparkSession.builder()
                .appName("SparkMongoConnector")
                .config("spark.mongodb.input.uri", Constants.defaultMongoURI())
                .config("spark.mongodb.output.uri", Constants.defaultMongoURI())
                .getOrCreate();

        JavaSparkContext sparkContext = new JavaSparkContext(spark.sparkContext());

        MongoCollectionRDDFactory mongoRDDFactory = new MongoCollectionRDDFactory(new HashMap<>());
        JavaMongoRDD<Document> reviewsRDD = mongoRDDFactory.buildCollectionRDD(sparkContext, Collections.REVIEWS);

        List<String> stopWordsList = FileIO.readLines(Config.STOP_WORDS_PATH);
        if (stopWordsList == null) {
            throw new FileNotFoundException("Stop words file not found");
        }
        WordFilter wordFilter = new WordFilter(stopWordsList);
        SparkWordCountMapReduce sparkMapReduce = new SparkWordCountMapReduce(wordFilter);

        Stream<Integer> bucketRange = IntStream.rangeClosed(1, Config.NUMBER_OF_BUCKETS).boxed();
        ReviewBucketter reviewBucketter = new ReviewBucketter(sparkMapReduce, bucketRange);
        reviewBucketter.bucketAndExtractTopNWords(reviewsRDD);

        spark.close();
    }
}
