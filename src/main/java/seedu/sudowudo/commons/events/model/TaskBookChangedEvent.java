package seedu.sudowudo.commons.events.model;

import seedu.sudowudo.commons.events.BaseEvent;
import seedu.sudowudo.model.ReadOnlyTaskBook;

/** Indicates the TaskBook in the model has changed*/
public class TaskBookChangedEvent extends BaseEvent {

    public final ReadOnlyTaskBook data;

    public TaskBookChangedEvent(ReadOnlyTaskBook data){
        this.data = data;
    }

    @Override
    public String toString() {
        return "number of items " + data.getItemList().size();// + ", number of tags " + data.getTagList().size();
    }
}
