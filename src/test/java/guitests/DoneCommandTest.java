package guitests;

import org.junit.Test;

import seedu.sudowudo.commons.core.Messages;
import seedu.sudowudo.logic.commands.DoneCommand;
import seedu.sudowudo.testutil.TestItem;
import seedu.sudowudo.testutil.TestUtil;
import static org.junit.Assert.assertTrue;

/**
 * GUI test for undo commands for each: Add, Delete, Clear, Done and Edit command
 * @@author A0144750J
 */
public class DoneCommandTest extends TaskBookGuiTest {

    @Test
    public void done() {
        // mark first item as done
        TestItem[] currentList = td.getTypicalItems();
        int targetIndex = 1;
        assertDoneSuccess(targetIndex, currentList);
        
        // mark last item as done
        currentList = TestUtil.doneItemFromList(currentList, targetIndex);
        targetIndex = currentList.length;
        assertDoneSuccess(targetIndex, currentList);
        
        //delete from the middle of the list
        currentList = TestUtil.doneItemFromList(currentList, targetIndex);
        targetIndex = currentList.length/2;
        assertDoneSuccess(targetIndex, currentList);
        
        // invalid index, expect error
        commandBox.runCommand("done " + currentList.length + 1);
        assertResultMessage(Messages.MESSAGE_INVALID_ITEM_DISPLAYED_INDEX);
    }
    
    /**
     * Runs the done command to mark an item as done at specified index and confirms the result is correct.
     * @param targetIndexOneIndexed e.g. to delete the first person in the list, 1 should be given as the target index.
     * @param currentList A copy of the current list of persons (before deletion).
     */
    private void assertDoneSuccess(int targetIndexOneIndexed, final TestItem[] currentList) {
        TestItem[] expectedRemainder = TestUtil.doneItemFromList(currentList, targetIndexOneIndexed);

        commandBox.runCommand("done " + targetIndexOneIndexed);

        //confirm the list now contains all previous persons except the deleted person
        assertTrue(itemListPanel.isListMatching(expectedRemainder));

        //confirm the result message is correct
        assertResultMessage(DoneCommand.MESSAGE_DONE_ITEM_SUCCESS);
    }
    
}
