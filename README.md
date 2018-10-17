# Product Review Analysis Using Spark-Mongo integration.
Performing different types of product review analysis on [Amazon dataset](http://jmcauley.ucsd.edu/data/amazon/links.html) using Apache Spark and MongoDB.

### Part 1 - Highest review product in each category
Given the 5-core dataset, using Spark SQL to join collections and find the highest rated product in each category!

### Part 2 - Top tokens in product reviews
Categorizing each product review based on the overall rating of the product and then find the top 2000 words(tokens) in each of these categories(buckets)! Extensively using Java 8 stream and Lamdba.

### Dependencies :-
1. Java 8
2. Spark 2.3.1
3. MongoDB 4.0.2

#### Using AWS-EMR for spark cluster set-up
