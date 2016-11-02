package seedu.address.logic;

import javafx.collections.ObservableList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.JumpToListRequestEvent;
import seedu.address.commons.events.ui.ListPageDownEvent;
import seedu.address.commons.events.ui.ListPageUpEvent;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.parser.Parser;
import seedu.address.model.Model;
import seedu.address.model.item.ReadOnlyItem;
import seedu.address.storage.Storage;

import java.util.Stack;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

/**
 * The main LogicManager of the app.
 */
public class LogicManager extends ComponentManager implements Logic {
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Parser parser;

    private int currentListIndex;
    private final int PAGE_STEP = 5;
    
    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.parser = new Parser();
        currentListIndex = 0;
        registerAsHandler();
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
    
    private void registerAsHandler() {
        eventsCenter.registerHandler(this);
    }
    
    @Subscribe
    private void handleListPageUpEvent(ListPageUpEvent event) {
        if (currentListIndex - PAGE_STEP < 0) {
            currentListIndex = 0;
        } else {
            currentListIndex = currentListIndex - PAGE_STEP;
        }
        EventsCenter.getInstance().post(new JumpToListRequestEvent(currentListIndex));
    }
    
    @Subscribe
    private void handleListPageDownEvent(ListPageDownEvent event) {
        if (currentListIndex + PAGE_STEP >= model.getFilteredItemList().size()) {
            currentListIndex = model.getFilteredItemList().size() - 1;
        } else {
            currentListIndex = currentListIndex + PAGE_STEP;
        }
        EventsCenter.getInstance().post(new JumpToListRequestEvent(currentListIndex));
    }
}
