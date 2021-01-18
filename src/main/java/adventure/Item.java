package adventure;
import org.json.simple.JSONObject;
/**
 * Represents an item in a room.
 */
public class Item implements java.io.Serializable{
    private static final long serialVersionUID = 1L;
    private String itemName;
    private int itemID;
    private String desc;
    private Room containingRoom;

    /**
     * Default constructor for Item class. Initializes member variables.
     */
    public Item(){
        itemName = null;
        itemID = 0;
        desc = null;
        containingRoom = null;
    }

    /**
     * Overloaded constructor that sets the member variables according to a JSONObject passed in.
     * @param item JSONObject containing all the details of the item
     */
    public Item(JSONObject item){
        setItemName((String) item.get("name"));
        setItemID(((Long)item.get("id")).intValue());
        setDesc((String) item.get("desc"));
    }

    /**
     * Gets the name of the item.
     * @return a string that contains the item name
     */
    public String getName(){
        return itemName;
    }

    /**
     * Sets the name of the item.
     * @param name a string with the name of the item
     */
    public void setItemName(String name){
        itemName = name;
    }

    /**
     * Gets the ID of the item.
     * @return an int that contains the item ID
     */
    public int getID(){
        return itemID;
    }

    /**
     * Sets the ID of the item.
     * @param id and int containing the item ID
     */
    public void setItemID(int id){
        itemID = id;
    }

    /**
     * Gets the description of the item.
     * @return A string that contains the description of the item
     */
    public String getLongDescription(){
        return desc;
    }

    /**
     * Sets the description of the item.
     * @param description A string that contains the item description
     */
    public void setDesc(String description){
        desc = description;
    }

    /**
     * Gets the room that the item is in.
     * @return a reference to the containing room
     */
    public Room getContainingRoom(){
        return containingRoom;
    }

    /**
     * Sets the containing room reference.
     * @param room the room that contains the item
     */
    public void setContainingRoom(Room room){
        containingRoom = room;
    }

    /**
     * Gets the item details.
     * @return a string containing details about the item
     */
    public String toString(){
        String str = "Item Name: " + itemName + "\nID: " + itemID 
                     + "\nDescription: " + desc + "\n";

        if(containingRoom != null){
            str += "Containing Room: " + getContainingRoom().getName() + "\n";
        }

        return str;
    }
}
