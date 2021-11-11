package dungeon.model;

import java.util.List;

import dungeon.model.character.Character;
import dungeon.model.directions.Direction;
import dungeon.model.location.ILocation;
import dungeon.model.treasure.ITreasure;
import dungeon.model.treasure.Treasure;
import dungeon.model.treasure.TreasureType;

/**
 * Interface for the game state. It is used by the client to interact with the game state.
 */
public interface IGameState {

  /**
   * Get the character in the game.
   *
   * @return the character in the game.
   */
  Character getPlayer();

  /**
   * Get if the game is over or not.
   *
   * @return true if the game is over, false otherwise.
   */
  boolean isGameOver();

  /**
   * Get the list of available directions to move to from the player's current location.
   *
   * @return the list of available directions to move to from the player's current location.
   */
  List<Direction> getAvailableDirectionsFromPlayerPosition();

  /**
   * Get the start location of the player.
   *
   * @return the start location of the player.
   */
  ILocation getPlayerStartLocation();

  /**
   * Get the end location of the player.
   *
   * @return the end location of the player.
   */
  ILocation getPlayerEndLocation();

  /**
   * Get the player's current location.
   * @return the player's current location.
   */
  ILocation getPlayerCurrentLocation();

  /**
   * Get the current location of the player.
   *
   * @param direction the direction to move to.
   * @return the current location of the player.
   */
  ILocation movePlayer(Direction direction);

  /**
   * Get the copy of the game dungeon.
   *
   * @return the copy of the game dungeon.
   */
  ILocation[][] getDungeon();

  /**
   * Get the player's travel history.
   *
   * @return the player's travel history.
   */
  String printPlayerTravelStatus();

  /**
   * Get the player's current location status.
   *
   * @return the player's current location status.
   */
  String getPlayerCurrentLocationStatus();


  void pickTreasureFromCurrentLocation(TreasureType treasureType);
}
