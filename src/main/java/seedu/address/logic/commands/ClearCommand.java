package seedu.address.logic.commands;

import java.util.List;

import seedu.address.model.ReadOnlyTaskBook;
import seedu.address.model.TaskBook;
import seedu.address.model.item.ReadOnlyItem;

/**
 * Clears the address book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "To-do list has been cleared!";
    public static final String MESSAGE_UNDO_SUCCESS = "Undo clear task list";
	public static final String MESSAGE_UNDO_FAILURE = "";
    
    private ReadOnlyTaskBook backUpTaskbook;

    public ClearCommand() {    	
    }
    
    @Override
    public CommandResult execute() {
        assert model != null;
        hasUndo = true;
        backUpTaskbook = model.getTaskBook();
        model.resetData(TaskBook.getEmptyTaskBook());
        return new CommandResult(MESSAGE_SUCCESS);
    }

    /**
     * Add back the deleted task book
     * @@author A0144750J
     */
	@Override
	public CommandResult undo() {
		assert backUpTaskbook != null;
		model.resetData(backUpTaskbook);
		return new CommandResult(MESSAGE_UNDO_SUCCESS);
	}
    
}
