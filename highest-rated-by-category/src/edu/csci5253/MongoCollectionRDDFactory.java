package edu.csci5253;

import com.mongodb.spark.MongoSpark;
import com.mongodb.spark.config.ReadConfig;
import com.mongodb.spark.rdd.api.java.JavaMongoRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.bson.Document;

import java.io.Serializable;
import java.util.Map;

class MongoCollectionRDDFactory implements Serializable {
    private final Map<String, String> readOverrides;

    MongoCollectionRDDFactory(Map<String, String> readOverrides) {
        this.readOverrides = readOverrides;
        this.readOverrides.put("readPreference.name", "secondaryPreferred");
    }

    JavaMongoRDD<Document> buildCollectionRDD(JavaSparkContext sparkContext, String collection){
        readOverrides.put("collection", collection);
        ReadConfig metadataReadConfig = ReadConfig.create(sparkContext).withOptions(readOverrides);
        return MongoSpark.load(sparkContext, metadataReadConfig);
    }
}
