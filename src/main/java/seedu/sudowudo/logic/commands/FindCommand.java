package seedu.sudowudo.logic.commands;

import java.util.Set;

/**
 * Finds and lists all items in task book whose descriptions, tags or start/end dates
 * contains any of the argument keywords.
 * Keyword matching is not case sensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";
    public static final String MESSAGE_UNDO_FAILURE = "";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all items that match at least "
            + "one of the given keywords \"[DESCRIPTION_KEYWORD]\", #[TAG_KEYWORD], [TIME_KEYWORD] "
            + "Example: " + COMMAND_WORD + " \"task\" \"CS2103\" #important tomorrow";

    private final Set<String> keywords;

    public FindCommand(Set<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public CommandResult execute() {
    	hasUndo = false;
        model.updateFilteredItemList(keywords);
		CommandResult res = new CommandResult(getMessageForItemListShownSummary(model.getFilteredItemList().size()));
		return res;
    }
    
    //@@author A0144750J
    @Override
    public CommandResult undo() {
        return new CommandResult(MESSAGE_UNDO_FAILURE);
    }

}
