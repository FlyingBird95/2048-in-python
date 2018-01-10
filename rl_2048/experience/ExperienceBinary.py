import numpy as np


class ExperienceBinary(object):

    def __init__(self, size):
        self.counter = 0
        self.size = size
        self.s_key = np.zeros((self.size, 16), np.int8)
        self.m_key = np.zeros((self.size, 1), np.int8)
        self.ns_key = np.zeros((self.size, 16), np.int8)
        self.experience = np.empty(self.size, object)

    def add(self, experience):
        if self.is_full():
            return

        self.s_key[self.counter] = experience.get_id_state()
        self.m_key[self.counter] = experience.get_id_action()
        self.ns_key[self.counter] = experience.get_id_next_state()
        self.experience[self.counter] = experience
        self.counter += 1

    def append(self, data):
        self.counter += data.counter
        self.size += data.size
        self.s_key = np.append(self.s_key, data.s_key, axis=0)
        self.m_key = np.append(self.m_key, data.m_key, axis=0)
        self.ns_key = np.append(self.ns_key, data.ns_key, axis=0)
        self.experience = np.append(self.experience, data.experience, axis=0)

    def is_full(self):
        return self.counter == self.size

    def get_size(self):
        return self.size

    def get_key(self):
        return np.append(np.append(self.s_key, self.m_key, axis=1), self.ns_key, axis=1)

    def remove_index(self, indexes):
        self.counter = len(indexes)
        self.size = len(indexes)

        self.s_key = self.s_key[indexes, :]
        self.m_key = self.m_key[indexes, :]
        self.ns_key = self.ns_key[indexes, :]
        self.experience = self.experience[indexes]

