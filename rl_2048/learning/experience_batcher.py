"""Class that builds experience batches."""

from __future__ import absolute_import
from __future__ import division
from __future__ import print_function

import numpy as np
import itertools

from rl_2048.game.play import Play
from rl_2048.learning.Strategies import Strategies
from rl_2048.learning.target_batch_computer import TargetBatchComputer


BATCH_SIZE = 32

# Number of batches for which the model is kept constant
BATCHES_KEEP_CONSTANT = 1e3

# Parameters for epsilon (for epsilon-greedy play)
START_DECREASE_EPSILON_GAMES = 200000
DECREASE_EPSILON_GAMES = 100000.0
MIN_EPSILON = 0.1


class ExperienceBatcher(object):

    def __init__(self, experience_collector, run_inference, get_q_values, state_normalize_factor):
        self.experience_collector = experience_collector
        self.run_inference = run_inference
        self.get_q_values = get_q_values
        self.state_normalize_factor = state_normalize_factor

    def get_batches_stepwise(self):
        """Wraps get_batches(), keeping the current predictions constant for BATCHES_KEEP_CONSTANT steps."""

        cache = []

        for batches in self.get_batches():
            cache.append(batches)

            if len(cache) >= BATCHES_KEEP_CONSTANT:
                for cached_batches in cache:
                    yield cached_batches
                cache = []

    def get_batches(self):
        """
            Yields randomized batches epsilon-greedy games.
            Maintains a replay memory at full capacity.
        """

        for i in itertools.count():
            if i < START_DECREASE_EPSILON_GAMES:
                epsilon = 1.0
            else:
                epsilon = max(MIN_EPSILON, 1.0 - (i - START_DECREASE_EPSILON_GAMES) / DECREASE_EPSILON_GAMES)

            strategy = Strategies.make_epsilon_greedy_strategy(self.get_q_values, epsilon)
            _, _, experiences = Play.play_game(strategy)

            for experience in Play.under_sample_game(experiences):
                self.experience_collector.add(experience)
                batch_experiences = self.experience_collector.sample(BATCH_SIZE)
                yield self.experiences_to_batches(batch_experiences)

    def experiences_to_batches(self, experiences):
        """Computes state_batch, targets, actions."""

        batch_size = len(experiences)
        state_batch = np.zeros((batch_size, 16), dtype=np.float)
        next_state_batch = np.zeros((batch_size, 16), dtype=np.float)
        actions = np.zeros((batch_size,), dtype=np.int)
        reward_batch = np.zeros((batch_size,), dtype=np.float)
        bad_action_batch = np.zeros((batch_size,), dtype=np.bool)
        available_actions_batch = np.zeros((batch_size, 4), dtype=np.bool)
        merged = np.zeros((batch_size,), dtype=np.float)

        for i, experience in enumerate(experiences):
            state_batch[i, :] = (experience.state.flatten() * self.state_normalize_factor)
            next_state_batch[i, :] = (experience.next_state.flatten() * self.state_normalize_factor)
            actions[i] = experience.action
            reward_batch[i] = experience.reward
            bad_action_batch[i] = experience.game_over
            available_actions_batch[i, experience.next_state_available_actions] = True
            merged[i] = (np.count_nonzero(experience.state) - np.count_nonzero(experience.next_state) + 1)

        targets = TargetBatchComputer(self.run_inference).compute(reward_batch, bad_action_batch, next_state_batch, available_actions_batch, merged)

        return state_batch, targets, actions
