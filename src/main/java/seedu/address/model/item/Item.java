package seedu.address.model.item;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.Objects;
import java.util.Observable;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.parser.DateTimeParser;
import seedu.address.model.tag.UniqueTagList;

/**
 * Represents a Item in the address book. Guarantees: details are present and
 * not null, field values are validated.
 */
public class Item extends Observable implements ReadOnlyItem, Comparable<Item> {

    private UniqueTagList tags;
    private Description description;
    private boolean isDone;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    // for recurring events only
    private boolean isRecurring;
    private Period recurInterval;
    private LocalDateTime recurEndDate;

    /**
     * constructor for floating item
     */
    public Item(Description desc) {
        // assert !CollectionUtil.isAnyNull(name, phone, email, address, tags);
        assert desc != null;
        this.description = desc;
        this.isDone = false;
        // this.tags = new UniqueTagList(tags); // protect internal tags from
        // changes in the arg list
    }

    /**
     * constructor for an item with a definite
     * start and end time (non-recurring)
     * @param desc
     * @param start
     * @param end
     * @author darren
     */
    public Item(Description desc, LocalDateTime start, LocalDateTime end) {
        assert !CollectionUtil.isAnyNull(desc);
        this.description = desc;
        this.startDate = start;
        this.endDate = end;
    }
    
	/**
	 * constructor for an item with a definite end time only (non-recurring)
	 * 
	 * @param desc
	 * @param end
	 * @author darren
	 */
    public Item(Description desc, LocalDateTime end) {
        assert !CollectionUtil.isAnyNull(desc);
        this.description = desc;
        this.endDate = end;
    }
    
    /**
     * Copy constructor to build an Item from a ReadOnlyItem
     * @param source: ReadOnlyItem that can return Description, startDate and EndDate
     */
    public Item(ReadOnlyItem source) {
        this(source.getDescription(), source.getStartDate(), source.getEndDate());
    }

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

    public boolean isRecurring() {
        return isRecurring;
    }

    public Period getRecurInterval() {
        return recurInterval;

    }
    public LocalDateTime getRecurEndDate() {
        return recurEndDate;
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

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
		setChanged();
		notifyObservers();
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
		setChanged();
		notifyObservers();
    }

    public void setRecurring(boolean isRecurring) {
        this.isRecurring = isRecurring;
		setChanged();
		notifyObservers();
    }

    public void setRecurInterval(Period recurInterval) {
        this.recurInterval = recurInterval;
		setChanged();
		notifyObservers();
    }

    public void setRecurEndDate(LocalDateTime recurEndDate) {
        this.recurEndDate = recurEndDate;
		setChanged();
		notifyObservers();
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

    @Override
    /**
     * sort by start date then end date then alphabetically
     * for UI chronological sort
     * @author darren
     */
    public int compareTo(Item other) {
        LocalDateTime thisStart, thisEnd, otherStart, otherEnd;
        
        thisStart = assignDummyLDT(startDate);
        thisEnd = assignDummyLDT(endDate);
        otherStart = assignDummyLDT(other.getStartDate());
        otherEnd = assignDummyLDT(other.getEndDate());
        
        if(thisStart.isBefore(otherStart)) {
            // this item starts earlier
            return -1;
        } else if(thisStart.isAfter(otherStart)) {
            // this item starts later
            return 1;
        } else {
            // both have same start datetime
            if(thisEnd.isBefore(otherEnd)) {
                return -1;
            } else if(thisEnd.isAfter(otherEnd)){
                return 1;
            }
        }
        
        // same start and end date
        // sort alphabetically by description
        return description.compareTo(other.getDescription());
    }

    /**
     * assign the max LocalDateTime as a dummy to a java.time.LocalDateTime
     * object if necessary
     * @param checkee
     * @return
     * @author darren
     */
    private LocalDateTime assignDummyLDT(LocalDateTime checkee) {
        if(checkee == null) {
            return LocalDateTime.MAX;
        }
        
        return checkee;
    }
    
    /**
     * Builds a pretty datetime line for this Item's card on the UI.
     * 
     * Nulls are handled by DateTimeParser.extractPrettyItemCardDateTime
     * @return
     * @author darren
     */
    public String extractPrettyItemCardDateTime() {
        return DateTimeParser.extractPrettyItemCardDateTime(this.startDate, this.endDate);
    }
    
    /**
     * Gets the pretty explicit datetime for this Item's start datetime
     * e.g. "This Monday, 7:30PM" or "Mon 27 Nov, 9:30AM"
     * @return
     */
    public String extractPrettyStartDateTime() {
        return DateTimeParser.extractPrettyDateTime(this.startDate);
    }

    /**
     * Gets the pretty explicit datetime for this Item's end datetime
     * e.g. "This Monday, 7:30PM" or "Mon 27 Nov, 9:30AM"
     * @return
     */
    public String extractPrettyEndDateTime() {
        return DateTimeParser.extractPrettyDateTime(this.endDate);
    }
    
    /**
     * Gets the pretty relative datetime for this Item's start datetime
     * e.g. "3 weeks from now"
     * @return EMPTY_STRING if datetime is null
     * @author darren
     */
    public String extractPrettyRelativeStartDateTime() {
        return DateTimeParser.extractPrettyRelativeDateTime(this.startDate);
    }

    /**
     * Gets the pretty relative datetime for this Item's end datetime
     * e.g. "3 weeks from now"
     * @return EMPTY_STRING if datetime is null
     * @author darren
     */
    public String extractPrettyRelativeEndDateTime() {
        if(this.endDate == null) {
            return extractPrettyRelativeStartDateTime();
        }
        return DateTimeParser.extractPrettyRelativeDateTime(this.endDate);
    }
}
