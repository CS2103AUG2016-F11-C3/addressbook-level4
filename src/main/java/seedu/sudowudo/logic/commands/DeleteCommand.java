package seedu.sudowudo.logic.commands;

import java.util.ArrayList;
import java.util.Set;

import seedu.sudowudo.commons.core.Messages;
import seedu.sudowudo.commons.core.UnmodifiableObservableList;
import seedu.sudowudo.commons.exceptions.IllegalValueException;
import seedu.sudowudo.model.item.Item;
import seedu.sudowudo.model.item.ReadOnlyItem;
import seedu.sudowudo.model.item.UniqueItemList;
import seedu.sudowudo.model.item.UniqueItemList.ItemNotFoundException;

/**
 * Deletes an item identified using it's last displayed index from the address book.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the task identified by  the index number used in the last task listing\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example 1: " + COMMAND_WORD + " 1\n";


    public static final String MESSAGE_DELETE_ITEM_SUCCESS = "Deleted Task: %1$s";
    public static final String MESSAGE_UNDO_SUCCESS = "Undo delete task: %1$s";
	public static final String MESSAGE_UNDO_FAILURE = "Undo failed! Task already existed!";

    protected static ArrayList<Hint> hints = new ArrayList<>();

    private Set<String> keywords;
    private int index;
    private Item itemToAddBack;

    public DeleteCommand(int index) {
        this.index = index;
    }

    @Override
    //@@author A0131560U
    /**
     * Attempts to execute a delete command based on the given keywords.
     */
    public CommandResult execute() {

        ReadOnlyItem itemToDelete = null;
        
        try{
            itemToDelete = findItemByIndex();
        } catch (IllegalValueException ive){
            return new CommandResult(ive.getMessage());
        }
        
        assert itemToDelete != null;
        itemToAddBack = new Item(itemToDelete);

        try {
            model.deleteItem(itemToDelete);
            hasUndo = true;
        } catch (ItemNotFoundException infe) {
        	hasUndo = false;
            assert false : "The target item cannot be missing";
        }
		return new CommandResult(String.format(MESSAGE_DELETE_ITEM_SUCCESS, itemToDelete), itemToDelete);
    }

    //@@author A0131560Urefactored
    /**
     * Returns item at given index on the last shown list 
     * @return
     * @throws IllegalValueException if the index is invalid
     */
    private ReadOnlyItem findItemByIndex() throws IllegalValueException {
        UnmodifiableObservableList<ReadOnlyItem> lastShownList = model.getFilteredItemList();
        
        ReadOnlyItem itemToDelete;
        if (lastShownList.size() < index) {
            indicateAttemptToExecuteIncorrectCommand();
            throw new IllegalValueException(Messages.MESSAGE_INVALID_ITEM_DISPLAYED_INDEX);
        }
        itemToDelete = lastShownList.get(getInternalListIndex(index));
        return itemToDelete;
    }

    /**
     * Returns the index number of an item in the backing list (starting from 0)
     * from its outward-facing index (starting from 1)
     */
    private int getInternalListIndex(int index) {
        return index-1;
    }

    //@@author A0144750J
    /**
     * Undo deletion by adding back the item to the list
     * @@author A0144750J
     */
	@Override
	public CommandResult undo() {
		assert model != null;
		try {
			model.addItem(itemToAddBack);
			return new CommandResult(String.format(MESSAGE_UNDO_SUCCESS, itemToAddBack), itemToAddBack);
		} catch (UniqueItemList.DuplicateItemException e) {
			return new CommandResult(MESSAGE_UNDO_FAILURE);
		}
	}

	/**
	 * Method to return hints for this command
	 * 
	 * @@author A0092390E
	 */
	public static ArrayList<Hint> getHints() {
		if (hints.size() == 0) {
			hints.add(new Hint("delete task", "delete", "delete CONTEXT_ID"));
		}
		return hints;
    }
}