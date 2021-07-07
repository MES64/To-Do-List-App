# To-Do-List-App

To-do List App with different levels of importance and deadlines; Android (Java)

[Video Demo](https://www.youtube.com/watch?v=pshMmGvd1QA&t=1s)

Made a to-do list app featuring: 
  - Adding and removing items
  - 2 separate lists based on importance
  - Items ordered by their deadline
  - Overdue items in red
  - Checked items being moved to the bottom

Enter the activity and deadline into their respective fields, toggle the importance, and click 'Add' to add the item. It will be added to the list of either important or unimportant items, depending on the importance chosen via the toggle. 

These items can be removed by simply clicking on the 'x' button. 

Items are ordered by their deadline, with the most urgent items at the top of their list. If an item becomes overdue, it turns the colour red to show this. 

Checked items are moved to the bottom of their list, below all the incomplete items. Items are also ordered by deadline within the checked items group. Overdue items that are checked will not be coloured red. 

Unchecking an item returns that item to the unchecked group, in the correct order, and coloured red for being overdue if needed. 

## Installation

For now, you must pull the code into Android Studio and run on an emulator to give it a go yourself. 

## Implementing To-do List

### Data Structures

2 array lists are used to keep track of the item deadlines, one for each to-do list, which maintains the order of the items by deadline. Array lists are used for easier access to deadline information, and to deal with the "None" and checked item cases more easily (compared to using the linear layout for everything). 

### Implementation

#### Adding Items to Correct To-Do List

When the 'Add' button is tapped, the information in the activity and deadline fields are gathered and put together into a newly created Linear Layout. This linear layout is then added to the linear layout representing the to-do list. The correct linear layout (to-do list) is chosen by noting the importance set via the toggle. The to-do list linear layout is contained in a scroll view so that unlimited items can be added and viewed. 

#### Order by Deadlines

The item is inserted into the correct position by comparing the deadlines in the array list, finding the index position where the item belongs, and inserting the item into the linear layout at that index (and the deadline at that index in the correct array list). This can be implemented via binary search (O(log(n)) time), but was implemented by simply looping through the array list until the current time remaining was no longer larger than the item at that index (O(n) time). This was due to simplicity, and the fact that to-do lists are unlikely to be long enough for time complexity to matter. This is something that will be updated at some point though. 

If the deadline is invalid then a deadline of "None" is set in the text shown. The deadline stored in the array list is set to "24:00", to make sure it is inserted after all items with valid deadlines. 

#### Overdue Items

Every second, a function runs which checks every deadline in both array lists and changes the background to red if it is overdue (and unchecked). 

#### Checking Items

An item can be checked by tapping on the checkbox view. This obtains the information contained in the item (linear layout), deletes this item in the to-do list, and reinserts a copy of it into the checked items group at the bottom of the list. The new position is found by adding 25 hours to the original text. Unchecking repeats this process, but using the original deadline. 

#### Removing Items

Items are removed by finding the parent of the 'x' button or checkbox pressed to get the item, then finding the parent of the item to get the correct to-do list, and then getting the index of the item in the to-do list via indexOfChild(). The item is then removed from both the array list and to-do list at that index position. 
