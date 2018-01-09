# 2048 in Python
Deep Q-Learning Project to play 2048.


# How does the game 2048 work?
2048 is played on a gray 4×4 grid, with numbered tiles that slide smoothly when a player moves them using the four arrow keys. Every turn, a new tile will randomly appear in an empty spot on the board with a value of either 2 or 4. Tiles slide as far as possible in the chosen direction until they are stopped by either another tile or the edge of the grid. If two tiles of the same number collide while moving, they will merge into a tile with the total value of the two tiles that collided. The resulting tile cannot merge with another tile again in the same move.

A scoreboard on the upper-right keeps track of the user's score. The user's score starts at zero, and is incremented whenever two tiles combine, by the value of the new tile. As with many arcade games, the user's best score is shown alongside the current score.

The game is won when a tile with a value of 2048 appears on the board, hence the name of the game. After reaching the 2048 tile, players can continue to play (beyond the 2048 tile) to reach higher scores. When the player has no legal moves (there are no empty spaces and no adjacent tiles with the same value), the game ends.

# How to run the code?
Install [TensorFlow](https://www.tensorflow.org/versions/r0.8/get_started/index.html), python & pip.
Then, run:

```bash
pip install -r requirements.txt
```

To run the code, you'll need to update your `PYTHONPATH`:

```bash
source set_pythonpath.sh
```

Now, you should be able to run the tests:

```bash
py.test
```

## Source Code Structure

All python source code lives in `py_2048_rl`.