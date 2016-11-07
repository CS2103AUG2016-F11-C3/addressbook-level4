//@@author A0144750J
package seedu.sudowudo.logic.commands;

import java.util.ArrayList;
import java.util.EmptyStackException;

public class UndoCommand extends Command {
	
	public static final String COMMAND_WORD = "undo";
	public static final String MESSAGE_FAILURE = "Unable to undo";

    protected static ArrayList<Hint> hints = new ArrayList<>();

	@Override
	public CommandResult execute() {
		hasUndo = false;
		try {
			Command lastCommandOnStack = model.returnCommandFromStack();
			return lastCommandOnStack.undo();
		} catch (EmptyStackException e) {
			return new CommandResult(MESSAGE_FAILURE);
		}
		
	}

	@Override
    public CommandResult undo() {
        return new CommandResult(MESSAGE_FAILURE);
    }

	/**
	 * Method to return hints for this command
	 * 
	 * @@author A0092390E
	 */
	public static ArrayList<Hint> getHints() {
		if (hints.size() == 0) {
			hints.add(new Hint("undo action", "undo", "undo"));
		}
		return hints;
    }
}
