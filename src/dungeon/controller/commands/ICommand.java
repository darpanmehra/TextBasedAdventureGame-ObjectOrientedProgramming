package dungeon.controller.commands;

import dungeon.model.IGameState;


public interface ICommand {

  void playGame(IGameState model);
}
