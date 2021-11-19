package dungeon.controller.commands;

import dungeon.model.IGameState;
import dungeon.model.treasure.TreasureType;

/**
 * This class represents a pick command.
 */
public class Pick implements ICommand {

  private final TreasureType treasureType;

  /**
   * Constructor for the pick command.
   *
   * @param treasureType The treasure type to pick.
   */
  public Pick(TreasureType treasureType) {
    if (treasureType == null) {
      throw new IllegalArgumentException("Treasure type cannot be null");
    }
    this.treasureType = treasureType;
  }

  @Override
  public void playGame(IGameState model) {
    if (model == null) {
      throw new IllegalArgumentException("Model cannot be null");
    }
    model.pickTreasureFromCurrentLocation(treasureType);
  }

}
