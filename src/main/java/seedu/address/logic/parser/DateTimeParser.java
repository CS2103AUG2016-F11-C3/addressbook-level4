package seedu.address.logic.parser;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
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
     * Examples of pretty relative dates:
     * (for future dates)
     * "3 weeks from now", "2 days from now", "12 minutes from now",
     * "moments from now"
     * 
     * (for past dates)
     * "3 weeks ago", "2 days ago", "12 minutes ago",
     * "moments ago"
     * @return
     * @author darren
     */
    public String extractPrettyRelativeStartDate() {
        return extractPrettyRelativeDate(0);
    }

    public LocalDateTime extractEndDate() {
        assert this.dates != null;

        if (this.dates.size() < 2) {
            return extractStartDate();
        }

        return changeDateToLocalDateTime(this.dates.get(1));
    }

    /**
     * Extracts a pretty relative start date
     * 
     * Examples of pretty relative dates:
     * (for future dates)
     * "3 weeks from now", "2 days from now", "12 minutes from now",
     * "moments from now"
     * 
     * (for past dates)
     * "3 weeks ago", "2 days ago", "12 minutes ago",
     * "moments ago"
     * @return
     * @author darren
     */
    public String extractPrettyRelativeEndDate() {
        if (this.dates.size() < 2) {
            return extractPrettyRelativeStartDate();
        }
        return extractPrettyRelativeDate(1);
    }

    public boolean isRecurring() {
        return this.dategroups.get(0).isRecurring();
    }

    public LocalDateTime getRecurEnd() {
        return changeDateToLocalDateTime(
                this.dategroups.get(0).getRecursUntil());
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
        Instant instant = date.toInstant().truncatedTo(ChronoUnit.SECONDS); // strip milliseconds
        return LocalDateTime.ofInstant(instant,
                ZoneId.systemDefault());
    }

    /**
     * Helper method for determining a human-readable relative date
     * from the date tokens in the input string
     * 
     * Note that this is dependent on the local system time, e.g.
     * the output from java.util.Date()
     * 
     * Examples of pretty relative dates:
     * (for future dates)
     * "3 weeks from now", "2 days from now", "12 minutes from now",
     * "moments from now"
     * 
     * (for past dates)
     * "3 weeks ago", "2 days ago", "12 minutes ago",
     * "moments ago"
     * 
     * @param index
     *      index of target java.util.Date inside DateTimeParser's List<Date>
     * @return
     *      pretty relative date
     * @author darren
     */
    private String extractPrettyRelativeDate(int index) {
        assert this.dates != null;
        return prettytime.format(this.dates.get(index));
    }
    
    /**
     * Helper method for determining a human-readable pretty date
     * from date tokens in the input string
     * 
     * This is independent of local system time, although it is
     * intended for dates that are upcoming in the next 7 days as
     * only the day of the week is indicated instead of a date.
     * 
     * This method does NOT check if the input date is within the
     * next 7 days.
     * 
     * Examples:
     * "Monday, 6:30AM"
     * "Saturday, 12:37PM"
     * 
     * @param index
     * @return
     *      pretty date for this week
     */
    private String extractThisWeekPrettyDate(int index) {
        assert this.dates != null;
        
        LocalDateTime ldt = changeDateToLocalDateTime(this.dates.get(index));
        
        DayOfWeek day = ldt.getDayOfWeek();
        int hour = ldt.getHour();
        int minute = ldt.getMinute();
        
        // convert to 12h time from 24h
        if(hour > 12) {
            hour = hour%12;
        }
        
        return day.toString() + ", " + hour + ":" + String.format("%02d", minute) + computeMeridian(hour);
    }
    
    /**
     * Returns the meridian of a given hour
     * 
     * Examples:
     * Returns "PM" if given hour is 23 (11PM)
     * Returns "AM" if given hour is 11 (11AM)
     * 
     * @param hour
     *      integer hour in 24h format
     * @return
     *      meridian of the hour
     * @author darren
     */
    private String computeMeridian(int hour) {
        if(hour > 12) {
            return "PM";
        }
        return "AM";
    }
    
    public DateGroup getDateGroup(int index) {
        return this.dategroups.get(index);
    }

    public String getDateTime() {
        return this.datetime;
    }

}
