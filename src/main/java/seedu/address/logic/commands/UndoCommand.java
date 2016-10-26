package seedu.address.logic.commands;

public class UndoCommand extends Command {
	
	public static final String MESSAGE_UNDO_FAILURE = "";

	@Override
	public CommandResult execute() {
		hasUndo = false;
		return null;
	}

	@Override
    public CommandResult undo() {
        return new CommandResult(MESSAGE_UNDO_FAILURE);
    }

}
