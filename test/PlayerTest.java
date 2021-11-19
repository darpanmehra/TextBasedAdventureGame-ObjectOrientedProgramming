import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import dungeon.model.character.Character;
import dungeon.model.character.Player;
import dungeon.model.directions.Direction;
import dungeon.model.location.ILocation;
import dungeon.model.location.Location;
import dungeon.model.treasure.TreasureType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * This class tests the Player class.
 */
public class PlayerTest {

  private Character player;
  private Random random;

  @Before
  public void setUp() throws Exception {
    random = new Random();
    random.setSeed(20);
    player = new Player("Player", random);
  }

  @Test
  public void getCurrentLocationEmpty() {
    assertEquals(null, player.getCurrentLocation());
  }


  @Test
  public void getName() {
    assertEquals("Player", player.getName());
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
  public void testPickTreasureFromCurrentLocation() {
    Random rand = new Random();
    rand.setSeed(20);
    //Create Location, add neighbor, add treasure
    ILocation location = new Location(0, 0, rand);
    ILocation south = new Location(1, 0, rand);
    location.joinLocationToSouthDirection(south);
    location.setTreasure();
    assertEquals("2 Sapphires, 7 Diamonds, 4 Rubies", location.getTreasure().toString());

    //Set location to player
    player.setCurrentLocation(location);

    player.pickTreasureFromCurrentLocation(TreasureType.RUBIES);
    assertEquals("{Rubies=1, Arrows=3}", player.getTreasures().toString());
    player.pickTreasureFromCurrentLocation(TreasureType.RUBIES);
    player.pickTreasureFromCurrentLocation(TreasureType.RUBIES);
    player.pickTreasureFromCurrentLocation(TreasureType.RUBIES);
    assertEquals("{Rubies=4, Arrows=3}", player.getTreasures().toString());

    assertEquals("2 Sapphires, 7 Diamonds", location.getTreasure().toString());
  }

  @Test
  public void testPickDifferentTypesOfTreasures() {
    Random rand = new Random();
    rand.setSeed(20);
    //Create Location, add neighbor, add treasure
    ILocation location = new Location(0, 0, rand);
    ILocation south = new Location(1, 0, rand);
    location.joinLocationToSouthDirection(south);
    location.setTreasure();
    assertEquals("2 Sapphires, 7 Diamonds, 4 Rubies", location.getTreasure().toString());

    //Set location to player
    player.setCurrentLocation(location);

    player.pickTreasureFromCurrentLocation(TreasureType.RUBIES);
    assertEquals("{Rubies=1, Arrows=3}", player.getTreasures().toString());
    player.pickTreasureFromCurrentLocation(TreasureType.SAPPHIRES);
    player.pickTreasureFromCurrentLocation(TreasureType.DIAMONDS);
    player.pickTreasureFromCurrentLocation(TreasureType.DIAMONDS);
    assertEquals("{Diamonds=2, Rubies=1, Sapphires=1, Arrows=3}",
            player.getTreasures().toString());

    assertEquals("1 Sapphires, 5 Diamonds, 3 Rubies", location.getTreasure().toString());
  }

  @Test
  public void testPickArrowFromLocation() {
    ILocation location = new Location(0, 0, random);
    location.addArrow();
    location.addArrow();
    location.addArrow();
    assertEquals("3 Arrows", location.getTreasure().toString());
    player.setCurrentLocation(location);

    //First pick
    player.pickTreasureFromCurrentLocation(TreasureType.ARROWS);
    assertEquals("{Arrows=4}", player.getTreasures().toString());
    assertEquals("2 Arrows", location.getTreasure().toString());

    //Second pick
    player.pickTreasureFromCurrentLocation(TreasureType.ARROWS);
    assertEquals("{Arrows=5}", player.getTreasures().toString());
    assertEquals("1 Arrows", location.getTreasure().toString());
  }

  @Test
  public void getTreasures() {
    Random rand = new Random();
    rand.setSeed(20);
    //Create Location, add neighbor, add treasure
    ILocation location = new Location(0, 0, rand);
    ILocation south = new Location(1, 0, rand);
    location.joinLocationToSouthDirection(south);
    location.setTreasure();
    assertEquals("2 Sapphires, 7 Diamonds, 4 Rubies", location.getTreasure().toString());

    //Set location to player and pick up treasure
    player.setCurrentLocation(location);
    player.pickTreasureFromCurrentLocation(TreasureType.RUBIES);
    //Get treasures
    assertEquals("{Rubies=1, Arrows=3}", player.getTreasures().toString());
  }

  @Test
  public void setCurrentLocation() {
    Random rand = new Random();
    rand.setSeed(20);
    //Create Location
    ILocation location = new Location(0, 0, rand);
    player.setCurrentLocation(location);
    assertEquals(location, player.getCurrentLocation());
  }

  @Test
  public void getCurrentLocation() {
    Random rand = new Random();
    rand.setSeed(20);
    //Create Location
    ILocation location = new Location(0, 0, rand);
    player.setCurrentLocation(location);
    assertEquals(location, player.getCurrentLocation());
  }

  @Test
  public void getHealth() {
    assertEquals(player.getHealth(), 100);
  }

  @Test
  public void decrementHealth() {
    player.decrementHealth();
    assertEquals(player.getHealth(), 0);
  }

  @Test
  public void isAlive() {
    assertTrue(player.isAlive());
    player.decrementHealth();
    assertFalse(player.isAlive());
  }

  @Test
  public void testPlayerStartWith3Arrows() {
    assertEquals("{Arrows=3}", player.getTreasures().toString());
  }

  @Test
  public void testArrowCountDecrementsAfterShooting() {
    ILocation location = new Location(0, 0, random);
    ILocation south = new Location(1, 0, random);
    location.joinLocationToSouthDirection(south);
    player.setCurrentLocation(location);

    player.shootArrow(Direction.SOUTH, 1);
    assertEquals("{Arrows=2}", player.getTreasures().toString());
    player.shootArrow(Direction.SOUTH, 1);
    assertEquals("{Arrows=1}", player.getTreasures().toString());
    player.shootArrow(Direction.SOUTH, 1);
    assertEquals("{Arrows=0}", player.getTreasures().toString());
  }

  @Test
  public void testPLayerShootsWithNoArrow() {
    ILocation location = new Location(0, 0, random);
    ILocation south = new Location(1, 0, random);
    location.joinLocationToSouthDirection(south);
    player.setCurrentLocation(location);

    player.shootArrow(Direction.SOUTH, 1);
    player.shootArrow(Direction.SOUTH, 1);
    player.shootArrow(Direction.SOUTH, 1);
    try {
      player.shootArrow(Direction.SOUTH, 1);
      fail("Should have thrown exception");
    } catch (IllegalStateException e) {
      assertEquals("No arrows remaining", e.getMessage());
    }
  }

  @Test
  public void testPlayerShootsInNonExistentDirection() {
    ILocation location = new Location(0, 0, random);
    ILocation south = new Location(1, 0, random);
    location.joinLocationToSouthDirection(south);
    player.setCurrentLocation(location);

    try {
      player.shootArrow(Direction.NORTH, 1);
      fail("Should have thrown exception");
    } catch (IllegalArgumentException e) {
      assertEquals("Direction is not valid", e.getMessage());
    }
  }


  @Test
  public void printTravelStatus() {
    ILocation location = new Location(0, 0, random);
    ILocation south = new Location(1, 0, random);
    location.joinLocationToSouthDirection(south);
    player.setCurrentLocation(location);
    player.setCurrentLocation(south);
    assertEquals("Player has traveled to the following locations: "
            + "[(0, 0), (1, 0)] and has 3 Arrows", player.printTravelStatus());
  }

  @Test
  public void testHalfSurvivalDies() {
    Random random = new Random();
    random.setSeed(2);
    player = new Player("Player", random);
    ILocation location = new Location(0, 0, random);
    ILocation south = new Location(1, 0, random);
    location.joinLocationToSouthDirection(south);
    location.setTreasure();
    location.setMonster();

    assertEquals(player.getHealth(), 100);
    assertTrue(player.isAlive());

    assertEquals(location.getMonster().getHealth(), 100);
    location.getMonster().decrementHealth();
    assertEquals(location.getMonster().getHealth(), 50);

    player.setCurrentLocation(location);

    assertEquals(player.getHealth(), 0);
    assertFalse(player.isAlive());
  }

  @Test
  public void testHalfSurvivalSurvives() {
    ILocation location = new Location(0, 0, random);
    ILocation south = new Location(1, 0, random);
    location.joinLocationToSouthDirection(south);
    location.setTreasure();
    location.setMonster();

    assertEquals(player.getHealth(), 100);
    assertTrue(player.isAlive());

    assertEquals(location.getMonster().getHealth(), 100); //monster is healthy
    location.getMonster().decrementHealth(); //Decrement monster health
    assertEquals(location.getMonster().getHealth(), 50); // monster is half dead

    player.setCurrentLocation(location);

    assertEquals(player.getHealth(), 100);
    assertTrue(player.isAlive()); //Player survives
  }

  @Test
  public void testPlayerDiesInLocationWithHealthyMonster() {
    ILocation location = new Location(0, 0, random);
    ILocation south = new Location(1, 0, random);
    location.joinLocationToSouthDirection(south);
    location.setTreasure();
    location.setMonster();

    assertEquals(player.getHealth(), 100);
    assertTrue(player.isAlive());
    player.setCurrentLocation(location);

    assertEquals(player.getHealth(), 0);
    assertFalse(player.isAlive()); //Player dies
  }

  @Test
  public void getLocationVisited() {
    ILocation location = new Location(0, 0, random);
    ILocation south = new Location(1, 0, random);
    location.joinLocationToSouthDirection(south);
    player.setCurrentLocation(location);
    player.setCurrentLocation(south);
    assertEquals(player.getLocationVisited().toString(), "[(0, 0), (1, 0)]");
  }

  @Test
  public void testToString() {
    ILocation location = new Location(0, 0, random);
    ILocation south = new Location(1, 0, random);
    location.joinLocationToSouthDirection(south);
    player.setCurrentLocation(location);

    assertEquals("You are in a cave and you have 3 Arrows.\n", player.toString());
  }
}