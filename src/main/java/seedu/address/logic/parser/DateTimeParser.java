package seedu.address.logic.parser;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import org.ocpsoft.prettytime.PrettyTime;
import org.ocpsoft.prettytime.nlp.PrettyTimeParser;
import org.ocpsoft.prettytime.nlp.parse.DateGroup;

/**
 * For parsing dates and times in Sudowudo command input
 * 
 * @author darren
 */
public class DateTimeParser {
    // the part of the command that contains the temporal part of the command
    private String datetime;

    // natty parser object
    // careful of name collision with our own Parser object
    // static so we only need to initialize it once
    private static PrettyTimeParser parser;

    // prettytime formatter
    private static PrettyTime prettytime;

    // result from parser
    private List<DateGroup> dategroups;
    private List<Date> dates;
    
    public static final String EMPTY_STRING = "";

    public static final DateTimeFormatter ABRIDGED_DATE_FORMAT = DateTimeFormatter.ofPattern("dd MMM");
    public static final DateTimeFormatter EXPLICIT_DATE_FORMAT = DateTimeFormatter.ofPattern("dd MMM yyyy");

    public DateTimeParser(String input) {
        assert input != null;
        assert input.isEmpty() != true;

        this.datetime = input;
        if (DateTimeParser.parser == null) {
            DateTimeParser.parser = new PrettyTimeParser();
        }
        if (DateTimeParser.prettytime == null) {
            DateTimeParser.prettytime = new PrettyTime();
        }

        // perform natty parsing
        this.dategroups = DateTimeParser.parser.parseSyntax(input);
        this.dates = DateTimeParser.parser.parse(input);
    }

    public LocalDateTime extractStartDate() {
        assert this.dates != null;

        if (this.dategroups.isEmpty()) {
            return null;
        }

        return changeDateToLocalDateTime(this.dates.get(0));
    }

    /**
     * Extracts a pretty relative start date
     * 
     * Examples of pretty relative dates: (for future dates) "3 weeks from now",
     * "2 days from now", "12 minutes from now", "moments from now"
     * 
     * (for past dates) "3 weeks ago", "2 days ago", "12 minutes ago", "moments
     * ago"
     * 
     * @return
     * @author darren
     */
    public String extractPrettyRelativeStartDateTime() {
        return extractPrettyRelativeDateTime(0);
    }

    /**
     * Extracts a pretty start date
     * 
     * @return
     * @author darren
     */
    public String extractPrettyStartDateTime() {
        return extractPrettyDateTime(0);
    }

    public LocalDateTime extractEndDate() {
        assert this.dates != null;

        if (this.dates.size() < 2) {
            return extractStartDate();
        }

        return changeDateToLocalDateTime(this.dates.get(1));
    }

    /**
     * Extracts a pretty relative end date
     * 
     * Examples of pretty relative dates: (for future dates) "3 weeks from now",
     * "2 days from now", "12 minutes from now", "moments from now"
     * 
     * (for past dates) "3 weeks ago", "2 days ago", "12 minutes ago", "moments
     * ago"
     * 
     * @return
     * @author darren
     */
    public String extractPrettyRelativeEndDateTime() {
        if (this.dates.size() < 2) {
            return extractPrettyRelativeStartDateTime();
        }
        return extractPrettyRelativeDateTime(1);
    }

    /**
     * Extracts a pretty end date
     * 
     * @return
     * @author darren
     */
    public String extractPrettyEndDateTime() {
        if (this.dates.size() < 2) {
            return extractPrettyStartDateTime();
        }
        return extractPrettyDateTime(1);
    }

    public boolean isRecurring() {
        return this.dategroups.get(0).isRecurring();
    }

    public LocalDateTime getRecurEnd() {
        return changeDateToLocalDateTime(
                this.dategroups.get(0).getRecursUntil());
    }
    
    /**
     * Checks if two given java.time.LocalDateTime objects are
     * of the same day.
     * 
     * @param ldt1
     * @param ldt2
     * @return true if they are both the same day, false otherwise
     * @author darren
     */
    public static boolean isSameDay(LocalDateTime ldt1, LocalDateTime ldt2) {
        return ldt1.toLocalDate().equals(ldt2.toLocalDate());
    }

