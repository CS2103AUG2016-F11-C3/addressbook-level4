package guitests;

import guitests.guihandles.ItemCardHandle;
import org.junit.Test;
import seedu.sudowudo.logic.commands.AddCommand;
import seedu.sudowudo.commons.core.Messages;
import seedu.sudowudo.testutil.TestItem;
import seedu.sudowudo.testutil.TestUtil;

import static org.junit.Assert.assertTrue;

public class AddCommandTest extends TaskBookGuiTest {

    //@@author A0144750J
    @Test
    public void add() {
        //add one item
        TestItem[] currentList = td.getTypicalItems();
        TestItem itemToAdd = td.help;
        assertAddSuccess(itemToAdd, currentList);
        currentList = TestUtil.addItemsToList(currentList, itemToAdd);

        //add another item
        itemToAdd = td.indeed;
        assertAddSuccess(itemToAdd, currentList);
        currentList = TestUtil.addItemsToList(currentList, itemToAdd);

        //add to empty list
        commandBox.runCommand("clear");
        assertAddSuccess(td.always);

        //invalid command
        commandBox.runCommand("adds Johnny");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);
    }
    
    //@@author A0144750J-reused
    private void assertAddSuccess(TestItem itemToAdd, TestItem... currentList) {
        commandBox.runCommand(itemToAdd.getAddCommand());

        //confirm the new card contains the right data
        ItemCardHandle addedCard = itemListPanel.navigateToItem(itemToAdd.getDescription().getFullDescription());
        assertMatching(itemToAdd, addedCard);

        //confirm the list now contains all previous persons plus the new person
        TestItem[] expectedList = TestUtil.addItemsToList(currentList, itemToAdd);
        assertTrue(itemListPanel.isListMatching(expectedList));
    }

}
