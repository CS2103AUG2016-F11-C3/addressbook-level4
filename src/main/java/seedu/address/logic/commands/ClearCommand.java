package seedu.address.logic.commands;

import javafx.collections.ObservableList;
import seedu.address.model.TaskBook;
import seedu.address.model.item.Item;

/**
 * Clears the address book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "To-do list has been cleared!";
    
    private final ObservableList<Item> itemsToAddBack;

    public ClearCommand() {
    	itemsToAddBack = TaskBook.getItems();
    }
    
    @Override
    public CommandResult execute() {
        assert model != null;
        model.resetData(TaskBook.getEmptyTaskBook());
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
