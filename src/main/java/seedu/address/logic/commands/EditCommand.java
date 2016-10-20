package seedu.address.logic.commands;

import java.time.LocalDateTime;
import java.util.ArrayList;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.parser.DateTimeParser;
import seedu.address.model.item.Description;
import seedu.address.model.item.Item;
import seedu.address.model.item.UniqueItemList;

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

	private static final String DEFAULT_ITEM_NAME = "BLOCK";
	
	private final Item toModify;
	private boolean hasTimeString = false;
	public final int targetIndex;

	
	/**
	 * Constructor using raw strings
	 * @param description: string containing description - required
	 * @param timeStr: the whole string containing start time and end time to be parsed. Not required
	 * @throws IllegalValueException
	 */
	public EditCommand(Integer index, ArrayList<String> arguments) throws IllegalValueException {
		this.targetIndex = index;
		assert descriptionStr != null;
		if (descriptionStr.equals("")) {
			descriptionStr = DEFAULT_ITEM_NAME;
		}
		if (timeStr != null && !timeStr.equals("")) {
			hasTimeString = true;
		}
		Description descriptionObj = new Description(descriptionStr);
		DateTimeParser parser = new DateTimeParser(timeStr);
		LocalDateTime startTimeObj = parser.extractStartDate();
		LocalDateTime endTimeObj = parser.extractEndDate();
		this.toModify = new Item(descriptionObj, startTimeObj, endTimeObj);
	}

	@Override
	public CommandResult execute() {
		assert model != null;
		try {
			model.addItem(toModify);
			// if user input something for time but it's not correct format
			if (this.hasTimeString && (this.toModify.getStartDate() == null || this.toModify.getEndDate() == null)) {
				return new CommandResult(MESSAGE_SUCCESS_TIME_NULL, toModify);
			} else {
				return new CommandResult(String.format(MESSAGE_SUCCESS, toModify), toModify);
			}
		} catch (UniqueItemList.DuplicateItemException e) {
			return new CommandResult(MESSAGE_DUPLICATE_ITEM);
		}

	}

}
