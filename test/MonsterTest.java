import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import dungeon.model.character.Character;
import dungeon.model.character.Monster;
import dungeon.model.location.ILocation;
import dungeon.model.location.Location;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MonsterTest {

  private Character monster;

  @Before
  public void setUp() {
    monster = new Monster();
  }

  @Test
  public void getName() {
    assertEquals("Otyugh", monster.getName());
  }

  @Test
  public void getHealth() {
    assertEquals(2, monster.getHealth());
  }

  @Test
  public void decrementHealth() {
    monster.decrementHealth();
    assertEquals(1, monster.getHealth());
  }

  @Test
  public void killMonster() {
    monster.decrementHealth();
    monster.decrementHealth();
    assertEquals(0, monster.getHealth());
  }

  @Test
  public void testAlive() {
    assertTrue(monster.isAlive());

    monster.decrementHealth();
    monster.decrementHealth();
    assertTrue(!monster.isAlive());
  }

  @Test
  public void getCurrentLocation() {
    Random rand = new Random();
    rand.setSeed(20);
    ILocation location = new Location(0, 0, rand);
    monster.setCurrentLocation(location);
    assertEquals(location, monster.getCurrentLocation());
  }

  @Test
  public void setCurrentLocation() {
    Random rand = new Random();
    rand.setSeed(20);
    ILocation location = new Location(0, 0, rand);
    monster.setCurrentLocation(location);
    assertEquals(location, monster.getCurrentLocation());
  }

}