package seedu.sudowudo.storage;

import seedu.sudowudo.commons.events.model.TaskBookChangedEvent;
import seedu.sudowudo.commons.events.storage.DataSavingExceptionEvent;
import seedu.sudowudo.commons.exceptions.DataConversionException;
import seedu.sudowudo.model.ReadOnlyTaskBook;
import seedu.sudowudo.model.UserPrefs;

import java.io.IOException;
import java.util.Optional;

/**
 * API of the Storage component
 */
public interface Storage extends TaskBookStorage, UserPrefsStorage {

    @Override
    Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException;

    @Override
    void saveUserPrefs(UserPrefs userPrefs) throws IOException;

    @Override
    String getTaskBookFilePath();

    @Override
    Optional<ReadOnlyTaskBook> readTaskBook() throws DataConversionException, IOException;

    @Override
    void saveTaskBook(ReadOnlyTaskBook taskBook) throws IOException;

    /**
     * Saves the current version of the Task Book to the hard disk.
     *   Creates the data file if it is missing.
     * Raises {@link DataSavingExceptionEvent} if there was an error during saving.
     */
    void handleTaskBookChangedEvent(TaskBookChangedEvent tbce);
}
