import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import dungeon.character.Character;
import dungeon.character.Player;
import dungeon.location.ILocation;
import dungeon.location.Location;
import dungeon.treasure.ITreasure;
import dungeon.treasure.Treasure;

import static org.junit.Assert.assertEquals;

/**
 * This class tests the Character class.
 */
public class CharacterTest {

  private Character player;
  private Random random;

  @Before
  public void setUp() throws Exception {
    random = new Random();
    random.setSeed(20);
    player = new Player("Player");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetUpNull() {
    player = new Player((Character) null);
  }

  @Test
  public void getName() {
    assertEquals("Player", player.getName());
  }

  @Test
  public void testToStringImpl() {
    //Set location
    ILocation location = new Location(1, 0, random);
    player.setCurrentLocation(location);

    //Add treasure
    ITreasure treasure = new Treasure(random);
    player.addTreasure(treasure);

    String expected = "Player is at (1, 0) and has treasures {DIAMONDS=7, RUBIES=4, SAPPHIRES=2}";
    assertEquals(expected, player.toString());
  }

  @Test
  public void addTreasure() {
    ITreasure treasure = new Treasure(random);
    player.addTreasure(treasure);
    String expected = "{DIAMONDS=7, RUBIES=4, SAPPHIRES=2}";

    assertEquals(expected, player.getTreasures().toString());
  }

  @Test
  public void addTwoTreasure() {
    ITreasure treasure1 = new Treasure(random);
    assertEquals("{RUBIES=4, SAPPHIRES=2, DIAMONDS=7}", treasure1.getTreasure().toString());

    ITreasure treasure2 = new Treasure(random);
    assertEquals("{RUBIES=2, SAPPHIRES=6, DIAMONDS=6}", treasure2.getTreasure().toString());

    player.addTreasure(treasure1);
    player.addTreasure(treasure2);
    String expected = "{DIAMONDS=13, RUBIES=6, SAPPHIRES=8}";
    assertEquals(expected, player.getTreasures().toString());
  }

  @Test
  public void getTreasures() {
    ITreasure treasure = new Treasure(random);
    player.addTreasure(treasure);
    String expected = "{DIAMONDS=7, RUBIES=4, SAPPHIRES=2}";

    assertEquals(expected, player.getTreasures().toString());
  }

  @Test
  public void getCurrentLocation() {
    ILocation location = new Location(0, 0, random);
    player.setCurrentLocation(location);
    assertEquals(location, player.getCurrentLocation());
  }

  @Test
  public void getCurrentLocationEmpty() {
    assertEquals(null, player.getCurrentLocation());
  }

  @Test
  public void setCurrentLocation() {
    ILocation location = new Location(0, 0, random);
    player.setCurrentLocation(location);
    assertEquals(location, player.getCurrentLocation());

    //Change location
    ILocation location2 = new Location(1, 0, random);
    player.setCurrentLocation(location2);
    assertEquals(location2, player.getCurrentLocation());
  }

  @Test
  public void movePlayer() {
    ILocation location = new Location(0, 0, random);
    player.setCurrentLocation(location);
    assertEquals(location, player.getCurrentLocation());

    //Change location
    ILocation location2 = new Location(0, 1, random);
    player.setCurrentLocation(location2);
    assertEquals(location2, player.getCurrentLocation());

    //Change location
    ILocation location3 = new Location(0, 2, random);
    player.setCurrentLocation(location3);

    assertEquals(location3, player.getCurrentLocation());
  }

  @Test
  public void printTravelStatus() {
    ILocation location = new Location(0, 0, random);
    ILocation location2 = new Location(1, 0, random);
    ILocation location3 = new Location(2, 0, random);
    player.setCurrentLocation(location);
    player.setCurrentLocation(location2);
    player.setCurrentLocation(location3);
    String expected = "Player has traveled to the following locations: [(0, 0) (1, 0) (2, 0) ].\n" +
            "Treasures: {}";
    assertEquals(expected, player.printTravelStatus());
  }

  @Test
  public void printTravelStatusEmpty() {
    String expected = "Player has traveled to the following locations: [].\n" +
            "Treasures: {}";
    assertEquals(expected, player.printTravelStatus());
  }


}