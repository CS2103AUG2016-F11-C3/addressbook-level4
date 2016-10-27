package seedu.address.logic.commands;

/**
 * Lists all persons in the address book to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";
    public static final String MESSAGE_UNDO_FAILURE = "";


    public static final String MESSAGE_SUCCESS = "Listed all items %1$s";
    public static final String MESSAGE_INVALID_TYPE = "List argument is invalid";    
    
    public static final Object MESSAGE_USAGE = COMMAND_WORD + " lists items that match the given parameter "
            + "Parameter: \"OPTIONAL_TYPE_ARGUMENT (task, event, done, overdue, undone)\" "
            + "Example: list task";

    //@@author A0131560U
    private enum Type{
        TASK("task", "that are a task"),
        EVENT("event", "that are an event"), 
        DONE("done", "that are done"), 
        ITEM("item", ""), 
        OVERDUE("overdue", "that are overdue"), 
        UNDONE("undone", "that are not done");
        
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
    }

    private Type itemType;
    
    public ListCommand(String argument) {
        this.itemType = Type.fromString(argument);
    }

    @Override
    //@@author A0131560U
    public CommandResult execute() {
        model.updateFilteredListDefaultPredicate(itemType.getTypeName());
    	hasUndo = false;
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public CommandResult undo() {
        return new CommandResult(MESSAGE_UNDO_FAILURE);
    }
}
