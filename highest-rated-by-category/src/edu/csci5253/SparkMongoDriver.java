package edu.csci5253;

import com.mongodb.spark.rdd.api.java.JavaMongoRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;
import org.bson.Document;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import static edu.csci5253.Constants.CollectionKeys;
import static edu.csci5253.Constants.Collections;


public class SparkMongoDriver {
    public static void main(String[] args) throws IOException {

        SparkSession spark = SparkSession.builder()
                .appName("SparkMongoConnector")
                .config("spark.mongodb.input.uri", Constants.defaultMongoURI())
                .config("spark.mongodb.output.uri", Constants.defaultMongoURI())
                .getOrCreate();

        JavaSparkContext sparkContext = new JavaSparkContext(spark.sparkContext());

        MongoCollectionRDDFactory mongoRDDFactory = new MongoCollectionRDDFactory(new HashMap<>());

        JavaMongoRDD<Document> reviewsRDD = mongoRDDFactory.buildCollectionRDD(sparkContext, Collections.REVIEWS);
        JavaMongoRDD<Document> metadataRDD = mongoRDDFactory.buildCollectionRDD(sparkContext, Collections.METADATA);
        Dataset<Row> reviewsDF = reviewsRDD.toDF();
        Dataset<Row> metadataDF = metadataRDD.toDF();

        Dataset<Row> shortMetadataDF = metadataDF.select(metadataDF.col(CollectionKeys.A_SIN),
                metadataDF.col(CollectionKeys.TITLE),
                functions.explode(metadataDF.col(CollectionKeys.CATEGORIES)).as(CollectionKeys.CATEGORY));

        reviewsDF.createOrReplaceTempView("reviews");
        shortMetadataDF.createOrReplaceTempView("shortMetadata");

        Dataset<Row> itemsWithAvgRatingPerCategory = spark.sql("SELECT reviews.asin as asin, shortMetadata.category as category, " +
                "COUNT(1) as reviewCount, AVG(reviews.overall) as avgRating " +
                "FROM reviews " +
                "INNER JOIN shortMetadata ON reviews.asin = shortMetadata.asin " +
                "WHERE shortMetadata.category IN ('Movies & TV', 'CDs & Vinyl', 'Video Games', 'Toys & Games')" +
                "GROUP BY reviews.asin, shortMetadata.category " +
                "HAVING COUNT(1) >= 100");
        itemsWithAvgRatingPerCategory.createOrReplaceTempView("AvgRatingPerCategory");

        Dataset<Row> highestRatedItemPerCategory = spark.sql("SELECT category, MAX(avgRating) as highestRating " +
                "FROM AvgRatingPerCategory " +
                "GROUP BY category");
        highestRatedItemPerCategory.createOrReplaceTempView("HighestRatedItemPerCategory");

        metadataDF.createOrReplaceTempView("metadata");
        Dataset<Row> output = spark.sql("SELECT hr.category, metadata.title, ar.reviewCount, hr.highestRating " +
                "FROM AvgRatingPerCategory ar " +
                "JOIN HighestRatedItemPerCategory hr " +
                "ON ar.category = hr.category " +
                "AND ar.avgRating = hr.highestRating " +
                "JOIN metadata " +
                "ON metadata.asin = ar.asin " +
                "ORDER BY hr.category, ar.reviewCount");

        List<String> lines = output.javaRDD().map(row -> {
            String category = row.getString(0);
            String title = row.getString(1);
            Long reviewCount = row.getLong(2);
            double avgRating = row.getDouble(3);

            return String.format("%s\t%s\t%d\t%f", category, title, reviewCount, avgRating);
        }).collect();

        Path file = Paths.get("output.txt");
        Files.write(file, lines);

        sparkContext.close();
    }
}
