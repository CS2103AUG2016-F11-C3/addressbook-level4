package seedu.sudowudo.model.tag;


import seedu.sudowudo.commons.exceptions.IllegalValueException;
import seedu.sudowudo.logic.parser.Parser;

/**
 * Represents a Tag in the task book.
 * Guarantees: immutable; name is valid as declared in {@link #isValidTagName(String)}
 */
public class Tag {

    public static final String MESSAGE_TAG_CONSTRAINTS = "Tags names should be alphanumeric and"
            + " cannot be the following words: \'done\', \'event\', \'task\', \'overdue\', \'(un)done\'";
    public static final String TAG_VALIDATION_REGEX = "\\p{Alnum}+";

    public String tagName;

    public Tag() {
    }

    /**
     * Validates given tag name.
     *
     * @throws IllegalValueException if the given tag name string is invalid.
     */
    public Tag(String name) throws IllegalValueException {
        assert name != null;
        name = name.trim();
        if (!isValidTagName(name)) {
            throw new IllegalValueException(MESSAGE_TAG_CONSTRAINTS);
        }
        this.tagName = name;
    }

    /**
     * Returns true if a given string is a valid tag name.
     * The tag must be alphanumeric, and cannot take the name of a type meta-tag,
     * e.g. Event, Task, Done
     */
    public static boolean isValidTagName(String test) {
        return test.matches(TAG_VALIDATION_REGEX) &&
                !Parser.isValidType(test);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Tag // instanceof handles nulls
                && this.toString().equals(((Tag) other).toString())); // state check
    }

    @Override
    public int hashCode() {
        return tagName.hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return '#' + tagName;
    }

}
