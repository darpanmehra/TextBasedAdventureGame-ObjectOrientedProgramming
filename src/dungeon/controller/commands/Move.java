package dungeon.controller.commands;


import dungeon.model.IGameState;
import dungeon.model.directions.Direction;


public class Move implements ICommand {
  private Direction d;

  public Move(String input) {
    if (input == null) {
      throw new IllegalArgumentException("Direction cannot be null");
    }

    input = input.toUpperCase();
    switch (input) {
      case "NORTH":
        d = Direction.NORTH;
        break;
      case "SOUTH":
        d = Direction.SOUTH;
        break;
      case "EAST":
        d = Direction.EAST;
        break;
      case "WEST":
        d = Direction.WEST;
        break;
      default:
        throw new IllegalArgumentException("Invalid direction");
    }
  }

  @Override
  public void playGame(IGameState model) {
    if (model == null) {
      throw new IllegalArgumentException("Model cannot be null");
    }
    model.movePlayer(this.d);
  }
}
