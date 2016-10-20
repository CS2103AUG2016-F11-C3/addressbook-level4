package seedu.address.logic.commands;

import java.util.ArrayList;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.item.Item;
import seedu.address.model.item.ReadOnlyItem;

/**
 * Adds a person to the address book.
 */
public class EditCommand extends Command {
	
	

	public static final String COMMAND_WORD = "for";

	public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits an existing item. " + "CONTEXT_ID FIELD:NEW"
			+ "Example: " + COMMAND_WORD + "2 start:2000 on 07/10/2016";

	public static final String MESSAGE_SUCCESS = "Successfully edited: %1$s";
	public static final String MESSAGE_SUCCESS_TIME_NULL = "START or END time not found but new task added!";
	public static final String MESSAGE_DUPLICATE_ITEM = "This task already exists in the to-do list";
	public static final String MESSAGE_INVALID_FIELD = "The available fields are: desc/description,start and end";

	public static final String[] ALLOWED_FIELDS = { "desc", "description", "start", "end" };

	private static final String DEFAULT_ITEM_NAME = "BLOCK";
	
	private Item toModify;
	private boolean hasTimeString = false;
	public final int targetIndex;
	private ArrayList<String[]> editFields;

	
	/**
	 * Constructor using raw strings
	 * 
	 * @param index:
	 *            contextual Index of the Item being referenced
	 * @param arguments:
	 *            ArrayList of Strings (previously comma-delimited), each of
	 *            which is a field:value pair
	 * @throws IllegalValueException
	 */
	public EditCommand(Integer index, ArrayList<String> arguments) throws IllegalValueException {
		this.targetIndex = index;
		assert arguments.size() != 0;

		editFields = new ArrayList<>();
		for(String argument : arguments){
			editFields.add(argument.split(":", 2));
		}
	}

	@Override
	public CommandResult execute() {
		UnmodifiableObservableList<ReadOnlyItem> lastShownList = model.getFilteredItemList();
		if (lastShownList.size() < targetIndex) {
			indicateAttemptToExecuteIncorrectCommand();
			return new CommandResult(Messages.MESSAGE_INVALID_ITEM_DISPLAYED_INDEX);
		}
		toModify = (Item) lastShownList.get(this.targetIndex - 1);
		// if user input something for time but it's not correct format
//			if (this.hasTimeString && (this.toModify.getStartDate() == null || this.toModify.getEndDate() == null)) {
//				return new CommandResult(MESSAGE_SUCCESS_TIME_NULL, toModify);
		for (String[] editField : editFields) {
			switch (editField[0]) {
			case "desc":
			case "description":
				try {
					toModify.setDescription(editField[1]);
				} catch (IllegalValueException e) {
				}
				break;
			default:
				break;
			}
		}
		return new CommandResult(String.format(MESSAGE_SUCCESS, toModify), toModify);

	}

}
