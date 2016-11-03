package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;

//@@author A0144750J
/**
 * Indicates a request to jump up 5 places
 */
public class CycleCommandHistoryEvent extends BaseEvent {
    
    public String userIput;

    public CycleCommandHistoryEvent(String userInput) {
        this.userIput = userInput;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
