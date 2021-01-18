package adventure;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Represents an adventure. Room methods are accessed by calling this class first.
 */
public class Adventure implements java.io.Serializable{
    private static final long serialVersionUID = 1L;
    private ArrayList<Item> items;
    private ArrayList<Room> rooms;
    private ArrayList<Integer> roomIDList;
    private ArrayList<Integer> itemIDList;
    private Room currentRoom;
    /**
     * Sets arraylist of all items
     * @param itemsList arraylist containing all items
     */
    public void setItems(ArrayList<Item> itemsList) {
        items = itemsList;
    }

    /**
     * Sets arraylist of all rooms
     * @param roomsList arraylist containing all rooms
     */
    public void setRooms(ArrayList<Room> roomsList) {
        rooms = roomsList;
    }

    /**
     * Default constructor for Adventure class. Initializes all member variables.
     */
    public Adventure(){
        rooms = new ArrayList<Room>();
        items = new ArrayList<Item>();
        roomIDList = new ArrayList<Integer>();
        itemIDList = new ArrayList<Integer>();
        currentRoom = null;
    }

    /**
     * Constructor for adventure that initializes member variables, rooms, and items.
     * @param jsonAdventure a JSONObject of the adventure that the user will play
     */
    public Adventure(JSONObject jsonAdventure) throws Exception{
        rooms = new ArrayList<Room>();
        items = new ArrayList<Item>();
        roomIDList = new ArrayList<Integer>();
        itemIDList = new ArrayList<Integer>();
        
        initializeItems((JSONArray) jsonAdventure.get("item"));
        initializeRooms((JSONArray) jsonAdventure.get("room")); 
        testJSON((JSONArray) jsonAdventure.get("room")); 
    }

    /**
     * Calls multiple methods to test if the JSON file has logic errors.
     * @param roomList JSONArray of rooms in JSON file
     * @throws Exception if a test method finds a logic error
     */
    public void testJSON(JSONArray roomList) throws Exception{
        testExitCompleteness();
        testNoExits();
        testExitCompletenessAcrossRooms(roomList);
        testItemCompleteness();
    }

    /**
     * Initializes all items and adds them to the items arraylist.
     * @param itemList a JSONArray of the items in the adventure json file
     */
    private void initializeItems(JSONArray itemList){
        for(Object currentItem : itemList){
            JSONObject item = (JSONObject) currentItem;
            if(item.containsKey("edible") && item.containsKey("tossable")){ addItem(new SmallFood(item));}
            else if(item.containsKey("wearable") && item.containsKey("readable")){ addItem(new BrandedClothing(item));}
            else if(item.containsKey("edible")){ addItem(new Food(item));}
            else if(item.containsKey("wearable")){ addItem(new Clothing(item));}
            else if(item.containsKey("tossable")){ addItem(new Weapon(item));}
            else if(item.containsKey("readable")){ addItem(new Spell(item));}
            else addItem(new Item(item));
            itemIDList.add(((Long)item.get("id")).intValue());
        }
    }

    /**
     * Initializes all rooms, loot, connections. Adds the rooms to the rooms arraylist.
     * @param roomList a JSONArray of the rooms in the adventure json file
     */
    private void initializeRooms(JSONArray roomList) throws Exception{
        for(Object curRoom : roomList){
            JSONObject room = (JSONObject) curRoom;       
            Room newRoom = new Room(room);  
            roomIDList.add(((Long)room.get("id")).intValue());   
            addRoom(newRoom);
            if(room.containsKey("entrance")){
                testRoomExitSyntax((JSONArray) room.get("entrance"));
            }else{
                throw new Exception("Room has no entrance array.");
            }
            
            
            
            if(room.containsKey("start")){
                setCurrentRoom(newRoom);
            }
            
            if(room.containsKey("loot")){
                JSONArray lootList = (JSONArray) room.get("loot");
                newRoom.initializeLoot(items, lootList);
            }
                     
        }
        
        for(int i = 0; i < rooms.size(); i++){
            JSONArray entranceList = (JSONArray)((JSONObject) roomList.get(i)).get("entrance");
            rooms.get(i).initializeConnections(rooms, entranceList);
        }
        
    }

