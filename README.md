# Space Adventure Game
* Name: Benjamin Rockman
* Language: Java
* Completed: June 2020
* Description: Text-Based adventure game where the user can choose where to go, items to use/inspect, etc., by typing commands. Game specifications are in JSON files, and the user can provide their own JSON game file if they wish. User provided JSON files are checked for validity.

# How to operate the program
All of the following commands have to be typed while in the /project directory.
* Clean:  mvn clean
* Compile : mvn compile
* Junit: mvn test
* Jar: mvn assembly:assembly (this will make two jar files.  Use the one with dependencies)
* Run the GUI: java -cp 'pathtoexecutablejarwithdependencies' adventure.AdventureView
* Run the game without GUI: java -cp 'pathtoexecutablejarwithdependencies' adventure.Game 'flags'
* Flags: -l 'save name' to load a serialization or -a 'json file name' to load your own json adventure

## Instructions for using the program
You can load your own adventure or save file by following the steps above. If you do not have one, the default adventure will be loaded. Enter 'quit' or 'q' to quit, 'items' to see a list of items in the current room, 'look' to see a long description of the room, 'look (item name)' to see a description of the item, 'take (item name)' to take an item in the room and put it in your inventory, 'inventory' to see the items in your inventory, or 'go (direction)' to go to another room. Not all directions work, so try until you find a direction you can travel. You can also type 'eat (item in inventory)' to eat an edible item, 'wear (item in inventory)' 
to wear a wearable item, 'toss (item in inventory)' to get rid of a tossable item, or 
'read (item in inventory)' to read a readable item.
