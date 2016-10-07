package seedu.address.testutil;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.tag.Tag;
import seedu.address.model.item.Description;
import seedu.address.model.person.*;

/**
 *
 */
public class FloatingTaskBuilder {

    private TestFloatingTask task;

    public FloatingTaskBuilder() {
        this.task = new TestFloatingTask();
    }

    public FloatingTaskBuilder withDescription(String description) throws IllegalValueException {
        this.task.setDescription(new Description(description));
        return this;
    }


    public TestFloatingTask build() {
        return this.task;
    }

}
