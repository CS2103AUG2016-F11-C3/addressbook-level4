package seedu.sudowudo.commons.events.ui;

import seedu.sudowudo.commons.events.BaseEvent;
import seedu.sudowudo.model.item.ReadOnlyItem;

/**
 * Represents a selection change in the Person List Panel
 */
public class ItemPanelSelectionChangedEvent extends BaseEvent {


    private final ReadOnlyItem newSelection;

    public ItemPanelSelectionChangedEvent(ReadOnlyItem newSelection){
        this.newSelection = newSelection;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public ReadOnlyItem getNewSelection() {
        return newSelection;
    }
}
