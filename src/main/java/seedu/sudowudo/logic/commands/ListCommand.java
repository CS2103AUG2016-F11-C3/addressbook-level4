package seedu.sudowudo.logic.commands;

import seedu.sudowudo.model.item.Item;

/**
 * Lists all items in the task book to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";
    public static final String MESSAGE_UNDO_FAILURE = "";


	public static final String MESSAGE_SUCCESS = "Listed all %1$s";
    public static final String MESSAGE_INVALID_TYPE = "List argument is invalid";    
    
	public static final Object MESSAGE_USAGE = COMMAND_WORD + " lists items that match the given parameter"
			+ "Parameter: \"OPTIONAL_TYPE_ARGUMENT (task, event, done, overdue, undone)\""
            + "Example: list task";


    private Item.Type itemType;
    
    //@@author 
    public ListCommand(String argument) {
        this.itemType = Item.Type.fromString(argument);
    }

    @Override
    //@@author A0131560U
    public CommandResult execute() {
       model.updateDefaultPredicate(itemType);
    	hasUndo = false;
		return new CommandResult(String.format(MESSAGE_SUCCESS, itemType));
    }

    @Override
    //@@author
    public CommandResult undo() {
        return new CommandResult(MESSAGE_UNDO_FAILURE);
    }
}
