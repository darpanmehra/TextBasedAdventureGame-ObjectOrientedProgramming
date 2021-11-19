# About

* Degree: Masters in Computer Science
* Course: **CS5010 - Programming Design Paradigms** (Northeastern University)
* Professor: **Clark Freifeld** (Northeastern University)
* Term: *Fall 2021*
* Designed and developed: **Darpan Mehra**

## Overview - Project 4 Text-based Adventure Game

Dungeons are generated at random following some set of constraints resulting in a different network each time the game begins.
A dungeon can be non-wrapping or wrapping in nature. For example, moving to the north from row 0 (at the top) in the grid moves the player to the location in the same column in row 5 (at the bottom). This is an example of a wrapping dungeon.


Each location in the dungeon is connected by exactly one path if the interconnectivity is 0 or there can be more number of paths if the interconnectivity is high.
Treasure is generated at random caves in the dungeon. The percentage of the caves that contain treasure is given by the client.
Arrows are added randomly to the dungeon. The percentage of the arrows that are added is given by the client and appears to be in the same frequency as the treasures.
Monsters are added to random caves in the dungeon. The number of monsters is given by the client.
There is a designated monster at the end location of the dungeon and never appears at the starting location.


A starting point is randomly selected from the dungeon. The player is given 3 arrows to start with.
The player can choose any of the fours actions (move, shoot, pick up, or quit) to take at any point in the game. The player can only move to a location that is connected to the current location. The player can pick up any treasure that is in the same location as the player. If the player enters a cave with a perfectly healthy monster, the player loses the game. A player has 50% of survival if the cave has a injured monster.

The player is given many cues to help them navigate the dungeon.

When a player reaches the end location without being killed, the game is over.

## List of Features

* Create a non-wrapping dungeon
* Create a wrapping dungeon
* Create a dungeon with the interconnectivity of 0 or more
* Create a dungeon with caves having treasures by specifying the percent of caves to have treasure
* Create a dungeon with arrows in the same frequency as the treasures
* Create a dungeon with monsters in random caves by specifying the number of monsters.
* Create a dungeon with a monster at the end location
* Generate a starting point for the player and assign it to the player
* Get the ending point for the player in the dungeon
* Ability to choose actions (move, shoot, pick up, or quit)
* Ability to move to a location that is connected to the current location
* Ability to pick up any treasure that is in the same location as the player
* Ability to lose the game if the player enters a cave with a perfectly healthy monster
* Ability to survive if the player enters a cave with an injured monster with a 50% chance
* Ability to shoot an arrow in a direction with a specified number of caves
* Ability to navigate the dungeon with the help of the cues
* Ability to quit the game
* Ability to win the game if the player reaches the end location without being killed

## How To Run

Run the jar file.
1. First argument: Dungeon height
2. Second argument: Dungeon width
3. Third argument: Interconnectivity
4. Fourth argument: Dungeon type (wrapping / nonwrapping)
5. Fifth argument: Percent of caves to have treasure
6. Sixth argument: Number of monsters in the dungeon

```bash
cd /YourProjectDirectory/res
java -jar Project04-AdventureGame.jar 6 6 0 nonwrapping 20 5
```

## How to Use the Program

1. Initialize the model with the dungeon attributes such as height, width, inter-connectivity, type, treasure percentage, number of monsters and random function
    1. e.g. ```model = new GameState(dungeonHeight, dungeonWidth, interConnectivity, dungeonType, treasurePercentage, monsterCount, rand);```
2. Initialize the controller with a Scanner, an Appendable, and the model
    1. e.g. ```controller = new Controller(scanner, appendable, model)```
3. Run the controller
   1. ```controller.playGame()``` 

Additional Information:

1. Player will be positioned at the starting location
2. At each location, player will be given a choice of actions (move, pick up, shoot, or quit)
   1. e.g. ```Move, Pickup, Shoot, or Quit (M-P-S-Q)?```
3. The player can choose a Command of choice and take the action
   1. e.g. ```Move, Pickup, or Shoot (M-P-S-Q)? M
      Move where? west
      Moving WEST```
4. Player gets cues in the gave if it is close to a monster
   1. e.g. ```You smell something terrible nearby``` - single Otyugh 1 position from the player's current location or that there are multiple Otyughs within 2 positions from the player's current location
   2. e.g. ```You slightly smell something nearby``` - there is a single Otyugh 2 positions from the player's current location
5. Player can shoot an arrow in the direction specifying the no.of caves
   1. e.g. ```Move, Pickup, or Shoot (M-P-S-Q)? S
      Which direction? east
      No.of caves (1-5)? 1
      Shooting in EAST at distance of 1```
6. Player can quit the game
   1. e.g. ```Move, Pickup, or Shoot (M-P-S-Q)? Q
      You quit the game.
      Goodbye!```
7. Player wins the game if the player reaches the end location without being killed
   1. e.g. ```Game Over!
      You win! You made it to the end.```
8. Player loses the game if the player enters a cave with a perfectly healthy monster or an injured monster with a 50% chance
   1. e.g. ```Game Over!
      Chomp, chomp, chomp, you are eaten by an Otyugh!
      Better luck next time.```

## Description of Examples

1. Example 1: Example1.txt
   1. Player navigates in the dungeon with the help of the cues.
   2. Player picks up different treasures.
   3. Player picks up arrows.
   4. Player shoots an arrow and kills an Otyugh.
   5. Player navigates to the end location without being killed
2. Example 2: Example2.txt
   1. Player kills an Otyugh and steps in that location.
   2. Player moves to another location with an Otyugh.
   3. Player gets eaten by an Otyugh and loses the game.
3. Example 3: Example3.txt
   1. Player quits the game.

## Design/Model Changes
1. Added a command interface to the controller
2. Extended various commands off the command interface
3. Added arrows to the treasure type enum
4. Added health to the player and the monster

## Assumptions
1. Dungeon height is between 6 and 100
2. Dungeon width is between 6 and 100
3. Interconnectivity cannot be less than 0
4. A big interconnectivity number (greater than maximum paths) means that the dungeon is fully open.
5. Dungeon can be only of two types - wrapping/ non-wrapping
6. Minimum quantity of each type in a treasure is 1 and maximum is 10.
7. If the player reaches the end location without being killed, he wins. But, he does not get to pick any type of treasure in that location as the game ends as soon as he steps into the end location.

## Limitations
* Java version 11 or more required.

## Citations
1. https://en.wikipedia.org/wiki/Kruskal%27s_algorithm
2. https://www.geeksforgeeks.org/kruskals-minimum-spanning-tree-algorithm-greedy-algo-2/
3. https://www.educba.com/depth-limited-search/
4. https://docs.oracle.com/javase/8/docs/api/java/util/stream/package-summary.html
5. https://refactoring.guru/design-patterns/command
