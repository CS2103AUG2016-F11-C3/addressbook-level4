package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
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

    public static final String COMMAND_TAG_REGEX = "#(.*)";
    public static final String COMMAND_DESCRIPTION_REGEX = "\"(.*)\"";
    public static final String COMMAND_TAG_PREFIX = "#";
    public static final String COMMAND_DESCRIPTION_PREFIX = "\"";


    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    // one or more keywords separated by whitespace
    private static final Pattern KEYWORDS_ARGS_FORMAT = Pattern.compile("(?<keywords>\\S+(?:\\s+\\S+)*)");

    private static final Pattern ITEM_DATA_ARGS_FORMAT = 
            Pattern.compile("(.*)\\\"(.*)\\\"(.*)");    // item has description and a string representing time to be processed
    private static final Pattern TASK_DATA_ARGS_FORMAT = Pattern.compile("(.*)\\\"(.*)\\\"");

    private static final String TASK_NO_DATE_DATA = "nothing";
    private static final String EMPTY_STRING = "";
	private static final Pattern ITEM_INDEX_ARGS_FORMAT = Pattern.compile("(?<targetIndex>.+)");

    private static final int COMMAND_DESCRIPTION_FIELD_NUMBER = 2;
    private static final int COMMAND_TYPE_FIELD_NUMBER = 1;
    private static final int COMMAND_TIME_FIELD_NUMBER = 3;

    private static final Pattern COMMAND_DESCRIPTION_SEARCH_FORMAT = Pattern.compile("\"([^\"]*)\"");
    private static final Pattern COMMAND_TAG_SEARCH_FORMAT = Pattern.compile("#([^ ]+)");
    
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	private static final Pattern ITEM_EDIT_ARGS_FORMAT = Pattern.compile("(?<targetIndex>\\d+) edit (?<arguments>.*)");

	
	public enum Type {TASK, DEADLINE, EVENT;}

    public enum Field {
        NAME("name"),
        START_DATE("start_date"),
        END_DATE("end_date"),
        START_TIME("start_time"),
        END_TIME("end_time"),
        DATE("date"),
        TIME("time");

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
            return prepareList(arguments);

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        case DoneCommand.COMMAND_WORD:
            return prepareDone(arguments);

        default:
            return new IncorrectCommand(MESSAGE_UNKNOWN_COMMAND);
        }
	};

	/**
     * Parses arguments in the context of the list item command.
     * @param arguments
     * @return
     * @@author A0131560U
     */
    private Command prepareList(String argument) {
        assert argument != null;
        if (argument.isEmpty()){
            return new ListCommand(EMPTY_STRING);
        }
        else if (isValidType(argument)){
            return new ListCommand(argument.toUpperCase());
        }
        else{
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE));
        }
    }

    /**
     * Checks if given String is a valid Type argument
     * @param argument
     * @return true if String is valid Type, false otherwise
     * @@author A0131560U
     */
    private boolean isValidType(String argument) {
        assert argument != null;
        try{
            Type.valueOf(argument.trim().toUpperCase());
        } catch (IllegalArgumentException iae) {
            return false;
        }
        return true;
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
        final Matcher taskMatch = TASK_DATA_ARGS_FORMAT.matcher(args.trim()); // Validate arg string format
        if (!itemMatch.matches() && !taskMatch.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }
        try {
            if (taskMatch.matches()) {
                return parseNewTask(itemMatch, args);
            } else {
                return parseNewItem(itemMatch, args);
            }
        // check if any thing before first quotation mark and return error if found
        } catch (IllegalValueException ive) {
            return new IncorrectCommand(ive.getMessage());
        }
    }

    /**
     * Parses strings from given argument to delimit description, start/end
     * time, tags and creates a list for the tags
     * 
     * @param itemMatch
     * @return new Command with separated description, start and end time
     *         strings, Set of tags
     * @throws IllegalValueException
     */
    private Command parseNewItem(final Matcher itemMatch, String args) throws IllegalValueException {
		// check if any thing before first quotation mark and return error if
        // found
        String postFix = itemMatch.group(COMMAND_TYPE_FIELD_NUMBER).trim();
        if (!postFix.equals(EMPTY_STRING)) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
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
     * 
     * @@author A0131560U
     */
    private static Set<String> getTagsFromArgs(String arguments) throws IllegalValueException {
        assert arguments != null;

        ArrayList<String> tagList = parseMultipleParameters(arguments.trim(), ' ');

        // no tags
        if (tagList.isEmpty()) {
            return Collections.emptySet();
        }

        Set<String> tagSet = new HashSet<>();
        for (String tag : tagList) {
            if (tag.matches(COMMAND_TAG_REGEX)) {
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
        final Matcher matcher = ITEM_INDEX_ARGS_FORMAT.matcher(command.trim());
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
     * Parses arguments in the context of the find item command.
     *
     * @param args
     *            full command args string
     * @return the prepared command
     * @@author A0131560U
     */
    private Command prepareFind(String args) {
        final Matcher matcher = KEYWORDS_ARGS_FORMAT.matcher(args.trim());
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        // search for all phrases within double quotes
        final Set<String> keywordSet = new HashSet<>();
        args = extractKeywordsFromPattern(args, COMMAND_DESCRIPTION_SEARCH_FORMAT, keywordSet);
        
        // search for tags
        args = extractKeywordsFromPattern(args, COMMAND_TAG_SEARCH_FORMAT, keywordSet);

        if (!args.isEmpty()) {
            boolean isDateTimeValid = extractDateTimeFromKeywords(args, keywordSet);
            if (!isDateTimeValid){
                return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
            }
        }
        
        return new FindCommand(keywordSet);
    }

    /**
     * Extracts a valid DateTime from the provided arguments and adds them to the keywordSet,
     * then returns true. If the arguments do not form a valid DateTime, returns false. 
     * @param args
     * @param keywordSet
     * @return
     */
    private boolean extractDateTimeFromKeywords(String args, final Set<String> keywordSet) {
        assert args != null;
        assert !args.isEmpty();
        assert keywordSet != null;
        DateTimeParser dateArgs = new DateTimeParser(args);
        
        if (dateArgs.extractStartDate() != null){
            keywordSet.add(dateArgs.extractStartDate().format(DATE_TIME_FORMATTER));
            if (dateArgs.extractEndDate()!= null){
                keywordSet.add(dateArgs.extractEndDate().format(DATE_TIME_FORMATTER));
            }
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Given a specific pattern, extracts all phrases that match the pattern and adds them
     * to keywordSet. Returns args string without the keywords that were extracted.
     * @param args
     * @param searchFormat
     * @return
     * @@author A0131560U
     */
    private String extractKeywordsFromPattern(String args, Pattern searchFormat, Set<String> keywordSet) {
        final Matcher matcher = searchFormat.matcher(args.trim());
        while (matcher.find()) {
            args = args.replace(matcher.group(), "").trim();
            keywordSet.add(matcher.group());
        }
        return args;
    }


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
