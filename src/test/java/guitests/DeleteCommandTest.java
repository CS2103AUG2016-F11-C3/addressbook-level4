package guitests;

import org.junit.Test;
import seedu.sudowudo.testutil.TestItem;
import seedu.sudowudo.testutil.TestUtil;

import static org.junit.Assert.assertTrue;
import static seedu.sudowudo.logic.commands.DeleteCommand.MESSAGE_DELETE_ITEM_SUCCESS;

public class DeleteCommandTest extends TaskBookGuiTest {

    @Test
    public void delete() {

        //delete the first in the list
        TestItem[] currentList = td.getTypicalItems();
        int targetIndex = 1;
        assertDeleteSuccess(targetIndex, currentList);

        //delete the last in the list
        currentList = TestUtil.removeItemFromList(currentList, targetIndex);
        targetIndex = currentList.length;
        assertDeleteSuccess(targetIndex, currentList);

        //delete from the middle of the list
        currentList = TestUtil.removeItemFromList(currentList, targetIndex);
        targetIndex = currentList.length/2;
        assertDeleteSuccess(targetIndex, currentList);

        //invalid index
        commandBox.runCommand("delete " + currentList.length + 1);
        assertResultMessage("The item index provided is invalid");

    }

    /**
     * Runs the delete command to delete the person at specified index and confirms the result is correct.
     * @param targetIndexOneIndexed e.g. to delete the first person in the list, 1 should be given as the target index.
     * @param currentList A copy of the current list of persons (before deletion).
     */
    private void assertDeleteSuccess(int targetIndexOneIndexed, final TestItem[] currentList) {
        TestItem personToDelete = currentList[targetIndexOneIndexed-1]; //-1 because array uses zero indexing
        TestItem[] expectedRemainder = TestUtil.removeItemFromList(currentList, targetIndexOneIndexed);

        commandBox.runCommand("delete " + targetIndexOneIndexed);

        //confirm the list now contains all previous persons except the deleted person
        assertTrue(itemListPanel.isListMatching(expectedRemainder));

        //confirm the result message is correct
        assertResultMessage(String.format(MESSAGE_DELETE_ITEM_SUCCESS, personToDelete));
    }

}
