package seedu.address.testutil;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.TaskBook;
import seedu.address.model.item.Item;
import seedu.address.model.item.UniqueItemList;

/**
 * @@author A0144750J
 */
public class TypicalTestItems {

    public static TestItem always, bags, cs2103, dover, eating, frolick, grass, help, indeed;

    //@@author A0144750J
    public TypicalTestItems() {
        try {
            always  = new ItemBuilder().withDescription("Always brush teeth").withDates("no date info").withTags("important","priority").build();
            bags    = new ItemBuilder().withDescription("Pack bag with the thing that I always need to bring").withDates("no date info").build();
            cs2103  = new ItemBuilder().withDescription("Finish my CS2103 homework").withDates("from today to next Sunday").withTags("CS2103","homework","important").build();
            dover   = new ItemBuilder().withDescription("Dover Road").withDates("October 5th next yeat to November 9th next year").build();
            eating  = new ItemBuilder().withDescription("eat 1 child").withDates("by Friday").withTags("ohdear","feefifofum").build();
            frolick = new ItemBuilder().withDescription("frolick in the grass").withDates("from tomorrow to Sunday").build();
            grass   = new ItemBuilder().withDescription("You are allergic to grass").withDates("by next week").build();
            //Manually added
            help    = new ItemBuilder().withDescription("Read help instructions").withDates("no date info").build();
            indeed  = new ItemBuilder().withDescription("Indeed this is a test item").withDates("22/10/2016 to december 12").build();
        } catch (IllegalValueException e) {
            e.printStackTrace();
            assert false : "not possible";
        }
    }

    //@@author A0144750J
    public static void loadTaskBookWithSampleData(TaskBook tb) {

        try {
            tb.addItem(new Item(always));
            tb.addItem(new Item(bags));
            tb.addItem(new Item(cs2103));
            tb.addItem(new Item(dover));
            tb.addItem(new Item(eating));
            tb.addItem(new Item(frolick));
            tb.addItem(new Item(grass));
        } catch (UniqueItemList.DuplicateItemException e) {
            assert false : "not possible";
        }
    }

    //@@author A0144750J
    public TestItem[] getTypicalItems() {
        return new TestItem[]{always, bags, cs2103, dover, eating, frolick, grass};
    }

    //@@author A0144750J-reused
    public TaskBook getTypicalTaskBook(){
        TaskBook tb = new TaskBook();
        loadTaskBookWithSampleData(tb);
        return tb;
    }
}
