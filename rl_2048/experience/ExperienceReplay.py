from rl_2048.game.play import Play
import numpy as np
import pickle


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

    def generate(self, count, strategy, verbose=False):
        def local_print(index, addition):
            if verbose:
                print('{0}/{1} += {2}'.format(count, index, addition))

        self.size = count
        self.keys = np.zeros((count, 16 + 1 + 16), np.int8)
        self.values = np.empty(count, object)

        if verbose:
            print('--Start generation--')

        x = 0
        while True:
            _, experience_list = Play.play_game(strategy)
            experience_list = Play.under_sample_game(experience_list)
            local_print(x, len(experience_list));

            for experience in experience_list:
                self.keys[x, :] = experience.get_id()
                self.values[x] = experience
                x += 1

                if x >= count:
                    local_print(x, x)
                    return

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

    def get_experience(self):
        return self.values

    def add(self, experience, n_deletes=1):
        self.keys = np.delete(self.keys, np.s_[0:n_deletes], 0)
        self.values = np.delete(self.values, np.s_[0:n_deletes], 0)

        self.keys = np.append(self.keys, [experience.get_id()], axis=0)
        self.values = np.append(self.values, experience)

    def print_stats(self): pass

    def get_size(self):
        return self.size