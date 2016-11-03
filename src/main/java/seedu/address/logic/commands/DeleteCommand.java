package seedu.address.logic.commands;

import java.util.Set;

import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.ListUtil;
import seedu.address.model.item.Item;
import seedu.address.model.item.ReadOnlyItem;
import seedu.address.model.item.UniqueItemList;
import seedu.address.model.item.UniqueItemList.ItemNotFoundException;

/**
 * Deletes an item identified using it's last displayed index from the address book.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the task identified by either "
            + "the index number used in the last task listing or a set of keywords.\n"
            + "Parameters: INDEX (must be a positive integer) OR KEYWORDS\n"
            + "Example 1: " + COMMAND_WORD + " 1\n"
            + "Example 2: " + COMMAND_WORD + " \"filing\" #homework\n";


    public static final String MESSAGE_DELETE_ITEM_SUCCESS = "Deleted Task: %1$s";
    public static final String MESSAGE_ITEM_NOT_FOUND = "Item matching your search cannot be found!";
    public static final String MESSAGE_UNDO_SUCCESS = "Undo delete task: %1$s";
	public static final String MESSAGE_UNDO_FAILURE = "Undo failed! Task already existed!";
<<<<<<< HEAD
    public static final String MESSAGE_UNIQUE_ITEM_NOT_FOUND = "More than one item matching your search was found! Please refine your search.";
=======
    
	private static final String MESSAGE_ITEM_NOT_FOUND = "Item matching your search cannot be found!";
    private static final String MESSAGE_UNIQUE_ITEM_NOT_FOUND = "More than one item matching your search was found! Please refine your search.";
>>>>>>> ef394fe890d3e864c4ee58312c3dd7d4a9156bef

    private Set<String> keywords;
    private int index;
    
    private Item itemToAddBack;

    public DeleteCommand(Set<String> keywords) {
        this.keywords = keywords;
    }
    
    public DeleteCommand(int index) {
        this.index = index;
        this.keywords = null;
    }

    @Override
    //@@author A0131560U
    /**
     * Attempts to execute a delete command based on the given keywords.
     */
    public CommandResult execute() {

        ReadOnlyItem itemToDelete = null;
        
        // delete by index
        if (keywords == null){
            try{
                itemToDelete = findItemByIndex();
            } catch (IllegalValueException ive){
                return new CommandResult(ive.getMessage());
            }
        }
        else {
            try{
                itemToDelete = findItemByKeywords();
            } catch (IllegalValueException ive){
                return new CommandResult(ive.getMessage());
            }
        }
        
        assert itemToDelete != null;
        itemToAddBack = new Item(itemToDelete);

        try {
            model.deleteItem(itemToDelete);
            hasUndo = true;
        } catch (ItemNotFoundException infe) {
        	hasUndo = false;
            assert false : "The target item cannot be missing";
        }
		return new CommandResult(String.format(MESSAGE_DELETE_ITEM_SUCCESS, itemToDelete), itemToDelete);
    }

    //@@author A0131560U
    /**
     * Searches all items in the list using given set of keywords to find a single match.
     * If the match is found, returns the match. If no single match is found, throws exception.
     * @return
     * @throws IllegalValueException
     */
    private ReadOnlyItem findItemByKeywords() throws IllegalValueException {
        FilteredList<Item> lastShownList = new FilteredList<>(model.getFilteredEditableItemList());
        ListUtil.getInstance().updateFilteredItemList(lastShownList, keywords);
        
        if (lastShownList.isEmpty()) {
            indicateAttemptToExecuteIncorrectCommand();
            throw new IllegalValueException(MESSAGE_ITEM_NOT_FOUND);
        }
        else if (lastShownList.size() == 1){
             return lastShownList.get(0);
        }
        else if (lastShownList.size() > 1){
            model.updateFilteredItemList(keywords);
            indicateAttemptToExecuteIncorrectCommand();
            throw new IllegalValueException(MESSAGE_UNIQUE_ITEM_NOT_FOUND);
        }
        else{
            assert false: "The list cannot have negative size";
            return null;
        }
    }

    //@@author A0131560U
    /**
     * Returns item at given index on the last shown list 
     * @return
     * @throws IllegalValueException if the index is invalid
     */
    private ReadOnlyItem findItemByIndex() throws IllegalValueException {
        UnmodifiableObservableList<ReadOnlyItem> lastShownList = model.getFilteredItemList();
        
        ReadOnlyItem itemToDelete;
        if (lastShownList.size() < index) {
            indicateAttemptToExecuteIncorrectCommand();
            throw new IllegalValueException(Messages.MESSAGE_INVALID_ITEM_DISPLAYED_INDEX);
        }
<<<<<<< HEAD
        itemToDelete = lastShownList.get(index-1);
=======
        itemToDelete = lastShownList.get(index);
>>>>>>> ef394fe890d3e864c4ee58312c3dd7d4a9156bef
        return itemToDelete;
    }

    //@@author A0144750J
    /**
     * Undo deletion by adding back the item to the list
     * @@author A0144750J
     */
	@Override
	public CommandResult undo() {
		assert model != null;
		try {
			model.addItem(itemToAddBack);
			return new CommandResult(String.format(MESSAGE_UNDO_SUCCESS, itemToAddBack), itemToAddBack);
		} catch (UniqueItemList.DuplicateItemException e) {
			return new CommandResult(MESSAGE_UNDO_FAILURE);
		}
	}
}