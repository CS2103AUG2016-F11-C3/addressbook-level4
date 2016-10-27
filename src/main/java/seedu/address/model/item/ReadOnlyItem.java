package seedu.address.model.item;

import java.time.LocalDateTime;
import java.util.Observer;

import seedu.address.model.tag.UniqueTagList;

/**
 * A read-only immutable interface for a Task in the todo list. Implementations
 * should guarantee: details are present and not null, field values are
 * validated.
 */
public interface ReadOnlyItem {

	Description getDescription();

	boolean getIsDone();

	// THIS IS TEMPORARY (@darren)
	void setIsDone(boolean doneness);

	/**
	 * These return the start and end dates of the item (if they exist). If not,
	 * then the default null is used. Override these in the implemented classes
	 * as necessary.
	 */
	default LocalDateTime getStartDate() {
		return null;
	}

	default LocalDateTime getEndDate() {
		return null;
	}
	
    /**
     * Flexible property querying, to support listing and filtering
     * Returns boolean, whether the item does or doesn't match query*/
    public boolean is(String query);

	
	/**
	 * The returned TagList is a deep copy of the internal TagList, changes on
	 * the returned list will not affect the person's internal tags.
	 */
	UniqueTagList getTags();

	/**
	 * Returns true if both have the same state. (interfaces cannot override
	 * .equals)
	 */
	default boolean isSameStateAs(ReadOnlyItem other) {
		return this == other
				|| (other != null
				&& this.getDescription().equals(((ReadOnlyItem) other).getDescription()))
		        && this.getTags().equals(((ReadOnlyItem) other).getTags());
	}

	/**
	 * Formats the Item as text, showing the description and tags
	 */
	default String getAsText() {
		final StringBuilder builder = new StringBuilder();
		builder.append(getDescription());
		getTags().forEach(builder::append);
		builder.append(getStartDate());
		builder.append(getEndDate());
		return builder.toString();
	}

	/**
	 * Returns a string representation of this Item's tags
	 */
	default String tagsString() {
		final StringBuffer buffer = new StringBuffer();
		final String separator = ", ";
		getTags().forEach(tag -> buffer.append(tag).append(separator));
		if (buffer.length() == 0) {
			return "";
		} else {
			return buffer.substring(0, buffer.length() - separator.length());
		}
	}

	public void addObserver(Observer o);

	String extractPrettyItemCardDateTime();

	String extractPrettyRelativeEndDateTime();
}
