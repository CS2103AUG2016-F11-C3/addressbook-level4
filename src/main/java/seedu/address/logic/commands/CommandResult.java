package seedu.address.logic.commands;

import seedu.address.model.item.ReadOnlyItem;

/**
 * Represents the result of a command execution.
 */
public class CommandResult {

    public final String feedbackToUser;
	protected ReadOnlyItem item;
	protected boolean clear;

    public CommandResult(String feedbackToUser) {
        assert feedbackToUser != null;
        this.feedbackToUser = feedbackToUser;
		this.clear = true;
		this.item = null;
	}

	public CommandResult(String feedbackToUser, ReadOnlyItem itemToComplete) {
		assert feedbackToUser != null;
		this.feedbackToUser = feedbackToUser;
		this.item = itemToComplete;
		this.clear = true;
	}

	public void setClear(boolean bool) {
		this.clear = bool;
	}

	public boolean getClear() {
		return this.clear;
	}
	public ReadOnlyItem getItem() {
		return this.item;
    }

}
