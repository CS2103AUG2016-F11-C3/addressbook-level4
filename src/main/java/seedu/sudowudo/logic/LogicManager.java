package seedu.sudowudo.logic;

import javafx.collections.ObservableList;
import seedu.sudowudo.commons.core.ComponentManager;
import seedu.sudowudo.commons.core.LogsCenter;
import seedu.sudowudo.logic.commands.Command;
import seedu.sudowudo.logic.commands.CommandResult;
import seedu.sudowudo.logic.parser.Parser;
import seedu.sudowudo.model.Model;
import seedu.sudowudo.model.item.ReadOnlyItem;
import seedu.sudowudo.storage.Storage;

import java.util.Stack;
import java.util.logging.Logger;

/**
 * The main LogicManager of the app.
 */
public class LogicManager extends ComponentManager implements Logic {
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Parser parser;

    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.parser = new Parser();
    }

    /**
     * Call on parser to parse a command 
     * Return command result after executing
     * Add command to undo stack if applicable
     * @@author A0144750J
     */
    @Override
    public CommandResult execute(String commandText) {
        logger.info("----------------[USER COMMAND][" + commandText + "]");
        Command command = parser.parseCommand(commandText);
        command.setData(model);
        CommandResult result = command.execute();
        if (command.getUndo()) {
        	model.addCommandToStack(command);
        }
        return result;
    }

    // @@author
    @Override
    public ObservableList<ReadOnlyItem> getFilteredItemList() {
        return model.getFilteredItemList();
    }
}
