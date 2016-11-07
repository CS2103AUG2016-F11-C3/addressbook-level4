package guitests;

import org.junit.Test;

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

	}
}
