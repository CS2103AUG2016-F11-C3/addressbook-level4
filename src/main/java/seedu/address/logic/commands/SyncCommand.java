package seedu.address.logic.commands;

public class SyncCommand extends Command {

    public static final String COMMAND_WORD = "sync";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Syncs items between Sudowudo and a Google Calendar.\n"
            + "Parameters: " + COMMAND_WORD + " CALENDAR_NAME\n"
            + "Example: " + COMMAND_WORD + " " + "NUS Timetable\n";

    private final String targetCalendar;

    public SyncCommand(String targetCalendar) {
        this.targetCalendar = targetCalendar;
    }
    
    @Override
    public CommandResult execute() {
        assert model != null;
        
        return null;
    }

    @Override
    public CommandResult undo() {
        return null;
    }

}
