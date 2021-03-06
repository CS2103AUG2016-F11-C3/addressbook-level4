package seedu.sudowudo.logic;

import javafx.collections.ObservableList;
import seedu.sudowudo.logic.commands.CommandResult;
import seedu.sudowudo.model.item.ReadOnlyItem;

/**
 * API of the Logic component
 */
public interface Logic {
    /**
     * Executes the command and returns the result.
     * @param commandText The command as entered by the user.
     * @return the result of the command execution.
     */
    CommandResult execute(String commandText);

    /** Returns the filtered list of persons */
    ObservableList<ReadOnlyItem> getFilteredItemList();

}
