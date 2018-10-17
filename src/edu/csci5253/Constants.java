package edu.csci5253;

class Constants {
    static class Collections {
        static String REVIEWS = "reviews";
        static String METADATA = "metadata";
    }

    static class CollectionKeys {
        static final String A_SIN = "asin";
        static final String TITLE = "title";
        static final String CATEGORIES = "categories";
        static final String CATEGORY = "category";
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
