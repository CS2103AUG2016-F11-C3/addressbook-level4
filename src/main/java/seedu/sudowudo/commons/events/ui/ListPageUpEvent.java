//@@author A0144750J
package seedu.sudowudo.commons.events.ui;

import seedu.sudowudo.commons.events.BaseEvent;

/**
 * Indicates a request to jump up 5 places
 */
public class ListPageUpEvent extends BaseEvent {

    public int jumpStep;
    
    public ListPageUpEvent(int jumpStep) {
        this.jumpStep = jumpStep;
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
