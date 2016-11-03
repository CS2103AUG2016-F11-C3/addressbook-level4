package seedu.sudowudo.logic;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.collections.ObservableList;
import seedu.sudowudo.commons.core.ComponentManager;
import seedu.sudowudo.commons.core.EventsCenter;
import seedu.sudowudo.commons.core.LogsCenter;
import seedu.sudowudo.commons.core.UnmodifiableObservableList;
import seedu.sudowudo.commons.events.ui.JumpToListRequestEvent;
import seedu.sudowudo.commons.events.ui.ListPageDownEvent;
import seedu.sudowudo.commons.events.ui.ListPageUpEvent;
import seedu.sudowudo.logic.commands.Command;
import seedu.sudowudo.logic.commands.CommandResult;
import seedu.sudowudo.logic.parser.Parser;
import seedu.sudowudo.model.Model;
import seedu.sudowudo.model.item.ReadOnlyItem;
import seedu.sudowudo.storage.Storage;

/**
 * The main LogicManager of the app.
 */
public class LogicManager extends ComponentManager implements Logic {
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Parser parser;

    private int currentListIndex;
    private int pageStep;
    
    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.parser = new Parser();
        currentListIndex = 0;
        this.setPageStep(5);
        registerAsHandler();
    }

    public int getPageStep() {
        return pageStep;
    }

    public void setPageStep(int pageStep) {
        this.pageStep = pageStep;
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
    
    //@@author A0144750J
    /**
     * Register the LogicManager as a handler for events
     */
    private void registerAsHandler() {
        eventsCenter.registerHandler(this);
    }
    
    //@@author A0144750J
    /**
     * Handle a ListPageUpEvent by jumping up page_length items (by default)
     * page_length can be set setPageLength method
     * @param event: ListPageUpEvent dispatched by EventsCenter
     */
    @Subscribe
    private void handleListPageUpEvent(ListPageUpEvent event) {
        
        if (currentListIndex - pageStep < 0) {
            currentListIndex = 0;
        } else {
            currentListIndex = currentListIndex - pageStep;
        }
        UnmodifiableObservableList<ReadOnlyItem> lastShownList = model.getFilteredItemList();
        assert lastShownList != null;
        if (lastShownList.size() != 0) {
            EventsCenter.getInstance().post(new JumpToListRequestEvent(currentListIndex));
        }
    }
    
    //@@author A0144750J
    /**
     * Handle a ListPageUpEvent by jumping down page_length items (by default)
     * page_length can be set setPageLength method
     * @param event: ListPageDownEvent dispatched by EventsCenter
     */
    @Subscribe
    private void handleListPageDownEvent(ListPageDownEvent event) {
        UnmodifiableObservableList<ReadOnlyItem> lastShownList = model.getFilteredItemList();
        assert lastShownList != null;
        if (currentListIndex + pageStep >= lastShownList.size()) {
            currentListIndex = lastShownList.size() - 1;
        } else {
            currentListIndex = currentListIndex + pageStep;
        }
        if (lastShownList.size() != 0) {
            EventsCenter.getInstance().post(new JumpToListRequestEvent(currentListIndex));
        }
    }
}
