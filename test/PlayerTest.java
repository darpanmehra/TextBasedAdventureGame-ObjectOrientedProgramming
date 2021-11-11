import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import dungeon.model.character.Character;
import dungeon.model.character.Player;
import dungeon.model.location.ILocation;
import dungeon.model.location.Location;
import dungeon.model.treasure.TreasureType;

import static org.junit.Assert.*;

public class PlayerTest {

  private Character player;

  @Before
  public void setUp() throws Exception {
    player = new Player("Player");
  }

  @Test
  public void getName() {
    assertEquals("Player", player.getName());
  }

  @Test
  public void pickTreasureFromCurrentLocation() {
    Random rand = new Random();
    rand.setSeed(20);
    ILocation location = new Location(0, 0, rand);
    ILocation south = new Location(1, 0, rand);
    location.joinLocationToSouthDirection(south);
    location.setTreasure();


    assertEquals("{RUBIES=4, DIAMONDS=7, SAPPHIRES=2}", location.getTreasure().toString());
    player.setCurrentLocation(location);

    player.pickTreasureFromCurrentLocation(TreasureType.RUBIES);
    assertEquals("{RUBIES=1}", player.getTreasures().toString());
    player.pickTreasureFromCurrentLocation(TreasureType.RUBIES);
    player.pickTreasureFromCurrentLocation(TreasureType.RUBIES);
    player.pickTreasureFromCurrentLocation(TreasureType.RUBIES);
    assertEquals("{RUBIES=4}", player.getTreasures().toString());

    assertEquals("{RUBIES=0, DIAMONDS=7, SAPPHIRES=2}", location.getTreasure().toString());
  }

  @Test
  public void getTreasures() {
  }

  @Test
  public void getCurrentLocation() {
  }

  @Test
  public void setCurrentLocation() {
  }

  @Test
  public void getLocationVisited() {
  }

  @Test
  public void printTravelStatus() {
  }

  @Test
  public void getHealth() {
  }

  @Test
  public void decrementHealth() {
  }

  @Test
  public void isAlive() {
  }

  @Test
  public void getArrowCount() {
  }

  @Test
  public void setArrowCount() {
  }
}