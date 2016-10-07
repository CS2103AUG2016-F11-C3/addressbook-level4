package seedu.address.testutil;

import seedu.address.model.tag.UniqueTagList;
import seedu.address.model.item.*;

/**
 * A mutable person object. For testing only.
 */
public class TestFloatingTask implements ReadOnlyItem {

    private Description desciption;

    public TestFloatingTask() {
        
    }
    
    public void setDescription(Description description) {
    	this.desciption = description;
    }
    
    @Override 
    public Description getDescription() {
    	return this.desciption;
    }
    
    @Override
    public UniqueTagList getTags() {
        return new UniqueTagList();
    }

    @Override
    public String toString() {
        return getAsText();
    }

    public String getAddCommand() {
        StringBuilder sb = new StringBuilder();
        sb.append("add " + "\"" + this.getDescription() + "\"" + " ");
        return sb.toString();
    }
}
