package guitests;

import guitests.guihandles.ItemCardHandle;
import org.junit.Test;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.commons.core.Messages;
import seedu.address.testutil.TestItem;
import seedu.address.testutil.TestUtil;

import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.DeleteCommand.MESSAGE_DELETE_ITEM_SUCCESS;

public class UndoCommandTest extends TaskBookGuiTest {

    @Test
    public void undo_add() {
        // add one item
        TestItem[] currentList = td.getTypicalItems();
        TestItem itemToAdd = td.help;
        commandBox.runCommand(itemToAdd.getAddCommand());
        currentList = TestUtil.addItemsToList(currentList, itemToAdd);
        
        // undo should succeed
        commandBox.runCommand("undo");
        assertNotFound(itemToAdd, currentList);
        
        // add a bad command
        commandBox.runCommand("add \"wrong format add command from 2pm to 3pm tomorrow");
        
        // undo should fail
        commandBox.runCommand("undo");
        assertResultMessage(UndoCommand.MESSAGE_FAILURE);

    }
    
    @Test
    public void undo_delete() {
    	// delete the first item from the list
    	TestItem[] currentList = td.getTypicalItems();
    	int targetIndex = 1;
        commandBox.runCommand("delete " + targetIndex);

        // undo should succeed
        commandBox.runCommand("undo");
        assertResultMessage("Undo delete task: Always brush teeth");
        
        // delete a bad command
        commandBox.runCommand("delete " + currentList.length + 2);
        
        // undo should fail
        commandBox.runCommand("undo");
        assertResultMessage(UndoCommand.MESSAGE_FAILURE);
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