    /**
     * Tests if any entrances in JSON file are incorrectly named.
     * @param entranceList JSONArray of entrances from JSON file
     * @throws Exception if an entrance is not named is JSON properly
     */
    public void testRoomExitSyntax(JSONArray entranceList) throws Exception{
        for(Object currentEntrance : entranceList){
            JSONObject entrance = (JSONObject) currentEntrance;
            String entranceDir = (String) entrance.get("dir");
            if(!entranceDir.matches("(?i)|N|E|S|W|up|down")){
                
                throw new Exception("Room has entrance not matching proper syntax.");
            }
        }
    }

    /**
     * Tests if there is an entrance ID that doesnt lead to a room.
     * @throws Exception if there is an entrance ID that doesnt lead to a room
     */
    public void testExitCompleteness() throws Exception{
        for(Room room : rooms){
            for(Integer entranceID : room.getEntranceIDList()){
                if(!roomIDList.contains(entranceID.intValue())){
                    
                    throw new Exception("Entrance ID does not correspond to any room.");
                }
            }
        }
    }

    /**
     * Tests to see if any room has no exits.
     * @throws Exception if any room has no exits
     */
    public void testNoExits() throws Exception{
        for (Room room : rooms){
            if(room.getEntranceIDList().size() == 0){
                
                throw new Exception("Room has no exits.");
            }
        }
    }

    /**
     * Tests to see that all the rooms are connected properly according to the JSON.
     * @param roomList JSONArray of rooms from the adventure JSON
     * @throws Exception if connectedroom exits dont match up properly
     */
    public void testExitCompletenessAcrossRooms(JSONArray roomList) throws Exception{
        for(int i = 0; i < roomList.size(); i ++){
            JSONObject room = (JSONObject) roomList.get(i);
            JSONArray entranceList = (JSONArray) room.get("entrance");
            testAcrossRoomsExtension(entranceList, i);
        }
    }

    /**
     * Tests to see that all the rooms are connected properly according to the JSON.
     * @param entranceList jsonarray of entrances
     * @param i current for loop index
     * @throws Exception if JSON has logic error
     */
    public void testAcrossRoomsExtension(JSONArray entranceList, int i) throws Exception{
        for(Object currentEntrance : entranceList){
            JSONObject entrance = (JSONObject) currentEntrance;
            String entranceDir = (String) entrance.get("dir");
            Room conRoom = rooms.get(i).getConnectedRoom(entranceDir);
            if(rooms.get(i) != conRoom.getConnectedRoom(getOpposite(entranceDir))){
                throw new Exception("Room exits do not match across rooms.");
            }
        }
    }

    /**
     * Tests if there is an item in the JSON loot that doesnt correspond to any game item
     * @throws Exception if there is an item in the JSON loot that doesnt correspond to any game item
     */
    public void testItemCompleteness() throws Exception{
        for(Room room : rooms){
            for(Integer lootID : room.getLootIDList()){
                if(!itemIDList.contains(lootID.intValue())){
                    throw new Exception("Loot ID does not correspond to any item.");
                }
            }
        }
    }

    /**
     * Gets the opposite direction of the one passed in.
     * @param dir original direction
     * @return string containing the opposite direction of the one passed in
     */
    private String getOpposite(String dir){
       if(dir.equalsIgnoreCase("N")){ return "S";}
       else if(dir.equalsIgnoreCase("S")){ return "N";}
       else if(dir.equalsIgnoreCase("E")){ return "W";}
       else if(dir.equalsIgnoreCase("W")){ return "E";}
       else if(dir.equalsIgnoreCase("Up")){ return "DOWN";}
       else if(dir.equalsIgnoreCase("Down")){ return "UP";}
       else if(dir.equalsIgnoreCase("N")){ return "S";}
       return null;
    }

    /**
     * Gets an arraylist of all rooms in the adventure.
     * @return arraylist of all rooms in the adventure
     */
    public ArrayList<Room> listAllRooms(){
        return rooms;
    }

    /**
     * Gets an arraylist of all items in the adventure.
     * @return arraylist of all items in the adventure
     */
    public ArrayList<Item> listAllItems(){
        return items;
    }
 
    /**
     * Gets a long description of the current room.
     * @return a string containing the long description of the current room
     */
    public String getCurrentRoomDescription(){
        return currentRoom.getLongDescription();
    }

    /**
     * Sets the member variable of current room.
     * @param room A room object that represents the room the user has just entered
     */
    public void setCurrentRoom(Room room){
        currentRoom = room;
        Game.getPlayer().setCurrentRoom(room);
    }

