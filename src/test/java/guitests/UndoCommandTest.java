package guitests;

import guitests.guihandles.ItemCardHandle;
import org.junit.Test;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.commons.core.Messages;
import seedu.address.testutil.TestItem;
import seedu.address.testutil.TestUtil;

import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.DeleteCommand.MESSAGE_DELETE_ITEM_SUCCESS;

public class UndoCommandTest extends TaskBookGuiTest {

    @Test
    public void undo_add() {
        //add one item
        TestItem[] currentList = td.getTypicalItems();
        TestItem itemToAdd = td.help;
        commandBox.runCommand(itemToAdd.getAddCommand());
        currentList = TestUtil.addItemsToList(currentList, itemToAdd);
        
        // undo add and check
        commandBox.runCommand("undo");
        assertNotFound(itemToAdd, currentList);
    }
    
    @Test
    public void undo_delete() {
    	// delete the first item from the list
    	TestItem[] currentList = td.getTypicalItems();
    	int targetIndex = 1;
        commandBox.runCommand("delete " + targetIndex);

        // undo delete and check
        commandBox.runCommand("undo");
        assertResultMessage("Undo delete task: Always brush teeth");
    }
    
    @Test
    public void undo_clear() {
        assertTrue(itemListPanel.isListMatching(td.getTypicalItems()));
        commandBox.runCommand("clear");
        commandBox.runCommand("undo");
        assertTrue(itemListPanel.isListMatching(td.getTypicalItems()));
    }
 
    private void assertNotFound(TestItem itemToFind, TestItem... currentList) {
    	ItemCardHandle deletedCard = itemListPanel.navigateToItem(itemToFind);
    	assertTrue(deletedCard == null);
    }
    
    

}
