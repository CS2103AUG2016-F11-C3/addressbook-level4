//@@author A0144750J
package seedu.sudowudo.commons.events.ui;

import seedu.sudowudo.commons.events.BaseEvent;

/**
 * Indicates a request to jump down 5 places
 */
public class ListPageDownEvent extends BaseEvent {
    
    public int jumpStep;
    
    public ListPageDownEvent(int jumpStep) {
        this.jumpStep = jumpStep;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
