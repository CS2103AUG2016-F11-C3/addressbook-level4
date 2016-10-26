package seedu.address.logic.commands;

import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.model.item.Item;
import seedu.address.model.item.ReadOnlyItem;

public class DoneCommand extends Command {
	public static final String COMMAND_WORD = "done";

	public static final String MESSAGE_UNDO_SUCCESS = "Undo set done task: %1$s";
	public static final String MESSAGE_UNDO_FAILURE = "";

	public static final String MESSAGE_USAGE = COMMAND_WORD + ": Marks the task identified by index as done.\n"
			+ "Parameters: INDEX (must be a positive integer)\n" + "Example 1: " + COMMAND_WORD + " 1\n";

	public static final String MESSAGE_DONE_ITEM_SUCCESS = "Task marked as complete!";
	public static final String MESSAGE_DONE_ITEM_FAIL = "Task already marked as complete!";

	public final int targetIndex;

	public DoneCommand(int index) {
		this.targetIndex = index;
	}

	private Item itemToUndone;

	@Override
	public CommandResult execute() {
		FilteredList<Item> lastShownList = model.getFilteredEditableItemList();
		if (lastShownList.size() < targetIndex) {
			indicateAttemptToExecuteIncorrectCommand();
			hasUndo = false;
			return new CommandResult(Messages.MESSAGE_INVALID_ITEM_DISPLAYED_INDEX);
		}
		Item itemToComplete = lastShownList.get(targetIndex - 1);

		if (itemToComplete.getIsDone()) {
			hasUndo = false;
			return new CommandResult(MESSAGE_DONE_ITEM_FAIL);
		} else {
			itemToUndone = itemToComplete;
			model.setDoneItem(itemToComplete);
			hasUndo = true;
		}
		return new CommandResult(MESSAGE_DONE_ITEM_SUCCESS, itemToComplete);
	}

	@Override
	public CommandResult undo() {
		assert itemToUndone != null;
		model.setNotDoneItem(itemToUndone);
		return new CommandResult(MESSAGE_UNDO_SUCCESS, itemToUndone);
	}

}
