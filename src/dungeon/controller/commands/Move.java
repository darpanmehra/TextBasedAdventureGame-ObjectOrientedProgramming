package dungeon.controller.commands;


import dungeon.model.IGameState;
import dungeon.model.directions.Direction;

/**
 * This class represents a move command.
 */
public class Move implements ICommand {

  private Direction d;

  /**
   * Constructor for Move command.
   * @param direction Direction the direction to move in
   */
  public Move(Direction direction) {
    if (direction == null) {
      throw new IllegalArgumentException("Direction cannot be null");
    }
    this.d = direction;
  }

  @Override
  public void playGame(IGameState model) {
    if (model == null) {
      throw new IllegalArgumentException("Model cannot be null");
    }
    model.movePlayer(this.d);
  }

}
