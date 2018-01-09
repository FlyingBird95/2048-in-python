import numpy as np


class Model(object):

    def __init__(self, state=None, initial_score=0, size=4):
        self._size = size
        self._score = initial_score
        if state is None:
            self._state = np.zeros((self._size, self._size), dtype=np.int)
        else:
            self._state = state

    def __contains__(self, value):
        return self._state.__contains__(value)

    def copy(self):
        """Return copy of model"""
        return Model(np.copy(self._state), self._score, self._size)

    def print_state(self):
        """Print model in console"""
        def tile_string(value):
            if value > 0:
                return '% 5d' % (2 ** value,)
            return "     "

        print("-" * 25)
        for row in range(self._size):
            print("|" + "|".join([tile_string(v) for v in self._state[row, :]]) + "|")
            print("-" * 25)

    def rotate(self, action):
        """Rotate model based on action - range(0, 3)"""
        self._state = np.rot90(self._state, action)

    def set_state(self, state):
        """Set the state"""
        self._state = state

    def set_state_value(self, x, y, value):
        """Set a state value"""
        self._state[y, x] = value

    def set_state_row(self, index, values):
        """Set a state row"""
        self._state[index, :] = values

    def get_state(self):
        """Return state"""
        return self._state

    def get_state_row(self, index):
        """Get a state row"""
        return self._state[index, :]

    def set_score(self, score):
        """Set the score"""
        self._score = score

    def get_score(self):
        """Return score"""
        return self._score

    def get_size(self):
        """Return size"""
        return self._size
