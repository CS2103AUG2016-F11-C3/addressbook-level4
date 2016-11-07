package seedu.sudowudo.logic.commands;


import java.util.ArrayList;

import seedu.sudowudo.commons.core.EventsCenter;
import seedu.sudowudo.commons.events.ui.ShowHelpRequestEvent;

/**
 * Format full help instructions for every command for display.
 */
public class HelpCommand extends Command {

    public static final String COMMAND_WORD = "help";
    public static final String MESSAGE_UNDO_FAILURE = "";


    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows program usage instructions.\n"
            + "Example: " + COMMAND_WORD;

    public static final String SHOWING_HELP_MESSAGE = "Opened help window.";

	protected static ArrayList<Hint> hints = new ArrayList<>();
    public HelpCommand() {}

    @Override
    public CommandResult execute() {
    	hasUndo = false;
        EventsCenter.getInstance().post(new ShowHelpRequestEvent());
		return new CommandResult("");
    }
    
    //@@author A0144750J
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
			hints.add(new Hint("list commands", "help", "help"));
		}
		return hints;
	}

}
