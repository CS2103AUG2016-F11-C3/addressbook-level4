package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;

/**
 * Indicates a request to jump down 5 places
 */
public class ListPageDownEvent extends BaseEvent {

    public ListPageDownEvent() {
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
