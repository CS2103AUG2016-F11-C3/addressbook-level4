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
        hasUndo = true;
        model.resetData(TaskBook.getEmptyTaskBook());
        return new CommandResult(MESSAGE_SUCCESS);
    }

    /**
     * Add back the deleted task book
     * @@author A0144750J
     */
	@Override
	public CommandResult undo() {
		assert itemsToAddBack != null;
		TaskBook savedTaskBook = new TaskBook();
    	savedTaskBook.resetData(itemsToAddBack);
		model.resetData(savedTaskBook);
		return null;
	}
    
}
