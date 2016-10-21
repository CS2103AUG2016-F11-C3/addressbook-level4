package seedu.address.logic.parser;

import java.time.Instant;
import java.time.LocalDateTime;
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
     * @return
     * @author darren
     */
    private String extractPrettyRelativeDate(int index) {
        assert this.dates != null;
        return prettytime.format(this.dates.get(index));
    }

    public DateGroup getDateGroup(int index) {
        return this.dategroups.get(index);
    }

    public String getDateTime() {
        return this.datetime;
    }

}