package adventure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.Before;

/**
 * METHOD CORRECT BEHAVIOUR ASSUMPTION
 * getConnectedRoom() will always return the correct room
 * when given the first letter or full name of a compass direction
 * or the direction up or down, no matter the case of the word.
 */

public class RoomTest{
    private Room roomTest;
    private Room connectedRoom;

@Before
public void setup(){
    roomTest = new Room();
    connectedRoom = new Room();
}

@Test
public void testGetConnectedRoomWithRandomWordInput(){
    System.out.println("Testing getConnectedRoom with direction that does not exist");
    roomTest.setConnectedRoom("FAKE", connectedRoom);
    assertTrue(roomTest.getConnectedRoom("FAKE").equals(connectedRoom));
}

@Test
public void testGetConnectedRoomWithSouthLowerInput(){
    System.out.println("Testing getConnectedRoom with a compass direction in lowercase");
    roomTest.setConnectedRoom("S", connectedRoom);
    assertTrue(roomTest.getConnectedRoom("south").equals(connectedRoom));
}

@Test
public void testGetConnectedRoomWithSouthUpperInput(){
    System.out.println("Testing getConnectedRoom with a compass direction in uppercase");
    roomTest.setConnectedRoom("S", connectedRoom);
    assertTrue(roomTest.getConnectedRoom("SOUTH").equals(connectedRoom));
}

@Test
public void testGetConnectedRoomWithSouthMixedInput(){
    System.out.println("Testing getConnectedRoom with a compass direction in mixed case");
    roomTest.setConnectedRoom("S", connectedRoom);
    assertTrue(roomTest.getConnectedRoom("SoUtH").equals(connectedRoom));
}

@Test
public void testGetConnectedRoomWithSLowerInput(){
    System.out.println("Testing getConnectedRoom with a compass first letter in lowercase");
    roomTest.setConnectedRoom("S", connectedRoom);
    assertTrue(roomTest.getConnectedRoom("s").equals(connectedRoom));
}

@Test
public void testGetConnectedRoomWithSUpperInput(){
    System.out.println("Testing getConnectedRoom with a compass first letter in uppercase");
    roomTest.setConnectedRoom("S", connectedRoom);
    assertTrue(roomTest.getConnectedRoom("S").equals(connectedRoom));
}

}
