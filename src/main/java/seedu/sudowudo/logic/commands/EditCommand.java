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

    public static final String MESSAGE_USAGE = COMMAND_WORD + "... edit ..."
            + ": Edits an existing item.\n"
            + "Syntax: edit CONTEXT_ID FIELD:NEW_DETAIL\n" + "Example: "
            + COMMAND_WORD + " 2 start:tomorrow 6pm";

    public static final String MESSAGE_SUCCESS = "Successfully edited: %1$s";
    public static final String MESSAGE_DUPLICATE_ITEM = "This task already exists in the to-do list";
    public static final String MESSAGE_INVALID_FIELD = "The available fields are: desc/description, start, end, by and period";
    public static final String MESSAGE_UNDO_SUCCESS = "Undo edit task: %1$s";
    public static final String MESSAGE_UNDO_FAILURE = "Failed to undo edit task: %1$s";

    public static final String[] ALLOWED_FIELDS = { "desc", "description",
            "start", "end", "by", "period" };

    private Item itemToModify;
    private Item previousTemplate;
    public final int targetIndex;
    private ArrayList<String[]> editFields;

    //@@author A0092390E
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
    public EditCommand(Integer index, ArrayList<String> arguments)
            throws IllegalValueException {
        this.targetIndex = index;
        assert arguments.size() != 0;

        editFields = new ArrayList<>();
        for (String argument : arguments) {
            editFields.add(argument.split(":", 2));
        }

        previousTemplate = new Item(new Description("dummy"), null, null,
                new UniqueTagList());
    }
    //@@author

    //@@author A0147609X
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
            return new CommandResult(
                    Messages.MESSAGE_INVALID_ITEM_DISPLAYED_INDEX);
        }

        itemToModify = lastShownList.get(this.targetIndex - 1);

        // deep copy the item to a template for undo
        previousTemplate = itemToModify.deepCopy();

        for (String[] editField : editFields) {
            try {
                switch (editField[0]) {
                    case "desc":
                    case "description":
                        model.setItemDesc(itemToModify, editField[1]);
                        break;
                    case "start":
                        model.setItemStart(itemToModify,
                                new DateTimeParser(editField[1])
                                        .extractStartDate());
                        break;
                    case "end":
                    case "by":
                        model.setItemEnd(itemToModify,
                                new DateTimeParser(editField[1]).extractStartDate());
                        break;
                    case "period":
                        DateTimeParser parser = new DateTimeParser(editField[1]);
                        model.setPeriod(itemToModify, parser.extractStartDate(), parser.extractEndDate());
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
        return new CommandResult(String.format(MESSAGE_SUCCESS, itemToModify),
                itemToModify);

    }
    //@@author

    /**
     * @@author A0144750J
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
        return new CommandResult(
                String.format(MESSAGE_UNDO_SUCCESS, itemToModify),
                itemToModify);
    }

}
