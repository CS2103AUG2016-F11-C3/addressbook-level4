package seedu.address.model;


import seedu.address.model.item.ReadOnlyItem;
import seedu.address.model.item.UniqueItemList;
import java.util.List;

/**
 * Unmodifiable view of an address book
 * @@author A0144750J-reused
 */
public interface ReadOnlyTaskBook {

    UniqueItemList getUniqueItemList();

    /**
     * Returns an unmodifiable view of persons list
     */
    List<ReadOnlyItem> getItemList();

}
