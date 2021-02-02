package com.importer.component.custom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InputContentMatcher {


        // First 26 Primes for corresponding Alphabet letters
        private static final int[] primeNumbers = new int[] { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59,
                61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113 };


        public static Map<Long, List<String>> findMatches(List<String> words) {
            Map<Long, List<String>> result = new HashMap<>();

            for (String word : words) {
                Long product = calculateProduct(word.toUpperCase().toCharArray());

                List<String> matchedElement = result.get(product);
                if (matchedElement == null) {
                    matchedElement = new ArrayList<>();

                    result.put(product, matchedElement);
                }
                matchedElement.add(word);
            }
            return result;
        }

        private static long calculateProduct(char[] letters) {
            long res = 1;
            for (char c : letters) {
                if (c < 'A' || c > 'Z') {
                    return -1;
                }
                int index = c - 'A';
                res = res * primeNumbers[index];
            }
            return res;
        }
    }

