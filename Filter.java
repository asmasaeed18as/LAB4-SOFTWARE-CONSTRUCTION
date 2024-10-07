/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Filter consists of methods that filter a list of tweets for those matching a
 * condition.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class Filter {

    /**
     * Find tweets written by a particular user.
     * 
     * @param tweets
     *            a list of tweets with distinct ids, not modified by this method.
     * @param username
     *            Twitter username, required to be a valid Twitter username as
     *            defined by Tweet.getAuthor()'s spec.
     * @return all and only the tweets in the list whose author is username,
     *         in the same order as in the input list.
     */
    public static List<Tweet> writtenBy(List<Tweet> tweets, String username) {
        
    	return tweets.stream()
                .filter(tweet -> tweet.getAuthor().equalsIgnoreCase(username))
                .collect(Collectors.toList());
    }

    /**
     * Find tweets that were sent during a particular timespan.
     * 
     * @param tweets
     *            a list of tweets with distinct ids, not modified by this method.
     * @param timespan
     *            timespan
     * @return all and only the tweets in the list that were sent during the timespan,
     *         in the same order as in the input list.
     */
    
    public static List<Tweet> inTimespan(List<Tweet> tweets, Timespan timespan) {
    	return tweets.stream()
                .filter(tweet -> !tweet.getTimestamp().isBefore(timespan.getStart()) 
                              && !tweet.getTimestamp().isAfter(timespan.getEnd()))
                .collect(Collectors.toList());

    }

    /**
     * Find tweets that contain certain words.
     * 
     * @param tweets
     *            a list of tweets with distinct ids, not modified by this method.
     * @param words
     *            a list of words to search for in the tweets. 
     *            A word is a nonempty sequence of nonspace characters.
     * @return all and only the tweets in the list such that the tweet text (when 
     *         represented as a sequence of nonempty words bounded by space characters 
     *         and the ends of the string) includes *at least one* of the words 
     *         found in the words list. Word comparison is not case-sensitive,
     *         so "Obama" is the same as "obama".  The returned tweets are in the
     *         same order as in the input list.
     */
    //VARIANT 1: REGEX 
   /* public static List<Tweet> containing(List<Tweet> tweets, List<String> words) {
        String pattern = "\\b(" + String.join("|", words.stream().map(Pattern::quote).collect(Collectors.toList())) + ")\\b";
        return tweets.stream()
                .filter(tweet -> Pattern.compile(pattern, Pattern.CASE_INSENSITIVE).matcher(tweet.getText()).find())
                .collect(Collectors.toList());
    }*/
    
    
 /*   //variant 2 :  Word Set Search through intersection set
    
    public static List<Tweet> containing(List<Tweet> tweets, List<String> words) {
        Set<String> lowerCaseWords = words.stream()
                                          .map(String::toLowerCase)
                                          .collect(Collectors.toSet());
        
        return tweets.stream()
                .filter(tweet -> {
                    Set<String> tweetWords = Arrays.stream(tweet.getText().split("\\s+"))
                                                   .map(word -> word.replaceAll("\\W", "").toLowerCase())
                                                   .collect(Collectors.toSet());
                    return tweetWords.stream().anyMatch(lowerCaseWords::contains);
                })
                .collect(Collectors.toList());
    }*/
    
 
    
    //Variant3 : Iterative Word Search
    public static List<Tweet> containing(List<Tweet> tweets, List<String> words) {
        // Convert the words list to lowercase for case-insensitive matching
        List<String> lowerCaseWords = words.stream()
                                           .map(String::toLowerCase)
                                           .collect(Collectors.toList());

        return tweets.stream()
                .filter(tweet -> {
                    // Split tweet's text into words (ignoring non-word characters)
                    String[] tweetWords = tweet.getText().toLowerCase().split("\\W+");
                    
                    // Check if any of the tweet words are in the list of words
                    for (String tweetWord : tweetWords) {
                        if (lowerCaseWords.contains(tweetWord)) {
                            return true;
                        }
                    }
                    return false;
                })
                .collect(Collectors.toList());
    }

}
    