package seedu.address.gcal;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.*;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.parser.DateTimeParser;
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

    // max number of events to pull
    public static final int MAX_RESULTS = 500;

    // default ID for primary calendar of user
    public static final String GCAL_PRIMARY = "primary";

    public static final String GCAL_TAG = "GCal";
    public static final String GCAL_CALENDAR_NOT_FOUND = "No such calendar exists.";

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
        List<CalendarListEntry> items = getCalendarList().getItems();

        for (CalendarListEntry calendarListEntry : items) {
            calendarNames.add(calendarListEntry.getSummary());
        }

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
     * Pulls events from the remote calendar on GCal and puts them into a
     * List<Item>
     * 
     * Events pulled must occur after the current system time.
     * 
     * @param fromCalendar
     *      name of the remote calendar (case-insensitive)
     * @return List<Item> from the remote calendar
     * @author darren
     */
    public List<Item> pullItems(String fromCalendar)
            throws IllegalArgumentException {
        assert this.calendar != null;
        assert fromCalendar != null;
        assert !fromCalendar.isEmpty();

        if (!hasCalendar(fromCalendar)) {
            throw new IllegalArgumentException(GCAL_CALENDAR_NOT_FOUND);
        }

        List<Item> pulledItems = new ArrayList<>();
        List<Event> events = new ArrayList<>();

        try {
            // fetch events that haven't happened yet
            events = getEvents(getCalendarID(fromCalendar), getCurrentDateTime())
                    .getItems();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        if (events.isEmpty()) {
            // nothing to pull
            // return an empty list
            return pulledItems;
        }

        // have upcoming events
        for (Event event : events) {
            try {
                pulledItems.add(changeEventToItem(event));
            } catch (IllegalValueException ive) {
            }
        }

        return pulledItems;
    }
    
    /**
     * Pushes events from a local List<Item> to a remote calendar.
     * 
     * @param toCalendar
     *      name of remote calendar
     * @param itemsToPush
     *      List<Item> that are to be pushed to remote
     * @throws IllegalArgumentException
     *      Thrown when remote calendar cannot be found
     */
    public void pushItems(String toCalendar, List<Item> itemsToPush) throws IllegalArgumentException {
        assert toCalendar != null;
        assert !toCalendar.isEmpty();
        assert itemsToPush != null;
        
        if(!hasCalendar(toCalendar)) {
            throw new IllegalArgumentException(GCAL_CALENDAR_NOT_FOUND);
        }
        
        String toCalID = getCalendarID(toCalendar);
        
        for(Item item : itemsToPush) {
            try {
                this.calendar.events().insert(toCalID, changeItemToEvent(item)).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static Event changeItemToEvent(Item item) {
        Event event = new Event();
        event.setSummary(item.getDescription().toString());
        
        if(item.getStartDate() != null) {
            event.setStart(changeLocalDateTimeToEventDateTime(item.getStartDate()));
        }

        if(item.getEndDate() != null) {
            event.setEnd(changeLocalDateTimeToEventDateTime(item.getEndDate()));
        }
        
        return event;
    }
    private static Item changeEventToItem(Event event)
            throws IllegalValueException {
        Description name = new Description(event.getSummary());
        LocalDateTime start = changeEventDateTimeToLocalDateTime(event.getStart());
        LocalDateTime end = changeEventDateTimeToLocalDateTime(event.getEnd());
        UniqueTagList tags = new UniqueTagList();
        tags.add(new Tag(GCAL_TAG));
        return new Item(name, start, end, tags);
    }

    private static EventDateTime changeLocalDateTimeToEventDateTime(LocalDateTime ldt) {
        if(ldt == null) {
            return null;
        }
        
        DateTime dt = new DateTime(ldt.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        EventDateTime edt = new EventDateTime();
        edt.setDateTime(dt);
        edt.setTimeZone(ZoneId.systemDefault().toString());
        
        return edt;
    }

    private static LocalDateTime changeEventDateTimeToLocalDateTime(
            EventDateTime edt) {
        if (edt == null) {
            return null;
        }

        DateTimeParser parser;
        if (edt.getDateTime() != null) {
            parser = new DateTimeParser(edt.getDateTime().toStringRfc3339());
            return parser.extractStartDate();
        }

        if (edt.getDate() != null) {
            parser = new DateTimeParser(edt.getDate().toStringRfc3339());
            return parser.extractStartDate();
        }

        return null;
    }

    private Events getEvents(String calId, DateTime epoch) throws IOException {
        assert calId != null;
        assert !calId.isEmpty();
        assert epoch != null;

        return this.calendar.events().list(calId).setMaxResults(MAX_RESULTS)
                .setTimeMin(epoch).setOrderBy("startTime").setSingleEvents(true)
                .execute();
    }

    /**
     * Get a Google Calendar identifier from the calendar's name.
     * 
     * @param calendarName
     * @return
     * @author darren
     */
    public String getCalendarID(String calendarName) {
        assert calendarName != null;
        assert !calendarName.isEmpty();

        CalendarList calendarList = getCalendarList();

        if (calendarList == null) {
            return null;
        }

        for (CalendarListEntry calendar : calendarList.getItems()) {
            if (calendar.getSummary().equalsIgnoreCase(calendarName)) {
                return calendar.getId();
            }
        }

        return null;
    }
    
    /**
     * Get the actual Google Calendar calendar name from
     * a possibly partial-matching-calendar name.
     * @param calendarName
     * @return
     * @author darren
     */
    public String getActualCalendarName(String calendarName) {
        assert calendarName != null;
        assert !calendarName.isEmpty();

        CalendarList calendarList = getCalendarList();

        if (calendarList == null) {
            return null;
        }

        for (CalendarListEntry calendar : calendarList.getItems()) {
            if (calendar.getSummary().equalsIgnoreCase(calendarName)) {
                return calendar.getSummary();
            }
        }

        return null;
        
    }

    private CalendarList getCalendarList() {
        try {
            return this.calendar.calendarList().list().execute();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return null;
    }

    public DateTime getCurrentDateTime() {
        return new DateTime(System.currentTimeMillis());
    }

}
