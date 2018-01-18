from __future__ import absolute_import
from __future__ import division
from __future__ import print_function

from rl_2048.game.game import Game
from rl_2048.experience.Experience import Experience

import numpy as np
import math

# Parameters for under sampling
DO_UNDER_SAMPLING = True
AVG_KEEP_PROB = 0.04
MIN_KEEP_PROB = 0.01


class Play(object):

    @staticmethod
    def play_game(strategy):
        game = Game()
        state = game.state().copy()
        game_over = game.game_over()

        experience_list = []
        while not game_over:
            old_state = state
            next_action = strategy(old_state, game.available_actions())

            reward = game.do_action(next_action)
            state = game.state().copy()
            game_over = game.game_over()

            experience_list.append(Experience(old_state, next_action, reward, state, game_over, game.available_actions()))

        return game.score(), experience_list

    @staticmethod
    def under_sample_game(experience_list):
        def get_keep_probability(index, length):
            if not DO_UNDER_SAMPLING:
                return 1.0

            value = 1 - index / (length - 1)
            return math.e ** (- 1 / (AVG_KEEP_PROB - MIN_KEEP_PROB) * value) + MIN_KEEP_PROB

        return [e for index, e in enumerate(experience_list)
                if (np.random.rand() < get_keep_probability(index, len(experience_list)))]
