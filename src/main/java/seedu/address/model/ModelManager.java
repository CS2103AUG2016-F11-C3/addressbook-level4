package seedu.address.model;

import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.commons.events.model.TaskBookChangedEvent;
import seedu.address.commons.util.StringUtil;
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
    private Predicate defaultPredicate;

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
    }

    public ModelManager() {
        this(new TaskBook(), new UserPrefs());
        this.defaultPredicate = null;
    }

    public ModelManager(ReadOnlyTaskBook initialData, UserPrefs userPrefs) {
        taskBook = new TaskBook(initialData);
        filteredItems = new FilteredList<>(taskBook.getItems());
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
    
    @Override
    public synchronized void doneItem(ReadOnlyItem item) {
    	item.setIsDone(true);
        updateFilteredListToShowAll();
        indicateTaskBookChanged();
    }

    //=========== Filtered Item List Accessors ===============================================================

    @Override
    /**
     * Returns a list sorted chronologically
     * @@author A0131560U
     */
    public UnmodifiableObservableList<ReadOnlyItem> getFilteredItemList() {
        return new UnmodifiableObservableList<>(filteredItems);
    }

    @Override
    public void updateFilteredListToShowAll() {
        filteredItems.setPredicate(null);
    }
    
    @Override
    public void updateFilteredListDefaultPredicate(String taskType){
        defaultPredicate = new QualifierPredicate(new TypeQualifier(taskType));
        updateFilteredItemList(defaultPredicate);
    }

    @Override
    public void updateFilteredItemList(Set<String> keywords){
		updateFilteredItemList(new QualifierPredicate(new KeywordQualifier(keywords))
		        .and(defaultPredicate));
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
    
    private class TypeQualifier implements Qualifier {
        private String type;
        
        TypeQualifier(String type) {
            this.type = type;
        }

        @Override
        public boolean run(ReadOnlyItem item) {
            if(!item.is(type)){
                System.out.println(item.getAsText() + " is not a \"" + type + "\"");
                return false;
            }
            return true;
        }
        
        @Override
        public String toString(){
            return "type= " + type;
        }
        
        
    }

    private class KeywordQualifier implements Qualifier {
		private Set<String> searchKeyWords;

        KeywordQualifier(Set<String> nameKeyWords) {
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
            return "keywords=" + String.join(", ", searchKeyWords);
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
