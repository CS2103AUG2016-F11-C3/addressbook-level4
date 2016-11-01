package seedu.sudowudo.logic.commands;

import seedu.sudowudo.commons.core.EventsCenter;
import seedu.sudowudo.commons.events.ui.ExitAppRequestEvent;

/**
 * Terminates the program.
 */
public class ExitCommand extends Command {

    public static final String COMMAND_WORD = "exit";

    public static final String MESSAGE_EXIT_ACKNOWLEDGEMENT = "Exiting Sudowudo as requested ...";
	public static final String MESSAGE_UNDO_FAILURE = "";


    public ExitCommand() {}

    @Override
    public CommandResult execute() {
    	hasUndo = false;
        EventsCenter.getInstance().post(new ExitAppRequestEvent());
        return new CommandResult(MESSAGE_EXIT_ACKNOWLEDGEMENT);
    }
    
    /**
     * @@author A0144750J
     */
    @Override
    public CommandResult undo() {
        return new CommandResult(MESSAGE_UNDO_FAILURE);
    }

}
