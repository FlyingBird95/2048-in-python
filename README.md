# 2048 in Java
A simple implementation for the 2048 Game in Java.

# How does the game 2048 work?
2048 is played on a gray 4×4 grid, with numbered tiles that slide smoothly when a player moves them using the four arrow keys. Every turn, a new tile will randomly appear in an empty spot on the board with a value of either 2 or 4. Tiles slide as far as possible in the chosen direction until they are stopped by either another tile or the edge of the grid. If two tiles of the same number collide while moving, they will merge into a tile with the total value of the two tiles that collided. The resulting tile cannot merge with another tile again in the same move.

A scoreboard on the upper-right keeps track of the user's score. The user's score starts at zero, and is incremented whenever two tiles combine, by the value of the new tile. As with many arcade games, the user's best score is shown alongside the current score.

The game is won when a tile with a value of 2048 appears on the board, hence the name of the game. After reaching the 2048 tile, players can continue to play (beyond the 2048 tile) to reach higher scores. When the player has no legal moves (there are no empty spaces and no adjacent tiles with the same value), the game ends.

# How does our program work?
Our implementation can be used for several purposes:
- You can play the game, like a regular 2048 game.
- You can run a number of controllers, that try to play the game for you. Currently, the controllers are:
  - RandomController, which randomly chooses a move with probability (0.25, 0.25, 0.25, 0.25)
  - SemiRandomController, which randomly chooses a move with probability (0.4, 0.4, 0.1, 0.1)
  - ..
  A controller above can be tested using the ControllerManager, which plays the game for a number of times (default 100). Then, the average score and play-time are computed.
  
# How to run the code?
The code is created using Maven. Therefore, you first have to install the right dependencies:
    
    mvn clean install

After installation is succesful, the code starts at the main, located in [src/main/java/Main.java](src/main/java/Main.java).
