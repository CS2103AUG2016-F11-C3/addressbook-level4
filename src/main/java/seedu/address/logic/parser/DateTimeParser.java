package seedu.address.logic.parser;

import java.time.LocalDateTime;
import java.time.ZoneId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.joestelmach.natty.*;

/**
 * For parsing dates and times in Sudowudo command input
 * @author darren
 */
public class DateTimeParser {
    // the part of the command that contains the temporal part of the command
    private String datetime;
    
    // natty parser object
    // careful of name collision with our own Parser object
    private com.joestelmach.natty.Parser parser;
    
    // result from parser
    private List<DateGroup> dategroups;
    
    DateTimeParser(String input) {
        assert input != null;
        assert input.isEmpty() != true;

        this.datetime = input;
        this.parser = new com.joestelmach.natty.Parser();
    }
    
    /**
     * wrapper for natty's parser
     * @return
     * @author darren
     */
    public void parseInput() {
        assert this.datetime != null;
        assert this.parser != null;

        // natty-side parsing
        dategroups = parser.parse(this.datetime);
    }
    
    /**
     * extracts the date from the DateGroup object and massages it into
     * a LocalDateTime object
     * @param dategroup
     * @return
     * @author darren
     */
    public static LocalDateTime extractLDT(DateGroup dategroup) {
        List<Date> dates = dategroup.getDates();
        // return first element of List<Date> (good enough?) TODO
        return Date2LocalDateTime(dates.get(0));
    }
    
    /**
     * helper method for parseInput()
     * converts a java.util.Date object to java.time.LocalDateTime object
     * 
     * @param date
     * @return
     * @author darren
     */
    public static LocalDateTime Date2LocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    public String getDateTime() {
        return this.datetime;
    }
    
}