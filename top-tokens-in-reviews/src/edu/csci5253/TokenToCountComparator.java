package edu.csci5253;

import scala.Serializable;
import scala.Tuple2;

public class TokenToCountComparator implements java.util.Comparator<scala.Tuple2<String, Integer>>, Serializable {
    @Override
    public int compare(Tuple2<String, Integer> tokenToCount1, Tuple2<String, Integer> tokenToCount2) {
        int compareCount = -1 * Integer.compare(tokenToCount1._2, tokenToCount2._2);

        if (compareCount != 0) {
            return compareCount;
        } else {
            return tokenToCount1._1.toLowerCase().compareTo(tokenToCount2._1.toLowerCase());
        }
    }
}
