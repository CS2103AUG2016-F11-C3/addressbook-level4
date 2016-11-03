package seedu.sudowudo.commons.events.ui;

import seedu.sudowudo.commons.events.BaseEvent;

//@@author A0144750J
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
