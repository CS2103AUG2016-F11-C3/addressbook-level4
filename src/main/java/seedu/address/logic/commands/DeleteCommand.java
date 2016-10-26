package seedu.address.logic.commands;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.model.item.Item;
import seedu.address.model.item.ReadOnlyItem;
import seedu.address.model.item.UniqueItemList;
import seedu.address.model.item.UniqueItemList.ItemNotFoundException;

/**
 * Deletes an item identified using it's last displayed index from the address book.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the task identified by "
            + "the index number used in the last task listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example 1: " + COMMAND_WORD + " 1\n";

    public static final String MESSAGE_DELETE_ITEM_SUCCESS = "Deleted Task: %1$s";
    public static final String MESSAGE_UNDO_SUCCESS = "Undo delete task: %1$s";
	public static final String MESSAGE_UNDO_FAILURE = "Undo failed!";

    public final int targetIndex;
    
    private Item itemToAddBack;

    public DeleteCommand(int targetIndex) {
        this.targetIndex = targetIndex;
    }


    @Override
    public CommandResult execute() {

        UnmodifiableObservableList<ReadOnlyItem> lastShownList = model.getFilteredItemList();

        if (lastShownList.size() < targetIndex) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_ITEM_DISPLAYED_INDEX);
        }

        ReadOnlyItem itemToDelete = lastShownList.get(targetIndex - 1);
        itemToAddBack = new Item(itemToDelete);

        try {
            model.deleteItem(itemToDelete);
        } catch (ItemNotFoundException infe) {
            assert false : "The target item cannot be missing";
        }
		return new CommandResult(String.format(MESSAGE_DELETE_ITEM_SUCCESS, itemToDelete), itemToDelete);
    }


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
}