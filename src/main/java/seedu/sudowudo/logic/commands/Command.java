package seedu.sudowudo.logic.commands;

import seedu.sudowudo.commons.core.EventsCenter;
import seedu.sudowudo.commons.core.Messages;
import seedu.sudowudo.commons.events.ui.IncorrectCommandAttemptedEvent;
import seedu.sudowudo.model.Model;

/**
 * Represents a command with hidden internal logic and the ability to be executed.
 */
public abstract class Command {
    protected Model model;
    protected boolean hasUndo = false;
    
    /**
     * getter method for hasUndo
     * @@author A0144750J
     */
    public boolean getUndo() {
    	return hasUndo;
    }

	/**
	 * Returns whether a command should result in clearing the command box.
	 * Defaults to true except where there is custom logic indicating otherwise.
	 * 
	 */
	public boolean ClearOnExecute() {
		return true;
	}
    /**
     * Constructs a feedback message to summarise an operation that displayed a listing of persons.
     *
     * @param displaySize used to generate summary
     * @return summary message for persons displayed
     */
    public static String getMessageForItemListShownSummary(int displaySize) {
        return String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, displaySize);
    }

    /**
     * Executes the command and returns the result message.
     * @return feedback message of the operation result for display
     */
    public abstract CommandResult execute();
    
    /**
     * Undo the result of previous execute and returns message.
     * @return feedback message of the operation result for display
     * @@author A0144750J
     */
    public abstract CommandResult undo();

    /**
     * Provides any needed dependencies to the command.
     * Commands making use of any of these should override this method to gain
     * access to the dependencies.
     * @@author 
     */
    public void setData(Model model) {
        this.model = model;
    }

    /**
     * Raises an event to indicate an attempt to execute an incorrect command
     */
    protected void indicateAttemptToExecuteIncorrectCommand() {
        EventsCenter.getInstance().post(new IncorrectCommandAttemptedEvent(this));
    }
}