package dungeon.location;

import java.util.Map;

import dungeon.directions.Direction;
import dungeon.treasure.ITreasure;

/**
 * Interface for a location in the dungeon. It has a name, a row and a column coordinate, neighbors,
 * and a map of different kinds of treasures.
 */
public interface ILocation extends Comparable<ILocation> {

  /**
   * Get the name of the location.
   *
   * @return the name of the location.
   */
  String getName();

  /**
   * Get the row coordinate of the location.
   *
   * @return the row coordinate of the location.
   */
  int getRowCoordinate();

  /**
   * Get the column coordinate of the location.
   *
   * @return the column coordinate of the location.
   */
  int getColCoordinate();

  /**
   * Join the given location to the North direction of this location.
   *
   * @param loc the location to join.
   */
  void joinLocationToNorthDirection(ILocation loc);

  /**
   * Join the given location to the South direction of this location.
   *
   * @param loc the location to join.
   */
  void joinLocationToSouthDirection(ILocation loc);

  /**
   * Get the location to the East direction of this location.
   *
   * @param loc the location to join.
   */
  void joinLocationToEastDirection(ILocation loc);

  /**
   * Get the location to the West direction of this location.
   *
   * @param loc the location to join.
   */
  void joinLocationToWestDirection(ILocation loc);

  /**
   * Get if the location is a cave or not.
   *
   * @return true if the location is a cave, false otherwise.
   */
  boolean isCave();

  /**
   * Set the treasure of the location as empty.
   */
  void setTreasureEmpty();

  /**
   * Set the treasure of the location.
   */
  void setTreasure();

  /**
   * Get the original treasure of the location (Treasure that was added originally - before a player
   * collected it).
   *
   * @return the original treasure of the location.
   */
  ITreasure getOriginalTreasure();

  /**
   * Get the treasure of the location.
   *
   * @return the treasure of the location.
   */
  ITreasure getTreasure();

  /**
   * Get the map of neighbours of the location.
   *
   * @return the map of neighbours of the location.
   */
  Map<Direction, ILocation> getNeighbours();

  /**
   * Get the location information.
   *
   * @return the location information.
   */
  String printLocationInfo();

}
