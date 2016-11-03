package seedu.address.gcal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.api.services.calendar.model.*;

public class Interfacer {
    // the remote calendar object
    private com.google.api.services.calendar.Calendar calendar;

    public Interfacer() {
        try {
            this.calendar = Initializer.getCalendarService();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets a list of remote calendar names
     * Google Calendar allows one user to manage multiple calendars.
     * 
     * To be used for validating user input for sync command
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
	                //System.out.println(calendarListEntry.getSummary());
	                calendarNames.add(calendarListEntry.getSummary());
	            }
	            pageToken = calendarList.getNextPageToken();
            } catch(IOException ioe) {
                ioe.printStackTrace();
            }
        } while (pageToken != null);
        
	    return calendarNames;
        
    }

    public static void main(String[] args) {
        Interfacer interfacer = new Interfacer();
        System.out.println(interfacer.getCalendarNames().toString());
    }
}
