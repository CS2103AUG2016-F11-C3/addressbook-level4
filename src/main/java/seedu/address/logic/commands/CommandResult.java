package seedu.address.logic.commands;

import seedu.address.model.item.ReadOnlyItem;

/**
 * Represents the result of a command execution.
 */
public class CommandResult {

    public final String feedbackToUser;
	protected ReadOnlyItem item;

    public CommandResult(String feedbackToUser) {
        assert feedbackToUser != null;
        this.feedbackToUser = feedbackToUser;
		this.item = null;
	}

	public CommandResult(String feedbackToUser, ReadOnlyItem itemToComplete) {
		assert feedbackToUser != null;
		this.feedbackToUser = feedbackToUser;
		this.item = itemToComplete;
	}

	public ReadOnlyItem getItem() {
		return this.item;
    }

}
