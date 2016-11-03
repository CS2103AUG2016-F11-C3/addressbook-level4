package seedu.address.logic.commands;

import java.util.List;

import seedu.address.gcal.Interfacer;
import seedu.address.model.item.Item;
import seedu.address.model.item.UniqueItemList.DuplicateItemException;

public class SyncCommand extends Command {

    public static final String COMMAND_WORD = "sync";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Syncs items between Sudowudo and a Google Calendar.\n"
            + "Parameters: " + COMMAND_WORD + " CALENDAR_NAME\n"
            + "Example: " + COMMAND_WORD + " " + "NUS Timetable\n";
    public static final String MESSAGE_SUCCESS = "Synchronization successful!";
    public static final String MESSAGE_FAILURE = "Synchronization failed.";

    private final String targetCalendar;
    
    // for communicating with the google calendar API
    private static final Interfacer interfacer = new Interfacer();

    public SyncCommand(String targetCalendar) {
        this.hasUndo = false;
        this.targetCalendar = targetCalendar;
    }
    
    @Override
    public CommandResult execute() {
        assert model != null;
        
        // pull items from remote calendar
        List<Item> toAdd = interfacer.pullItems(this.targetCalendar);
        for(Item item : toAdd) {
            try {
                model.addItem(item);
            } catch (DuplicateItemException e) {
                // do nothing if attempted to add duplicate
            }
        }
        
        // push
        
        return null;
    }

    @Override
    public CommandResult undo() {
        return null;
    }

}