    /**
     * Gets the current room the user is in.
     * @return a reference to the room object the user is currently in.
     */
    public Room getCurrentRoom(){
        return currentRoom;
    }

    /**
     * Adds a room to the arraylist of all rooms.
     * @param newRoom the room object to add to the arraylist
     */
    public void addRoom(Room newRoom){
        rooms.add(newRoom);
    }

    /**
     * Adds an items to the arraylist of all items.
     * @param newItem the item object to add to the arraylist
     */
    public void addItem(Item newItem){
        items.add(newItem);
    }

    /**
     * Removes items from items arraylist.
     * @param itemForRemoval items to be removed
     */
    public void removeItem(Item itemForRemoval){
        if(items.contains(itemForRemoval)){
            items.remove(itemForRemoval);
        }
    }


    /**
     * Gets information about the rooms and items in the advenure
     * @return a string containing the 
     */
    public String toString(){
        String str = "";

        if(listAllRooms().size() > 0){
            str += "Rooms in adventure:\n\n";
            for(Room room : listAllRooms()){
                str += room.toString() + "\n";
            }
            str += "\n";
        }
        return str;
    }

    /**
     * Parses user command and calls function according to what the command is.
     * @param cmd the user's command
     * @return a string containing what the game should print, null if quitting.
     */
    public String parseCommand(Command cmd){
        String str = null;
        if(!cmd.hasSecondWord()){
            if(cmd.getActionWord().equalsIgnoreCase("look")){ str = lookCommandOne();}
            else if(cmd.getActionWord().equalsIgnoreCase("help")){ str = helpCommand(); }  
            else if(cmd.getActionWord().equalsIgnoreCase("inventory")){ str = inventoryCommand(); }  
            else if(cmd.getActionWord().equalsIgnoreCase("items")){ str = itemsCommand();}  
        }else{
            str = parseTwoWordCommand(cmd);     
        }

        return str;
    }
    /**
     * Parses user command if it is two words and calls function according to what the command is.
     * @param cmd the user's two word command
     * @return a string containing what the game should print
     */

    private String parseTwoWordCommand(Command cmd){
        String str = null;
        if(cmd.getActionWord().equalsIgnoreCase("go")){ str = goCommand(cmd.getNoun());}
        else if(cmd.getActionWord().equalsIgnoreCase("look")){ str = lookCommandTwo(cmd.getNoun());}
        else if(cmd.getActionWord().equalsIgnoreCase("take")){ str = takeCommand(cmd.getNoun());}
        else if(cmd.getActionWord().equalsIgnoreCase("eat")){ str = eatCommand(cmd.getNoun());}
        else if(cmd.getActionWord().equalsIgnoreCase("wear")){ str = wearCommand(cmd.getNoun());}
        else if(cmd.getActionWord().equalsIgnoreCase("toss")){ str = tossCommand(cmd.getNoun());}
        else if(cmd.getActionWord().equalsIgnoreCase("read")){ str = readCommand(cmd.getNoun());}

        return str;
    }

    /**
     * Executes the look command if it's one word.
     * @return string containing current room info.
     */
    private String lookCommandOne(){
        String str = currentRoom.getLongDescription() + "\n";
        return str;
    }

    /**
     * Executes the look command if it's two words.
     * @param itemName item being looked at
     * @return string containing item description
     */
    private String lookCommandTwo(String itemName){
        String str = "";
        for(Item item : currentRoom.listItems()){
            if(itemName.equalsIgnoreCase(item.getName())){
                str = item.getLongDescription() + "\n";
            }
        }
        return str;
    }

    /**
     * Gets instructions for the game.
     * @return a string containing full game instructions
     */
    private String helpCommand(){
        String str = "Enter 'quit' or 'q' to quit, 'items' to see a list of items in the " 
        + "current room, 'look' to see a long description of the room, "
        + "'take <item name>' to take an item in the room and put it in your inventory, "
        + "'inventory' to see the items in your inventory, "
        + "or 'go <direction>' to go to another room. Not all directions work, "
        + "so try until you find a direction you can travel. You can also type "
        + "'eat <item in inventory>' to eat an edible item, wear <item in inventory>' "
        + "to wear a wearable item, 'toss <item in inventory>' to get rid of a tossable item, or "
        + "'read <item in inventory>' to read a readable item.";

        return str;
    }

