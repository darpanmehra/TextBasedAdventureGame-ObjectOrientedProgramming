package dungeon.controller.commands;

import dungeon.model.IGameState;
import dungeon.model.treasure.TreasureType;

public class Pick implements ICommand {

  private final TreasureType treasureType;

  public Pick(String input) {
    if (input == null) {
      throw new IllegalArgumentException("Direction cannot be null");
    }

    input = input.toLowerCase();
    switch (input) {
      case "rubies":
      case "ruby":
        treasureType = TreasureType.RUBIES;
        break;
      case "diamonds":
      case "diamond":
        treasureType = TreasureType.DIAMONDS;
        break;
      case "sapphire":
      case "sapphires":
        treasureType = TreasureType.SAPPHIRES;
        break;
      case "arrow":
      case "arrows":
        treasureType = TreasureType.ARROWS;
        break;
      default:
        throw new IllegalArgumentException("Invalid treasure type");
    }
  }

  @Override
  public void playGame(IGameState model) {
    if (model == null) {
      throw new IllegalArgumentException("Model cannot be null");
    }
    model.pickTreasureFromCurrentLocation(treasureType);
  }
}
