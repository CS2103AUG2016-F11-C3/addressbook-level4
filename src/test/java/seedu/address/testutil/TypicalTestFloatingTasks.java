package seedu.address.testutil;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.item.*;

/**
 *
 */
public class TypicalTestFloatingTasks {

    public static TestFloatingTask task1, task2, task3;

    public TypicalTestFloatingTasks() {
        try {
        	task1 = new FloatingTaskBuilder().withDescription("CS2103 Post Lecture Quiz 9").build();
        	task2 = new FloatingTaskBuilder().withDescription("Go to school").build();
        	task3 = new FloatingTaskBuilder().withDescription("Groceries").build();
        } catch (IllegalValueException e) {
            e.printStackTrace();
            assert false : "not possible";
        }
    }

    public static void loadAddressBookWithSampleData(AddressBook ab) {

        try {
        	ab.addItem(new FloatingTask(task1));
        	ab.addItem(new FloatingTask(task2));
        	ab.addItem(new FloatingTask(task3));
        } catch (UniqueItemList.DuplicateItemException e) {
            assert false : "not possible";
        }
    }

    public TestFloatingTask[] getTypicalPersons() {
        return new TestFloatingTask[]{task1, task2, task3};
    }

    public AddressBook getTypicalAddressBook(){
        AddressBook ab = new AddressBook();
        loadAddressBookWithSampleData(ab);
        return ab;
    }
}
