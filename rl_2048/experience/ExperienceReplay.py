from rl_2048.game.game import Game
from rl_2048.experience.Experience import Experience
import numpy as np
import pickle
import random
import math

# Parameters for under sampling
DO_UNDER_SAMPLING = True
AVG_KEEP_PROB = 0.04
MIN_KEEP_PROB = 0.01


class ExperienceReplay(object):

    def __init__(self):
        self.size = 0
        self.keys = None
        self.values = None

    def save(self, file):
        with open(file, 'wb') as f:
            f.write(pickle.dumps(self, pickle.HIGHEST_PROTOCOL))

    @classmethod
    def load(cls, file):
        with open(file, 'rb') as f:
            return pickle.load(f)

    def generate(self, count, strategy):
        self.size = count
        self.keys = np.zeros((count, 16 + 1 + 16), np.int8)
        self.values = np.empty(count, object)

        x = 0
        while True:
            experience_list = ExperienceReplay.generate_game(strategy)
            for experience in experience_list:
                self.keys[x, :] = experience.get_id()
                self.values[x] = experience
                x += 1

                if x >= count:
                    return

    @staticmethod
    def generate_game(strategy):
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

        experience_list = [e for index, e in enumerate(experience_list)
                           if (np.random.rand() < ExperienceReplay.__get_keep_probability(index, len(experience_list)))]

        return experience_list

    def merge(self, experience_replay):
        self.size += experience_replay.size
        self.keys = np.append(self.keys, experience_replay.keys, axis=0)
        self.values = np.append(self.values, experience_replay.values, axis=0)

    def distinct(self):
        _, indices = np.unique(self.keys, axis=0, return_index=True)
        self.keys = self.keys[indices, :]
        self.values = self.keys[indices]

    def sample(self, n_samples):
        idx = np.random.randint(self.size, size=n_samples)
        return self.values[idx]

    def add(self, experience, n_deletes=1):
        self.keys = np.delete(self.keys, np.s_[0:n_deletes], 0)
        self.values = np.delete(self.values, np.s_[0:n_deletes], 0)

        self.keys = np.append(self.keys, [experience.get_id()], axis=0)
        self.values = np.append(self.values, experience)

    def print_stats(self): pass

    def get_size(self):
        return self.size

    @staticmethod
    def __get_keep_probability(index, length):
        """Computes the keep probability for the experience with a given index.

        First, the index is mapped to a value x between 0 and 1 (last index mapped
        to 0, index 0 mapped to 1). Then, the keep probability is computed by a
        function keep_prob = e^(ax) + MIN_KEEP_PROB, such that the average
        probability is AVG_KEEP_PROB.

        For small AVG_KEEP_PROB, a can be approximated by
        a = - 1 / (AVG_KEEP_PROB - MIN_KEEP_PROB).

        Args:
          index: zero-based index of the experience.
          length: total number of experiences.
        """
        if not DO_UNDER_SAMPLING:
            return 1.0

        value = 1 - index / (length - 1)
        return math.e ** (- 1 / (AVG_KEEP_PROB - MIN_KEEP_PROB) * value) + MIN_KEEP_PROB
