package seedu.address.gcal;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.*;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.parser.DateTimeParser;
import seedu.address.model.Model;
import seedu.address.model.item.Description;
import seedu.address.model.item.Item;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;

/**
 * This class provides wrapper methods to the Google Calendar API.
 * 
 * @author darren
 *
 */
public class Interfacer {
    // the remote calendar object
    private com.google.api.services.calendar.Calendar calendar;

    private static final int MAX_RESULTS = 500;

    public Interfacer() {
        try {
            this.calendar = Initializer.getCalendarService();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets a list of remote calendar names Google Calendar allows one user to
     * manage multiple calendars.
     * 
     * To be used for validating user input for sync command
     * 
     * @return
     * @author darren
     */
    public List<String> getCalendarNames() {
        assert this.calendar != null;

        List<String> calendarNames = new ArrayList<>();

        // Iterate through entries in calendar list
        String pageToken = null;
        do {
            try {
                CalendarList calendarList = this.calendar.calendarList().list()
                        .setPageToken(pageToken).execute();
                List<CalendarListEntry> items = calendarList.getItems();

                for (CalendarListEntry calendarListEntry : items) {
                    // System.out.println(calendarListEntry.getSummary());
                    calendarNames.add(calendarListEntry.getSummary());
                }
                pageToken = calendarList.getNextPageToken();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        } while (pageToken != null);

        return calendarNames;

    }

    /**
     * Checks if a given calendar name exists.
     * 
     * @param calendarName
     * @return
     */
    public boolean hasCalendar(String calendarName) {
        List<String> calendarNames = getCalendarNames();
        for (String cal : calendarNames) {
            if (cal.equalsIgnoreCase(calendarName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Pulls events from the remote calendar on GCal and inserts them into the
     * current local model.
     * 
     * @author darren
     */
    public List<Item> pullItems() {
        assert this.calendar != null;

        List<Item> pulledItems = new ArrayList<>();

        List<Event> items = null;
        try {
            // fetch events that haven't happened yet
            items = getEvents("primary", getCurrentDateTime()).getItems();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        if (items.size() == 0 || items == null) {
            // nothing to pull
            return null;
        }

        // have upcoming events
        for (Event event : items) {
            try {
                pulledItems.add(changeEventToItem(event));
            } catch (IllegalValueException ive) {
            }
        }

        return pulledItems;
    }

    private static Item changeEventToItem(Event event)
            throws IllegalValueException {
        Description name = new Description(event.getSummary());
        LocalDateTime start = changeDateTimeToLocalDateTime(
                event.getStart());
        LocalDateTime end = changeDateTimeToLocalDateTime(
                event.getEnd());
        UniqueTagList tags = new UniqueTagList();
        return new Item(name, start, end, tags);
    }

    private static LocalDateTime changeDateTimeToLocalDateTime(EventDateTime edt) {
        if(edt == null) {
            return null;
        }
        
        DateTimeParser parser;
        if(edt.getDateTime() != null) {
            System.out.println(edt.getDateTime().toStringRfc3339());
            parser = new DateTimeParser(edt.getDateTime().toStringRfc3339());
            return parser.extractStartDate();
        }
        
        if(edt.getDate() != null) {
            System.out.println(edt.getDate().toStringRfc3339());
            parser = new DateTimeParser(edt.getDate().toStringRfc3339());
            return parser.extractStartDate();
        }
        
        return null;
    }

    private Events getEvents(String calId, DateTime epoch) throws IOException {
        return this.calendar.events().list(calId).setMaxResults(MAX_RESULTS)
                .setTimeMin(epoch).setOrderBy("startTime").setSingleEvents(true)
                .execute();
    }

    public DateTime getCurrentDateTime() {
        return new DateTime(System.currentTimeMillis());
    }

    public static void main(String[] args) {
        Interfacer interfacer = new Interfacer();
        System.out.println(interfacer.pullItems());
    }
}
