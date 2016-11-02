package seedu.address.logic;

import javafx.collections.ObservableList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.parser.Parser;
import seedu.address.model.Model;
import seedu.address.model.item.ReadOnlyItem;
import seedu.address.storage.Storage;

import java.util.Stack;
import java.util.logging.Logger;

/**
 * The main LogicManager of the app.
 */
public class LogicManager extends ComponentManager implements Logic {
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Parser parser;
    
    private int currentPointer; //Pointer at the current command in history

    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.parser = new Parser();
    }

    //@@author A0144750J
    /**
     * Call on parser to parse a command 
     * Return command result after executing
     * Add command to undo stack if applicable
     */
    @Override
    public CommandResult execute(String commandText) {
        logger.info("----------------[USER COMMAND][" + commandText + "]");
        Command command = parser.parseCommand(commandText);
        command.setData(model);
        CommandResult result = command.execute();
        // putting valid command into undo stack
        if (command.getUndo()) {
        	model.addCommandToStack(command);
        }
        // putting command in String format in history
        
        return result;
    }
    // @@author
    
    @Override
    public ObservableList<ReadOnlyItem> getFilteredItemList() {
        return model.getFilteredItemList();
    }
}
