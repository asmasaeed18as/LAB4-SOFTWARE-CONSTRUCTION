/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Extract consists of methods that extract information from a list of tweets.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class Extract {

    /**
     * Get the time period spanned by tweets.
     * 
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @return a minimum-length time interval that contains the timestamp of
     *         every tweet in the list.
     */
	// VARIANT 3 :  using streams API and MIN MAX functions 
    public static Timespan getTimespan(List<Tweet> tweets) {
    	if (tweets.isEmpty()) {
            Instant now = Instant.now();
            return new Timespan(now, now);
        }

    	// Use streams to find the earliest and latest timestamps
        Optional<Instant> earliest = tweets.stream()
                                           .map(Tweet::getTimestamp)
                                           .min(Instant::compareTo);
        Optional<Instant> latest = tweets.stream()
                                         .map(Tweet::getTimestamp)
                                         .max(Instant::compareTo);

        if (earliest.isPresent() && latest.isPresent()) {
            return new Timespan(earliest.get(), latest.get());
        } else {
            throw new IllegalArgumentException("Error in determining timespan");
        }
    }

    	



    /**
     * Get usernames mentioned in a list of tweets.
     * 
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @return the set of usernames who are mentioned in the text of the tweets.
     *         A username-mention is "@" followed by a Twitter username (as
     *         defined by Tweet.getAuthor()'s spec).
     *         The username-mention cannot be immediately preceded or followed by any
     *         character valid in a Twitter username.
     *         For this reason, an email address like bitdiddle@mit.edu does NOT 
     *         contain a mention of the username mit.
     *         Twitter usernames are case-insensitive, and the returned set may
     *         include a username at most once.
     */
    public static Set<String> getMentionedUsers(List<Tweet> tweets) {
    	 Set<String> mentionedUsers = new HashSet<>();
         Pattern mentionPattern = Pattern.compile("(?<!\\w)@(\\w+)", Pattern.CASE_INSENSITIVE); // matches @username

         for (Tweet tweet : tweets) {
             String text = tweet.getText();
             Matcher matcher = mentionPattern.matcher(text);

             while (matcher.find()) {
                 String mentionedUser = matcher.group(1).toLowerCase(); // Twitter usernames are case-insensitive
                 mentionedUsers.add(mentionedUser);
             }
         }

         return mentionedUsers;
    }

}
