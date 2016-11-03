package seedu.sudowudo.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
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
import seedu.sudowudo.commons.util.StringUtil;
import seedu.sudowudo.logic.commands.Command;
import seedu.sudowudo.logic.parser.DateTimeParser;
import seedu.sudowudo.logic.parser.Parser;
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
        commandStack = new Stack<>();
        commandHistory = new ArrayList<String>();
        this.defaultPredicate = new QualifierPredicate(new TypeQualifier("item"));
    }

    public ModelManager() {
        this(new TaskBook(), new UserPrefs());
        this.defaultPredicate = new QualifierPredicate(new TypeQualifier("item"));
    }

    public ModelManager(ReadOnlyTaskBook initialData, UserPrefs userPrefs) {
        taskBook = new TaskBook(initialData);
        filteredItems = new FilteredList<>(taskBook.getItems());
        commandStack = new Stack<>();
        commandHistory = new ArrayList<String>();
        this.defaultPredicate = new QualifierPredicate(new TypeQualifier("item"));
    }

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
	public void setItemDesc(Item item, String desc) {
        try {
            item.setDescription(desc);
            updateFilteredListToShowAll();
            indicateTaskBookChanged();
        } catch (IllegalValueException ive) {
        }
    }
    //@@author
    
    //@@author A0147609X
    @Override
	public void setItemStart(Item item, LocalDateTime startDate) {
        item.setStartDate(startDate);
        updateFilteredListToShowAll();
        indicateTaskBookChanged();
    }
    //@@author

    //@@author A0147609X
    @Override
	public void setItemEnd(Item item, LocalDateTime endDate) {
        item.setEndDate(endDate);
        updateFilteredListToShowAll();
        indicateTaskBookChanged();
    }
    //@@author
    
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
        SortedList<Item> sortedList = new SortedList<>(filteredItems, Item.chronologicalComparator);
        return new UnmodifiableObservableList<>(sortedList);
    }
    
    //@@author A0144750J
    /**
     * Return a list of Item instead of ReadOnlyItem
     */
    @Override
	public FilteredList<Item> getFilteredEditableItemList() {
		return filteredItems;
	}

    // @@author
    @Override
    public void updateFilteredListToShowAll() {
        filteredItems.setPredicate(null);
    }

    @Override
    //@@author A0131560U
    public void updateFilteredListDefaultPredicate(String taskType) {
        defaultPredicate = new QualifierPredicate(new TypeQualifier(taskType));
        updateFilteredItemList(defaultPredicate);
    }

    @Override
    public void updateFilteredItemList(Set<String> keywords) {
        updateFilteredItemList(new QualifierPredicate(new KeywordQualifier(keywords)).and(defaultPredicate));
    }

	/*
	 * @@author A0092390E
	 * 
	 */
    private void updateFilteredItemList(Predicate pred) {
        // Not used, to narrow searches the user has to type the entire search string in
        // if(filteredItems.getPredicate() != null){
        // filteredItems.setPredicate(pred.and(filteredItems.getPredicate()));
        // } else{
        filteredItems.setPredicate(pred);
        // }
    }
    
    //@@author A0144750J
    @Override
    /**
     * Add a command in String format to command history
     * Maximum size is set by HISTORY_LENGTH
     */
    public void addCommandToHistory(String command) {
        assert commandHistory != null;
        if (commandHistory.size() > HISTORY_LENGTH) {
            commandHistory.remove(0);
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
    
    // ========== Inner classes/interfaces used for filtering ==================================================

    private class QualifierPredicate implements Predicate<ReadOnlyItem> {

        private final Qualifier qualifier;

        QualifierPredicate(Qualifier qualifier) {
            this.qualifier = qualifier;
        }

        @Override
        public String toString() {
            return qualifier.toString();
        }

        @Override
        public boolean test(ReadOnlyItem item) {
            return qualifier.run(item);
        }

    }

    interface Qualifier {
        boolean run(ReadOnlyItem item);

        @Override
        String toString();
    }

    //@@author A0131560U
    /**
     * A Qualifier class that particularly checks for Item Type (e.g. Task, Event, Done).
     * @author craa
     *
     */
    private class TypeQualifier implements Qualifier {
        private String type;

        TypeQualifier(String type) {
            this.type = type;
        }

        @Override
        public boolean run(ReadOnlyItem item) {
            if (!item.is(type)) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return "type= " + type;
        }

    }

    //@@author A0131560U
    /**
     * A Qualifier class that particularly checks a set of keywords.
     * @author craa
     *
     */
    private class KeywordQualifier implements Qualifier {
        private Set<String> searchKeyWords;

        KeywordQualifier(Set<String> nameKeyWords) {
            this.searchKeyWords = nameKeyWords;
        }

        @Override
        public boolean run(ReadOnlyItem item) {
            for (String keyword : searchKeyWords) {
                if (!new Keyword(keyword).matches(item)) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public String toString() {
            return "keywords=" + String.join(", ", searchKeyWords);
        }
    }

    // @@author A0092390E-idea
    /**
     * An anonymous class that holds a keyword. This keyword is used to search against items.
     * @author craa
     *
     */
    private class Keyword {
        private String keyword;

        Keyword(String _keyword) {
            keyword = _keyword;
        }

        //@@author A0131560U
        /**
         * Returns true if the item matches the keyword in this instance of Keyword.
         * @param item
         * @return
         */
        public boolean matches(ReadOnlyItem item) {
            if (keyword.matches(Parser.COMMAND_DESCRIPTION_REGEX)) {
                return matchesDescription(item);
            } else if (keyword.matches(Parser.COMMAND_TAG_REGEX)) {
                return matchesTags(item);
            } else {
                return matchesDates(item);
            }
        }

        /**
         * Checks if the item's start or end date matches the keyword.
         * @param item
         * @return
         */
        private boolean matchesDates(ReadOnlyItem item) {
            DateTimeParser parseDate = new DateTimeParser(keyword);
            return ((item.getStartDate() != null
                    && DateTimeParser.isSameDay(item.getStartDate(), parseDate.extractStartDate())
                    || (item.getEndDate() != null
                            && DateTimeParser.isSameDay(item.getEndDate(), parseDate.extractStartDate()))));
        }

        /**
         * Checks if the item's tags (or types, if the tag string actually
         * aliases to a type meta-tag) match the keyword.
         * @param item
         * @return
         */
        private boolean matchesTags(ReadOnlyItem item) {
            keyword = keyword.replaceFirst(Parser.COMMAND_TAG_PREFIX, "");
            if (isKeywordType()){
                return item.is(keyword);
            }

            return StringUtil.containsIgnoreCase(item.getTags().listTags(),keyword);
        }

        /**
         * Checks if the item's description matches the keyword
         * @param item
         * @return
         */
        private boolean matchesDescription(ReadOnlyItem item) {
            return StringUtil.containsIgnoreCase(item.getDescription().getFullDescription(),
                    keyword.replace(Parser.COMMAND_DESCRIPTION_PREFIX, ""));
        }
        
        private boolean isKeywordType(){
            return Parser.isValidType(keyword);
        }
    }

}
