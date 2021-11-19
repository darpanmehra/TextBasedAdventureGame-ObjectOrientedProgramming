package dungeon.model.character;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dungeon.model.directions.Direction;
import dungeon.model.location.ILocation;
import dungeon.model.treasure.TreasureType;

/**
 * A monster is a character that can be encountered in the dungeon.
 */
public class Monster implements Character {

  static private final int MAX_HEALTH = 100;
  private final String name;
  private int health;
  private ILocation currentLocation;

  /**
   * Constructor for a monster.
   */
  public Monster() {
    this.name = "Otyugh";
    this.health = MAX_HEALTH;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public void pickTreasureFromCurrentLocation(TreasureType treasureType) {
    //A monsters cannot pick up treasure
  }

  @Override
  public Map<TreasureType, Integer> getTreasures() {
    return null;
  }

  @Override
  public void setCurrentLocation(ILocation location) {
    this.currentLocation = location;
  }

  @Override
  public ILocation getCurrentLocation() {
    return this.currentLocation;
  }

  @Override
  public List<ILocation> getLocationVisited() {
    return new ArrayList<>();
  }

  @Override
  public String printTravelStatus() {
    return "";
  }

  @Override
  public int getHealth() {
    return this.health;
  }

  @Override
  public void decrementHealth() {
    this.health = this.health - 50;
  }

  @Override
  public boolean isAlive() {
    return this.health > 0;
  }

  @Override
  public void shootArrow(Direction direction, int distance) {
    //A monster cannot shoot arrows
  }

  @Override
  public String toString() {
    return this.name + " with health " + this.health;
  }
}
