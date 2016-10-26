package seedu.address.model;

import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.model.item.Item;
import seedu.address.model.item.ReadOnlyItem;
import seedu.address.model.item.UniqueItemList;

import java.util.Set;

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
    void setDoneItem(Item item);
    
    /** Set the item isDone field to true */
    void setNotDoneItem(Item item);

    /** Returns the filtered person list as an {@code UnmodifiableObservableList<ReadOnlyPerson>} */
    UnmodifiableObservableList<ReadOnlyItem> getFilteredItemList();

    /** Updates the filter of the filtered person list to show all persons */
    void updateFilteredListToShowAll();

    /** Updates the filter of the filtered person list to filter by the given keywords*/
    void updateFilteredItemList(Set<String> keywords);

}
