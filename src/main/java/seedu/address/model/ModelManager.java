package seedu.address.model;

import java.util.Comparator;
import java.util.EmptyStackException;
import java.util.Set;
import java.util.Stack;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.commons.events.model.TaskBookChangedEvent;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.commands.Command;
import seedu.address.logic.parser.DateTimeParser;
import seedu.address.logic.parser.Parser;
import seedu.address.model.item.Item;
import seedu.address.model.item.ReadOnlyItem;
import seedu.address.model.item.UniqueItemList;
import seedu.address.model.item.UniqueItemList.ItemNotFoundException;

/**
 * Represents the in-memory model of the address book data.
 * All changes to any model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final TaskBook taskBook;
    private final FilteredList<Item> filteredItems;
    private Stack<Command> commandStack;

    /**
     * Initializes a ModelManager with the given AddressBook
     * AddressBook and its variables should not be null
     */
    public ModelManager(TaskBook src, UserPrefs userPrefs) {
        super();
        assert src != null;
        assert userPrefs != null;

        logger.fine("Initializing with address book: " + src + " and user prefs " + userPrefs);

        taskBook = new TaskBook(src);
        filteredItems = new FilteredList<>(taskBook.getItems());
        commandStack = new Stack<>();
    }

    public ModelManager() {
        this(new TaskBook(), new UserPrefs());
    }

    public ModelManager(ReadOnlyTaskBook initialData, UserPrefs userPrefs) {
        taskBook = new TaskBook(initialData);
        filteredItems = new FilteredList<>(taskBook.getItems());
        commandStack = new Stack<>();
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
    
    // @@author A0144750J
    @Override
	public void setDoneItem(Item item) {
    	item.setIsDone(true);
        updateFilteredListToShowAll();
        indicateTaskBookChanged();
	}
    
    // @@author A0144750J
	@Override
	public void setNotDoneItem(Item item) {
		item.setIsDone(false);
        updateFilteredListToShowAll();
        indicateTaskBookChanged();
		
	}
	
	// @@author A0144750J
	@Override
	public void addCommandToStack(Command command) {
		assert command.getUndo() == true;
		assert this.commandStack != null;
		this.commandStack.push(command);
	}

	// @@author A0144750J
	@Override
	public Command returnCommandFromStack() throws EmptyStackException {
		assert this.commandStack != null;
		if (this.commandStack.isEmpty()) {
			throw new EmptyStackException();
		}
		return commandStack.pop();
	}


    //=========== Filtered Item List Accessors ===============================================================

    @Override
    /**
     * Returns a list sorted chronologically
     * @@author A0131560U
     */
    public UnmodifiableObservableList<ReadOnlyItem> getFilteredItemList() {
        Comparator<Item> chronologicalComparator = new Comparator<Item>(){
            @Override
            public int compare(Item x, Item y) {
                return x.compareTo(y);
            }
        };
        //SortedList<Item> sortedList = new SortedList<>(filteredItems, chronologicalComparator);
        return new UnmodifiableObservableList<>(filteredItems);
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
    public void updateFilteredItemList(Set<String> keywords){
		updateFilteredItemList(new QualifierPredicate(new DescriptionAndTagQualifier(keywords)));
    }

	private void updateFilteredItemList(Predicate pred) {
		// Not used, to narrow searches the user has to type the entire search
		// string in
		// if(filteredItems.getPredicate() != null){
		// filteredItems.setPredicate(pred.and(filteredItems.getPredicate()));
		// } else{
		filteredItems.setPredicate(pred);
		// }
    }

    //========== Inner classes/interfaces used for filtering ==================================================

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

    private class DescriptionAndTagQualifier implements Qualifier {
		private Set<String> searchKeyWords;

        DescriptionAndTagQualifier(Set<String> nameKeyWords) {
            this.searchKeyWords = nameKeyWords;
        }

        @Override
        public boolean run(ReadOnlyItem item) {
        	for (String keyword: searchKeyWords){
        		if(!new Keyword(keyword).search(item)){
        			return false;
        		}
        	}
			return true;
        }

        @Override
        public String toString() {
            return "name=" + String.join(", ", searchKeyWords);
        }
    }

	// Idea @@author A0092390E
	private class Keyword {
		private String keyword;

		Keyword(String _keyword) {
			keyword = _keyword;
		}

		public boolean search(ReadOnlyItem item){
			if(keyword.matches(Parser.COMMAND_DESCRIPTION_REGEX)){
				return StringUtil.containsIgnoreCase(item.getDescription().getFullDescription(), 
				        keyword.replace(Parser.COMMAND_DESCRIPTION_PREFIX, ""));
			} else if (keyword.matches(Parser.COMMAND_TAG_REGEX)){
				return StringUtil.containsIgnoreCase(item.getTags().listTags(), 
				        keyword.replaceFirst(Parser.COMMAND_TAG_PREFIX, ""));
			}
			else {
			    DateTimeParser parseDate = new DateTimeParser(keyword);
			    return ((item.getStartDate() != null
			            && DateTimeParser.isSameDay(item.getStartDate(), parseDate.extractStartDate())
			            || (item.getEndDate() != null
			            && DateTimeParser.isSameDay(item.getEndDate(),parseDate.extractStartDate()))));
			}
		}
	}

}
