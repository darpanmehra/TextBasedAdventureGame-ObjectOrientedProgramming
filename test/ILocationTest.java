import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import dungeon.directions.Direction;
import dungeon.location.ILocation;
import dungeon.location.Location;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests the ILocation interface.
 */
public class ILocationTest {

  private Random rand;
  private ILocation location;

  @Before
  public void setUp() throws Exception {
    this.rand = new Random();
    rand.setSeed(50);
    location = new Location(0, 0, rand);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIncorrectLocationCreation() {
    location = new Location(-1, 2, rand);
  }

  @Test
  public void getName() {
    assertEquals("(0, 0)", location.getName());
  }

  @Test
  public void getRowCoordinate() {
    assertEquals(0, location.getRowCoordinate());
  }

  @Test
  public void getColCoordinate() {
    assertEquals(0, location.getColCoordinate());
  }

  @Test
  public void joinLocationToNorthDirection() {
    location = new Location(1, 2, rand);
    ILocation north = new Location(0, 2, rand);
    location.joinLocationToNorthDirection(north);
    assertEquals(north, location.getNeighbours().get(Direction.NORTH));
  }

  @Test
  public void joinLocationToSouthDirection() {
    location = new Location(1, 2, rand);
    ILocation south = new Location(2, 2, rand);
    location.joinLocationToSouthDirection(south);
    assertEquals(south, location.getNeighbours().get(Direction.SOUTH));
  }

  @Test
  public void joinLocationToEastDirection() {
    location = new Location(1, 2, rand);
    ILocation east = new Location(1, 3, rand);
    location.joinLocationToEastDirection(east);
    assertEquals(east, location.getNeighbours().get(Direction.EAST));
  }

  @Test
  public void joinLocationToWestDirection() {
    location = new Location(1, 2, rand);
    ILocation west = new Location(1, 1, rand);
    location.joinLocationToWestDirection(west);
    assertEquals(west, location.getNeighbours().get(Direction.WEST));
  }

  @Test
  public void isCave() {
    location = new Location(1, 2, rand);
    assertFalse(location.isCave());

    ILocation north = new Location(0, 2, rand);
    ILocation south = new Location(2, 2, rand);
    ILocation east = new Location(1, 3, rand);
    ILocation west = new Location(1, 1, rand);
    location.joinLocationToNorthDirection(north);
    location.joinLocationToSouthDirection(south);
    location.joinLocationToEastDirection(east);
    location.joinLocationToWestDirection(west);
    assertTrue(location.isCave());
  }

  @Test
  public void setTreasureEmpty() {

    ILocation north = new Location(0, 2, rand);
    ILocation south = new Location(2, 2, rand);
    ILocation east = new Location(1, 3, rand);
    ILocation west = new Location(1, 1, rand);
    location.joinLocationToNorthDirection(north);
    location.joinLocationToSouthDirection(south);
    location.joinLocationToEastDirection(east);
    location.joinLocationToWestDirection(west);

    location.setTreasure();
    assertEquals("{SAPPHIRES=4, RUBIES=8, DIAMONDS=9}", location.getTreasure().toString());

    location.setTreasureEmpty();
    assertEquals(null, location.getTreasure());
  }

  @Test
  public void setTreasureInTunnel() {
    ILocation north = new Location(0, 2, rand);
    ILocation south = new Location(2, 2, rand);

    location.joinLocationToNorthDirection(north);
    location.joinLocationToSouthDirection(south);

    location.setTreasure();
    assertEquals(null, location.getTreasure());
  }

  @Test
  public void setTreasure() {
    ILocation north = new Location(0, 2, rand);
    ILocation south = new Location(2, 2, rand);
    ILocation east = new Location(1, 3, rand);
    ILocation west = new Location(1, 1, rand);
    location.joinLocationToNorthDirection(north);
    location.joinLocationToSouthDirection(south);
    location.joinLocationToEastDirection(east);
    location.joinLocationToWestDirection(west);

    location.setTreasure();
    assertEquals("{SAPPHIRES=4, RUBIES=8, DIAMONDS=9}", location.getTreasure().toString());
  }

  @Test
  public void getOriginalTreasure() {
    ILocation north = new Location(0, 2, rand);
    ILocation south = new Location(2, 2, rand);
    ILocation east = new Location(1, 3, rand);
    ILocation west = new Location(1, 1, rand);
    location.joinLocationToNorthDirection(north);
    location.joinLocationToSouthDirection(south);
    location.joinLocationToEastDirection(east);
    location.joinLocationToWestDirection(west);

    location.setTreasure();
    assertEquals("{SAPPHIRES=4, RUBIES=8, DIAMONDS=9}",
            location.getOriginalTreasure().toString());

    location.setTreasureEmpty();
    assertEquals(null, location.getTreasure());

    assertEquals("{SAPPHIRES=4, RUBIES=8, DIAMONDS=9}",
            location.getOriginalTreasure().toString());
  }

  @Test
  public void getTreasure() {
    ILocation north = new Location(0, 2, rand);
    ILocation south = new Location(2, 2, rand);
    ILocation east = new Location(1, 3, rand);
    ILocation west = new Location(1, 1, rand);
    location.joinLocationToNorthDirection(north);
    location.joinLocationToSouthDirection(south);
    location.joinLocationToEastDirection(east);
    location.joinLocationToWestDirection(west);

    location.setTreasure();
    assertEquals("{SAPPHIRES=4, RUBIES=8, DIAMONDS=9}", location.getTreasure().toString());
  }

  @Test
  public void getNeighbours() {
    ILocation north = new Location(0, 2, rand);
    ILocation south = new Location(2, 2, rand);
    ILocation east = new Location(1, 3, rand);
    ILocation west = new Location(1, 1, rand);
    location.joinLocationToNorthDirection(north);
    location.joinLocationToSouthDirection(south);
    location.joinLocationToEastDirection(east);
    location.joinLocationToWestDirection(west);

    assertEquals(4, location.getNeighbours().size());
    assertEquals(north, location.getNeighbours().get(Direction.NORTH));
    assertEquals(south, location.getNeighbours().get(Direction.SOUTH));
    assertEquals(east, location.getNeighbours().get(Direction.EAST));
    assertEquals(west, location.getNeighbours().get(Direction.WEST));
    assertEquals("{SOUTH=(2, 2), EAST=(1, 3), WEST=(1, 1), NORTH=(0, 2)}",
            location.getNeighbours().toString());
  }

  @Test
  public void printLocationInfo() {
    ILocation north = new Location(0, 2, rand);
    location.joinLocationToNorthDirection(north);

    location.setTreasure();
    assertEquals("(0, 0), treasure: {SAPPHIRES=4, RUBIES=8, DIAMONDS=9}, " +
            "neighbours: {NORTH=(0, 2)}", location.printLocationInfo());
  }

}