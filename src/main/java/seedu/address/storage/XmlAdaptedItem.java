package seedu.address.storage;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.parser.DateTimeParser;
import seedu.address.model.item.*;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;

import javax.xml.bind.annotation.XmlElement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


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
    
    @XmlElement
    private List<XmlAdaptedTag> tagged = new ArrayList<>();
    
    
    /**
     * No-arg constructor for JAXB use.
     */
    public XmlAdaptedItem() {}


    /**
     * Converts a given Item into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedPerson
     */
    public XmlAdaptedItem(ReadOnlyItem source) {

        description = source.getDescription().getFullDescription();
        
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
            
        tagged = new ArrayList<>();
        for (Tag tag : source.getTags()) {
            tagged.add(new XmlAdaptedTag(tag));
        }

    }

    /**
     * Converts this jaxb-friendly adapted item object into the model's Item object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person
     */
    public Item toModelType() throws IllegalValueException {
        final Description description = new Description(this.description);
        LocalDateTime start;
        LocalDateTime end;
        UniqueTagList tags;
        
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
        final List<Tag> itemTags = new ArrayList<>();
        for (XmlAdaptedTag tag : tagged) {
            itemTags.add(tag.toModelType());
        }
        
        tags = new UniqueTagList(itemTags);

        return new Item(description, start, end, tags);
    }
}
