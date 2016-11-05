package seedu.sudowudo.model.item;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.Comparator;
import java.util.Objects;
import java.util.Observable;

import seedu.sudowudo.commons.exceptions.IllegalValueException;
import seedu.sudowudo.commons.util.CollectionUtil;
import seedu.sudowudo.logic.parser.DateTimeParser;
import seedu.sudowudo.model.tag.UniqueTagList;

/**
 * Represents a Item in the task book. Guarantees: details are present and not
 * null, field values are validated.
 * 
 */
public class Item extends Observable implements ReadOnlyItem, Comparable<Item> {

    // @@author A0131560U
    public enum Type {
        TASK("task"), EVENT("event"), DONE("done"), ITEM("item"), OVERDUE("overdue"), UNDONE("undone");

        private String typeName;

        Type(String name) {
            this.typeName = name;
        }

        public String getTypeName() {
            return this.typeName;
        }
    }
    // @@author

    public static final String MESSAGE_DATE_CONSTRAINTS = "Start date must come before end date.";

    private UniqueTagList tags;
    private Description description;
    private boolean isDone;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    /**
     * constructor for floating item
     */
    public Item(Description desc, UniqueTagList tags) {
        assert desc != null;
        this.description = desc;
        this.isDone = false;
        this.tags = new UniqueTagList(tags);
    }

    // @@author A0147609X
    /**
     * constructor for an item with a definite start and end time
     * (non-recurring)
     * 
     * @param desc
     * @param start
     * @param end
     * @author darren
     */
    public Item(Description desc, LocalDateTime start, LocalDateTime end, UniqueTagList tags)
            throws IllegalValueException {
        assert !CollectionUtil.isAnyNull(desc);
        this.description = desc;
        if (!isValidInterval(start, end)) {
            throw new IllegalValueException(MESSAGE_DATE_CONSTRAINTS);
        }
        this.startDate = start;
        this.endDate = end;
        this.tags = new UniqueTagList(tags);
    }
    // @@author

    /**
     * constructor for an item with a definite end time only (non-recurring)
     * 
     * @param desc
     * @param end
     * @author darren
     */
    public Item(Description desc, LocalDateTime end, UniqueTagList tags) {
        assert !CollectionUtil.isAnyNull(desc);
        this.description = desc;
        this.endDate = end;
        this.tags = new UniqueTagList(tags);
    }

    /**
     * constructor for an item with all fields: description, start/end date,
     * tags and isDone
     * 
     * @param desc
     * @param end
     * @@author A0144750J
     */
    public Item(Description desc, LocalDateTime start, LocalDateTime end, UniqueTagList tags, boolean isDone) {
        assert !CollectionUtil.isAnyNull(desc);
        this.description = desc;
        this.startDate = start;
        this.endDate = end;
        this.tags = new UniqueTagList(tags);
        this.setIsDone(isDone);
    }

    /**
     * Copy constructor to build an Item from a ReadOnlyItem
     * 
     * @param source:
     *            ReadOnlyItem that can return Description, startDate, endDate
     *            and isDone;
     * @@author A0144750J
     */
    public Item(ReadOnlyItem source) {
        this(source.getDescription(), source.getStartDate(), source.getEndDate(), source.getTags(), source.getIsDone());
    }

    /* @@author A0092390E */

    @Override
    public UniqueTagList getTags() {
        return this.tags;
    }

    @Override
    public Description getDescription() {
        return description;
    }

    @Override
    public boolean getIsDone() {
        return isDone;
    }

    @Override
    public LocalDateTime getStartDate() {
        return startDate;
    }

    @Override
    public LocalDateTime getEndDate() {
        return endDate;
    }

    /**
     * Flexible property querying, to support listing and filtering
     * 
     * @return boolean, whether the item is or isn't
     * @@author A0092390E
     */
    @Override
    public boolean is(String query) {
        switch (query.toLowerCase()) {
        case "done":
            return this.getIsDone();
        case "undone":
            return !this.getIsDone();
        case "event":
            return this.getStartDate() != null;
        case "task":
            return this.getStartDate() == null;
        case "overdue":
            return this.is("task") && this.getEndDate() != null && this.getEndDate().isBefore(LocalDateTime.now());
        case "item":
            return true;
        default:
            return false;
        }
    }

    /**
     * Returns the type of the item. Useful for Item cards and messages.
     * 
     * @return The type of the item, computed based on start/end dates.
     *         [Event|Floating Task|Task]
     * @@author A0092390E
     */
    @Override
    public String getType() {
        if (this.getStartDate() != null) {
            return "Event";
        } else if (this.getEndDate() == null) {
            return "Floating Task";
        } else {
            return "Task";
        }
    }

    /**
     * Replaces this Item's tags with the tags in the argument tag list.
     */
    public void setTags(UniqueTagList replacement) {
        tags.setTags(replacement);
        setChanged();
        notifyObservers();
    }

    @Override
    public void setIsDone(boolean doneness) {
        this.isDone = doneness;
        setChanged();
        notifyObservers();
    }

    public void setStartDate(LocalDateTime startDate) throws IllegalValueException {
        if (!isValidInterval(startDate, this.endDate)) {
            throw new IllegalValueException(MESSAGE_DATE_CONSTRAINTS);
        }
        this.startDate = startDate;
        setChanged();
        notifyObservers();
    }

