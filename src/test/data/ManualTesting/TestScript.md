# Sudowudo Manual Testing

## Set Up

1. Create a directory `data` in the same directory as the jar.
2. Rename the sample data file `TaskBook.xml` and place it inside the `data` folder.
3. Double-click the jar to open it; it should load the data from the sample data file.
4. Here commences manual testing!

## Getting Acquainted

1. Type `help` to find out how you can use our commands! It's real helpful.
2. Try out `undo` now! It should tell you that you can't undo anything. Isn't that great?
2. Any time you mistype something, hit the `up` key. Your last-typed command will be right there! Just like a terminal...
3. If you'd like to scroll around the page, hit `pageup` and `pagedown` on Windows or `CMD+UP` and `CMD+DOWN` on Mac. No need for your mouse!
4. Now let's try out some commands...

5. Try some adding! These should work...

```
add "eat a turtle" from next wednesday 6pm to 8pm

add "CS homework" #homework #important

add "Do my job" from tomorrow 9am to 5pm
```

7. But these will probably not...

```
add

add 123

add a task without quotemarks

add "a thing with a bad name!!"
```

8. Let's try marking things as done...

```java
done 1

done 3

done 7
```

9. But these won't work

```
done 0

done 1900

done "task"
```

10. If you mark an already done item as `done` again, nothing will happen!

```java
done 16

done 16
```

11. Go ahead and try to `undo` multiple times; each item you have marked `done` will get its done marker removed.

```java
undo

undo

undo
```

12. Now let's try to find some things:

```java
find find "CS"
find "finish" #task
find "finish" #homework #done
find yesterday
find ""
```

13. On the other hand, these won't work:

```java
find
find not a date
find admioarmaioebmlkdfmzvlckvm
```

14. Let's do some editing.

```java
edit 1 desc:New Description

edit 2 start:none

edit 3 desc:A New Hope, period:tomorrow 6pm, to tomorrow 9pm
```
If Item 4 does not currently end before 6pm today, we can also do this:
```
edit 4 start:6pm
```

15. However, this shouldn't work:
```java
edit 1 desc:Walk the dog!

edit 1 period: tomorrow till yesterday
```
This won't work if the event currently ends before 7 October at 9am, unfortunately. Try using `period: 7 oct 9am to 7 oct 10am` instead!
```
edit 1 start: 7 oct 9am end: 7 oct 10am
```
This one has a start time that comes after an end time, which is a silly way to schedule an event. So it won't work!
```
edit 3 start: tomorrow 6pm, end: today 9pm

```
16. Now let's get rid of some stuff

```
delete 1

delete 39

delete 25
```

17. But these delete commands don't make sense
```
delete 0

delete 1234908

delete "my item"

delete #cool

delete -1
```

18. Let's list some stuff!
```
list
list task
list event
list done
list undone
list overdue
```

19. But this won't work..
```
list cat
list 23124
list tasks
list #fun
```

17. All done?

`exit`

21. See you!

## Some more commands you can try out...
### Successful Commands
`add "eat a turtle" from next wednesday 6pm to 8pm`

`add "CS homework" #homework #important`

`add "Do my job" from tomorrow 9am to 5pm`

`add "Do laundry" from 1600 to 1700 #chores`

`add "CS2103 Hackathon" from 1000 on 12 November to 1200 on 15 November`

`add "Attempt tutorial" from 12 dec 9:30pm to 10:30pm #optional #tags`

`add "Go to school" from 1800 fifth january till the sixth october at 9:30pm`

`add "Submit essay" by next friday 2359h`

### Unsuccessful Commands
Typing this into the command box should show an error message.
`add`

`add 123`

`add a task without quotemarks`

`add "a thing with a bad name!!"`

`add "a task" #with #bad #tags!`

`add "weird dates" from today till yesterday`
`
