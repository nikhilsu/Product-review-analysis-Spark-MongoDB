package edu.csci5253;

class Constants {
    static class Config {
        static final int TOP_N = 500;
        static final int NUMBER_OF_BUCKETS = 5;
        static final String STOP_WORDS_PATH = "data/stop_words.txt";

        static String bucketOutputPath(int bucketNumber) {
            String bucketDirPrefix = "Bucket";
            String outputFileName = "output.txt";
            return bucketDirPrefix + bucketNumber + "/" + outputFileName;
        }
    }
    static class Collections {
        static String REVIEWS = "reviews";
    }

    static class CollectionKeys {
        static final String OVERALL = "overall";
        static final String REVIEW_TEXT = "reviewText";
    }

    static String defaultMongoURI() {
        // TODO: Move to env variable
        String username = "student";
        String password = "student";
        String SERVER = "ec2-54-210-44-189.compute-1.amazonaws.com";
        String DATABASE = "test";
        return "mongodb://" + username + ":" + password + "@" + SERVER + "/" + DATABASE + "." + Collections.REVIEWS;
    }
}
