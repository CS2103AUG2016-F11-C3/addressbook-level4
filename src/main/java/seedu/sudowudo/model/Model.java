package seedu.sudowudo.model;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.function.Predicate;

import javafx.collections.transformation.FilteredList;
import seedu.sudowudo.commons.core.UnmodifiableObservableList;
import seedu.sudowudo.logic.commands.Command;
import seedu.sudowudo.model.item.Item;
import seedu.sudowudo.model.item.ReadOnlyItem;
import seedu.sudowudo.model.item.UniqueItemList;

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

    /** Adds the given person */
    void addItem(Item item) throws UniqueItemList.DuplicateItemException;
    
    /** Edit the given Item's description */
    void setItemDesc(Item item, String desc);
    
    /** Edit the given Item's start datetime */
    void setItemStart(Item item, LocalDateTime start);
    
    /** Edit the given Item's end datetime */
    void setItemEnd(Item item, LocalDateTime end);
    
    // @@author A0144750J
    /** Set the item isDone field to true */
    void setDoneItem(Item item);
    
    // @@author A0144750J
    /** Set the item isDone field to true */
    void setNotDoneItem(Item item);
    
    // @@author A0144750J
    /** Add the command to stack for undo */
    void addCommandToStack(Command command);
    
    // @@author A0144750J
    /** Add the command to stack for undo */
    Command returnCommandFromStack();
    
    // @@author A0144750J
    /** Add the command to history */
    void addCommandToHistory(String command);
    // @@author
    
    // @@author A0144750J
    /** Return command from history */
    String returnCommandFromHistory(int index);
    // @@author
    
    //@@author A0144750J
    /** Return the current size of command history */
    int getHistorySize();
    //@@author
    
    //@@author A0144750J
    /** Refresh the current list showing in the same predicate */
    void refreshInCurrentPredicate();
    //@@author
    
    /** Returns the filtered item list as an {@code UnmodifiableObservableList<ReadOnlyPerson>} */
    UnmodifiableObservableList<ReadOnlyItem> getFilteredItemList();

    /** Returns the filtered person list as an {@code UnmodifiableObservableList<ReadOnlyPerson>} */
    FilteredList<Item> getFilteredEditableItemList();
    
    /** Updates the filter of the filtered person list to show all persons */
    void updateFilteredListToShowAll();

    /** Updates the filter of the filtered item list to filter by the given keywords*/
    void updateFilteredItemList(Set<String> keywords);

    /** Updates the default filter of the item list to filter by a specific task type limiter */
    void updateDefaultPredicate(String taskType);

}
