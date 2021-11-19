import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import dungeon.model.character.Character;
import dungeon.model.character.Monster;
import dungeon.model.location.ILocation;
import dungeon.model.location.Location;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This class tests the Monster class.
 */
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
    assertEquals(100, monster.getHealth());
  }

  @Test
  public void decrementHealth() {
    monster.decrementHealth();
    assertEquals(50, monster.getHealth());
  }

  @Test
  public void killMonster() {
    assertTrue(monster.isAlive());
    monster.decrementHealth();
    monster.decrementHealth();
    assertEquals(0, monster.getHealth());
    assertFalse(monster.isAlive());
  }

  @Test
  public void testAlive() {
    assertTrue(monster.isAlive());
    //Decrement health to 0
    monster.decrementHealth(); //First hit will decrement health to 50 (half)
    monster.decrementHealth(); //Second hit will decrement health to 0 (kill)
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

  @Test
  public void testGetName() {
    assertEquals("Otyugh", monster.getName());
  }

  @Test
  public void isAlive() {
    assertEquals(true, monster.isAlive());
    monster.decrementHealth();
    assertEquals(true, monster.isAlive());
    monster.decrementHealth();
    assertEquals(false, monster.isAlive());
  }

  @Test
  public void testToString() {
    assertEquals("Otyugh with health 100", monster.toString());
  }
}