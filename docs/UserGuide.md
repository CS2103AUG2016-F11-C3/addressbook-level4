# Sudowudo User Guide

## Quick Start
<!-- for getting the thing to actually work -->
<!-- need to wait on our refactoring for addressbook-level4 -->
1. Ensure you have Java version `1.8.0_60` or later installed.
2. Download the latest `sudowudo.jar` from the releases tab.
3. Run `sudowudo.jar`

## Features
<!-- each individual command -->
In the following sections, we outline the format of commands to use `Sudowudo`. Tags such as `date` are placeholders in the command syntax and denoted as *fields*.

### Getting Help
`Sudowudo` also stores documentation that can be accessed from its command-line interface using the following command.

```bash
# format
help           # for general how-to-use help
help COMMAND   # for command-specific help
```

```bash
# examples
help add       # command-specific help for add
help list       # command-specific help for list
```
### Field Formats

#### `descriptors`
Descriptors are words/phrases used for identification, such as for the name of an event or for an event's description. Quote marks are used to denote a descriptor.

```bash
"Dental Appointment" # valid
Dental Appointment   # invalid
```

#### `event_id`
Events can be assigned a numbers as an *identifier*. Each event/task has a unique and persistent numerical `event_id`. The identifier does not need to be enclosed in quote marks.

#### `context_id`
Tasks and events are shown on the main interface of Sudowudo and paged. The `context_id` allows you to modify tasks and events that are shown on the interface based on the *contextual* identifier, i.e. the numerical index that is shown on the interface. The identifier does not need to be enclosed in quote marks.

The `context_id` is not persistent and can change depending on what tasks/events are on the interface at the time. It is meant to provide a more convenient and interactive way to interact with tasks/events.

#### `datetime`
Dates and times are expressed together ("datetime") and have a natural format.

Do not put commas in the `datetime` parameter!

##### Examples of `datetime`
```bash
# for a period of time
5 sept at 5pm to 6 sept at 6pm
fifth november at 2130
this wednesday 6pm to this saturday 7pm
12/12/2016 9am to 25/12 1600

# for a recurring period of time
every monday 9-10am

# for a deadline
by 12/12/2016 at 1pm

# for a recurring deadline
every friday at 11:30pm
```

### `field_name`
The field names denote specific parts of a task/event.

The available field names:

| `field_name` | Type | Description |
|--------------|------|-------------|
| `name`       | [descriptor](#descriptors) | Task/event name |
| `start`      | [datetime](#datetime) | Start datetime |
| `end`        | [datetime](#datetime) | End datetime |
| `period`     | [datetime](#datetime) | Start and end datetime |
| `by`         | [datetime](#datetime) | Deadline datetime |

### Notes on Syntax Examples
In the remainder of this section, note the following:

1. Fields that are in uppercase are *user parameters*.
2. The order of parameters in command formats is fixed.
3. Commands that begin with `for` have autocomplete to assist the user.

### Adding an Event
#### Start and End Times
For an event with a definite start and end time, you can use the following syntax to add this event.

```bash
# format
add EVENT_NAME from DATETIME
```

Fields: [`EVENT_NAME`](#descriptors), [`DATETIME`](#datetime)

```bash
# examples
add "Do laundry" from 1600 to 1700
add "CS2103 Hackathon" from 1000 on 12 November to 1200 on 15 November  # multiday event
add "Attempt tutorial" from 12/12/2016 9:30pm to 10:30pm
add "Go to school" from 1800 fifth january till the sixth october at 9:30pm
```

#### Deadlines
For a task with no definite start time but a definite end time (e.g. a homework assignment), you can use the following syntax.

```bash
# format
add TASK_NAME by DATETIME
```

Fields: [`TASK_NAME`](#descriptors), [`DATETIME`](#datetime)

```bash
# examples
add "CS2103 Tutorial 6" by 7 October
add "CS2103 Peer Feedback" by 2359h 27 Sep
add "Walk dog" by 9:41pm
add "Perform magic tricks" by two weeks from now
```

#### Floating Tasks
Floating tasks do not have a definite start or end time.

```bash
# format
add TASK_NAME
```
Fields: [`TASK_NAME`](#descriptors)

```bash
# examples
add "Schedule CS2103 Consult" 
```

### Updating an Event
#### Editing Event Details
Sometimes it is necessary to change the details of your event because life.

```bash
# format
for EVENT_ID edit FIELD_NAME:NEW_DETAIL
for CONTEXT_ID edit FIELD_NAME:NEW_DETAIL
```
Fields: [`FIELD_NAME`](#field-name), [`EVENT_ID`](#event-id), [`CONTEXT_ID`](#context-id), `NEW_DETAIL`

You can change multiple fields for the same event at the same time by separating multiple `FIELD_NAME:NEW_DETAIL` parameters with a comma.

```bash
# examples
for 2412 edit start :today 1600
for 10 edit by: 29 October 5pm
for 4 edit end:1/2/2016 10:51am # edits the forth item currently listed
for 5 edit period : 11 nov 4:30pm to 6:30pm

# change multiple fields at the same time
# both of these commands are equivalent
for 1 edit start: this friday 1600, end:this friday 1645
for 1 edit period: this friday 1600 to 1645
```

#### Marking as Complete
```bash
# format
done EVENT_ID
done CONTEXT_ID
```
Fields: [`EVENT_ID`](#event-id), [`CONTEXT_ID`](#context-id)

```bash
# examples
done 3 # marks the third item displayed as done
```

### Deleting a Task/Event
You can delete an event using its name. This is not the same as marking an event as complete (see [Marking as Complete](#marking-as-complete)), as it removes the task/event from the record.

```bash
# format
delete EVENT_ID
delete CONTEXT_ID
```

Fields: [`EVENT_ID`](#event-id), [`CONTEXT_ID`](#context-id)

```bash
# examples
delete 9 # deletes the ninth item displayed
delete 241 # delete the item with event ID 241
```

### Searching for a Task/Event
#### Searching by Keyword
You can search for specific events using keywords. The keywords are case-insensitive and can be simply part of the event name.
```bash
# format
find KEYWORD
find EVENT_ID
```
Fields: [`KEYWORD`](#descriptors), [`EVENT_ID`](#event-id)
```bash
# examples
find "cake"
find 12093
```

### Enumerating Tasks
You can enumerate a list of all the events and show it on the main interface.

```bash
list        # lists all events by name in chronological order
```

### Paging
The main interface of Sudowudo pages your upcoming tasks/events.

```bash
next           # shows next page of tasks/events
back           # shows previous page of tasks/events
```

### Undoing
Use the `undo` command to undo the most recent action.
```bash
undo
```
