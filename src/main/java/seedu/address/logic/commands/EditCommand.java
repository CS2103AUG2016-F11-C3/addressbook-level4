package seedu.address.logic.commands;

import java.util.ArrayList;

import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.parser.DateTimeParser;
import seedu.address.model.item.Item;
import seedu.address.model.item.ReadOnlyItem;

/**
 * Adds a person to the address book.
 */
public class EditCommand extends Command {
	
	

	public static final String COMMAND_WORD = "edit";

	public static final String MESSAGE_USAGE = COMMAND_WORD + "... edit ..." + ": Edits an existing item.\n"
	        + "Syntax: for CONTEXT_ID edit FIELD:NEW_DETAIL\n"
			+ "Example: " + COMMAND_WORD + " 2 edit start:2000 on 07/10/2016";

	public static final String MESSAGE_SUCCESS = "Successfully edited: %1$s";
	public static final String MESSAGE_SUCCESS_TIME_NULL = "START or END time not found but new task added!";
	public static final String MESSAGE_DUPLICATE_ITEM = "This task already exists in the to-do list";
	public static final String MESSAGE_INVALID_FIELD = "The available fields are: desc/description, start, end, by and period";

	public static final String[] ALLOWED_FIELDS = { "desc", "description", "start", "end", "by", "period" };

	private static final String DEFAULT_ITEM_NAME = "BLOCK";
	
	private Item itemToModify;
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
	    FilteredList<Item> lastShownList = model.getFilteredEditableItemList();

		if (lastShownList.size() < targetIndex) {
			indicateAttemptToExecuteIncorrectCommand();
			hasUndo = false;
			return new CommandResult(Messages.MESSAGE_INVALID_ITEM_DISPLAYED_INDEX);
		}

		itemToModify = lastShownList.get(this.targetIndex - 1);

		for (String[] editField : editFields) {
			switch (editField[0]) {
			case "desc":
			case "description":
			    model.setItemDesc(itemToModify, editField[1]);
				break;
			case "start":
			    model.setItemStart(itemToModify, new DateTimeParser(editField[1]).extractStartDate());
			    break;
			case "end":
			case "by":
			    model.setItemEnd(itemToModify, new DateTimeParser(editField[1]).extractEndDate());
			    break;
			case "period":
			    DateTimeParser parser = new DateTimeParser(editField[1]);
			    model.setItemStart(itemToModify, parser.extractStartDate());
			    model.setItemEnd(itemToModify, parser.extractEndDate());
			    break;
			default:
				break;
			}
		}
		return new CommandResult(String.format(MESSAGE_SUCCESS, itemToModify), itemToModify);

	}

	@Override
	public CommandResult undo() {
		// TODO Auto-generated method stub
		return null;
	}

}
