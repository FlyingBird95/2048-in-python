"""Script to play a single game from a checkpoint."""

from __future__ import absolute_import
from __future__ import division
from __future__ import print_function

from rl_2048.game import play
from rl_2048.learning import learning
from rl_2048.learning.model import FeedModel

import tensorflow as tf
import numpy as np


def average_score(strategy, window=None):
    """Plays a number of games, returns average score."""

    scores = []
    for _ in range(window.config.get_num_games()):
        score, _ = play.play(strategy, allow_unavailable_action=False, window=window)
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