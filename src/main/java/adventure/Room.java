package adventure;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Represents a room in the adventure.
 */
public class Room implements java.io.Serializable{
    private static final long serialVersionUID = 1L;
    private String roomName;
    private int roomID;
    private String shortDesc;
    private String longDesc;
    private ArrayList<Item> itemsInRoom;
    private HashMap<String, Room> map;
    private ArrayList<Integer> entranceIDList;
    private ArrayList<Integer> lootIDList;

    /**
     * Default constructor for Room class. Initializes member variables.
     */
    public Room(){
        roomName = null;
        roomID = 0;
        shortDesc = null;
        longDesc = null;
        itemsInRoom = new ArrayList<Item>();
        map = new HashMap<String, Room>();
        entranceIDList = new ArrayList<Integer>();
        lootIDList = new ArrayList<Integer>();
    }

    /**
     * Overloaded constructor that sets the member variables according to a JSONObject passed in.
     * @param room JSONObject containing details of the room
     */
    public Room(JSONObject room){
        itemsInRoom = new ArrayList<Item>();
        map = new HashMap<String, Room>();
        entranceIDList = new ArrayList<Integer>();
        lootIDList = new ArrayList<Integer>();

        setRoomName((String) room.get("name"));
        setRoomID(((Long)room.get("id")).intValue());
        setShortDesc((String) room.get("short_description"));
        setLongDesc((String) room.get("long_description"));
    }

    /**
     * @param items arraylist of items
     */
    public void setItemsInRoom(ArrayList<Item> items){
        itemsInRoom = items;
    }

    /**
     * @param roomsMap hashmap of rooms
     */
    public void setMap(HashMap<String, Room> roomsMap){
        map = roomsMap;
    }

    /**
     * Gets the arraylist of items in the room.
     * @return an arraylist of items in the room
     */
    public ArrayList<Item> listItems(){
        return itemsInRoom;
    }

    /**
     * Gets the name of the room.
     * @return a string containing the room name
     */
    public String getName(){
        return roomName;
    }

    /**
     * Sets the name of the room.
     * @param name a string that contains the room name
     */
    public void setRoomName(String name){
        roomName = name;
    }

    /**
     * Gets the ID of the room.
     * @return an int that contains the room ID
     */
    public int getID(){
        return roomID;
    }

    /**
     * Sets the ID of the room.
     * @param id an int that contains the room ID
     */
    public void setRoomID(int id){
        roomID = id;
    }

    /**
     * Gets the long description of the room.
     * @return a string containing the long description of the room
     */
    public String getLongDescription(){
        return longDesc;
    }

    /**
     * Sets the long description of the room.
     * @param desc a string containing the short description of the room
     */
    public void setLongDesc(String desc){
        longDesc = desc;    
    }

    /**
     * Gets the short description of the room.
     * @return a string containing the short description of the room
     */
    public String getShortDescription(){
        return shortDesc;
    }

    /**
     * Sets the short description of the room.
     * @param desc a string containing the short description of the room
     */
    public void setShortDesc(String desc){
        shortDesc = desc;    
    }

    /**
     * Gets the connected room in a specified direction.
     * @param direction The direction of the requested connected room
     * @return a reference to the room that is in the given direction
     */
    public Room getConnectedRoom(String direction) {
        if(direction.equalsIgnoreCase("North")){direction = "N";}
        else if(direction.equalsIgnoreCase("East")){direction = "E";}
        else if(direction.equalsIgnoreCase("South")){direction = "S";}
        else if(direction.equalsIgnoreCase("West")){direction = "W";}

        if(map.containsKey(direction.toUpperCase())){
            return map.get(direction.toUpperCase());
        }

        return null;
    }

    /**
     * Sets a connected room in a given direction.
     * @param direction the direction for the given room to be set to
     * @param connectedRoom a reference to the room that is to bed connected
     */
    public void setConnectedRoom(String direction, Room connectedRoom) {
       map.put(direction.toUpperCase(), connectedRoom);
    }

    /**
     * Sets the connections between rooms in the hashmap.
     * @param rooms arraylist of all rooms in the adventure
     * @param entranceList JSONArray of the entrances to the room according to the adventure JSON
     */
    public void initializeConnections(ArrayList<Room> rooms, JSONArray entranceList) throws Exception{
        for(Object currentEntrance : entranceList){
            JSONObject entrance = (JSONObject) currentEntrance;
            int entranceID = ((Long) entrance.get("id")).intValue();
            String entranceDir = (String) entrance.get("dir");
            entranceIDList.add(entranceID);
            for(Room room : rooms){
                if(entranceID == room.getID()){setConnectedRoom(entranceDir, room);}
            }
        }
    }

    /**
     * Adds the items in the room into the room. Adds the items to the items in room arraylist.
     * @param items arraylist of all items in the adventure
     * @param lootList JSONArray of the loot in the room according to the adventure JSON
     */
    public void initializeLoot(ArrayList<Item> items, JSONArray lootList){
        for(Object currentLoot : lootList){
            JSONObject loot = (JSONObject) currentLoot;
            int lootID = ((Long) loot.get("id")).intValue();
            lootIDList.add(lootID);
            for(Item item : items){
                if(lootID == item.getID()){
                    addItem(item);   
                    item.setContainingRoom(this);
                }
            }
        }
    }

    /**
     * Gets loot ID list
     * @return loot id list
     */
    public ArrayList<Integer> getLootIDList(){
        return lootIDList;
    }

    /**
     * Adds an item to the arraylist of items in the room.
     * @param newItem the item to be added to the arraylist
     */
    public void addItem(Item newItem){
        itemsInRoom.add(newItem);
    }

    /**
     * Removes item from list of items in room.
     * @param item item to be removed from list of room items
     */
    public void removeItem(Item item){
        itemsInRoom.remove(item);
    }

    /**
     * Gets the entrance ID list.
     * @return entrance id list
     */
    public ArrayList<Integer> getEntranceIDList(){
        return entranceIDList;
    }
    
    /**
     * Gets the room details and details about items in the room.
     * @return a string containing details about rooms and any items in the room
    */
    public String toString(){
        String str = "Room Name: " + roomName + "\nID: " + roomID + "\nShort Description: "
                     + shortDesc + "\nLong Description: " + longDesc + "\n";
        if(listItems().size() > 0){
            str += "Items in room:\n";
            for(Item item : listItems()){
                str += item.toString() + "\n";
            }
            str += "\n";
        }
        return str;
    }
}
