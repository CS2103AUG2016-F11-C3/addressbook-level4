package guitests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.sudowudo.logic.commands.AddCommand;

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
	}

	@Test
	public void clearHelpPrompt() {
		commandBox.runCommand("list");
		assertEquals(0, hintDisplay.isVisible());
	}

	@Test
	public void addCommands() {
		commandBox.enterCommand("add");
		assertTrue(hintDisplay.containsHintsExactly(AddCommand.getHints()));
	}
}