    public void setEndDate(LocalDateTime endDate) throws IllegalValueException {
        if (!isValidInterval(this.startDate, endDate)) {
            throw new IllegalValueException(MESSAGE_DATE_CONSTRAINTS);
        }
        this.endDate = endDate;
        setChanged();
        notifyObservers();
    }

    public void setPeriod(LocalDateTime startDate, LocalDateTime endDate) throws IllegalValueException {
        if (!isValidInterval(startDate, endDate)) {
            throw new IllegalValueException(MESSAGE_DATE_CONSTRAINTS);
        }
        this.startDate = startDate;
        this.endDate = endDate;
        setChanged();
        notifyObservers();
    }

    private boolean isValidInterval(LocalDateTime start, LocalDateTime end) {
        return start == null || end == null || start.isBefore(end);
    }

    public void setDescription(String desc) throws IllegalValueException {
        this.description.setFullDescription(desc);
        setChanged();
        notifyObservers();
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing
        // your own
        return Objects.hash(description);
    }

    @Override
    public String toString() {
        return description.toString();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ReadOnlyItem // instanceof handles nulls
                        && this.isSameStateAs((ReadOnlyItem) other));
    }

    // @@author A0147609X
    @Override
    /**
     * sort by start date then end date then alphabetically
     * for UI chronological sort
     * @author darren
     */
    public int compareTo(Item other) {
        LocalDateTime thisStart = assignDummyLDT(this.startDate);
        LocalDateTime thisEnd = assignDummyLDT(this.endDate);
        LocalDateTime otherStart = assignDummyLDT(other.getStartDate());
        LocalDateTime otherEnd = assignDummyLDT(other.getEndDate());
        
        // Assign same start/end date to a deadline for easier checking
        if (this.is(Type.TASK.toString())){
            thisStart = thisEnd;
        }
        if (other.is(Type.TASK.toString())){
            otherStart = otherEnd;
        }

        if(thisEnd.isBefore(otherEnd)) {
        // this item ends earlier
            return -1;
        } else if(thisEnd.isAfter(otherEnd)){
            return 1;
        } else if(thisStart.isBefore(otherStart)) {
            // this item starts earlier
            return -1;
        } else if(thisStart.isAfter(otherStart)) {
            // this item starts later
            return 1;
        }
        
        // same start and end date
        // sort alphabetically by description
        return description.compareTo(other.getDescription());
    }

    /**
     * assign the max LocalDateTime as a dummy to a java.time.LocalDateTime
     * object if necessary
     * 
     * @param checkee
     * @return
     * @author darren
     */
    private LocalDateTime assignDummyLDT(LocalDateTime checkee) {
        if (checkee == null) {
            return LocalDateTime.MAX;
        }

        return checkee;
    }
    // @@author

    // @@author A0144750J
    /**
     * Returns a deep copy of the current Item
     * 
     * @return deep copy of this Item
     * @author duc
     */
    public Item deepCopy() {
        Item duplicate;

        // copy each field to new item
        try {
            duplicate = new Item(new Description("dummy"), null, null, new UniqueTagList());
            duplicate.setDescription(this.getDescription().getFullDescription());
            duplicate.setStartDate(this.getStartDate());
            duplicate.setEndDate(this.getEndDate());
            duplicate.setIsDone(this.getIsDone());
            duplicate.setTags(this.getTags());
            return duplicate;
        } catch (IllegalValueException e) {
            e.printStackTrace();
        }

        return null;
    }
    // @@author

    // @@author A0147609X
    /**
     * Builds a pretty datetime line for this Item's card on the UI.
     * 
     * Nulls are handled by DateTimeParser.extractPrettyItemCardDateTime
     * 
     * @return
     * @author darren
     */
    @Override
    public String extractPrettyItemCardDateTime() {
        return DateTimeParser.extractPrettyItemCardDateTime(this.startDate, this.endDate);
    }

    /**
     * Gets the pretty explicit datetime for this Item's start datetime e.g.
     * "This Monday, 7:30PM" or "Mon 27 Nov, 9:30AM"
     * 
     * @return
     * @author darren
     */
    public String extractPrettyStartDateTime() {
        return DateTimeParser.extractPrettyDateTime(this.startDate);
    }

    /**
     * Gets the pretty explicit datetime for this Item's end datetime e.g. "This
     * Monday, 7:30PM" or "Mon 27 Nov, 9:30AM"
     * 
     * @return
     * @author darren
     */
    public String extractPrettyEndDateTime() {
        return DateTimeParser.extractPrettyDateTime(this.endDate);
    }

    /**
     * Gets the pretty relative datetime for this Item's start datetime e.g. "3
     * weeks from now"
     * 
     * @return EMPTY_STRING if datetime is null
     * @author darren
     */
    public String extractPrettyRelativeStartDateTime() {
        return DateTimeParser.extractPrettyRelativeDateTime(this.startDate);
    }

    /**
     * Gets the pretty relative datetime for this Item's end datetime e.g. "3
     * weeks from now"
     * 
     * @return EMPTY_STRING if datetime is null
     * @author darren
     */
    @Override
    public String extractPrettyRelativeEndDateTime() {
        if (this.endDate == null) {
            return extractPrettyRelativeStartDateTime();
        }
        return DateTimeParser.extractPrettyRelativeDateTime(this.endDate);
    }
    // @@author

    public static final Comparator<Item> chronologicalComparator = new Comparator<Item>() {
        @Override
        public int compare(Item x, Item y) {
            return x.compareTo(y);
        }
    };

}
