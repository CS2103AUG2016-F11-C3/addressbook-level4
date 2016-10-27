package seedu.address.logic.commands;

import javax.sound.sampled.AudioFileFormat.Type;

import seedu.address.logic.parser.Parser;

/**
 * Lists all persons in the address book to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_SUCCESS = "Listed all tasks";
    public static final String MESSAGE_INVALID_TYPE = "List argument is invalid";

    public static final Object MESSAGE_USAGE = COMMAND_WORD + " lists items that match the given parameter "
            + "Parameter: \"OPTIONAL_TYPE_ARGUMENT (task, deadline, event)\" "
            + "Example: list task";
        
    private String itemType;
    

    public ListCommand(String argument) {
        this.itemType = argument;
    }

    @Override
    //@@author A0131560U
    public CommandResult execute() {
        model.updateFilteredListDefaultPredicate(itemType);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
