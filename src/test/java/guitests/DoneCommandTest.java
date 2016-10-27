package guitests;

import org.junit.Test;
import seedu.address.logic.commands.DoneCommand;
import seedu.address.testutil.TestItem;
import seedu.address.testutil.TestUtil;
import static org.junit.Assert.assertTrue;

/**
 * GUI test for undo commands for each: Add, Delete, Clear, Done and Edit command
 * @author Darren Le
 * @@author A0144750J
 */
public class DoneCommandTest extends TaskBookGuiTest {

    @Test
    public void done() {
        // mark one item as done
        TestItem[] currentList = td.getTypicalItems();
        int targetIndex = 1;
        assertDoneSuccess(targetIndex, currentList);
    }
    
    /**
     * 
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
