package seedu.address.storage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.item.Description;
import seedu.address.model.item.Item;
import seedu.address.model.item.ReadOnlyItem;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;


/**
 * JAXB-friendly version of the Person.
 */
public class XmlAdaptedItem {

    @XmlElement(required = true)
    private String description;
    
    @XmlElement(required = true)
    private String startDate;
    
    @XmlElement(required = true)
    private String endDate;
    
    @XmlElement(required = true)
    private String isDone;
    
    @XmlElement
    private List<XmlAdaptedTag> tagged = new ArrayList<>();

    
    /**
     * No-arg constructor for JAXB use.
     */
    public XmlAdaptedItem() {}


    /**
     * Converts a given Item into this class for JAXB use.
     * @@author A0144750J
     * @param source future changes to this will not affect the created XmlAdaptedPerson
     */
    public XmlAdaptedItem(ReadOnlyItem source) {
    	// get XML description
        description = source.getDescription().getFullDescription();
        // get XML date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        if (source.getStartDate() == null) {
        	startDate = "";
        } else {
        	startDate = source.getStartDate().format(formatter);
        }
        
        if (source.getEndDate() == null) {
        	endDate = "";
        } else {
        	endDate = source.getEndDate().format(formatter);
        }
        // get XML isDone
        if (source.getIsDone()) {
        	isDone = "true";
        } else {
        	isDone = "false";
        }
        // get XML tags
        tagged = new ArrayList<>();
        for (Tag tag : source.getTags()) {
            tagged.add(new XmlAdaptedTag(tag));
        }
        
        if (source.getIsDone()) {
        	isDone = "true";
        } else {
        	isDone = "false";
        }
    }

    /**
     * Converts this jaxb-friendly adapted item object into the model's Item object.
     * @@author A0144750J
     * @throws IllegalValueException if there were any data constraints violated in the adapted person
     */
    public Item toModelType() throws IllegalValueException {
        final Description description = new Description(this.description);
        LocalDateTime start;
        LocalDateTime end;
        boolean isDone;
        UniqueTagList tags;
        // extract dates from XML data
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        if (this.startDate.equals("")) {
        	start = null;
        } else {
        	start = LocalDateTime.parse(startDate, formatter);
        }
        if (this.endDate.equals("")) {
        	end = null;
        } else {
        	end = LocalDateTime.parse(endDate, formatter);
        }
        // extract isDone from XML data
        assert !this.isDone.isEmpty();
        if (this.isDone.equals("true")) {
        	isDone = true;
        } else {
        	isDone = false;
        }
        // extract tags from XML data
        final List<Tag> itemTags = new ArrayList<>();
        for (XmlAdaptedTag tag : tagged) {
            itemTags.add(tag.toModelType());
        }
        tags = new UniqueTagList(itemTags);

		Item itemToReturn = new Item(description, start, end, tags);
		if (isDone.equals(false)) {
			itemToReturn.setIsDone(false);
		} else {
        	itemToReturn.setIsDone(true);
        }
        return itemToReturn;
    }
}