package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.opencsv.CSVParser;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.DoneCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.IncorrectCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.SelectCommand;

/**
 * Parses user input.
 */
public class Parser {

<<<<<<< HEAD
    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

	private static final Pattern PERSON_INDEX_ARGS_FORMAT = Pattern.compile("(?<targetIndex>.+)");

    private static final Pattern KEYWORDS_ARGS_FORMAT =
            Pattern.compile("(?<keywords>\\S+(?:\\s+\\S+)*)"); // one or more keywords separated by whitespace

    private static final Pattern PERSON_DATA_ARGS_FORMAT = // '/' forward slashes are reserved for delimiter prefixes
            Pattern.compile("(?<name>[^/]+)"
                    + " (?<isPhonePrivate>p?)p/(?<phone>[^/]+)"
                    + " (?<isEmailPrivate>p?)e/(?<email>[^/]+)"
                    + " (?<isAddressPrivate>p?)a/(?<address>[^/]+)"
                    + "(?<tagArguments>(?: t/[^/]+)*)"); // variable number of tags
    
    private static final Pattern ITEM_DATA_ARGS_FORMAT =
    		Pattern.compile("(.*)\\\"(.*)\\\"(.*)");	// item has description and a string representing time to be processed

	private static final Pattern ITEM_EDIT_ARGS_FORMAT = Pattern.compile("(?<targetIndex>\\d+) edit (?<arguments>.*)");

    public enum Field {
        NAME("name"), START_DATE("start_date"), END_DATE("end_date"), START_TIME("start_time"),
        END_TIME("end_time"), DATE("date"), TIME("time");

        private String field_name;

        Field(String name) {
            this.field_name = name;
        }

        public String getFieldName() {
            return this.field_name;
        }
    }

    public Parser() {}

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     */
    public Command parseCommand(String userInput) {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");
        switch (commandWord) {

        case AddCommand.COMMAND_WORD:
            return prepareAdd(arguments);

        case SelectCommand.COMMAND_WORD:
            return prepareSelect(arguments);

        case DeleteCommand.COMMAND_WORD:
            return prepareDelete(arguments);

        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

		case EditCommand.COMMAND_WORD:
			return prepareEdit(arguments);

        case FindCommand.COMMAND_WORD:
            return prepareFind(arguments);

        case ListCommand.COMMAND_WORD:
            return new ListCommand();

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        case DoneCommand.COMMAND_WORD:
            return prepareDone(arguments);

        default:
            return new IncorrectCommand(MESSAGE_UNKNOWN_COMMAND);
        }
    }

    /**
     * Parses arguments in the context of the add person command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareAdd(String args){
        final Matcher matcher = ITEM_DATA_ARGS_FORMAT.matcher(args.trim());
        // Validate arg string format
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }
        try {
        	// check if any thing before first quotation mark and return error if found
        	String postFix = matcher.group(1).trim();
        	if (!postFix.equals("")) {
                return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        	}
        	String description = matcher.group(2).trim();
        	String timeStr = matcher.group(3).trim();
        	return new AddCommand(description, timeStr);
        } catch (IllegalValueException ive) {
            return new IncorrectCommand(ive.getMessage());
        }
    }

    /**
     * Extracts the new person's tags from the add command's tag arguments string.
     * Merges duplicate tag strings.
     */
    private static Set<String> getTagsFromArgs(String tagArguments) throws IllegalValueException {
        // no tags
        if (tagArguments.isEmpty()) {
            return Collections.emptySet();
        }
        // replace first delimiter prefix, then split
        final Collection<String> tagStrings = Arrays.asList(tagArguments.replaceFirst(" t/", "").split(" t/"));
        return new HashSet<>(tagStrings);
    }



    /**
	 * Parses arguments in the context of the find item command.

	 *
	 * @param args
	 *            full command args string
	 * @return the prepared command
	 */
    
