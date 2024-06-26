---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# Realodex Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

_{ list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well }_
https://ay2324s2-cs2103t-w10-1.github.io/tp/DeveloperGuide.html

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `LogicManager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

Realodex has implemented a dynamic delete function that either deletes user by index or by their name. Here we illustrate
deletion by index for brevity.

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `RealodexParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `RealodexParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `RealodexParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="700" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<box type="info" seamless>

**Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the Realodex, which `Person` references. This allows Realodex to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<puml src="diagrams/BetterModelClassDiagram.puml" width="450" />

</box>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `RealodexStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.realodex.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

``### Filter by Name feature

#### Implementation

The Filter by Name feature allows users to filter the list of persons in Realodex based on their names. 
It is implemented using a predicate that checks if a person's name contains the keyphrase provided by the user. 
The key components involved in this feature include:

- FilterCommand: A command that, when executed, updates the filtered person list based on the predicate.
- NameContainsKeyphrasePredicate: A predicate that tests whether a person's name contains the given keyphrase.

#### Example Usage Scenario
1. The user launches the application and the Model is initialized with the initial list of persons. 
2. The user executes the command filter n/John, intending to filter out persons whose names contain "John".
3. `LogicManager` parses the command, creating a FilterCommand with a NameContainsKeyphrasePredicate initialized with "John".
4. `FilterCommand` is executed, using the predicate to filter the list of persons.
5. The UI is updated to show only the persons whose names contain "John".

#### Design considerations

Aspect: Handling of partial names

**Alternative 1 (current choice): Allow partial matches of names. For example, filter n/Jo will match "John", "Joanna", etc.**

Pros: More flexible search.

Cons: May return too many results for very short keyphrases.

**Alternative 2: Require exact matches.**

Pros: Precise filtering.

Cons: Less flexible; users must remember exact names.
``

### Sort by Birthday feature

####  Implementation

The "Sort by Birthday" feature enables users to sort persons based on their birthday associated with them, if any. The core components for this feature are:
- SortCommand: A command that, when executed, updates the sorted person list based on the predicate.

#### Example Usage Scenario
1. User launches the application, and the list of persons is loaded.
2. User executes `sort`, aiming to sort persons by birthday
3. LogicManager parses the command into a SortCommand.
4. The SortCommand is executed, sorting the list based on the predicate.
5. The UI sorts the persons by birthday, with the next upcoming birthday first, and all persons without birthdays will not show up. 

#### Design considerations:

Aspect: Matching of remarks

__Alternative 1 (current choice): Support sort by other days besides Today.__

Pros: Allows for more flexible searches.

Cons: May be difficult to understand

### Help feature

####  Implementation

The Help feature provides help to the user by giving details on how all commands are used in a new window. 
The core components for this feature are:
- HelpCommand: A command that, when executed, either shows a new window summarising help for all commands, or
prints the help message in the Main Window for the requested command, depending on user input.
- HelpCommandParser: Processes the user input to instantiate the HelpCommand object appropriately to perform the
correct action (the type of help to give, in this case help for all commands).

#### Example Usage Scenario
1. User launches the application.
2. User executes `help`, wanting to get the help for all commands.
3. LogicManager instantiates a RealodexParser, which parses the command into a HelpCommand.
4. The HelpCommand is executed, showing a new window with help for all the features in Realodex.
5. The GUI reflects that the help window is currently open.

#### Design Considerations

Aspect: Information to include in the Help Window

__Alternative 1 (current choice): Includes summary of ways to use all commands.__

Pros: User does not need to leave the app to get the appropriate help, and can visit the UG if he/she needs more information.

Cons: May be lengthy and hard to find when the set of commands added becomes larger in the future.

__Alternative 2: Only include link to User Guide in the help window.__

Pros: Help window does not have too much information.

Cons: User will need to leave the application and look at a website everytime they require help which can be inconvenient.

### Help by command feature

####  Implementation

The Help by command feature provides help to the user for an individual command specified by the user.
The core components for this feature are:
- HelpCommand: A command that, when executed, either shows a new window summarising help for all commands, or
  prints the help message in the Main Window for the requested command, depending on user input.
- HelpCommandParser: Processes the user input to instantiate the HelpCommand object appropriately to perform the
  correct action (the type of help to give, in this case for individual commands).

#### Example Usage Scenario 
1. User launches the application.
2. User executes `COMMAND help`, wanting to get the help for only specified `COMMAND`.
3. LogicManager instantiates a RealodexPraser, which parses the command into a HelpCommand with appropriate parameters.
4. The HelpCommand is executed, printing the help message for the specified `COMMAND` in the GUI.

#### Design Considerations

Aspect: Method to request for help

__Alternative 1 (current choice): Format is `COMMAND help`.__

Pros: Intuitive syntax for the user, and is consistent with other CLI-based applications.

Cons: Harder to implement and maintain as a Developer as awareness of how other commands are currently being parsed is needed to preserve functionality.

__Alternative 2: Format is `help COMMAND`.__

Pros: Easy to implement as all functionality can be contained within help-related classes only.

Cons: Syntax may not be as intuitive.

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* A real estate agent
* has a need to manage a significant number of contacts of their clients
* has to note down many details about each client
* has to frequently add, delete, and search for clients
* prefer desktop apps over other types
* can type fast
* is reasonably comfortable using CLI apps
* is a real estate agent that wants to store relevant information about clients
* able to store additional notes about contacts

**Value proposition**:
* manage contacts faster than a typical mouse/GUI driven applications.
* storing of information tailored to real-estate agents


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                    | I want to …​                                             | So that I can…​                                                                                   |
|----------|----------------------------|----------------------------------------------------------|---------------------------------------------------------------------------------------------------|
| `* * *`  | first-time user            | receive a simple tutorial on app usage                    | easily navigate Realodex                                                                          |
| `* * *`  | tech-savvy user            | use a command-line interface                              | navigate the app more efficiently due to my fast typing speed                                    |
| `* * *`  | fast typer                 | quickly input various commands in the text box           | perform actions like adding new clients, editing profiles, finding clients, without using GUI    |
| `* * *`  | real-estate agent user     | easily log personal notes after client interactions       | reference these in future conversations for more personalized communication                     |
| `* * *`  | user with inactive clients | delete inactive clients permanently                      | remove them from my database and free up space                                                   |
| `* *`    | real estate agent user     | search for clients interested in specific property listings | quickly match selling and buying clients                                                         |
| `* *`    | real estate agent user     | record and access clients' preferred house types          | filter and match clients with relevant property listings                                         |
| `* *`    | real estate agent user     | analyze trends in housing preferences                     | understand market demands and tailor my services                                                 |
| `* *`    | efficient user             | filter clients by tag                                    | organize and access client information more efficiently                                           |
| `* *`    | efficient user             | filter clients by categories                             | better categorize and manage client information based on personal attributes                     |
| `* *`    | first-time user            | be guided through setting up my user profile              | save my details for future use                                                                   |
| `* *`    | first-time user            | learn how to create and edit client profiles              | manage client information efficiently                                                            |
| `* *`    | first-time user            | understand how to navigate the app and use CLI commands   | effectively use Realodex's features                                                              |
| `* *`    | forgetful user             | get instructions on how to set up profiles and navigate  | refresh my memory on how to use Realodex when needed                                             |
| `* *`    | user with inactive clients | archive inactive clients                                 | hide them from my active list while keeping their information for future reference               |
| `* `     | real estate agent user     | be notified of upcoming client birthdays                  | send personalized greetings and strengthen my relationships                                      |
| `*`      | real estate agent user     | be notified of upcoming holidays                          | prepare gifts for my clients and enhance our relationship                                        |
| `*`      | real estate agent user     | be reminded of significant client milestones              | acknowledge these events and further personalize our relationship                                |
| `*`      | tech-savvy user            | use tab to autofill parts of my command                  | speed up my use of the command line                                                              |


### Use cases

(For all use cases below, the **System** is Realodex and the **Actor** is the user, unless specified otherwise)

**Use case: Creating a user profile**

**Actor: User**

**MSS**

1. User Executes "Add client...." Command:
2. System adds use profile to local storage and replies to user with success message.

    Use case ends.

**Extensions**

* 1a. Name exceeds the length constraints.
  * 1a1. Realodex throws an error and requests shorter name representation.
  * 1a2. User enters new data.
  * Use case resumes from step 1.

* 1b. Name Length is not fully English.
    * 1b1. Realodex throws an error and requests for only English input.
    * 1b2. User enters new data.
      * Use case resumes from step 1.

* 1c. Name contains erraneous whitespace.
    * 1c1. Realodex throws a warning and fixes this for user.
    * Use case ends.

* 1d. Name is not capitalized.
    * 1d1. Realodex throws a warning and fixes this for user.
    * Use case ends.

* 1e. Name is not in expected format.
    * 1e1. Realodex throws an error and highlights the format to user.
    * 1e2. User enters new data.
    * Use case resumes from step 1.

* 1f. Address is not fully English
    * 1f1. Realodex throws an error and requests for only English input.
    * 1f2. User enters new data.
    * Use case resumes from step 1.

* 1g. Address exceeds the length constraints
    * 1g1. Realodex throws an error and requests shorter address representation.
    * 1g2. User enters new data.
    * Use case resumes from step 1.

* 1h. Address is not capitalized for each part.
    * 1h1. Realodex throws a warning and fixes this for user.
    * Use case ends.

* 1i. Income is not in SGD
    * 1i. Realodex throws an error and requests a SGD value.
    * 1i2. User enters new data.
    * Use case ends.

* 1j. Income is negative
    * 1j1. Realodex throws an error and requests a positive income value.
    * 1j2. User enters new data.
    * Use case ends.

* 1k. Additonal notes exceed length constraints.
    * 1k1. Realodex throws an error and requests a shorter input.
    * 1k2. User enters new data.
    * Use case ends.

**Use case: Delete a person**

**MSS**

1.  User requests to delete user
2.  Realodex deletes the person with success message

    Use case ends.

**Extensions**

* 2a. The input name is not found
 * 2a1. Realodex shows an error message "<Name> is not found".
 * Use case ends.
**Use case: List**

**MSS**

1.  User requests to list
2.  Realodex shows the list of all clients

    Use case ends.

**Extensions**

* 2a. The list is empty
  * 2a1. Realodex shows an empty list.
  * Use case ends.

**Use case: Filter by Name**

**MSS**

1. User requests to filter clients by providing a name substring. 
2. Realodex filters and displays a list of all clients whose names include the input substring.

    Use case ends.

**Extensions**

* 1a. The input substring is empty.

    * 1a1. Realodex shows an error message indicating that the filter criteria cannot be empty.

        Use case ends.

* 1b. No clients' names match the input substring.

    * 1b1. Realodex displays an empty list and shows a message indicating that no matches were found.
        
      Use case ends.

**Use case: Filter by Remarks**

**MSS**

1. User requests to filter clients by providing a remark substring. 
2. Realodex filters and displays a list of all clients whose remarks include the input substring.
   Use case ends.

**Extensions**

* 1a. The input substring is empty.

    * 1a1. Realodex shows an error message indicating that the filter criteria cannot be empty.

      Use case ends.

* 1b. No clients' remarks match the input substring.

    * 1b1. Realodex displays an empty list and shows a message indicating that no matches were found.

      Use case ends.

**Use case: Getting help**

**MSS**

1. User requests for help.
2. Realodex displays a new window showing a summary of how all features are used with examples. 

   Use case ends.


**MSS**

1. User requests for help for a specific command.
2. A string summarising how that individual command is used with examples is displayed on the main window.

**Extensions**

* 1a. The requested command does not exist.

    * 1a1. Realodex shows an error message command does not exist.

      Use case ends.

  
### Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `11` or above installed.
2.  Should be able to hold up to 1000 persons without a noticeable sluggishness in performance for typical usage.
3.  A real estate agent with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4.  Should be able to have up to 500 client profiles.
5.  The response to any command should become visible within 5 seconds.

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **Client Profile**: Details of customer of the Real Esate Agent looking to buy / sell / rent a property
* **Command Line Interface (CLI)**: A text-based interface used to interact with the software by entering commands into a terminal or console window, typically preferred by users who prefer efficiency and automation.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
