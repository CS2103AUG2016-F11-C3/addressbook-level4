package seedu.address.logic.commands;

import java.util.List;

import seedu.address.model.TaskBook;
import seedu.address.model.item.ReadOnlyItem;

/**
 * Clears the address book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "To-do list has been cleared!";
    
    private final List<ReadOnlyItem> itemsToAddBack;

    public ClearCommand() {
    	itemsToAddBack = model.getTaskBook().getItemList();
    	
    }
    
    @Override
    public CommandResult execute() {
        assert model != null;
        model.resetData(TaskBook.getEmptyTaskBook());
        return new CommandResult(MESSAGE_SUCCESS);
    }

	@Override
	public CommandResult undo() {
		TaskBook savedTaskBook = new TaskBook();
    	savedTaskBook.resetData(itemsToAddBack);
		model.resetData(savedTaskBook);
		return null;
	}
    
}
