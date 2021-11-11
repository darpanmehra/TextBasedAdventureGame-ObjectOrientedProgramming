package dungeon.model.character;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dungeon.model.location.ILocation;
import dungeon.model.treasure.ITreasure;
import dungeon.model.treasure.TreasureType;

public class Monster implements Character {

  private String name;
  private int health;
  private ILocation currentLocation;

  public Monster() {
    this.name = "Otyugh";
    this.health = 2;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public void pickTreasureFromCurrentLocation(TreasureType treasureType) {
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
    this.health--;
  }

  @Override
  public boolean isAlive() {
    return this.health > 0;
  }

  @Override
  public int getArrowCount() {
    return 0;
  }

  @Override
  public void setArrowCount(int count) {
  }

  @Override
  public String toString() {
    return this.name + ": " + this.health + " health";
  }
}
