package seedu.sudowudo.logic.commands;

import java.util.ArrayList;

import javafx.collections.transformation.FilteredList;
import seedu.sudowudo.commons.core.Messages;
import seedu.sudowudo.commons.exceptions.IllegalValueException;
import seedu.sudowudo.logic.parser.DateTimeParser;
import seedu.sudowudo.model.item.Description;
import seedu.sudowudo.model.item.Item;
import seedu.sudowudo.model.tag.UniqueTagList;

/**
 * Adds a person to the task book.
 * 
 * @@author A0092390E
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";
    public static final String MESSAGE_USAGE = COMMAND_WORD + "... edit ..." + ": Edits an existing item.\n"
            + "Syntax: edit CONTEXT_ID FIELD:NEW_DETAIL\n" + "Example: " + COMMAND_WORD
            + " 2 start:tomorrow 6pm";

    public static final String MESSAGE_SUCCESS = "Successfully edited: %1$s";
    public static final String MESSAGE_DUPLICATE_ITEM = "This task already exists in the to-do list";
    public static final String MESSAGE_INVALID_FIELD = "The available fields are: desc/description, start, end, by and period";
    public static final String MESSAGE_UNDO_SUCCESS = "Undo edit task: %1$s";
    public static final String MESSAGE_UNDO_FAILURE = "Failed to undo edit task: %1$s";


    private DateTimeParser dtParser = DateTimeParser.getInstance();

    protected static ArrayList<Hint> hints = new ArrayList<>();

    private Item itemToModify;
    private Item previousTemplate;
    private ArrayList<String[]> editFields;
    private final int targetIndex;


    // @@author A0092390E
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
        for (String argument : arguments) {
            editFields.add(argument.split(":", 2));
        }

        previousTemplate = new Item(new Description("dummy"), null, null, new UniqueTagList());
    }
    // @@author

    // @@author A0147609X
    /**
     * Executes the edit command.
     * 
     * @author darren yuchuan
     */
    @Override
    public CommandResult execute() {
        FilteredList<Item> lastShownList = model.getFilteredEditableItemList();

        if (lastShownList.size() < targetIndex) {
            indicateAttemptToExecuteIncorrectCommand();
            hasUndo = false;
            return new CommandResult(Messages.MESSAGE_INVALID_ITEM_DISPLAYED_INDEX);
        }

        itemToModify = lastShownList.get(this.targetIndex - 1);

        // deep copy the item to a template for undo
        previousTemplate = itemToModify.deepCopy();

        for (String[] editStruct : editFields) {
            String fieldToEdit = editStruct[0];
            String newFieldDetail = editStruct[1];

            try {
                switch (fieldToEdit) {
                case "desc":
                case "description":
                    model.setItemDesc(itemToModify, newFieldDetail);
                    break;
                case "start":
                    model.setItemStart(itemToModify, dtParser.parse(newFieldDetail).extractStartDate());
                    break;
                case "end":
                case "by":
                    model.setItemEnd(itemToModify, dtParser.parse(newFieldDetail).extractStartDate());
                    break;
                case "period":
                    dtParser.parse(newFieldDetail);
                    model.setItemStart(itemToModify, dtParser.extractStartDate());
                    model.setItemEnd(itemToModify, dtParser.extractEndDate());
                    break;
                default:
                    // field names not valid
                    return new CommandResult(MESSAGE_INVALID_FIELD);
                }
            } catch (IllegalValueException ive) {
                return new CommandResult(ive.getMessage());
            }
        }
        
        hasUndo = true;
        model.refreshInCurrentPredicate();
        return new CommandResult(String.format(MESSAGE_SUCCESS, itemToModify),
                itemToModify);
    }
    // @@author

    //@@author A0144750J
    /**
     *
     */
    @Override
    public CommandResult undo() {
        // deep copy the item to a template for undo
        try{
            model.setItemDesc(itemToModify, previousTemplate.getDescription().getFullDescription());
            model.setItemStart(itemToModify, previousTemplate.getStartDate());
            model.setItemEnd(itemToModify, previousTemplate.getEndDate());
            itemToModify.setIsDone(previousTemplate.getIsDone());
            itemToModify.setTags(previousTemplate.getTags());
        } catch (IllegalValueException ive){
            assert false: "Original item values not valid";
        }
        model.refreshInCurrentPredicate();
        return new CommandResult(
                String.format(MESSAGE_UNDO_SUCCESS, itemToModify), itemToModify);
    }

	/**
	 * Method to return hints for this command
	 * 
	 * @@author A0092390E
	 */
	public static ArrayList<Hint> getHints() {
		if (hints.size() == 0) {
			hints.add(new Hint("edit task", "edit",
					"edit CONTEXT_ID desc|start|end|by|period:NEW_VALUE [desc.."));

		}
		return hints;
	}
}
