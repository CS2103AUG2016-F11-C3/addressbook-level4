package seedu.address.logic.commands;

import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;
/*{
 * Represents a Hint object that shows up in command hinting
 * @@author A0092390E 
 */
public class Hint {
	public final static DiffMatchPatch dmp = new DiffMatchPatch();
	private String description;

	private String keyword;
	private String usage;

	public Hint(String description, String keyword, String usage) {
		this.description = description;
		this.keyword = keyword;
		this.usage = usage;
	}

	public String getDescription() {
		return description;
	}

	public String getKeyword() {
		return keyword;
	}

	public String getUsage() {
		return usage;
	}

	public boolean equals(String str) {
		if(dmp.diffLevenshtein(dmp.diffMain(str, this.keyword)) <= 2){
			return true;
		} else{
			return false;
		}
	}
}
