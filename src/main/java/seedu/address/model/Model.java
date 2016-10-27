package seedu.address.model;

import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.logic.commands.Command;
import seedu.address.model.item.Item;
import seedu.address.model.item.ReadOnlyItem;
import seedu.address.model.item.UniqueItemList;

import java.util.Set;
import javafx.collections.transformation.FilteredList;

/**
 * The API of the Model component.
 */
public interface Model {
    /** Clears existing backing model and replaces with the provided new data. */
    void resetData(ReadOnlyTaskBook newData);

    /** Returns the AddressBook */
    ReadOnlyTaskBook getTaskBook();

    /** Deletes the given person. */
    void deleteItem(ReadOnlyItem target) throws UniqueItemList.ItemNotFoundException;

    /** Adds the given person */
    void addItem(Item item) throws UniqueItemList.DuplicateItemException;
    
    /** Set the item isDone field to true */
    // @@author A0144750J
    void setDoneItem(Item item);
    
    /** Set the item isDone field to true */
    // @@author A0144750J
    void setNotDoneItem(Item item);
    
    /** Add the command to stack for undo */
    // @@author A0144750J
    void addCommandToStack(Command command);
    
    /** Add the command to stack for undo */
    // @@author A0144750J
    Command returnCommandFromStack();

    /** Returns the filtered person list as an {@code UnmodifiableObservableList<ReadOnlyPerson>} */
    UnmodifiableObservableList<ReadOnlyItem> getFilteredItemList();

    /** Returns the filtered person list as an {@code UnmodifiableObservableList<ReadOnlyPerson>} */
    FilteredList<Item> getFilteredEditableItemList();
    
    /** Updates the filter of the filtered person list to show all persons */
    void updateFilteredListToShowAll();

    /** Updates the filter of the filtered person list to filter by the given keywords*/
    void updateFilteredItemList(Set<String> keywords);

}
