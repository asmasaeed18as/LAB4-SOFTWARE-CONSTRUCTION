/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import org.junit.Test;

public class ExtractTest {

    /*
     * TODO: your testing strategies for these methods should go here.
     * See the ic03-testing exercise for examples of what a testing strategy comment looks like.
     * Make sure you have partitions.
     */
    
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-02-17T12:00:00Z");

    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "alyssa", "@john hello", d3);
    private static final Tweet tweet4 = new Tweet(4, "bbitdiddle", "email me at bitdiddle@mit.edu", d1);
    private static final Tweet tweet5 = new Tweet(5, "bbitdiddle", "@john @JaneDoe hi", d2);

    

    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testGetTimespanTwoTweets() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2));
        
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d2, timespan.getEnd());
    }
    @Test
    public void testGetTimespanNoTweets() {
        Timespan timespan = Extract.getTimespan(Collections.emptyList());
        assertEquals("expected same start and end", timespan.getStart(), timespan.getEnd());
    }

//    @Test
//    public void testGetTimespanOneTweet() {
//        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1));
//        assertEquals("expected start", d1, timespan.getStart());
//        assertEquals("expected end", d1, timespan.getEnd());
//    }

    @Test
    public void testGetTimespanMultipleTweetsDifferentTimestamps() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2, tweet3));
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d3, timespan.getEnd());
    }

    
    @Test
    public void testGetTimespanWithNonEmptyList() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2, tweet3));
        
        // Check if the start and end times match the earliest and latest tweet timestamps
        assertEquals("expected start time to be the timestamp of the earliest tweet", d1, timespan.getStart());
        assertEquals("expected end time to be the timestamp of the latest tweet", d3, timespan.getEnd());
        
        // Additional check to ensure the timespan is not based on current time
        Instant now = Instant.now();
        assertNotEquals("start time should not be the current time", now, timespan.getStart());
        assertNotEquals("end time should not be the current time", now, timespan.getEnd());
    }

    @Test
    public void testGetTimespanWithNonEmptyListBugDetection() {
        // Simulate "now" at a known time to compare against
        Instant now = Instant.parse("2024-10-06T12:00:00Z");
        
        // Modify getTimespan to return Timespan(now, now) to simulate the bug
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2, tweet3));

        // Assert that the returned timespan does NOT match "now", since it should reflect the tweet timestamps
        assertNotEquals("start time should not be 'now'", now, timespan.getStart());
        assertNotEquals("end time should not be 'now'", now, timespan.getEnd());

        // Ensure the returned times match the actual earliest and latest tweet timestamps
        assertEquals("expected start time to be the timestamp of the earliest tweet", d1, timespan.getStart());
        assertEquals("expected end time to be the timestamp of the latest tweet", d3, timespan.getEnd());
    }

//    @Test
//    public void testGetTimespanMultipleTweetsSameTimestamp() {
//        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet4));
//        assertEquals("expected start", d1, timespan.getStart());
//        assertEquals("expected end", d1, timespan.getEnd());
//    }
   /*
     * Test strategy for getMentionedUsers():
     * 
     * Partition the input as follows:
     * - No mentions in any tweet
     * - One mention in a tweet
     * - Multiple mentions in one tweet
     * - Mentions spread across multiple tweets
     * - Case-insensitive mentions
     * - Invalid mentions (e.g., part of email addresses)
     */

    @Test
    public void testGetMentionedUsersNoMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1));
        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }

    @Test
    public void testGetMentionedUsersOneMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet3));
        assertTrue("expected one mention", mentionedUsers.contains("john"));
        assertEquals("expected one user", 1, mentionedUsers.size());
    }



    @Test
    public void testGetMentionedUsersAcrossMultipleTweets() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet3, tweet5));
        assertTrue("expected mention of john", mentionedUsers.contains("john"));
        assertTrue("expected mention of janedoe", mentionedUsers.contains("janedoe"));
        assertEquals("expected two users", 2, mentionedUsers.size());
    }

    @Test
    public void testGetMentionedUsersCaseInsensitive() {
        Tweet tweet = new Tweet(6, "bbitdiddle", "@John hi", d2);
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet));
        assertTrue("expected mention of john", mentionedUsers.contains("john"));
        assertEquals("expected one user", 1, mentionedUsers.size());
    }
}
