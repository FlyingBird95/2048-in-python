from rl_2048.game.game import Game
from rl_2048.experience.Experience import Experience
import numpy as np
import pickle
import random


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
        self.keys = np.zeros((count, 16 + 1 + 16), np.int8)
        self.values = np.empty(count, object)

        for x in range(count):
            game = Game()
            state = game.state().copy()
            game_over = game.game_over()

            while not game_over:
                old_state = state
                next_action = strategy(old_state, game.available_actions())

                reward = game.do_action(next_action)
                state = game.state().copy()
                game_over = game.game_over()

                experience = Experience(old_state, next_action, reward, state, game_over, game.available_actions())

                self.keys[x, :] = experience.get_id()
                self.values[x] = experience

    def merge(self, experience_replay):
        self.size += experience_replay.size
        self.keys = np.append(self.keys, experience_replay.keys, axis=0)
        self.values = np.append(self.values, experience_replay.values, axis=0)

    def distinct(self):
        _, indices = np.unique(self.keys, axis=0, return_index=True)
        self.keys = self.keys[indices, :]
        self.values = self.keys[indices]

    def sample(self, n_samples):
        return random.sample(self.values, n_samples)

    def add(self, experience, n_deletes):
        if n_deletes > 0:
            n_deletes = range(n_deletes - 1)
            np.delete(self.keys, n_deletes, 0)
            np.delete(self.values, n_deletes, 0)

        self.keys = np.append(self.keys, experience.get_id(), axis=0)
        self.values = np.append(self.values, experience)

    def print_stats(self): pass

    def get_size(self):
        return self.size
