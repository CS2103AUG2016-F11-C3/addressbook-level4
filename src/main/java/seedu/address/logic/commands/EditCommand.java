package seedu.address.logic.commands;

/**
 * Support for editing items (tasks, events) on Sudowudo.
 * 
 * @author darren
 */
public class EditCommand extends Command {
    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits a task on the to-do list.\n"
            + "Parameters: \"EVENT_NAME\" edit FIELD_NAME to NEW_DETAIL\n"
            + "Example: " + "for EVENT_NAME " + COMMAND_WORD + " start_time,end_time to 1200,1215\n";

    public static final String MESSAGE_SUCCESS = "Item successfully edited.";
    public static final String MESSAGE_FAILURE = "Item could not be edited.";
    public static final String MESSAGE_DUPLICATE_ITEM = "Multiple items with the same description. Please use edit by event ID instead.";

    @Override
    public CommandResult execute() {
        return null;
    }

}
