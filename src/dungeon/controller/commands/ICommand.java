package dungeon.controller.commands;

import dungeon.model.IGameState;

/**
 * Interface for all commands client can make to interact with the program.
 */
public interface ICommand {

  void playGame(IGameState model);

}
