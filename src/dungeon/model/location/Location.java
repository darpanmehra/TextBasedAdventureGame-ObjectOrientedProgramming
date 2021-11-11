package dungeon.model.location;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import dungeon.model.character.Character;
import dungeon.model.character.Monster;
import dungeon.model.directions.Direction;
import dungeon.model.treasure.ITreasure;
import dungeon.model.treasure.Treasure;
import dungeon.model.treasure.TreasureType;

/**
 * A location in the dungeon. It has a name, coordinates, a map of neighbors, and a map of
 * treasures.
 */
public class Location implements ILocation {

  private final String locationName;
  private final int[] coordinates = new int[2];
  private HashMap<Direction, ILocation> neighbours;
  private ITreasure treasure;
  private Random random;
  private Character monster;

  /**
   * Constructor for a location.
   *
   * @param rowCoordinate The row coordinate of the location.
   * @param colCoordinate The column coordinate of the location.
   * @param random        The random function.
   */
  public Location(int rowCoordinate, int colCoordinate, Random random) {

    if (rowCoordinate < 0 || colCoordinate < 0) {
      throw new IllegalArgumentException("Coordinates must be positive");
    }

    if (random == null) {
      throw new IllegalArgumentException("Random cannot be null");
    }

    this.locationName = String.format("(%d, %d)", rowCoordinate, colCoordinate);
    this.coordinates[0] = rowCoordinate;
    this.coordinates[1] = colCoordinate;
    this.neighbours = new HashMap<>();
    this.treasure = null;
    this.monster = null;
    this.random = random;
  }

  /**
   * A copy constructor for a location.
   *
   * @param loc The location to copy.
   */
  public Location(ILocation loc) {
    if (loc == null) {
      throw new IllegalArgumentException("Location cannot be null");
    }
    this.locationName = loc.getName();
    this.coordinates[0] = loc.getRowCoordinate();
    this.coordinates[1] = loc.getColCoordinate();
    this.neighbours = new HashMap<>(loc.getNeighbours());
    this.treasure = loc.getTreasure();
    this.monster = loc.getMonster();
  }

  @Override
  public String getName() {
    return this.locationName;
  }

  @Override
  public int getRowCoordinate() {
    return coordinates[0];
  }

  @Override
  public int getColCoordinate() {
    return coordinates[1];
  }

  @Override
  public Map<Direction, ILocation> getNeighbours() {
    return neighbours;
  }

  @Override
  public boolean isCave() {
    if (neighbours.size() == 0) {
      return false;
    }
    return neighbours.size() != 2;
  }

  @Override
  public void joinLocationToNorthDirection(ILocation loc) {
    neighbours.put(Direction.NORTH, loc);
  }

  @Override
  public void joinLocationToSouthDirection(ILocation loc) {
    neighbours.put(Direction.SOUTH, loc);
  }

  @Override
  public void joinLocationToEastDirection(ILocation loc) {
    neighbours.put(Direction.EAST, loc);
  }

  @Override
  public void joinLocationToWestDirection(ILocation loc) {
    neighbours.put(Direction.WEST, loc);
  }

  @Override
  public void setTreasure() {
    if (isCave()) {
      this.treasure = new Treasure(random);
    }
  }

  @Override
  public ITreasure removeTreasure(TreasureType type) {
    if (type == null) {
      throw new IllegalArgumentException("Treasure type cannot be null");
    }
    if (this.treasure == null) {
      return null;
    }
    if (this.treasure.getTreasure().containsKey(type)) {
      this.treasure.getTreasure().put(type, this.treasure.getTreasure().get(type) - 1);
    }
    return this.treasure;
  }

  @Override
  public ITreasure getTreasure() {
    return this.treasure;
  }

  @Override
  public int compareTo(ILocation o) {
    return this.toString().compareTo(o.toString());
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj.getClass() != this.getClass()) {
      return false;
    }
    final Location other = (Location) obj;
    return (this.coordinates[0] == other.coordinates[0])
            && (this.coordinates[1] == other.coordinates[1]);
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 53 * hash + Arrays.hashCode(this.coordinates);
    return hash;
  }

  @Override
  public String toString() {
    return String.format("(%d, %d)", this.coordinates[0], this.coordinates[1]);
  }

  @Override
  public String printLocationInfo() {
    return String.format("(%d, %d), monster: %s, treasure: %s, neighbours: %s", this.coordinates[0],
            this.coordinates[1], this.monster, this.treasure, this.neighbours);
  }

  @Override
  public Character getMonster() {
    return this.monster;
  }

  @Override
  public void setMonster() {
    this.monster = new Monster();
  }

}
