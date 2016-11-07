package seedu.sudowudo.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Set;
import java.util.Stack;
import java.util.function.Predicate;
import java.util.logging.Logger;


import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import seedu.sudowudo.commons.core.ComponentManager;
import seedu.sudowudo.commons.core.LogsCenter;
import seedu.sudowudo.commons.core.UnmodifiableObservableList;
import seedu.sudowudo.commons.events.model.TaskBookChangedEvent;
import seedu.sudowudo.commons.exceptions.IllegalValueException;
import seedu.sudowudo.commons.util.ListUtil;
import seedu.sudowudo.logic.commands.Command;
import seedu.sudowudo.model.item.Item;
import seedu.sudowudo.model.item.ReadOnlyItem;
import seedu.sudowudo.model.item.UniqueItemList;
import seedu.sudowudo.model.item.UniqueItemList.ItemNotFoundException;

/**
 * Represents the in-memory model of the address book data. All changes to any
 * model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);
    private final int HISTORY_LENGTH = 100;
    
    private final TaskBook taskBook;
    private final FilteredList<Item> filteredItems;
    private Predicate defaultPredicate;
    private Stack<Command> commandStack;
    private ArrayList<String> commandHistory;

    /**
     * Initializes a ModelManager with the given AddressBook AddressBook and its
     * variables should not be null
     */
    public ModelManager(TaskBook src, UserPrefs userPrefs) {
        super();
        assert src != null;
        assert userPrefs != null;
        logger.fine("Initializing with address book: " + src + " and user prefs " + userPrefs);
        taskBook = new TaskBook(src);
        filteredItems = new FilteredList<>(taskBook.getItems());
        init();
        this.defaultPredicate = ListUtil.getInstance().setDefaultPredicate(Item.Type.ITEM);
    }

    public ModelManager() {
        this(new TaskBook(), new UserPrefs());
        this.defaultPredicate = ListUtil.getInstance().setDefaultPredicate(Item.Type.ITEM);
    }

    public ModelManager(ReadOnlyTaskBook initialData, UserPrefs userPrefs) {
        taskBook = new TaskBook(initialData);
        filteredItems = new FilteredList<>(taskBook.getItems());
        init();
        this.defaultPredicate = ListUtil.getInstance().setDefaultPredicate(Item.Type.ITEM);
    }
    
    //@@author A0144750J
    /**
     * Prepare the command stack for undo
     * And the history list for cycling
     */
    public void init() {
    	commandStack = new Stack<>();
        commandHistory = new ArrayList<String>();
        commandHistory.add("");
    }
    //@@author

    @Override
    public void resetData(ReadOnlyTaskBook newData) {
        taskBook.resetData(newData);
        indicateTaskBookChanged();
    }

    @Override
    public ReadOnlyTaskBook getTaskBook() {
        return taskBook;
    }

    /** Raises an event to indicate the model has changed */
    private void indicateTaskBookChanged() {
        raise(new TaskBookChangedEvent(taskBook));
    }

    @Override
    public synchronized void deleteItem(ReadOnlyItem target) throws ItemNotFoundException {
        taskBook.removeItem(target);
        indicateTaskBookChanged();
    }

    @Override
    public synchronized void addItem(Item item) throws UniqueItemList.DuplicateItemException {
        taskBook.addItem(item);
        updateFilteredListToShowAll();
        indicateTaskBookChanged();
    }

    //@@author A0147609X
    @Override
	public void setItemDesc(Item item, String desc) throws IllegalValueException {
        item.setDescription(desc);
        updateFilteredListToShowAll();
        indicateTaskBookChanged();
    }
    //@@author
    
    //@@author A0147609X
    @Override
	public void setItemStart(Item item, LocalDateTime startDate) throws IllegalValueException {
        item.setStartDate(startDate);
        updateFilteredListToShowAll();
        indicateTaskBookChanged();
    }
    //@@author

    //@@author A0147609X
    @Override
	public void setItemEnd(Item item, LocalDateTime endDate) throws IllegalValueException {
        item.setEndDate(endDate);
        updateFilteredListToShowAll();
        indicateTaskBookChanged();
    }
    //@@author
    
    //@@author A0131560U
    @Override
    public void setPeriod(Item item, LocalDateTime startDate, LocalDateTime endDate) throws IllegalValueException {
        item.setPeriod(startDate, endDate);
        updateFilteredListToShowAll();
        indicateTaskBookChanged();
    }
    
    // @@author A0144750J
    @Override
	public void setDoneItem(Item item) {
    	item.setIsDone(true);
        updateFilteredListToShowAll();
        indicateTaskBookChanged();
	}
    //@@author
    
    // @@author A0144750J
    @Override
    public void setNotDoneItem(Item item) {
        item.setIsDone(false);
        updateFilteredListToShowAll();
        indicateTaskBookChanged();
    }
    //@@author
	
    // @@author A0144750J
    @Override
    public void addCommandToStack(Command command) {
	assert command.getUndo() == true;
	assert this.commandStack != null;
	this.commandStack.push(command);
    }
    //@@author

    // @@author A0144750J
    @Override
    public Command returnCommandFromStack() throws EmptyStackException {
        assert this.commandStack != null;
        if (this.commandStack.isEmpty()) {
            throw new EmptyStackException();
	}
        return commandStack.pop();
    }
    //@@author


    //=========== Filtered Item List Accessors ===============================================================

    @Override
    //@@author A0131560U
    /**
     * Returns a list sorted chronologically
     */
    public UnmodifiableObservableList<ReadOnlyItem> getFilteredItemList() {
        return new UnmodifiableObservableList<>(this.getFilteredEditableItemList());
    }
    
    //@@author A0144750J
    /**
     * Return a list of Item instead of ReadOnlyItem
     */
    @Override
	public FilteredList<Item> getFilteredEditableItemList() {
        SortedList<Item> sortedList = new SortedList<>(filteredItems, Item.chronologicalComparator);
        return new FilteredList<Item>(sortedList);
	}

    // @@author
    @Override
    public void updateFilteredListToShowAll() {
        filteredItems.setPredicate(null);
    }

    @Override
    //@@author A0131560U
    public void updateDefaultPredicate(Item.Type taskType) {
        defaultPredicate = ListUtil.getInstance().setDefaultPredicate(taskType);
        filteredItems.setPredicate(defaultPredicate);
    }

    @Override
    public void updateFilteredItemList(Set<String> keywords) {
    	ListUtil.getInstance().updateFilteredItemList(filteredItems, keywords, defaultPredicate);
    }
    
    // ========== Command History Helper Methods ==================================================
    
    //@@author A0144750J
    @Override
    /**
     * Add a command in String format to command history
     * Maximum size is set by HISTORY_LENGTH
     */
    public void addCommandToHistory(String command) {
        assert commandHistory != null;
        if (commandHistory.size() > HISTORY_LENGTH) {
            commandHistory.remove(1); // remove the second command, the first one is always an empty string
            commandHistory.add(command);
        } else {
            commandHistory.add(command);
        }
    }
    //@@author
    
    //@@author A0144750J
    @Override
    /**
     * Return a command as input by user from history
     */
    public String returnCommandFromHistory(int index) {
        assert commandHistory != null;
        assert (index >= 0) && (index < commandHistory.size());
        return commandHistory.get(index);
    }
    //@@author

    //@@author A0144750J
    @Override
    public int getHistorySize() {
        return commandHistory.size();
    }
    //@@author
}
    
