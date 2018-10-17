## Top tokens in product reviews
Categorizing each product review based on the overall rating of a product and finding the top 2000 words(tokens) in each of these categories(buckets)! Extensively using Java Streams and Lamdbas.

- Product reviews are categorized into 5 buckets using the overall rating integer!
- Each review is then cleaned by removing stop-words (data/stop_words.txt) and non-alphabetical characters!
- Word count analysis is then performed and top 2000 words are chosed for each bucket!

### Build and run:-
```
mvn clean install package
bash run.sh
```

#### Ouputs in Bucket1, Bucket2... Bucket5 directories. 
