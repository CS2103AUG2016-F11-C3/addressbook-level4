package seedu.sudowudo.logic.parser;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;
import org.ocpsoft.prettytime.nlp.PrettyTimeParser;
import org.ocpsoft.prettytime.nlp.parse.DateGroup;

//@@author A0147609X
/**
 * beware of tests involving relative dates as the current date
 * is taken to be the current system time (whoever is building)
 * @author darren
 */
public class DateTimeParserTest {

    // commonly used temporal markers
    private static final LocalDateTime LDT_TODAY = LocalDateTime.now();
    private static final LocalDateTime LDT_TOMORROW = LDT_TODAY.plusDays(1);
    private static final LocalDateTime LDT_THIS_MONDAY = LDT_TODAY.with(DayOfWeek.MONDAY);
    private static final LocalDateTime LDT_THIS_SUNDAY = LDT_TODAY.with(DayOfWeek.SUNDAY);
    private static final LocalDateTime LDT_LAST_SUNDAY = LDT_TODAY.with(TemporalAdjusters.previous(DayOfWeek.SUNDAY));
    private static final LocalDateTime LDT_TODAY_LAST_WEEK = LDT_TODAY.minusDays(7);
    private static final LocalDateTime LDT_TODAY_NEXT_WEEK = LDT_TODAY.plusDays(7);
    

    private DateTimeParser parser = DateTimeParser.getInstance();
    
    @Test
    public void getDateTime_validString_stringReturned() {
        String input = "16 september 2016 5pm to 17 september 2016 6pm";
        parser.parse(input);
        assertEquals(input, parser.getDateTime());
    }
    
    @Test
    public void extractStartDate_explicitDate_correctStartDate() {
        String input = "16 september 2016 5pm to 17 september 2016 6pm";
        parser.parse(input);

        LocalDateTime start = LocalDateTime.of(LocalDate.of(2016, 9, 16), LocalTime.of(17, 0));

        assertEquals(start, parser.extractStartDate());
    }
    
    @Test
    public void extractStartDate_implicitDate_correctStartDate() {
        String input = "2213 fifth january";
        parser.parse(input);
        
        LocalDateTime start = LocalDateTime.of(LocalDate.of(2016, 1, 5), LocalTime.of(22, 13));
        
        assertEquals(start, parser.extractStartDate());
    }
    
    @Test
    public void extractEndDate_explicitDate_correctEndDate() {
        String input = "16 september 2016 5pm to 17 september 2016 6:30pm";
        parser.parse(input);

        LocalDateTime end = LocalDateTime.of(LocalDate.of(2016, 9, 17), LocalTime.of(18, 30));

        assertEquals(end, parser.extractEndDate());
    }

    @Test
    public void extractEndDate_implicitDate_correctEndDate() {
        String input = "1800 fifth january till the sixth october at 9:30pm";
        parser.parse(input);
        
        LocalDateTime end = LocalDateTime.of(LocalDate.of(2016, 10, 6), LocalTime.of(21, 30));
        
        assertEquals(end, parser.extractEndDate());
    }

    @Test
    public void extractDates_missingEndDate_nullEndDate() {
        String input = "by 12 november 1996 at 5pm";
        parser.parse(input);

        LocalDateTime deadline = LocalDateTime.of(LocalDate.of(1996, 11, 12), LocalTime.of(17, 0));

        assertEquals(deadline, parser.extractStartDate());
        assertEquals(null, parser.extractEndDate());
    }
    
    public static Date makeDate(int year, int month, int day, int hour, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(year, month-1, day, hour, minute); //month-1 because Calendar treats JANUARY as 0
        return cal.getTime();
    }

    @Test
    public void changeDateToLocalDateTime_successfulCast() {
        int year = 2016;
        int month = 11;
        int day = 12;
        int hour = 17;
        int minute = 0;

        Date date = makeDate(year, month, day, hour, minute);

        LocalDateTime answer = LocalDateTime.of(LocalDate.of(year, month, day), LocalTime.of(hour, minute));
        
        assertEquals(answer, DateTimeParser.changeDateToLocalDateTime(date));
    }

