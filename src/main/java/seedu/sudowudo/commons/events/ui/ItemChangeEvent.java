package seedu.sudowudo.commons.events.ui;

import seedu.sudowudo.commons.events.BaseEvent;
import seedu.sudowudo.model.item.ReadOnlyItem;

/**
 * Indicates a request to jump to an item that has changed
 */
public class ItemChangeEvent extends BaseEvent {

    public final ReadOnlyItem item;

    public ItemChangeEvent(ReadOnlyItem item) {
        this.item = item;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
    
	public ReadOnlyItem getItem() {
		return this.item;
	}

}
