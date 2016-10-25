package seedu.address.logic.parser;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

/**
 * beware of tests involving relative dates as the current date
 * is taken to be the current system time (whoever is building)
 * @author darren
 */
public class DateTimeParserTest {

    @Test
    public void extractExplicitStartDateTimeTest() {
        String input = "16 september 2016 5pm to 17 september 2016 6pm";
        DateTimeParser parser = new DateTimeParser(input);

        LocalDateTime start = LocalDateTime.of(LocalDate.of(2016, 9, 16), LocalTime.of(17, 0));

        assertEquals(start, parser.extractStartDate());
    }
    
    @Test
    public void extractImplicitStartDateTimeTest() {
        String input = "2213 fifth january";
        DateTimeParser parser = new DateTimeParser(input);
        
        LocalDateTime start = LocalDateTime.of(LocalDate.of(2016, 1, 5), LocalTime.of(22, 13));
        
        assertEquals(start, parser.extractStartDate());
    }
    
    @Test
    public void extractExplicitEndDateTimeTest() {
        String input = "16 september 2016 5pm to 17 september 2016 6:30pm";
        DateTimeParser parser = new DateTimeParser(input);

        LocalDateTime end = LocalDateTime.of(LocalDate.of(2016, 9, 17), LocalTime.of(18, 30));

        assertEquals(end, parser.extractEndDate());
    }

    @Test
    public void extractImplicitEndDateTimeTest() {
        String input = "1800 fifth january till the sixth october at 9:30pm";
        DateTimeParser parser = new DateTimeParser(input);
        
        LocalDateTime end = LocalDateTime.of(LocalDate.of(2016, 10, 6), LocalTime.of(21, 30));
        
        assertEquals(end, parser.extractEndDate());
    }

    @Test
    public void extractMissingEndDateTimeTest() {
        String input = "by 12 november 1996 at 5pm";
        DateTimeParser parser = new DateTimeParser(input);

        LocalDateTime deadline = LocalDateTime.of(LocalDate.of(1996, 11, 12), LocalTime.of(17, 0));

        // extractStartDate and extractEndDate return the same thing if there's only
        // one date token inside the input string
        assertEquals(deadline, parser.extractStartDate());
        assertEquals(deadline, parser.extractEndDate());
    }
    
    @Test
    public void changeDateToLocalDateTimeTest() {
        int year = 2016;
        int month = 11;
        int day = 12;
        int hour = 17;
        int minute = 0;

        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(year, month-1, day, hour, minute); //month-1 because Calendar treats JANUARY as 0
        Date date = cal.getTime();

        LocalDateTime answer = LocalDateTime.of(LocalDate.of(year, month, day), LocalTime.of(hour, minute));
        
        assertEquals(answer, DateTimeParser.changeDateToLocalDateTime(date));
    }

    @Test
    public void changeLocalDateTimeToDateTest() {
        int year = 2016;
        int month = 11;
        int day = 12;
        int hour = 17;
        int minute = 0;

        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(year, month-1, day, hour, minute); //month-1 because Calendar treats JANUARY as 0
        Date date = cal.getTime();

        LocalDateTime ldt = LocalDateTime.of(LocalDate.of(year, month, day), LocalTime.of(hour, minute));
        
        assertEquals(date, DateTimeParser.changeLocalDateTimeToDate(ldt));
    }

    @Test
    public void extractRecurringEventDetails() {
        String input = "every monday at 9am until 25 december 2016";
        DateTimeParser parser = new DateTimeParser(input);

        LocalDateTime recurEndDateTime = LocalDateTime.of(LocalDate.of(2016, 12, 25), LocalTime.of(9, 0));
        assertEquals(true, parser.isRecurring());
        assertEquals(recurEndDateTime, parser.getRecurEnd());
    }
    
    @Test
    public void extractNonRecurringEventDetails() {
        String input = "on fifth of november at 5pm";
        DateTimeParser parser = new DateTimeParser(input);

        assertEquals(false, parser.isRecurring());
    }
    
    private static ArrayList<LocalDateTime> generateWeeklyLDTs() {
        LocalDateTime day = LocalDateTime.of(2016, 10, 17, 12, 0); // Monday
        ArrayList<LocalDateTime> week = new ArrayList<LocalDateTime>();

        week.add(day);
        
        for(int i = 0; i < 6; i++) {
            day = day.plusDays(1);
            week.add(day);
        }
        
        return week;
    }
    
    @Test
    public void extractLongDayOfWeekTest() {
        ArrayList<LocalDateTime> weekLDTs = generateWeeklyLDTs();
        String[] daysOfWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        
        for(int i = 0; i < 7; i++) {
            assertEquals(daysOfWeek[i], DateTimeParser.extractLongDayOfWeek(weekLDTs.get(i)));
        }
    }
    
    @Test
    public void extractShortDayOfWeekTest() {
        ArrayList<LocalDateTime> weekLDTs = generateWeeklyLDTs();
        String[] daysOfWeek = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        
        for(int i = 0; i < 7; i++) {
            assertEquals(daysOfWeek[i], DateTimeParser.extractShortDayOfWeek(weekLDTs.get(i)));
        }
    }
    
    @Test
    public void extractTwelveHourTimeTest_Morning() {
        LocalDateTime morning = LocalDateTime.of(2016, 11, 11, 11, 11);
        String expected = "11:11AM";
        assertEquals(expected, DateTimeParser.extractTwelveHourTime(morning));
    }

    @Test
    public void extractTwelveHourTimeTest_Evening() {
        LocalDateTime evening = LocalDateTime.of(2016, 11, 11, 18, 31);
        String expected = "6:31PM";
        assertEquals(expected, DateTimeParser.extractTwelveHourTime(evening));
    }
    
    @Test
    public void extractTwelveHourTimeTest_Midnight() {
        LocalDateTime evening = LocalDateTime.of(2016, 11, 11, 0, 0);
        String expected = "12:00AM";
        assertEquals(expected, DateTimeParser.extractTwelveHourTime(evening));
    }

    @Test
    public void extractTwelveHourTimeTest_Midday() {
        LocalDateTime evening = LocalDateTime.of(2016, 11, 11, 12, 0);
        String expected = "12:00PM";
        assertEquals(expected, DateTimeParser.extractTwelveHourTime(evening));
    }
    
    @Test
    public void isSameDayTest_Positive() {
        LocalDateTime ldt1 = LocalDateTime.of(2016, 11, 11, 11, 11);
        LocalDateTime ldt2 = LocalDateTime.of(2016, 11, 11, 19, 46);
        assertEquals(true, DateTimeParser.isSameDay(ldt1, ldt2));
    }

    @Test
    public void isSameDayTest_Negative() {
        LocalDateTime ldt1 = LocalDateTime.of(2016, 11, 11, 11, 11);
        LocalDateTime ldt2 = LocalDateTime.of(2016, 11, 15, 19, 46);
        assertEquals(false, DateTimeParser.isSameDay(ldt1, ldt2));
    }
    
}
