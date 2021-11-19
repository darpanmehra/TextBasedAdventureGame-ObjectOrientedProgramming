package dungeon.controller.commands;

import dungeon.model.IGameState;
import dungeon.model.directions.Direction;

/**
 * This class represents a shoot command.
 */
public class Shoot implements ICommand {

  private final Direction startDirection;
  private final int distance;

  /**
   * Constructor for the shoot command.
   *
   * @param direction the initial direction of the arrow.
   * @param distance  the number of caves the arrow can travel to.
   */
  public Shoot(Direction direction, int distance) {
    if (direction == null) {
      throw new IllegalArgumentException("Direction cannot be null");
    }

    if (distance <= 0 || distance > 5) {
      throw new IllegalArgumentException("Distance must be between 1 and 5");
    }
    this.startDirection = direction;
    this.distance = distance;
  }

  @Override
  public void playGame(IGameState model) {
    model.shootArrow(startDirection, distance);
  }
}
