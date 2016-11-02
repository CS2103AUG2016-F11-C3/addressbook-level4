package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;

/**
 * Indicates a request to jump up 5 places
 */
public class ListPageUpEvent extends BaseEvent {

    public ListPageUpEvent() {

    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
