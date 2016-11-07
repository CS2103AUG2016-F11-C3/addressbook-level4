package seedu.sudowudo.testutil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Objects;
import java.util.Observable;

import seedu.sudowudo.logic.parser.DateTimeParser;
import seedu.sudowudo.model.item.Description;
import seedu.sudowudo.model.item.ReadOnlyItem;
import seedu.sudowudo.model.item.Item.Type;
import seedu.sudowudo.model.tag.UniqueTagList;

public class TestItem extends Observable implements ReadOnlyItem, Comparable<TestItem>{


    private UniqueTagList tags;
    private Description description;
    private boolean isDone;
    private LocalDateTime startDate;
    private LocalDateTime endDate; 
    
    /**
     * Every field must be present and not null.
     */
    public TestItem() {
        tags = new UniqueTagList();
        this.isDone = false;
    }

    public void setDescription(Description description){
        this.description = description;
    }

    @Override
	public Description getDescription() {
        return description;
    }
    
    @Override
   	public LocalDateTime getStartDate() {
   		return startDate;
   	}

   	public void setStartDate(LocalDateTime startDate) {
   		this.startDate = startDate;
   	}
   	
   	@Override
   	public LocalDateTime getEndDate() {
   		return endDate;
   	}

   	public void setEndDate(LocalDateTime endDate) {
   		this.endDate = endDate;
   	}
    
    public void setDone(){
        this.isDone = true;
    }
    
    public void setUndone(){
        this.isDone = false;
    }
    
    @Override
	public boolean getIsDone() {
        return this.isDone;
    }

    @Override
    public UniqueTagList getTags() {
        return new UniqueTagList(tags);
    }

    /**
     * Replaces this Item's tags with the tags in the argument tag list.
     */
    public void setTags(UniqueTagList replacement) {
        tags.setTags(replacement);
    }
    
    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(description);
    }

    @Override
    public String toString() {
        return description.toString();
    }

    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }
    
    //@@author A0144750J
    public String getAddCommand() {
    	assert this.getDescription() != null;
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String startTime = new String();
        String endTime = new String();
        if (this.getStartDate() == null) {
        	startTime = " ";
        } else {
        	startTime = this.getStartDate().format(formatter);
        }
        if (this.getEndDate() == null) {
        	endTime = " ";
        } else {
        	endTime = this.getEndDate().format(formatter);
        }
        sb.append("add \"" + this.getDescription().getFullDescription() + "\" from " + startTime + " to " + endTime);
        return sb.toString();
    }

	// @@author A0092390E-repeated
    @Override
    public boolean is(Type query) {
        switch (query) {
        case DONE:
            return this.getIsDone();
        case UNDONE:
            return !this.getIsDone();
        case EVENT:
            return this.getStartDate() != null;
        case TASK:
            return this.getStartDate() == null;
        case OVERDUE:
            return this.is(Type.TASK) && this.getEndDate() != null && this.getEndDate().isBefore(LocalDateTime.now());
        case ITEM:
            return true;
        default:
            return false;
        }
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
    @Override
    public String extractPrettyRelativeEndDateTime() {
        if(this.endDate == null) {
            return extractPrettyRelativeStartDateTime();
        }
        return DateTimeParser.extractPrettyRelativeDateTime(this.endDate);
    }

    @Override
    public String extractPrettyItemCardDateTime() {
        return DateTimeParser.extractPrettyDateTime(this.startDate);
    }

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
	
    public static final Comparator<TestItem> chronologicalComparator = new Comparator<TestItem>(){
        @Override
		public int compare(TestItem x, TestItem y) {
            return x.compareTo(y);
        }
    };

    //@@author A0147609X-reused
    @Override
    /**
     * sort by start date then end date then alphabetically
     * for UI chronological sort
     * @author darren
     */
    public int compareTo(TestItem other) {
        LocalDateTime thisStart = assignDummyLDT(this.startDate);
        LocalDateTime thisEnd = assignDummyLDT(this.endDate);
        LocalDateTime otherStart = assignDummyLDT(other.getStartDate());
        LocalDateTime otherEnd = assignDummyLDT(other.getEndDate());

        // Assign same start/end date to a deadline for easier checking
        if (this.is(Type.TASK)) {
            thisStart = thisEnd;
        }
        if (other.is(Type.TASK)) {
            otherStart = otherEnd;
        }

        if (thisEnd.isBefore(otherEnd)) {
            // this item ends earlier
            return -1;
        } else if (thisEnd.isAfter(otherEnd)) {
            return 1;
        } else if (thisStart.isBefore(otherStart)) {
            // this item starts earlier
            return -1;
        } else if (thisStart.isAfter(otherStart)) {
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
    //@@author


}
