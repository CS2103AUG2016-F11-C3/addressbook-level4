package seedu.address.logic.commands;


/**
 * Represents an incorrect command. Upon execution, produces some feedback to the user.
 */
public class IncorrectCommand extends Command {

    public final String feedbackToUser;
    public static final String MESSAGE_UNDO_FAILURE = "";


    public IncorrectCommand(String feedbackToUser){
        this.feedbackToUser = feedbackToUser;
    }

    @Override
    public CommandResult execute() {
        indicateAttemptToExecuteIncorrectCommand();
        return new CommandResult(feedbackToUser);
    }

    @Override
    public CommandResult undo() {
        return new CommandResult(MESSAGE_UNDO_FAILURE);
    }
}

