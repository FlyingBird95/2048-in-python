import numpy as np


class ExperienceBinary(object):

    def __init__(self, size):
        self.counter = 0
        self.size = size
        self.s_key = np.zeros((self.size, 16), np.int8)
        self.m_key = np.zeros((self.size, 1), np.int8)
        self.ns_key = np.zeros((self.size, 16), np.int8)
        self.value = np.zeros((self.size, 1), np.int8)

    def add(self, experience):
        if self.is_full():
            return False
        return True

    def is_full(self):
        return self.counter == self.size - 1
