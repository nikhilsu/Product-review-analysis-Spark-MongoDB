#!/usr/bin/env bash

spark-submit --packages org.mongodb.spark:mongo-spark-connector_2.11:2.3.0 --class edu.csci5253.SparkMongoDriver highestRatedReview.jar