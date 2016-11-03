package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;

//@@author A0144750J
/**
 * Indicates a request to jump down 5 places
 */
public class ListPageDownEvent extends BaseEvent {

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