    @Test
    public void changeLocalDateTimeToDate_successfulCast() {
        int year = 2016;
        int month = 11;
        int day = 12;
        int hour = 17;
        int minute = 0;

        Date date = makeDate(year, month, day, hour, minute);

        LocalDateTime ldt = LocalDateTime.of(LocalDate.of(year, month, day), LocalTime.of(hour, minute));
        
        assertEquals(date, DateTimeParser.changeLocalDateTimeToDate(ldt));
    }

    @Test
    public void extractRecurringEventDetails() {
        String input = "every monday at 9am until 25 december 2016";
        parser.parse(input);

        LocalDateTime recurEndDateTime = LocalDateTime.of(LocalDate.of(2016, 12, 25), LocalTime.of(9, 0));
        assertEquals(true, parser.isRecurring());
        assertEquals(recurEndDateTime, parser.getRecurEnd());
    }
    
    @Test
    public void extractNonRecurringEventDetails() {
        String input = "on fifth of november at 5pm";
        parser.parse(input);

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
    public void extractLongDayOfWeek_everyDayOfWeek_successfulExtract() {
        ArrayList<LocalDateTime> weekLDTs = generateWeeklyLDTs();
        String[] daysOfWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        
        for(int i = 0; i < 7; i++) {
            assertEquals(daysOfWeek[i], DateTimeParser.extractLongDayOfWeek(weekLDTs.get(i)));
        }
    }
    
    @Test
    public void extractShortDayOfWeek_everyDayOfWeek_successfulExtract() {
        ArrayList<LocalDateTime> weekLDTs = generateWeeklyLDTs();
        String[] daysOfWeek = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        
        for(int i = 0; i < 7; i++) {
            assertEquals(daysOfWeek[i], DateTimeParser.extractShortDayOfWeek(weekLDTs.get(i)));
        }
    }
    
    @Test
    public void extractTwelveHourTime_morning_successfulExtract() {
        LocalDateTime morning = LocalDateTime.of(2016, 11, 11, 11, 11);
        String expected = "11:11AM";
        assertEquals(expected, DateTimeParser.extractTwelveHourTime(morning));
    }

    @Test
    public void extractTwelveHourTime_evening_successfulExtract() {
        LocalDateTime evening = LocalDateTime.of(2016, 11, 11, 18, 31);
        String expected = "6:31PM";
        assertEquals(expected, DateTimeParser.extractTwelveHourTime(evening));
    }
    
    @Test
    public void extractTwelveHourTime_midnight_successfulExtract() {
        LocalDateTime evening = LocalDateTime.of(2016, 11, 11, 0, 0);
        String expected = "12:00AM";
        assertEquals(expected, DateTimeParser.extractTwelveHourTime(evening));
    }

    @Test
    public void extractTwelveHourTime_midday_successfulExtract() {
        LocalDateTime evening = LocalDateTime.of(2016, 11, 11, 12, 0);
        String expected = "12:00PM";
        assertEquals(expected, DateTimeParser.extractTwelveHourTime(evening));
    }
    
    @Test
    public void isSameDay_sameDay_true() {
        LocalDateTime ldt1 = LocalDateTime.of(2016, 11, 11, 11, 11);
        LocalDateTime ldt2 = LocalDateTime.of(2016, 11, 11, 19, 46);
        assertEquals(true, DateTimeParser.isSameDay(ldt1, ldt2));
    }

    @Test
    public void isSameDay_differentDay_false() {
        assertEquals(false, DateTimeParser.isSameDay(LDT_TODAY, LDT_TOMORROW));
    }
    
    @Test
    public void isToday_todaysDate_true() {
        assertEquals(true, DateTimeParser.isToday(LDT_TODAY));
    }

    @Test
    public void isToday_tomorrowsDate_false() {
        assertEquals(false, DateTimeParser.isToday(LDT_TOMORROW));
    }
    
    @Test
    public void isTomorrow_tomorrowsDate_true() {
        assertEquals(true, DateTimeParser.isTomorrow(LDT_TOMORROW));
    }

    @Test
    public void isTomorrow_todaysDate_false() {
        assertEquals(false, DateTimeParser.isTomorrow(LDT_TODAY));
    }
    
    @Test
    public void isWithinTwoWeeks_withinTwoWeeks_true() {
        for(int i = 0; i < 14; i++) {
            assertEquals(true, DateTimeParser.isWithinTwoWeeks(LDT_TODAY.plusDays(i)));
            assertEquals(true, DateTimeParser.isWithinTwoWeeks(LDT_TODAY.minusDays(i)));
        }
    }
    
    @Test
    public void isWithinTwoWeeks_outsideTwoWeeks_false() {
        LocalDateTime ldtPlus = LDT_TODAY.plusDays(14);
        LocalDateTime ldtMinus = LDT_TODAY.minusDays(14);
        for(int i = 0; i < 14; i++) {
            assertEquals(false, DateTimeParser.isWithinTwoWeeks(ldtPlus.plusDays(i)));
            assertEquals(false, DateTimeParser.isWithinTwoWeeks(ldtMinus.minusDays(i)));
        }
    }
    
    @Test
    public void isWithinThisYear_withinYear_true() {
        assertEquals(true, DateTimeParser.isWithinThisYear(LDT_TODAY));
    }

    @Test
    public void isWithinThisYear_outsideYear_false() {
        assertEquals(false, DateTimeParser.isWithinThisYear(LDT_TODAY.plusYears(1)));
    }
    
    @Test
    public void isLastWeek_insideLastWeek_true() {
        // 7 days ago is obviously inside last week
        assertEquals(true, DateTimeParser.isLastWeek(LDT_TODAY_LAST_WEEK));
    }

    @Test
    public void isLastWeek_lastSunday_true() {
        // final day of last week
        assertEquals(true, DateTimeParser.isLastWeek(LDT_LAST_SUNDAY));
    }

    @Test
    public void isLastWeek_outsideLastWeek_false() {
        // today is obviously not last week
        assertEquals(false, DateTimeParser.isLastWeek(LDT_TODAY));
    }

    @Test
    public void isLastWeek_thisMonday_false() {
        // first day of this week
        assertEquals(false, DateTimeParser.isLastWeek(LDT_THIS_MONDAY));
    }
    
    @Test
    public void isThisWeek_today_true() {
        assertEquals(true, DateTimeParser.isThisWeek(LDT_TODAY));
    }

    @Test
    public void isThisWeek_thisMonday_true() {
        assertEquals(true, DateTimeParser.isThisWeek(LDT_THIS_MONDAY));
    }
    
    @Test
    public void isThisWeek_lastWeek_false() {
        assertEquals(false, DateTimeParser.isThisWeek(LDT_TODAY_LAST_WEEK));
    }

    @Test
    public void isThisWeek_lastSunday_false() {
        assertEquals(false, DateTimeParser.isThisWeek(LDT_LAST_SUNDAY));
    }
    
    @Test
    public void isNextWeek_nextWeek_true() {
        assertEquals(true, DateTimeParser.isNextWeek(LDT_TODAY_NEXT_WEEK));
    }
    
    @Test
    public void isNextWeek_nextMonday_true() {
        assertEquals(true, DateTimeParser.isNextWeek(LDT_THIS_MONDAY.plusDays(7)));
    }
    
    @Test
    public void isNextWeek_today_false() {
        assertEquals(false, DateTimeParser.isNextWeek(LDT_TODAY));
    }
    
    @Test
    public void isNextWeek_thisSunday_false() {
        assertEquals(false, DateTimeParser.isNextWeek(LDT_THIS_SUNDAY));
    }
}
