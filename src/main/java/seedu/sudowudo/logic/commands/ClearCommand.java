package seedu.sudowudo.logic.commands;

import seedu.sudowudo.model.TaskBook;
import seedu.sudowudo.model.item.Item;
import seedu.sudowudo.model.item.ReadOnlyItem;
import seedu.sudowudo.model.item.UniqueItemList;
import seedu.sudowudo.model.item.UniqueItemList.DuplicateItemException;

/**
 * Clears the task book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "To-do list has been cleared!";
    public static final String MESSAGE_UNDO_SUCCESS = "Undo clear task list";
	public static final String MESSAGE_DUPLICATE_ITEM = "This task already exists in the to-do list";

    
    private UniqueItemList savedList;

    public ClearCommand() {    	
    }
    
    @Override
    public CommandResult execute() {
        assert model != null;
        hasUndo = true;
        savedList = new UniqueItemList();
        UniqueItemList oldList = model.getTaskBook().getUniqueItemList();
        for (ReadOnlyItem i: oldList) {
        	try {
				savedList.add(new Item(i));
			} catch (DuplicateItemException e) {
				return new CommandResult(MESSAGE_DUPLICATE_ITEM);
			}
        }
        model.resetData(TaskBook.getEmptyTaskBook());
        return new CommandResult(MESSAGE_SUCCESS);
    }

    /**
     * Add back the deleted task book
     * @@author A0144750J
     */
	@Override
	public CommandResult undo() {
		assert savedList != null;
		model.resetData(new TaskBook(savedList));
		return new CommandResult(MESSAGE_UNDO_SUCCESS);
	}
    
}
