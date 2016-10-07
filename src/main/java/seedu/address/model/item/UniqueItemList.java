package seedu.address.model.item;

import java.util.Collection;
import java.util.Iterator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.exceptions.DuplicateDataException;
import seedu.address.commons.util.CollectionUtil;

/**
 * A list of Items that enforces uniqueness between its elements and does not
 * allow nulls.
 *
 * Supports a minimal set of list operations.
 *
 * @see Item#equals(Object)
 * @see CollectionUtil#elementsAreUnique(Collection)
 */
public class UniqueItemList implements Iterable<ReadOnlyItem> {

    /**
     * Signals that an operation would have violated the 'no duplicates' property of the list.
     */
	public static class DuplicateItemException extends DuplicateDataException {
		protected DuplicateItemException() {
			super("Operation would result in duplicate Items");
        }
    }

    /**
	 * Signals that an operation targeting a specified Item in the list would
	 * fail because there is no such matching Item in the list.
	 */
	public static class ItemNotFoundException extends Exception {
	}

	private final ObservableList<ReadOnlyItem> internalList = FXCollections.observableArrayList();

    /**
	 * Constructs empty ItemList.
	 */
    public UniqueItemList() {}

    /**
	 * Returns true if the list contains an equivalent Item as the given
	 * argument.
	 */
	public boolean contains(ReadOnlyItem toCheck) {
        assert toCheck != null;
        return internalList.contains(toCheck);
    }

    /**
	 * Adds a Item to the list.
	 *
	 * @throws DuplicateItemException
	 *             if the Item to add is a duplicate of an existing Item in the
	 *             list.
	 */
	public void add(ReadOnlyItem toAdd) throws DuplicateItemException {
        assert toAdd != null;
        if (contains(toAdd)) {
			throw new DuplicateItemException();
        }
        internalList.add(toAdd);
    }

    /**
	 * Removes the equivalent Item from the list.
	 *
	 * @throws ItemNotFoundException
	 *             if no such Item could be found in the list.
	 */
	public boolean remove(ReadOnlyItem toRemove) throws ItemNotFoundException {
        assert toRemove != null;
		final boolean ItemFoundAndDeleted = internalList.remove(toRemove);
		if (!ItemFoundAndDeleted) {
			throw new ItemNotFoundException();
        }
		return ItemFoundAndDeleted;
    }

	public ObservableList<ReadOnlyItem> getInternalList() {
        return internalList;
    }

    @Override
	public Iterator<ReadOnlyItem> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueItemList // instanceof handles nulls
                && this.internalList.equals(
                ((UniqueItemList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }
}
