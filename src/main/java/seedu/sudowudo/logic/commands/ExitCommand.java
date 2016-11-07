package seedu.sudowudo.logic.commands;

import java.util.ArrayList;

import seedu.sudowudo.commons.core.EventsCenter;
import seedu.sudowudo.commons.events.ui.ExitAppRequestEvent;

/**
 * Terminates the program.
 */
public class ExitCommand extends Command {

    public static final String COMMAND_WORD = "exit";

    public static final String MESSAGE_EXIT_ACKNOWLEDGEMENT = "Exiting Sudowudo as requested ...";
	public static final String MESSAGE_UNDO_FAILURE = "";

    protected static ArrayList<Hint> hints = new ArrayList<>();

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

	/**
	 * Method to return hints for this command
	 * 
	 * @@author A0092390E
	 */
	public static ArrayList<Hint> getHints() {
		if (hints.size() == 0) {
			hints.add(new Hint("exit sudowudo", "exit", "exit"));
		}
		return hints;
    }
}
