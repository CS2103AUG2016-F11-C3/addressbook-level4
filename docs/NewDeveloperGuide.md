# Developer Guide

* [Appendix A: User Stories](#appendix-a-user-stories)
* [Appendix B: Use Cases](#appendix-b-use-cases)
* [Appendic C: Non-Functional Requirements](#appendix-c-non-functional-product-requirements)
* [Appendix D: Product Survey](#appendix-d-product-survey)

<!--- @@author A0131560U --->
## Appendix A: User Stories
Priorities: 
- `* * *` -- high priority, must have
- `* *` -- medium priority, good to have
- `*` -- low priority, unlikely to have

Priority | As a ... | I want to ... | So that I can...
-------- | :------- | :------------ | :-----------
`* * *` | new user | see usage instructions | refer to instructions when I forget how to use the App
`* * *` | user | add a new event | 
`* * *` | user | mark an event as completed |
`* * *` | user | change details of an event | update events according to my schedule
`* * *` | user | see whether an event is completed | know what events are incomplete
`* * *` | user | search for an event by keywords |
`* * *` | user | delete an event | remove entries that I cannot complete
`* * *` | user | list all uncompleted events | see what I have left to do
`* *`	| user | schedule multiple time blocks for an event | tentatively plan events
`* *` | user | tag events using particular keywords | group related events
`* *` | user | know what events are urgent | plan my time accordingly
`*`		| user | sort events by deadline | know which events are urgent
`*` 	| user with many tasks | sort events by priority | know which upcoming events are important
`*`   | user | use natural language to type my commands| not have to remember complex commands
`*`   | user | receive feedback when I am typing in commands | know whether I am typing in the command correctly
`*`   | user | have the app autocomplete my task name | more quickly type in commands
`*`   | power user | use keyboard shortcuts to access frequently-used features | more quickly access useful features
<!--- @@author --->

<!--- @@author A0131560U --->
## Appendix B: Use Cases
(For all use cases below, the `System` is Wudodo and the `Actor` is the user, unless specified otherwise)

### Use case: Add task
#### MSS
1. User inputs task details
2. System stores task details in database

#### Extensions
1a. User inputs details in incorrect format
> System displays error message and help text
> Use case resumes at step 1

### Use case: Find  task
#### MSS
1. User inputs search string
2. System shows matching tasks
(Use case ends)

#### Extensions
1a. User inputs invalid search string
> System displays errors message
> Use case resumes at step 1

2a. No matching tasks
> Use case ends
<!--- @@author --->

<!--- @@author A0092390E --->
### Use case: Mark task as completed
#### MSS
1. User requests to complete a specific task in the list by index
4. System marks task as completed
3. System indicates successful mark as complete
(Use case ends)

#### Extensions
1a. The given task index is invalid
> 1a1. System shows an error message
> Use case resumes at step 1

2a. Task is already completed
> Use case ends

### Use case: Delete task
#### MSS
1. User requests to delete a task in the list by specifying its index
2. System deletes selected task from the list
3. System shows delete success message
(Use case ends)

#### Extensions
1a. The given task index is invalid
>1a1. System shows an error message
> Use case resumes at step 1

1b. The given task is recurring
> 1b1. System checks if user wants to delete this task or all succeeding tasks
> 1b2. User selects desired choice
> 1b3. System deletes selected tasks
<!--- @@author --->

<!--- @@author A0131560U --->
### Use case: List all tasks
#### MSS
1. User requests to list all tasks
2. System shows all tasks
3. System shows visual feedback that listing is done

#### Extensions
1a. The list is empty
> Use case resumes at step 3

2a. The given task is invalid
> 2a1. System shows an error message
> Use case resumes at step 2
<!--- @@author --->

<!--- @@author A0144750J --->
### Use case: Clear list
#### MSS
1. User requests to clear list
2. System clears list
3. System displays visual feedback that clearing is done

#### Extensions
1a. The list is empty
> Use case resumes at step 3
<!--- @@author --->

<!--- @@author A0131560U --->
## Appendix C: Non-Functional Product Requirements
1. Should work on any mainstream OS as long as it has `Java 1.8.0_60` or higher installed.
2. Should be able to hold up to 1000 items.
3. Should come with automated unit tests and open source code.
4. Should favor DOS style commands over Unix-style commands.
5. Should save and retrieve data from local text files
6. Should not use relational databases
7. Should be reliant on CLI instead of GUI
<!--- @@author --->

<!--- @@author A0131560U --->
## Appendix D: Product Survey

### Desired features
This list of features is taken from the [Handbook](http://www.comp.nus.edu.sg/~cs2103/AY1617S1/contents/handbook.html#handbook-project-product).

1. Command line-based UI
2. Take in events with specified start and end time
2. Block out multiple tentative start/end times for an event
2. Take in events with deadlines but no specified start/end time
3. Take in floating tasks  without user-specified start end times
5. Mark items as done
4. Track uncompleted to-do items past end time
5. Quick-add events
6. Access to-do list without internet connection
7. Easily choose which to-do item to do next
8. Keyword search
8. Specify data storage location

### Todoist

#### Meets Specifications
- Setting deadlines allowed
- Floating tasks allowed
- Can easily mark items as done by clicking on them
- Keeps track of uncompleted deadlines in chronological order
- Natural language quick-add function gives flexibility when adding to-do items
- Desktop app allows offline access
- Organization of inbox, as well as list of items due today and in the new week allows easy choice of what to-do item to do next
- Keyword search (implemented with nifty shortcut!)

#### Does not meet specifications
- Events cannot block off specific start and end times
- Not Command Line Interface
- Specify data storage location (but has its own cloud storage)

#### Interesting features
- Use of tagging to split to-do items into different categories
- Use of colours to mark different levels of priority, drawing visual attention to high-priority items
- Shortcut allows postponing to tomorrow, next week
- Reminder feature (requires in-app purchase)
- Can schedule recurring events using natural language commands
- Use of keyboard shortcuts while in app [^2]

#### Takeaways:
- Use of hashtags for tagging
- Use of keyboard shortcuts
- On-screen shortcuts for particular features (e.g. postponing, making event recurring)
<!--- @@author --->

<!--- @@author A0147609X --->
### Wunderlist
#### Meets Specifications
- Complete keyboard-only interaction for creating, reading, updating and deleting tasks (command line-like UI)
- Quick-add feature allows natural language adding of items
- Take in events with specified start and end times
- Allow events with deadlines
- Take in floating tasks  without user-specified start end times
- Click to check item as done
- Keeps track of uncompleted deadlines/tasks in chronological order
- Keyword search
- Track uncompleted to-do items past end time
- Easily choose which to-do item to do next
- Access to-do list without internet connection (only if not using web interface)

#### Does not meet specifications
- Data is locally stored but is not in a human-readable format

#### Interesting features
- Interface is very sleek and appealing
- To-do list sharing for collaborators
- Excellent multi-platform integration (e.g. iOS, Android, Windows, Mac, web interface)

#### Takeaways
- Use of multiplatform integration
<!--- @@author --->

<!--- @@author A0147609X --->
### Google Calendar
#### Meets Specifications
- Take in events with specified start and end times
- Allow events with deadlines
- Allow floating tasks
- Click to check item as done
- Keeps track of uncompleted deadlines/tasks in chronological order
- Quick-add feature allows natural language adding of items
- Keyword search
- Track uncompleted to-do items past end time
- Easily choose which to-do item to do next
- Block out multiple tentative start/end times for an event

#### Does not meet specifications
- No command line interface
- No local storage of event/task data
- Cannot be accessed without an internet connection

#### Interesting features
- Excellent multi-platform integration (e.g. iOS, Android, Windows, Mac, web interface)
- "All-day" events
- Multiple layers for calendar (e.g. a calendar for work, a personal calendar, a school calendar overlayed on top of each other)
- Color-coding for multiple calendars
- Different levels of views - per day, per four days, per week, per month 
- Import/export to iCalendar file (for Google Calendar, Outlook, iCal)
- Calendar sharing for collaborators

#### Takeaways
- Split pane views for tasks and events
- Quick add functionality
<!--- @@author --->

<!--- @@author A0131560U --->
### Apple Reminders
#### Meets Specifications
- Allows events with deadlines
- Allows floating events
- Click to mark item as done
- Lists uncompleted items in chronological order past end-time (under "Scheduled")
- Allows variable natural language input (buggy)

#### Does not meet specifications
- Not command line-based UI
- Cannot take in events with specified start time
- Cannot specify data storage location
- Keyword search is not very user-friendly

#### Interesting features
- Desktop reminders
- Multiple separate to-do lists

#### Takeaways
- Native support for Mac gives additional features (e.g. widgets, shortcuts in email etc.)

[^2]: https://support.todoist.com/hc/en-us/articles/205063212-Keyboard-shortcuts
<!--- @@author --->
