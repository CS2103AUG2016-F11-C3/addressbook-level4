package seedu.address.model.item;

public class FloatingTask extends Item implements ReadOnlyItem {

	public FloatingTask(Description desc) {
		super(desc);
	}
	
    /**
     * Copy constructor.
     */
    public FloatingTask(ReadOnlyItem source) {
        this(source.getDescription());
    }


}
