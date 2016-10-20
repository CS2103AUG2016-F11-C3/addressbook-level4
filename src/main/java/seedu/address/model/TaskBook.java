package seedu.address.model;

import javafx.collections.ObservableList;
import seedu.address.model.item.Item;
import seedu.address.model.item.UniqueItemList;
import seedu.address.model.item.ReadOnlyItem;
import seedu.address.model.item.FloatingTask;


import java.util.*;
import java.util.stream.Collectors;

/**
 * Wraps all data at the task-book level
 * Duplicates are not allowed (by .equals comparison)
 */
public class TaskBook implements ReadOnlyTaskBook {

    private final UniqueItemList items;
    {
        items = new UniqueItemList();
    }

    public TaskBook() {}

    /**
     * Persons and Tags are copied into this taskbook
     */
    public TaskBook(ReadOnlyTaskBook toBeCopied) {
        this(toBeCopied.getUniqueItemList());
    }

    /**
     * Items and Tags are copied into this taskbook
     */
    public TaskBook(UniqueItemList items) {
        resetData(items.getInternalList());
    }

    public static ReadOnlyTaskBook getEmptyTaskBook() {
        return new TaskBook();
    }

//// list overwrite operations

    public ObservableList<Item> getItems() {
        return items.getInternalList();
    }

    public void setItems(List<Item> items) {
        this.items.getInternalList().setAll(items);
    }

    public void resetData(Collection<? extends ReadOnlyItem> newItems) {
        setItems(newItems.stream().map(Item::new).collect(Collectors.toList()));
    }

    public void resetData(ReadOnlyTaskBook newData) {
        resetData(newData.getItemList());
    }

    /**************************** ITEM-LEVEL OPERATIONS *****************************************/

    /**
     * Adds an item to the task book.
     * Also checks the new item's tags and updates {@link #tags} with any new tags found,
     * and updates the Tag objects in the item to point to those in {@link #tags}.
     *
     * @throws UniqueItemList.DuplicateItemException if an equivalent person already exists.
     */
    public void addItem(Item i) throws UniqueItemList.DuplicateItemException {
        items.add(i);
    }

    /**
     * Removes the item given by key.
     * 
     * @param key gives the item's 
     * @throws UniqueItemList.ItemNotFoundException if the item cannot be found.
     */
    public boolean removeItem(ReadOnlyItem key) throws UniqueItemList.ItemNotFoundException {
        if (items.remove(key)) {
            return true;
        } else {
            throw new UniqueItemList.ItemNotFoundException();
        }
    }

//// util methods

    @Override
    public String toString() {
        return items.getInternalList().size() + " items";
        // TODO: refine later
    }

    @Override
    public List<ReadOnlyItem> getItemList() {
        return Collections.unmodifiableList(items.getInternalList());
    }

    @Override
    public UniqueItemList getUniqueItemList() {
        return this.items;
    }

     @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TaskBook // instanceof handles nulls
                && this.items.equals(((TaskBook) other).items));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(items);
    }

}
