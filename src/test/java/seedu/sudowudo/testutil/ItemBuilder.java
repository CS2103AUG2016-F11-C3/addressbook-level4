package seedu.sudowudo.testutil;

import java.time.LocalDateTime;

import seedu.sudowudo.commons.exceptions.IllegalValueException;
import seedu.sudowudo.logic.parser.DateTimeParser;
import seedu.sudowudo.model.item.Description;
import seedu.sudowudo.model.tag.Tag;
import seedu.sudowudo.model.tag.UniqueTagList;

public class ItemBuilder {

    private TestItem item;
    
    public ItemBuilder(){
        this.item = new TestItem();
    }
    //@@author A0131560U
    public ItemBuilder withDescription(String description) throws IllegalValueException{
        this.item.setDescription(new Description(description));
        return this;
    }
    //@@author A0144750J
    public ItemBuilder withDates(String startdate) throws IllegalValueException{
    	DateTimeParser parser = new DateTimeParser(startdate);
		LocalDateTime startTimeObj = parser.extractStartDate();
		LocalDateTime endTimeObj = parser.extractEndDate();

		// if the item is an event
		if (item.is(Item.Type.EVENT)){
		    this.item.setStartDate(startTimeObj);
		}
		this.item.setEndDate(endTimeObj);
		return this;
    }
    
    //@@author
    public ItemBuilder withTags(String... tags) throws IllegalValueException{
        UniqueTagList replacement = new UniqueTagList(); 
        for (String tag : tags){
            replacement.add(new Tag(tag));
        }
        this.item.setTags(replacement);
        return this;
    }

    
    public TestItem build(){
        return this.item;
    }
    
}
