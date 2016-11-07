package guitests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.sudowudo.logic.commands.AddCommand;
import seedu.sudowudo.logic.commands.ClearCommand;
import seedu.sudowudo.logic.commands.DeleteCommand;
import seedu.sudowudo.logic.commands.DoneCommand;
import seedu.sudowudo.logic.commands.EditCommand;
import seedu.sudowudo.logic.commands.ExitCommand;
import seedu.sudowudo.logic.commands.FindCommand;
import seedu.sudowudo.logic.commands.HelpCommand;
import seedu.sudowudo.logic.commands.ListCommand;
import seedu.sudowudo.logic.commands.UndoCommand;

/**
 * 
 * @@author A0092390E
 *
 */

public class HelpCommandTest extends TaskBookGuiTest {

	@Test
	public void openHelpPrompt() {
        commandBox.runCommand("help");
		hintDisplay = commandBox.runHelpCommand();
		assertTrue(hintDisplay.containsHints(AddCommand.getHints()));
		assertTrue(hintDisplay.containsHints(AddCommand.getHints()));
		assertTrue(hintDisplay.containsHints(ClearCommand.getHints()));
		assertTrue(hintDisplay.containsHints(DeleteCommand.getHints()));
		assertTrue(hintDisplay.containsHints(DoneCommand.getHints()));
		assertTrue(hintDisplay.containsHints(EditCommand.getHints()));
		assertTrue(hintDisplay.containsHints(ExitCommand.getHints()));
		assertTrue(hintDisplay.containsHints(FindCommand.getHints()));
		assertTrue(hintDisplay.containsHints(HelpCommand.getHints()));
		assertTrue(hintDisplay.containsHints(ListCommand.getHints()));
		assertTrue(hintDisplay.containsHints(UndoCommand.getHints()));
	}

	@Test
	public void clearHelpPrompt() {
		commandBox.runCommand("list");
		assertEquals(false, hintDisplay.isVisible());
	}

	@Test
	public void addCommands() {
		commandBox.enterCommand("add");
		commandBox.triggerKeyRelease();
		assertTrue(hintDisplay.containsHintsExactly(AddCommand.getHints()));
	}

	@Test
	public void listCommands() {
		commandBox.enterCommand("list");
		commandBox.triggerKeyRelease();
		assertTrue(hintDisplay.containsHintsExactly(ListCommand.getHints()));
	}

	@Test
	public void clearCommands() {
		commandBox.enterCommand("clear");
		commandBox.triggerKeyRelease();
		assertTrue(hintDisplay.containsHintsExactly(ClearCommand.getHints()));
	}
}
