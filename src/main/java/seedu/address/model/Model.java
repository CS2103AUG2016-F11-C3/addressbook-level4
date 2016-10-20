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

    /** Returns the TaskBook */
    ReadOnlyTaskBook getTaskBook();

    /** Deletes the given item. */
    void deleteItem(ReadOnlyItem target) throws UniqueItemList.ItemNotFoundException;

    /** Adds the given item */
    void addItem(Item item) throws UniqueItemList.DuplicateItemException;
    
    /** Set task to done*/
    void doneItem(ReadOnlyItem item);

    /** Returns the filtered person list as an {@code UnmodifiableObservableList<ReadOnlyPerson>} */
    UnmodifiableObservableList<ReadOnlyItem> getFilteredItemList();

    /** Updates the filter of the filtered person list to show all persons */
    void updateFilteredListToShowAll();

    /** Updates the filter of the filtered person list to filter by the given keywords*/
    void updateFilteredItemList(Set<String> keywords);

}
