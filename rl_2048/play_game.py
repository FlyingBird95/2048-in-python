"""Script to play a single game from a checkpoint."""

from __future__ import absolute_import
from __future__ import division
from __future__ import print_function

from rl_2048.game import play
from rl_2048.learning import learning
from rl_2048.learning.model import FeedModel

import sys

import tensorflow as tf
import numpy as np


def average_score(strategy, num_games=100):
    """Plays 100 games, returns average score."""

    scores = []
    for _ in range(num_games):
        score, _ = play.play(strategy, allow_unavailable_action=False)
        scores.append(score)
    return np.mean(scores)


def make_greedy_strategy(train_dir, verbose=False):
    """Load the latest checkpoint from train_dir, make a greedy strategy."""

    session = tf.Session()
    model = FeedModel()
    saver = tf.train.Saver()
    print(train_dir)
    saver.restore(session, tf.train.latest_checkpoint(train_dir))

    get_q_values = learning.make_get_q_values(session, model)
    greedy_strategy = play.make_greedy_strategy(get_q_values, verbose)

    return greedy_strategy


def play_single_game(train_dir):
    """Play a single game using the latest model snapshot in train_dir."""

    s, _ = play.play(make_greedy_strategy(train_dir, True),
                     allow_unavailable_action=False)
    print(s)


def print_average_score(train_dir):
    """Prints the average score of 100 games."""

    print("Average Score: ", average_score(make_greedy_strategy(train_dir)))


def main(args):
    """Main function."""

    print(args)

    if len(args) != 3:
        print("Usage: %s (single|avg) train_dir" % args[0])
        sys.exit(1)

    _, mode, train_dir = args

    if mode == "single":
        play_single_game(train_dir)
    elif mode == "avg":
        print_average_score(train_dir)
    else:
        print("Unknown mode:", mode)


if __name__ == '__main__':
    tf.app.run()
