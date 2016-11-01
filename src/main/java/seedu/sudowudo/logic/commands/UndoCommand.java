package seedu.sudowudo.logic.commands;

import java.util.EmptyStackException;

/**
 * @@author A0144750J
 */
public class UndoCommand extends Command {
	
	public static final String COMMAND_WORD = "undo";
	public static final String MESSAGE_FAILURE = "Unable to undo";

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

}
