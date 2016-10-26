package seedu.address.logic.commands;

public class UndoCommand extends Command {
	
	public static final String MESSAGE_UNDO_FAILURE = "";
	public static final String COMMAND_WORD = "undo";
	public static final String MESSAGE_FAILURE = "Unable to undo";

	@Override
	public CommandResult execute() {
		hasUndo = false;
		Command lastCommandOnStack = model.returnCommandFromStack();
		return lastCommandOnStack.undo();
	}

	@Override
    public CommandResult undo() {
        return new CommandResult(MESSAGE_UNDO_FAILURE);
    }

}
