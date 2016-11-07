package seedu.sudowudo.logic.commands;

import java.util.ArrayList;

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

    protected static ArrayList<Hint> hints = new ArrayList<>();

    //@@author A0131560U
    private enum Type{
        TASK("task", "tasks"),
        EVENT("event", "events"), 
        DONE("done", "completed tasks"), 
        ITEM("item", "items"), 
        OVERDUE("overdue", "overdue tasks"), 
		UNDONE("undone", "incomplete tasks");
        
        private String typeName;
        private String typeMessage;

        Type(String name, String message) {
            this.typeName = name;
            this.typeMessage = message;
        }
        
        static Type fromString(String input) {
            for (Type type : values() ){
                if (type.typeName.equals(input)){
                    return type;
                }
            }
            return null;
        }


        public String getTypeName() {
            return this.typeName;
        }

        public Object getTypeMessage() {
            return this.typeMessage;
        }

		@Override
		public String toString() {
			return this.typeMessage;
		}
    }

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
		return new CommandResult(String.format(MESSAGE_SUCCESS, itemType.getMessage()));
    }

    @Override
    //@@author
    public CommandResult undo() {
        return new CommandResult(MESSAGE_UNDO_FAILURE);
    }

	/**
	 * Method to return hints for this command
	 * 
	 * @@author A0092390E
	 */
	public static ArrayList<Hint> getHints() {
		if (hints.size() == 0) {
			hints.add(new Hint("list all", "list", "list"));
			hints.add(new Hint("list all tasks", "list", "list task"));
			hints.add(new Hint("list all events", "list", "list event"));
			hints.add(new Hint("list all done", "list", "list done"));
			hints.add(new Hint("list all undone", "list", "list undone"));
			hints.add(new Hint("list all overdue", "list", "list overdue"));
		}
		return hints;
    }
}
