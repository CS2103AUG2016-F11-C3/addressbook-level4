package seedu.address.model;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.EmptyStackException;
import java.util.Set;
import java.util.Stack;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.commons.events.model.TaskBookChangedEvent;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.commands.Command;
import seedu.address.logic.parser.DateTimeParser;
import seedu.address.logic.parser.Parser;
import seedu.address.model.item.Item;
import seedu.address.model.item.ReadOnlyItem;
import seedu.address.model.item.UniqueItemList;
import seedu.address.model.item.UniqueItemList.ItemNotFoundException;

/**
 * Represents the in-memory model of the address book data. All changes to any
 * model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final TaskBook taskBook;
    private final FilteredList<Item> filteredItems;
    private Predicate defaultPredicate;
    private Stack<Command> commandStack;

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
    
    /**
     * Return a list of Item instead of ReadOnlyItem
     * @@author A0144750J
     */
    @Override
	public FilteredList<Item> getFilteredEditableItemList() {
		return filteredItems;
	}

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

    private void updateFilteredItemList(Predicate pred) {
        // Not used, to narrow searches the user has to type the entire search string in
        // if(filteredItems.getPredicate() != null){
        // filteredItems.setPredicate(pred.and(filteredItems.getPredicate()));
        // } else{
        filteredItems.setPredicate(pred);
        // }
    }

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
    private class KeywordQualifier implements Qualifier {
        private Set<String> searchKeyWords;

        KeywordQualifier(Set<String> nameKeyWords) {
            this.searchKeyWords = nameKeyWords;
        }

        @Override
        public boolean run(ReadOnlyItem item) {
            for (String keyword : searchKeyWords) {
                if (!new Keyword(keyword).search(item)) {
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
    //@@author A0131560U
    /**
     * Given an item, returns true if the item matches this keyword, and false otherwise.
     */
    private class Keyword {
        private String keyword;

        Keyword(String _keyword) {
            keyword = _keyword;
        }

        public boolean search(ReadOnlyItem item) {
            if (keyword.matches(Parser.COMMAND_DESCRIPTION_REGEX)) {
                return matchesDescription(item);
            } else if (keyword.matches(Parser.COMMAND_TAG_REGEX)) {
                return matchesTags(item);
            } else {
                return matchesDates(item);
            }
        }

        private boolean matchesDates(ReadOnlyItem item) {
            DateTimeParser parseDate = new DateTimeParser(keyword);
            return ((item.getStartDate() != null
                    && DateTimeParser.isSameDay(item.getStartDate(), parseDate.extractStartDate())
                    || (item.getEndDate() != null
                            && DateTimeParser.isSameDay(item.getEndDate(), parseDate.extractStartDate()))));
        }

        private boolean matchesTags(ReadOnlyItem item) {
            return StringUtil.containsIgnoreCase(item.getTags().listTags(),
                    keyword.replaceFirst(Parser.COMMAND_TAG_PREFIX, ""));
        }

        private boolean matchesDescription(ReadOnlyItem item) {
            return StringUtil.containsIgnoreCase(item.getDescription().getFullDescription(),
                    keyword.replace(Parser.COMMAND_DESCRIPTION_PREFIX, ""));
        }
    }

}
