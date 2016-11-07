package seedu.sudowudo.testutil;

import seedu.sudowudo.model.item.Item;
import seedu.sudowudo.model.item.UniqueItemList;
import seedu.sudowudo.model.TaskBook;

/**
 * A utility class to help with building Taskbook objects.
 * Example usage: <br>
 *     {@code TaskBook ab = new TaskBookBuilder().withPerson("John", "Doe").withTag("Friend").build();}
 */
public class TaskBookBuilder {

    private TaskBook taskBook;

    public TaskBookBuilder(TaskBook taskBook){
        assert taskBook != null;
        this.taskBook = taskBook;
    }

    public TaskBookBuilder withItem(Item item) throws UniqueItemList.DuplicateItemException {
        taskBook.addItem(item);
        return this;
    }
    
    public TaskBook build(){
        return taskBook;
    }
}
