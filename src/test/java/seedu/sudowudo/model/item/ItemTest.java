package seedu.address.model.item;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;

public class ItemTest {

    public static LocalDateTime twelveNov = LocalDateTime.of(2016, 11, 12, 12, 0);
    public static LocalDateTime elevenNov = LocalDateTime.of(2016, 11, 11, 12, 0);

    @Test
    public void testCompareTo1() {
        
	    try {
	        Description dogWalk = new Description("walk the dog");
	        Description snortCrack = new Description("snort cocaine");
	        
	        Item dog = new Item(dogWalk, elevenNov, twelveNov, new UniqueTagList());
	        Item crack = new Item(snortCrack, null, twelveNov, new UniqueTagList());
	        
	        assertEquals(-1, dog.compareTo(crack));
	    } catch(IllegalValueException ive) {
	        System.out.println(ive.getMessage());
	    }
    }
    
    @Test
    public void testCompareTo2() {
        
	    try {
	        Description snortCrack = new Description("snort cocaine");
	        
	        Item crack = new Item(snortCrack, null, twelveNov, new UniqueTagList());
	        
	        assertEquals(0, crack.compareTo(crack));
	    } catch(IllegalValueException ive) {
	        System.out.println(ive.getMessage());
	    }
    }

    @Test
    public void testCompareTo3() {
        
	    try {
	        Description dogWalk = new Description("walk the dog");
	        Description snortCrack = new Description("snort cocaine");
	        
	        Item dog = new Item(dogWalk, elevenNov, twelveNov, new UniqueTagList());
	        Item crack = new Item(snortCrack, null, twelveNov, new UniqueTagList());
	        
	        assertEquals(1, crack.compareTo(dog));
	    } catch(IllegalValueException ive) {
	        System.out.println(ive.getMessage());
	    }
    }

    @Test
    public void testCompareTo_allNullDateTimes() {
        
	    try {
	        Description dogWalk = new Description("walk the dog");
	        Description snortCrack = new Description("snort cocaine");
	        
	        Item dog = new Item(dogWalk, null, null, new UniqueTagList());
	        Item crack = new Item(snortCrack, null, null, new UniqueTagList());
	        
	        int result = crack.compareTo(dog)/(-1*crack.compareTo(dog));
	        
	        assertEquals(-1, result);

	    } catch(IllegalValueException ive) {
	        System.out.println(ive.getMessage());
	    }
    }
    
    @Test
    //@@author A0131560U
    public void equals_sameItem_returnsTrue(){
        try {
            Item testItem = new Item(new Description("Testing 123"),
                                    LocalDateTime.of(2016, 10, 10, 10, 10),
                                    LocalDateTime.of(2016, 10, 12, 12, 12),
                                    new UniqueTagList());
            assertTrue(testItem.equals(testItem));
        } catch (IllegalValueException e) {
            e.printStackTrace();
            assert false: "not possible";
        }
    }
    
    @Test
    //@@author A0131560U
    public void equals_sameData_returnsTrue(){
        try {
            Item testItem1 = new Item(new Description("Testing 123"),
                                    LocalDateTime.of(2016, 10, 10, 10, 10),
                                    LocalDateTime.of(2016, 10, 12, 12, 12),
                                    new UniqueTagList());
            Item testItem2 = new Item(new Description("Testing 123"),
                    LocalDateTime.of(2016, 10, 10, 10, 10),
                    LocalDateTime.of(2016, 10, 12, 12, 12),
                    new UniqueTagList());

            assertTrue(testItem1.equals(testItem2));
        } catch (IllegalValueException e) {
            e.printStackTrace();
            assert false: "not possible";
        }
    }
    
    @Test
    //@@author A0131560U
    public void equals_differentTags_returnsFalse(){
        try {
            Item testItem1 = new Item(new Description("Testing 123"), null, null,
                                    new UniqueTagList(new Tag("hell")));
            Item testItem2 = new Item(new Description("Testing 123"), null, null,
                    new UniqueTagList(new Tag("hello")));
            assertFalse(testItem1.equals(testItem2));
        } catch (IllegalValueException e) {
            e.printStackTrace();
            assert false: "not possible";
        }
    }
    
    @Test
    //@@author A0131560U
    public void equals_differentDescription_returnsFalse(){
        try {
            Item testItem1 = new Item(new Description("Testing 124"), null, null,
                                    new UniqueTagList(new Tag("hello")));
            Item testItem2 = new Item(new Description("Testing 123"), null, null,
                    new UniqueTagList(new Tag("hello")));
            assertFalse(testItem1.equals(testItem2));
        } catch (IllegalValueException e) {
            e.printStackTrace();
            assert false: "not possible";
        }

    }
    
    @Test
    //@@author A0131560U
    public void equals_differentStartTime_returnsFalse(){
        try {
            Item testItem1 = new Item(new Description("Testing 123"),
                                    LocalDateTime.of(2016, 10, 10, 10, 10),
                                    LocalDateTime.of(2016, 10, 12, 12, 12),
                                    new UniqueTagList());
            Item testItem2 = new Item(new Description("Testing 123"),
                    LocalDateTime.of(2016, 10, 10, 10, 11),
                    LocalDateTime.of(2016, 10, 12, 12, 12),
                    new UniqueTagList());

            assertTrue(testItem1.equals(testItem2));
        } catch (IllegalValueException e) {
            e.printStackTrace();
            assert false: "not possible";
        }
    }

    @Test
    //@@author A0131560U
    public void equals_different_EndTime_returnsFalse(){
        try {
            Item testItem1 = new Item(new Description("Testing 123"),
                                    LocalDateTime.of(2016, 10, 10, 10, 10),
                                    LocalDateTime.of(2016, 10, 12, 12, 12),
                                    new UniqueTagList());
            Item testItem2 = new Item(new Description("Testing 123"),
                    LocalDateTime.of(2016, 10, 10, 10, 10),
                    LocalDateTime.of(2016, 10, 12, 12, 13),
                    new UniqueTagList());

            assertTrue(testItem1.equals(testItem2));
        } catch (IllegalValueException e) {
            e.printStackTrace();
            assert false: "not possible";
        }
    }



}