    /**
     * Prints the names of items in a user's inventory.
     * @return string containing the user's inventory item names
     */
    private String inventoryCommand(){
        String str = "";
        if(Game.getPlayer().getInventory().size() == 0){
            str = "There are no items in your inventory.\n";
        }else{
            for(Item item : Game.getPlayer().getInventory()){
                str += item.getName() + "\n";
            }
        }

        return str;
    }

    /**
     * Prints the names of items in the room.
     * @return string containing names of items in the room
     */
    private String itemsCommand(){
        String str = "";
        if(getCurrentRoom().listItems().size() == 0){
            str = "There are no items in this room.\n";
        }else{
            for(Item item : getCurrentRoom().listItems()){
                str += item.getName() + "\n";
            }
        }

        return str;
    }

    /**
     * Changes the current room according to user command.
     * @param dir direction of room to move the user to
     * @return String containing new room details
     */
    private String goCommand(String dir){
        String str = "";

        setCurrentRoom(currentRoom.getConnectedRoom(dir));
        str += newRoomString(currentRoom);

        return str;
    }

    /**
     * Gets information about a newly entered room.
     * @param room the room being entered
     * @return string containing room information
     */
    public String newRoomString(Room room){
        String str  = "";
        str += "You are in " + currentRoom.getName() + "\n";
        str += currentRoom.getShortDescription() + "\n";
        str +="Items:\n";
        str += itemsCommand();

        return str;
    }

    /**
     * Takes an item out of a room, and puts it in inventory.
     * @param itemName name of the item being put into inventory
     * @return String containing a confirmation of item being put into inventory
     */
    private String takeCommand(String itemName){
        String str = "";
        Item item = null;
        for(Item curItem : currentRoom.listItems()){
            if(itemName.equalsIgnoreCase(curItem.getName())){
                item = curItem;
            }
        }
        executeTakeCommand(item);
        return str;
    }
    /**
     * Executes the take command.
     * @param item items to be take
     * @return string with take confirmation
     */
    private String executeTakeCommand(Item item){
        String str = "";
        Game.getPlayer().addItem(item);
        currentRoom.removeItem(item);
        item.setContainingRoom(null);
        str = "You put " + item.getName() + " in your inventory.\n";

        return str;
    }

    /**
     * Executes the eat command. Takes the item out of the game.
     * @param itemName item to be eaten
     * @return string containing feedback from eating the item
     */
    private String eatCommand(String itemName){
        String str = "";
        Item item = null;
        for(Item curItem : Game.getPlayer().getInventory()){
            if(itemName.equalsIgnoreCase(curItem.getName())){ item = curItem;}
        }
        Game.getPlayer().removeItem(item);
        removeItem(item);
        currentRoom.removeItem(item);
        str += ((Food)item).eat();

        return str;
    }

    /**
     * Executes the wear command. User equips the item
     * @param itemName item to be worn
     * @return string containing feedback from wearing the item
     */
    private String wearCommand(String itemName){
        String str = "";
        Item item = null;
        for(Item curItem : Game.getPlayer().getInventory()){
            if(itemName.equalsIgnoreCase(curItem.getName())){ item = curItem;}
        }
        item.setItemName(item.getName() + " (Equipped)");
        str += ((Clothing)item).wear();
        return str;
    }

    /**
     * Executes the toss command. Takes the item out of inventory and puts it in current room.
     * @param itemName item to be tossed
     * @return string containing feedback from tossing the item
     */
    private String tossCommand(String itemName){
        String str = "";
        Item item = null;
        for(Item curItem : Game.getPlayer().getInventory()){
            if(itemName.equalsIgnoreCase(curItem.getName())){ item = curItem;}
        }
        if(item instanceof SmallFood){ str += ((SmallFood)item).toss();}
        else if(item instanceof Weapon){ str += ((Weapon)item).toss();}
        Game.getPlayer().removeItem(item);
        currentRoom.addItem(item);
        return str;
    }

    /**
     * Executes the read command. Reads item description.
     * @param itemName item to be read
     * @return string containing feedback from reading the item
     */
    private String readCommand(String itemName){
        String str = "";
        Item item = null;
        for(Item curItem : Game.getPlayer().getInventory()){
            if(itemName.equalsIgnoreCase(curItem.getName())){ item = curItem;}
        }
        if(item instanceof BrandedClothing){ str += ((BrandedClothing)item).read();}
        else if(item instanceof Spell){ str += ((Spell)item).read();}

        return str;
    }
}
