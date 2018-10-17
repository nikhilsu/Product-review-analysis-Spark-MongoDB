package edu.csci5253;

import com.mongodb.spark.rdd.api.java.JavaMongoRDD;
import org.apache.spark.api.java.JavaRDD;
import org.bson.Document;
import scala.Serializable;

import java.util.stream.Stream;

import static edu.csci5253.Constants.CollectionKeys;
import static edu.csci5253.Constants.Config;

class ReviewBucketter implements Serializable {
    private final SparkWordCountMapReduce sparkMapReduce;
    private final Stream<Integer> bucketRange;

    ReviewBucketter(SparkWordCountMapReduce sparkMapReduce, Stream<Integer> bucketRange) {
        this.sparkMapReduce = sparkMapReduce;
        this.bucketRange = bucketRange;
    }

    private JavaRDD<String> getReviewTextRDDForBucket(JavaMongoRDD<Document> reviewsRDD, int bucketNumber) {
        return  reviewsRDD.filter(document -> document.getDouble(CollectionKeys.OVERALL) == bucketNumber)
                .map(document -> document.getString(CollectionKeys.REVIEW_TEXT));
    }

    void bucketAndExtractTopNWords(JavaMongoRDD<Document> reviewsRDD) {
        bucketRange.forEach(i -> {
            JavaRDD<String> bucketI = getReviewTextRDDForBucket(reviewsRDD, i);
            WordToCountTuples topNWords = sparkMapReduce.fetchTopNWords(bucketI);
            FileIO.write(topNWords.toString(),  Config.bucketOutputPath(i));
        });
    }
}
