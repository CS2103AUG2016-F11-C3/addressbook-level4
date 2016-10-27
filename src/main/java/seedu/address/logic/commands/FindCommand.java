package seedu.address.logic.commands;

import java.util.Set;

/**
 * Finds and lists all persons in address book whose name contains any of the argument keywords.
 * Keyword matching is case sensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all items that match at least "
            + "one of the given keywords \"[DESCRIPTION_KEYWORD]\", #[TAG_KEYWORD], [TIME_KEYWORD] "
            + "Example: " + COMMAND_WORD + " \"task\" \"CS2103\" #important tomorrow";

    private final Set<String> keywords;

    public FindCommand(Set<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public CommandResult execute() {
        model.updateFilteredItemList(keywords);
        return new CommandResult(getMessageForItemListShownSummary(model.getFilteredItemList().size()));
    }

}
