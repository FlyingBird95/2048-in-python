from rl_2048.ExperienceBinary import ExperienceBinary
import numpy as np
import pickle
from os import walk

class ExperienceFactory(object):

    def __init__(self): pass

    def generate(self, path, key, count, verbose=False):
        eb = ExperienceBinary(count)
        file_name = ExperienceFactory.__get_name(path, key, count)

        if verbose:
            print("New file generated:", file_name)
            print("<--Starting simulation-->")

        # for x in range(count): pass
        #     # play games and fill binary

        if verbose:
            print("<--Ending simulation-->")
            print("Storing data")

        xxx = path + '/' + file_name
        ExperienceFactory.save(path + '/' + file_name, eb)

        if verbose:
            print("Stored data")

    def merge(self, file_list, name=None): pass

    def distinct(self, name): pass


    @staticmethod
    def save(file, data):
        with open(file, "wb") as f:
            pickle.dump(data, f, pickle.HIGHEST_PROTOCOL)

    @staticmethod
    def load(file): pass

    @staticmethod
    def __get_name(path, key, count):
        extension = '.binary'
        existing_files = ExperienceFactory.__get_existing_files(path)

        max_array = np.zeros(existing_files.__len__() + 1, np.int8)
        for x in range(existing_files.__len__()):
            current_name = '{0}_experience_{1}_'.format(key, count)
            file_name = existing_files[x]

            if file_name.__contains__(current_name):
                file_name = file_name[len(current_name):]
                file_name = file_name[:-len(extension)]
                max_array[x] = int(file_name)

        minIndex = np.amin(max_array) + 1
        return '{0}_experience_{1}_{2}{3}'.format(key, count, minIndex, extension)

    @staticmethod
    def __get_existing_files(path):
        for (dirpath, dirnames, filenames) in walk(path):
            return filenames