    /**
     * helper method for casting java.util.Date to java.time.LocalDateTime
     * safely
     * 
     * @param date
     * @return
     * @author darren
     */
    public static LocalDateTime changeDateToLocalDateTime(Date date) {
        Instant instant = date.toInstant().truncatedTo(ChronoUnit.SECONDS); // strip
                                                                            // milliseconds
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    /**
     * Helper method for determining a human-readable relative date from the
     * date tokens in the input string
     * 
     * Note that this is dependent on the local system time, e.g. the output
     * from java.util.Date()
     * 
     * Examples of pretty relative dates: (for future dates) "3 weeks from now",
     * "2 days from now", "12 minutes from now", "moments from now"
     * 
     * (for past dates) "3 weeks ago", "2 days ago", "12 minutes ago", "moments
     * ago"
     * 
     * @param index
     *            index of target java.util.Date inside DateTimeParser's
     *            List<Date>
     * @return pretty relative date
     * @author darren
     */
    private String extractPrettyRelativeDateTime(int index) {
        assert this.dates != null;
        return prettytime.format(this.dates.get(index));
    }

    /**
     * Helper method for determining a human-readable pretty date from date
     * tokens in the input string
     * 
     * Examples: "This Monday, 6:30AM", "Next Saturday, 12:37PM",
     * "Mon 27 November, 9:30PM"
     * 
     * @param index
     * @return pretty date for this week
     */
    private String extractPrettyDateTime(int index) {
        assert this.dates != null;

        LocalDateTime ldt = changeDateToLocalDateTime(this.dates.get(index));

        // convert to 12h time from 24h
        int hour = ldt.getHour();
        String meridian = computeMeridian(hour);
        if (hour > 12) {
            hour = hour % 12;
        }
        
        String dayOfWeek = toTitleCase(ldt.getDayOfWeek().toString());
        String dayOfWeekShort = dayOfWeek.substring(0, 3);
        String minute = String.format("%02d", ldt.getMinute());

        // add relative prefix (this/next <day of week>) if applicable
        if(computeDaysTo(ldt) < 14) {
            return makeRelativePrefix(ldt) + dayOfWeek + ", " + hour + ":"
                    + minute + meridian;
        }

        // explicit date; no relative prefix
        String prettyDateTime;
        if(computeDaysTo(ldt) < 365) {
            prettyDateTime = ldt.toLocalDate().format(ABRIDGED_DATE_FORMAT);
        } else {
            prettyDateTime = ldt.toLocalDate().format(EXPLICIT_DATE_FORMAT);
        }
        return dayOfWeekShort + " " + prettyDateTime + ", " + hour + ":"
                + minute + meridian;
    }

    /**
     * Determine the appropriate relative prefix to use for reference to a DayOfWeek enum
     * 
     * @param ldt
     * @return
     * @author darren
     */
    private static String makeRelativePrefix(LocalDateTime ldt) {
        if (computeDaysTo(ldt) < 7) {
            return "This ";
        } else if (computeDaysTo(ldt) < 14) {
            return "Next ";
        }
        return EMPTY_STRING;
    }
    
    /**
     * Computes number of days between current system time to the given
     * java.time.LocalDateTime
     * 
     * @param ldt
     *            future LocalDateTime
     * @return number of days between now to future LocalDateTime
     * @author darren
     */
    public static long computeDaysTo(LocalDateTime ldt) {
        assert ldt.isAfter(LocalDateTime.now());
        return ChronoUnit.DAYS.between(LocalDate.now(), ldt.toLocalDate());
    }

    /**
     * Returns the meridian of a given hour
     * 
     * Examples: Returns "PM" if given hour is 23 (11PM) Returns "AM" if given
     * hour is 11 (11AM)
     * 
     * @param hour
     *            integer hour in 24h format
     * @return meridian of the hour
     * @author darren
     */
    public static String computeMeridian(int hour) {
        if (hour > 12) {
            return "PM";
        }
        return "AM";
    }

    /**
     * Transforms a String into a title-cased string.
     * 
     * The first letter of the string will be uppercase while every letter after
     * will be lowercase.
     * 
     * @param string
     *            string to be transformed
     * @return s in title case
     * @author darren
     */
    public static String toTitleCase(String string) {
        return string.substring(0, 1).toUpperCase()
                + string.substring(1).toLowerCase();
    }

    public DateGroup getDateGroup(int index) {
        return this.dategroups.get(index);
    }

    public String getDateTime() {
        return this.datetime;
    }

}