	/**
	 * Parses arguments in the context of the Edit item command
	 * 
	 * @param args
	 * @return
	 */
	private Command prepareEdit(String args) {
		final Matcher matcher = ITEM_EDIT_ARGS_FORMAT.matcher(args.trim());
		if (!matcher.matches()) {
			return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
		}
		int index = Integer.parseInt(matcher.group("targetIndex"));
		ArrayList<String> arguments = parseMultipleParameters(matcher.group("arguments"), ',');
		try {
			return new EditCommand(index, arguments);
		} catch (IllegalValueException e) {
			return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, e.getMessage()));
		}
	}

	private Command prepareDelete(String args) {

		Optional<Integer> index = parseIndex(args);
		if (!index.isPresent()) {
			return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
		}

		return new DeleteCommand(index.get());
	}

	/**
	 * Parses arguments for done task command
	 * 
	 * @param args
	 *            full command args string
	 * @return the prepared done command
	 * @author darren
	 */
	private Command prepareDone(String args) {
		Optional<Integer> index = parseIndex(args);
		if (!index.isPresent()) {
			return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DoneCommand.MESSAGE_USAGE));
		}

		return new DoneCommand(index.get());
	}

	/**
	 * Parses arguments in the context of the select person command.
	 *
	 * @param args
	 *            full command args string
	 * @return the prepared command
	 */
	private Command prepareSelect(String args) {
		Optional<Integer> index = parseIndex(args);
		if (!index.isPresent()) {
			return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectCommand.MESSAGE_USAGE));
		}

		return new SelectCommand(index.get());
	}

	/**
	 * Returns the specified index in the {@code command} IF a positive unsigned
	 * integer is given as the index. Returns an {@code Optional.empty()}
	 * otherwise.
	 */
	private Optional<Integer> parseIndex(String command) {
		final Matcher matcher = PERSON_INDEX_ARGS_FORMAT.matcher(command.trim());
		if (!matcher.matches()) {
			return Optional.empty();
		}

		String index = matcher.group("targetIndex");
		if (!StringUtil.isUnsignedInteger(index)) {
			return Optional.empty();
		}
		return Optional.of(Integer.parseInt(index));

	}

	/**
	 * Parses arguments in the context of the find person command.
	 *
	 * @param args
	 *            full command args string
	 * @return the prepared command
	 */
	private Command prepareFind(String args) {
		final Matcher matcher = KEYWORDS_ARGS_FORMAT.matcher(args.trim());
		if (!matcher.matches()) {
			return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
		}

		// keywords delimited by whitespace
		final String[] keywords = matcher.group("keywords").split("\\s+");
		final Set<String> keywordSet = new HashSet<>(Arrays.asList(keywords));
		return new FindCommand(keywordSet);
	}

	/**
	 * splits multi-arguments into a nice ArrayList of strings
	 * 
	 * @param params
	 *            comma-separated parameters
	 * @param delimiter
	 *            delimiting character
	 * @return ArrayList<String> of parameters
	 * @author darren
	 */
	public static ArrayList<String> parseMultipleParameters(String params, char delimiter) {
		CSVParser parser = new CSVParser(delimiter);

		try {
			String[] tokens = parser.parseLine(params);

			// strip leading and trailing whitespaces
			for (int i = 0; i < tokens.length; i++) {
				tokens[i] = tokens[i].trim();
			}

			return new ArrayList<>(Arrays.asList(tokens));
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		}

		return null;
	}

	/**
	 * checks field names are valids
	 * 
	 * @param fieldNames
	 *            an ArrayList<String> of field names
	 * @return true if all fields are valid, false otherwise
	 * @author darren
||||||| merged common ancestors
	/**
	 * Used for initial separation of command word and args.
	 */
	private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

	private static final Pattern PERSON_INDEX_ARGS_FORMAT = Pattern.compile("(?<targetIndex>.+)");

	private static final Pattern KEYWORDS_ARGS_FORMAT = Pattern.compile("(?<keywords>\\S+(?:\\s+\\S+)*)"); // one
																											// or
																											// more
																											// keywords
																											// separated
																											// by
																											// whitespace

	private static final Pattern PERSON_DATA_ARGS_FORMAT = // '/' forward
															// slashes are
															// reserved for
															// delimiter
															// prefixes
			Pattern.compile("(?<name>[^/]+)" + " (?<isPhonePrivate>p?)p/(?<phone>[^/]+)"
					+ " (?<isEmailPrivate>p?)e/(?<email>[^/]+)" + " (?<isAddressPrivate>p?)a/(?<address>[^/]+)"
					+ "(?<tagArguments>(?: t/[^/]+)*)"); // variable number of
															// tags

	private static final Pattern ITEM_DATA_ARGS_FORMAT = Pattern.compile("(.*)\\\"(.*)\\\"(.*)");
	private static final Pattern TASK_DATA_ARGS_FORMAT = Pattern.compile("(.*)\\\"(.*)\\\"");

	public enum Field {
		NAME("name"), START_DATE("start_date"), END_DATE("end_date"), START_TIME("start_time"), END_TIME(
				"end_time"), DATE("date"), TIME("time");

		private String field_name;

		Field(String name) {
			this.field_name = name;
		}

		public String getFieldName() {
			return this.field_name;
		}
	}

	public Parser() {
	}

	/**
	 * Parses user input into command for execution.
	 *
	 * @param userInput
	 *            full user input string
	 * @return the command based on the user input
	 */
	public Command parseCommand(String userInput) {
		final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
		if (!matcher.matches()) {
			return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
		}

		final String commandWord = matcher.group("commandWord");
		final String arguments = matcher.group("arguments");
		switch (commandWord) {

		case AddCommand.COMMAND_WORD:
			return prepareAdd(arguments);

		case SelectCommand.COMMAND_WORD:
			return prepareSelect(arguments);

		case DeleteCommand.COMMAND_WORD:
			return prepareDelete(arguments);

		case ClearCommand.COMMAND_WORD:
			return new ClearCommand();

		case FindCommand.COMMAND_WORD:
			return prepareFind(arguments);

		case ListCommand.COMMAND_WORD:
			return new ListCommand();

		case ExitCommand.COMMAND_WORD:
			return new ExitCommand();

		case HelpCommand.COMMAND_WORD:
			return new HelpCommand();

		case DoneCommand.COMMAND_WORD:
			return prepareDone(arguments);

		default:
			return new IncorrectCommand(MESSAGE_UNKNOWN_COMMAND);
		}
	}

	/**
	 * Parses arguments in the context of the add person command.
	 *
	 * @param args
	 *            full command args string
	 * @return the prepared command
	 */
	private Command prepareAdd(String args) {
		final Matcher matcher = ITEM_DATA_ARGS_FORMAT.matcher(args.trim());
		final Matcher matcher_task = TASK_DATA_ARGS_FORMAT.matcher(args.trim());
		// Validate arg string format
		if (!matcher.matches() && !matcher_task.matches()) {
			return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
		}
		try {
			if (matcher_task.matches()) {
				String postFix = matcher.group(1).trim();
				if (!postFix.equals("")) {
					return new IncorrectCommand(
							String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
				}
				String description = matcher.group(2).trim();
				return new AddCommand(description, "nonsensical data");
				
			} else {
				// check if any thing before first quotation mark and return
				// error
				// if found
				String postFix = matcher.group(1).trim();
				if (!postFix.equals("")) {
					return new IncorrectCommand(
							String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
				}
				String description = matcher.group(2).trim();
				String timeStr = matcher.group(3).trim();
				return new AddCommand(description, timeStr);
			}
		} catch (IllegalValueException ive) {
			return new IncorrectCommand(ive.getMessage());
		}
	}

	/**
	 * Extracts the new person's tags from the add command's tag arguments
	 * string. Merges duplicate tag strings.
	 */
	private static Set<String> getTagsFromArgs(String tagArguments) throws IllegalValueException {
		// no tags
		if (tagArguments.isEmpty()) {
			return Collections.emptySet();
		}
		// replace first delimiter prefix, then split
		final Collection<String> tagStrings = Arrays.asList(tagArguments.replaceFirst(" t/", "").split(" t/"));
		return new HashSet<>(tagStrings);
	}

	/**
	 * Parses arguments in the context of the delete person command.
	 *
	 * @param args
	 *            full command args string
	 * @return the prepared command
	 */
	private Command prepareDelete(String args) {

		Optional<Integer> index = parseIndex(args);
		if (!index.isPresent()) {
			return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
		}

		return new DeleteCommand(index.get());
	}

	/**
	 * Parses arguments for done task command
	 * 
	 * @param args
	 *            full command args string
	 * @return the prepared done command
	 * @author darren
	 */
	private Command prepareDone(String args) {
		Optional<Integer> index = parseIndex(args);
		if (!index.isPresent()) {
			return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DoneCommand.MESSAGE_USAGE));
		}

		return new DoneCommand(index.get());
	}

	/**
	 * Parses arguments in the context of the select person command.
	 *
	 * @param args
	 *            full command args string
	 * @return the prepared command
	 */
	private Command prepareSelect(String args) {
		Optional<Integer> index = parseIndex(args);
		if (!index.isPresent()) {
			return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectCommand.MESSAGE_USAGE));
		}

		return new SelectCommand(index.get());
	}

	/**
	 * Returns the specified index in the {@code command} IF a positive unsigned
	 * integer is given as the index. Returns an {@code Optional.empty()}
	 * otherwise.
	 */
	private Optional<Integer> parseIndex(String command) {
		final Matcher matcher = PERSON_INDEX_ARGS_FORMAT.matcher(command.trim());
		if (!matcher.matches()) {
			return Optional.empty();
		}

		String index = matcher.group("targetIndex");
		if (!StringUtil.isUnsignedInteger(index)) {
			return Optional.empty();
		}
		return Optional.of(Integer.parseInt(index));

	}

	/**
	 * Parses arguments in the context of the find person command.
	 *
	 * @param args
	 *            full command args string
	 * @return the prepared command
	 */
	private Command prepareFind(String args) {
		final Matcher matcher = KEYWORDS_ARGS_FORMAT.matcher(args.trim());
		if (!matcher.matches()) {
			return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
		}

		// keywords delimited by whitespace
		final String[] keywords = matcher.group("keywords").split("\\s+");
		final Set<String> keywordSet = new HashSet<>(Arrays.asList(keywords));
		return new FindCommand(keywordSet);
	}

	/**
	 * splits multi-arguments into a nice ArrayList of strings
	 * 
	 * @param params
	 *            comma-separated parameters
	 * @param delimiter
	 *            delimiting character
	 * @return ArrayList<String> of parameters
	 * @author darren
	 */
	public static ArrayList<String> parseMultipleParameters(String params, char delimiter) {
		CSVParser parser = new CSVParser(delimiter);

		try {
			String[] tokens = parser.parseLine(params);

			// strip leading and trailing whitespaces
			for (int i = 0; i < tokens.length; i++) {
				tokens[i] = tokens[i].trim();
			}

			return new ArrayList<String>(Arrays.asList(tokens));
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		}

		return null;
	}

	/**
	 * checks field names are valids
	 * 
	 * @param fieldNames
	 *            an ArrayList<String> of field names
	 * @return true if all fields are valid, false otherwise
	 * @author darren
=======
    private static final String EMPTY_STRING = "";

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    private static final Pattern PERSON_INDEX_ARGS_FORMAT = Pattern.compile("(?<targetIndex>.+)");

    // one or more keywords separated by whitespace
    private static final Pattern KEYWORDS_ARGS_FORMAT = Pattern.compile("(?<keywords>\\S+(?:\\s+\\S+)*)");

    private static final Pattern ITEM_DATA_ARGS_FORMAT = Pattern.compile("(.*)\\\"(.*)\\\"(.*)");
    private static final Pattern TASK_DATA_ARGS_FORMAT = Pattern.compile("(.*)\\\"(.*)\\\"");

    private static final String TASK_NO_DATE_DATA = "nothing";

    private static final int COMMAND_DESCRIPTION_FIELD_NUMBER = 2;
    private static final int COMMAND_TYPE_FIELD_NUMBER = 1;
    private static final int COMMAND_TIME_FIELD_NUMBER = 3;

    private static final String TAG_PREFIX = "#";

    public enum Field {
        NAME("name"), START_DATE("start_date"), END_DATE("end_date"), START_TIME("start_time"), END_TIME(
                "end_time"), DATE("date"), TIME("time");

        private String field_name;

        Field(String name) {
            this.field_name = name;
        }

        public String getFieldName() {
            return this.field_name;
        }
    }

    public Parser() {
    }

    /**
     * Parses user input into command for execution.
     *
     * @param userInput
     *            full user input string
     * @return the command based on the user input
     */
    public Command parseCommand(String userInput) {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");
        switch (commandWord) {

        case AddCommand.COMMAND_WORD:
            return prepareAdd(arguments);

        case SelectCommand.COMMAND_WORD:
            return prepareSelect(arguments);

        case DeleteCommand.COMMAND_WORD:
            return prepareDelete(arguments);

        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case FindCommand.COMMAND_WORD:
            return prepareFind(arguments);

        case ListCommand.COMMAND_WORD:
            return new ListCommand();

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        case DoneCommand.COMMAND_WORD:
            return prepareDone(arguments);

        default:
            return new IncorrectCommand(MESSAGE_UNKNOWN_COMMAND);
        }
    }

    /**
     * Parses arguments in the context of the add person command.
     *
     * @param args
     *            full command args string
     * @return the prepared command
     */
    private Command prepareAdd(String args) {
        final Matcher itemMatch = ITEM_DATA_ARGS_FORMAT.matcher(args.trim());
        final Matcher taskMatch = TASK_DATA_ARGS_FORMAT.matcher(args.trim());

        // Validate arg string format
        if (!itemMatch.matches() && !taskMatch.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }

        try {
            if (taskMatch.matches()) {
                return parseNewTask(itemMatch, args);
            } else {
                return parseNewItem(itemMatch, args);
            }
        } catch (IllegalValueException ive) {
            return new IncorrectCommand(ive.getMessage());
        }
    }

    /**
	 * Parses strings from given argument to delimit description, start/end time, tags
	 * and creates a list for the tags 
	 * @param itemMatch
	 * @return new Command with separated description, start and end time strings, Set of tags
	 * @throws IllegalValueException
>>>>>>> 350f161687dbf3b7a68b43c14d3752c922bae214
	 */
    private Command parseNewItem(final Matcher itemMatch, String args) throws IllegalValueException {
        // check if any thing before first quotation mark and return error if found
        String postFix = itemMatch.group(COMMAND_TYPE_FIELD_NUMBER).trim();
        if (!postFix.equals(EMPTY_STRING)) {
        	return new IncorrectCommand(
        			String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }
        String description = itemMatch.group(COMMAND_DESCRIPTION_FIELD_NUMBER).trim();
        String timeStr = itemMatch.group(COMMAND_TIME_FIELD_NUMBER).trim();
        String argsWithoutDescription = args.replace("\"" + description + "\"", "");
        return new AddCommand(description, timeStr, getTagsFromArgs(argsWithoutDescription));
    }

    /**
     * Parses strings from the given argument to delimit description and tags
     * 
     * @param itemMatch
     * @param args 
     * @return new Command with separated description, Set of tags, but no time
     *         information
     * @throws IllegalValueException
     */
    private Command parseNewTask(final Matcher itemMatch, String args) throws IllegalValueException {
        String postFix = itemMatch.group(COMMAND_TYPE_FIELD_NUMBER).trim();
        if (!postFix.equals(EMPTY_STRING)) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }
        String description = itemMatch.group(COMMAND_DESCRIPTION_FIELD_NUMBER).trim();
        String argsWithoutDescription = args.replace(description, "");
        return new AddCommand(description, TASK_NO_DATE_DATA, getTagsFromArgs(argsWithoutDescription));
    }

    /**
     * Extracts the new person's tags from the add command's tag arguments
     * string. Merges duplicate tag strings.
     * @@author A0131560U
     */
    private static Set<String> getTagsFromArgs(String arguments) throws IllegalValueException {
        assert arguments != null;
        
        ArrayList<String> tagList = parseMultipleParameters(arguments.trim(), ' ');

        // no tags
        if (tagList.isEmpty()) {
            return Collections.emptySet();
        }
        
        Set<String> tagSet = new HashSet<String>();
        for (String tag : tagList){
            if (tag.startsWith(TAG_PREFIX)){
                tagSet.add(tag.replaceFirst("#", ""));
            }
        }
        return tagSet;
    }

    /**
     * Parses arguments in the context of the delete person command.
     *
     * @param args
     *            full command args string
     * @return the prepared command
     */
    private Command prepareDelete(String args) {

        Optional<Integer> index = parseIndex(args);
        if (!index.isPresent()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

        return new DeleteCommand(index.get());
    }

    /**
     * Parses arguments for done task command
     * 
     * @param args
     *            full command args string
     * @return the prepared done command
     * @author darren
     */
    private Command prepareDone(String args) {
        Optional<Integer> index = parseIndex(args);
        if (!index.isPresent()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DoneCommand.MESSAGE_USAGE));
        }

        return new DoneCommand(index.get());
    }

    /**
     * Parses arguments in the context of the select person command.
     *
     * @param args
     *            full command args string
     * @return the prepared command
     */
    private Command prepareSelect(String args) {
        Optional<Integer> index = parseIndex(args);
        if (!index.isPresent()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectCommand.MESSAGE_USAGE));
        }

        return new SelectCommand(index.get());
    }

    /**
     * Returns the specified index in the {@code command} IF a positive unsigned
     * integer is given as the index. Returns an {@code Optional.empty()}
     * otherwise.
     */
    private Optional<Integer> parseIndex(String command) {
        final Matcher matcher = PERSON_INDEX_ARGS_FORMAT.matcher(command.trim());
        if (!matcher.matches()) {
            return Optional.empty();
        }

        String index = matcher.group("targetIndex");
        if (!StringUtil.isUnsignedInteger(index)) {
            return Optional.empty();
        }
        return Optional.of(Integer.parseInt(index));

    }

    /**
     * Parses arguments in the context of the find person command.
     *
     * @param args
     *            full command args string
     * @return the prepared command
     */
    private Command prepareFind(String args) {
        final Matcher matcher = KEYWORDS_ARGS_FORMAT.matcher(args.trim());
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        // keywords delimited by whitespace
        final String[] keywords = matcher.group("keywords").split("\\s+");
        final Set<String> keywordSet = new HashSet<>(Arrays.asList(keywords));
        return new FindCommand(keywordSet);
    }

    /**
     * splits multi-arguments into a nice ArrayList of strings
     * 
     * @param params
     *            comma-separated parameters
     * @param delimiter
     *            delimiting character
     * @return ArrayList<String> of parameters
     * @author darren
     */
    public static ArrayList<String> parseMultipleParameters(String params, char delimiter) {
        CSVParser parser = new CSVParser(delimiter);

        try {
            String[] tokens = parser.parseLine(params);

            // strip leading and trailing whitespaces
            for (int i = 0; i < tokens.length; i++) {
                tokens[i] = tokens[i].trim();
            }

            return new ArrayList<String>(Arrays.asList(tokens));
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }

        return null;
    }

    /**
     * checks field names are valids
     * 
     * @param fieldNames
     *            an ArrayList<String> of field names
     * @return true if all fields are valid, false otherwise
     * @author darren
     */
    private static boolean fieldsAreValid(ArrayList<String> fieldNames) {
        assert fieldNames != null;
        for (String fieldName : fieldNames) {
            try {
                Field ret = Field.valueOf(fieldName.toUpperCase());
            } catch (IllegalArgumentException iae) {
                return false;
            }
        }
        return true;
    }
}
