package seedu.address.logic.parser;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
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

    // PrettyTimeParser object
    // careful of name collision with our own Parser object
    private static PrettyTimeParser parser = new PrettyTimeParser();

    // PrettyTime formatter
    private static PrettyTime prettytime = new PrettyTime();

    // result from parser
    private List<DateGroup> dategroups;
    private List<Date> dates;

    public static final String EMPTY_STRING = "";
    public static final String SINGLE_WHITESPACE = " ";

    // handy strings for making pretty dates
    public static final String TODAY_DATE_REF = "Today";
    public static final String TOMORROW_DATE_REF = "Tomorrow";
    public static final String NEXT_WEEK_REF = "Next" + SINGLE_WHITESPACE;
    public static final String THIS_WEEK_REF = "This" + SINGLE_WHITESPACE;
    public static final String PRETTY_COMMA_DELIMITER = "," + SINGLE_WHITESPACE;
    public static final String PRETTY_TO_DELIMITER = SINGLE_WHITESPACE + "-"
            + SINGLE_WHITESPACE;

    // DateTime formatting patterns
    public static final DateTimeFormatter ABRIDGED_DATE_FORMAT = DateTimeFormatter
            .ofPattern("dd MMM");
    public static final DateTimeFormatter EXPLICIT_DATE_FORMAT = DateTimeFormatter
            .ofPattern("dd MMM yyyy");
    public static final DateTimeFormatter TWELVE_HOUR_TIME = DateTimeFormatter
            .ofPattern("h:mma");
    public static final DateTimeFormatter LONG_DAYOFWEEK = DateTimeFormatter
            .ofPattern("EEEE");
    public static final DateTimeFormatter SHORT_DAYOFWEEK = DateTimeFormatter
            .ofPattern("EEE");

    public DateTimeParser(String input) {
        assert input != null;
        assert input.isEmpty() != true;

        this.datetime = input;

        // perform parsing
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

    public LocalDateTime extractEndDate() {
        assert this.dates != null;

        if (this.dates.size() < 2) {
            return extractStartDate();
        }

        return changeDateToLocalDateTime(this.dates.get(1));
    }

    public boolean isRecurring() {
        return this.dategroups.get(0).isRecurring();
    }

    public LocalDateTime getRecurEnd() {
        return changeDateToLocalDateTime(
                this.dategroups.get(0).getRecursUntil());
    }

    /**
     * Makes the pretty datetime line for an Item's card on the UI.
     * 
     * The UI should only be calling this method for displaying datetime of
     * event on the Item's card on the agenda pane.
     * 
     * @return
     * @author darren
     */
    public static String extractPrettyItemCardDateTime(LocalDateTime start,
            LocalDateTime end) {
        if (start == null && end == null) {
            return EMPTY_STRING;
        }

        if (start == null) {
            return extractPrettyDateTime(end);
        }

        if (end == null) {
            return extractPrettyDateTime(start);
        }

        // is an event with a definite start and end datetime
        if (isSameDay(start, end)) {
            return extractPrettyDateTime(start) + PRETTY_TO_DELIMITER
                    + extractTwelveHourTime(end);
        }

        // not same day
        return extractPrettyDateTime(start) + PRETTY_TO_DELIMITER
                + extractPrettyDateTime(end);
    }

    /**
     * Checks if two given java.time.LocalDateTime objects are of the same day.
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
     * Check if the given java.time.LocalDateTime object is the same date as the
     * current date on local system time
     * 
     * @param ldt
     * @return true if the LocalDateTime is for today, false otherwise
     * @author darren
     */
    public static boolean isToday(LocalDateTime ldt) {
        return isSameDay(ldt, LocalDateTime.now());
    }

    /**
     * Check if the given java.time.LocalDateTime object is the same date as the
     * next day relative to the local system time
     * 
     * @param ldt
     * @return true if the LocalDateTime is for tomorrow, false otherwise
     * @author darren
     */
    public static boolean isTomorrow(LocalDateTime ldt) {
        return isSameDay(ldt, LocalDateTime.now().plusDays(1L));
    }

    /**
     * Helper method for casting java.util.Date to java.time.LocalDateTime
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
     * Helper method for casting java.time.LocalDateTime to java.util.Date
     * 
     * @param ldt
     * @return
     * @author darren
     */
    public static Date changeLocalDateTimeToDate(LocalDateTime ldt) {
        Instant instant = ldt.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
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
    public static String extractPrettyRelativeDateTime(LocalDateTime ldt) {
        if (ldt == null) {
            return EMPTY_STRING;
        }
        return prettytime.format(changeLocalDateTimeToDate(ldt));
    }

    /**
     * Helper method for determining a human-readable pretty date from date
     * tokens in the input string
     * 
     * Examples: "This Monday, 6:30AM", "Next Saturday, 12:37PM", "Mon 27
     * November, 9:30PM", "Today, 3:57PM"
     * 
     * @param index
     * @return pretty date for this week
     */
    public static String extractPrettyDateTime(LocalDateTime ldt) {
        // special case for today/tomorrow relative to local system time
        if (isToday(ldt)) {
            return TODAY_DATE_REF + PRETTY_COMMA_DELIMITER
                    + extractTwelveHourTime(ldt);
        }

        if (isTomorrow(ldt)) {
            return TOMORROW_DATE_REF + PRETTY_COMMA_DELIMITER
                    + extractTwelveHourTime(ldt);
        }

        // add relative prefix (this/next <day of week>) if applicable
        if (computeDaysTo(ldt) < 14) {
            // is within the next two weeks
            return makeRelativePrefix(ldt) + extractLongDayOfWeek(ldt)
                    + PRETTY_COMMA_DELIMITER + extractTwelveHourTime(ldt);
        }

        // explicit date; no relative prefix
        String prettyDate;
        if (computeDaysTo(ldt) < 365) {
            // same year in start and end datetimes
            prettyDate = ldt.toLocalDate().format(ABRIDGED_DATE_FORMAT);
        } else {
            // different years in start and end datetimes
            prettyDate = ldt.toLocalDate().format(EXPLICIT_DATE_FORMAT);
        }
        return extractShortDayOfWeek(ldt) + SINGLE_WHITESPACE + prettyDate
                + PRETTY_COMMA_DELIMITER + extractTwelveHourTime(ldt);
    }

    /**
     * Extracts the time component of a java.time.LocalDateTime object and
     * returns it in 12-hour format.
     * 
     * @param ldt
     * @return
     * @author darren
     */
    public static String extractTwelveHourTime(LocalDateTime ldt) {
        return ldt.toLocalTime().format(TWELVE_HOUR_TIME);
    }

    /**
     * Extracts the day-of-week component of a java.time.LocalDateTime object
     * and returns it in long or short format (Monday or Mon)
     * 
     * @param ldt
     * @param isLongFormat
     *            result is long format?
     * @return day-of-week
     * @author darren
     */
    private static String extractDayOfWeek(LocalDateTime ldt,
            boolean isLongFormat) {
        if (isLongFormat) {
            return ldt.toLocalDate().format(LONG_DAYOFWEEK);
        }
        return ldt.toLocalDate().format(SHORT_DAYOFWEEK);
    }

    public static String extractLongDayOfWeek(LocalDateTime ldt) {
        return extractDayOfWeek(ldt, true);
    }

    public static String extractShortDayOfWeek(LocalDateTime ldt) {
        return extractDayOfWeek(ldt, false);
    }

    /**
     * Determine the appropriate relative prefix to use for reference to a
     * DayOfWeek enum
     * 
     * @param ldt
     * @return
     * @author darren
     */
    private static String makeRelativePrefix(LocalDateTime ldt) {
        if (computeDaysTo(ldt) < 7) {
            return THIS_WEEK_REF;
        } else if (computeDaysTo(ldt) < 14) {
            return NEXT_WEEK_REF;
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

    public DateGroup getDateGroup(int index) {
        return this.dategroups.get(index);
    }

    public String getDateTime() {
        return this.datetime;
    }

}
