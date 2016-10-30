# About Us

We are a project team [CS2103AUG2016-F11-C3](https://github.com/CS2103AUG2016-F11-C3) for the module *CS2103: Software Engineering* from the [School of Computing, National University of Singapore](http://www.comp.nus.edu.sg).

<!-- Template

* Components in charge of: [Storage](https://github.com/se-edu/addressbook-level4/blob/master/docs/DeveloperGuide.md#storage-component)
* Aspects/tools in charge of: Testing, Git
* Features implemented:
   * [List persons](https://github.com/se-edu/addressbook-level4/blob/master/docs/UserGuide.md#listing-all-persons--list)
   * [Delete person](https://github.com/se-edu/addressbook-level4/blob/master/docs/UserGuide.md#deleting-a-person--delete)
* Code written: [[functional code](A123456.md)][[test code](A123456.md)][[docs](A123456.md)]
* Other major contributions:
  * Did the initial refactoring from AddressBook to ToDoList [[#133](https://github.com/se-edu/addressbook-level4/pull/152) ]
  * Set up Travis and Coveralls 

-->

## Project Team

#### [Muthu Kumar Chandrasekaran](http://wing.comp.nus.edu.sg/~cmkumar)
<img src="images/Muthu.jpg" width="150"><br>

**Role**: Project Advisor

-----

#### [CARA LEONG SU-YI](https://github.com/craaaa)
<img src="images/Cara.jpg" width="150"><br>

- Component in charge of: [Model](https://github.com/CS2103AUG2016-F11-C3/main/blob/master/src/main/java/seedu/address/model/ModelManager.java)
- Aspects/tools in charge of: Git
- Features implemented:
	- [List tasks](#11)
	- [Find tasks](#9)
- Code written: [[functional code](../collated/main/A0131560U.md)][[test code](../collated/test/A0131560U.md)][[docs](../collated/docs/A0131560U.md)]
- Other major contributions:
	- Did the initial refactoring and automating of most tests
	- Refactored DeleteCommand
	- Refactored Storage
	- Wrote most of Developer Guide

-----

#### [DARREN WEE ZHE YU](https://github.com/darrenwee)
<img src="images/Darren.jpg" width="150"><br>
* Components in charge of: [`Parser`](https://github.com/CS2103AUG2016-F11-C3/main/blob/master/src/main/java/seedu/address/logic/parser/Parser.java), [`DateTimeParser`](https://github.com/CS2103AUG2016-F11-C3/main/blob/master/src/main/java/seedu/address/logic/parser/DateTimeParser.java)
* Aspects/tools in charge of: Regex, `PrettyTime`, `opencsv`
* Features implemented:
   * `edit`
   * Date/time parser for input
   * Date/time formatter for output (pretty-printing)
- Code written: [[functional code](../collated/main/A0147609X.md)][[test code](../collated/test/A0147609X.md)][[docs](../collated/docs/A0147609X.md)]
- Other major contributions:
   - Wrote most of user guide
   - Polymorphic `Item` class (with Yuchuan, Duc, Cara)
   - Miscellaneous parsing methods
   - Wrappers for `DateTimeParser` in `Item`

-----

#### [LE MINH DUC](https://github.com/acuodancer)
<img src="images/MinhDuc.jpg" width="150"><br>

* Components in charge of: [Logic]()
* Aspects/tools in charge of: Eclipse
* Features implemented:
   * [Add tasks]()
   * [Mark tasks as done]()
   * [Undo previous command]()
- Code written: [[functional code](../collated/main/A0144750J.md)][[test code](../collated/test/A0144750J.md)][[docs](../collated/docs/A0144750J.md)]
- Other major contributions:
  - Did the initial refactoring of 
      - `Logic`, `LogicManager`
      - `AddressBook` to `TaskBook`
      - `XmlAdaptedItem`
      - UI elements

-----

#### [YUAN YUCHUAN](https://github.com/yyc)
<img src="images/Yuchuan.png" width="150"><br>

- Components in charge of: [UI](), code integration, `Item`
- Aspects/tools in charge of: Regex, JavaFX
- Features implemented:
   - `edit`
   - UI Modifications
   - Polymorphic `Item` behaviour
- Code written: [[functional code](../collated/main/A0092390E.md)][[test code](../collated/test/A0092390E.md)][[docs](../collated/docs/A0092390E.md)]
- Other major contributions:
  - [`Parser`](https://github.com/CS2103AUG2016-F11-C3/main/blob/master/src/main/java/seedu/address/logic/parser/Parser.java), [`DateTimeParser`](https://github.com/CS2103AUG2016-F11-C3/main/blob/master/src/main/java/seedu/address/logic/parser/DateTimeParser.java)
  - `find` Command

-----
