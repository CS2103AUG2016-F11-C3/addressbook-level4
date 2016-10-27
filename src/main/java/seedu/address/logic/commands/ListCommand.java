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
    
    private static final String LIST_TYPE_DEADLINE = "DEADLINE";
    private static final String LIST_TYPE_EVENT = "EVENT";
    private static final String LIST_TYPE_TASK = "TASK";
    private static final String LIST_TYPE_ALL = "";

    
    private String limiter;
    

    public ListCommand(String argument) {
        this.limiter = argument;
    }

    @Override
    //@@author A0131560U
    public CommandResult execute() {
        switch (limiter){
            case LIST_TYPE_DEADLINE:    model.updateFilteredListToShowDeadlines();
            case LIST_TYPE_EVENT:       model.updateFilteredListToShowEvents();
            case LIST_TYPE_TASK:        model.updateFilteredListToShowTasks();
            case LIST_TYPE_ALL:         model.updateFilteredListToShowAll();
            default: return new CommandResult(MESSAGE_INVALID_TYPE);
        }
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
