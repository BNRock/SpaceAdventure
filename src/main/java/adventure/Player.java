package adventure;
import java.util.ArrayList;

/**
 * Represents a player in the game.
 */
public class Player implements java.io.Serializable{
    private static final long serialVersionUID = 1L;
    private ArrayList<Item> inventory;
    private String name;
    private Room currentRoom;
    private String saveGameName;

    /**
     * Default constructor. Sets member variables.
     */
    public Player(){
        inventory = new ArrayList<Item>();
        name = "";
        currentRoom = null;
        saveGameName = "";
    }

    /**
     * @param inv arraylist of items in inventory
     */
    public void setInventory(ArrayList<Item> inv){
        inventory = inv;
    }
    /**
     * Gets arraylist containing items in inventory
     * @return inventory arraylist
     */
    public ArrayList<Item> getInventory(){
        return inventory;
    }

    /**
     * Adds item to inventory arraylist
     * @param item item to be added to inventory
     */
    public void addItem(Item item){
        inventory.add(item);
    }

    /**
     * Removes item from inventory arraylist
     * @param item item to be removed from inventory
     */
    public void removeItem(Item item){
        if(inventory.contains(item)){
            inventory.remove(item);
        }
    }

    /**
     * Gets the player's name.
     * @return string containing player's name
     */
    public String getName(){
        return name;
    }

    /**
     * Sets the player's name.
     * @param userName string containing player's name
     */
    public void setName(String userName){
        name = userName;
    }

    /**
     * Sets the member variable of current room.
     * @param room A room object that represents the room the user has just entered
     */
    public void setCurrentRoom(Room room){
        currentRoom = room;
    }

    /**
     * Gets the current room the user is in.
     * @return a reference to the room object the user is currently in.
     */
    public Room getCurrentRoom(){
        return currentRoom;
    }

    /**
     * Gets the filepath of the serialized state.
     * @return string containing filepath of serialized state
     */
    public String getSaveGameName(){
        return saveGameName;
    }

    /**
     * Sets the filepath of the serialized state.
     * @param saveName string containing filepath of serialized state
     */
    public void setSaveGameName(String saveName){
        saveGameName = saveName;
    }

    /**
     * Gets the player details.
     * @return a string containing details about the player
     */
    public String toString(){
        String str = "Player Name: " + name + "\nCurrent Room: " + currentRoom.getName() 
        + "\nSave Name: "+ saveGameName + "\n";
        if(inventory.size() > 0){
            str += "Items in inventory:\n";
            for(Item item : inventory){
                str += item.toString() + "\n";
            }
            str += "\n";
        }
        return str;
    }
}
