from rl_2048.experience.ExperienceBinary import ExperienceBinary
from rl_2048.experience.ExperienceGenerator import ExperienceGenerator
import numpy as np
import pickle
from os import walk
from os import path


class ExperienceFactory(object):

    def __init__(self, key):
        self.key = key

    def generate(self, location, count, verbose=False):
        experience_binary = ExperienceBinary(count)
        file_name = ExperienceFactory.__get_name(location, 'experience', self.key, count)

        if verbose:
            print('New file generated:', file_name)
            print('<--Starting simulation-->')

        experience_generator = ExperienceGenerator(experience_binary)
        experience_generator.play(0, verbose)  # for now only random is possible

        if verbose:
            print('<--Ending simulation-->')
            print('Storing data')

        ExperienceFactory.__write(path.join(location, file_name), experience_binary)

        if verbose:
            print('Stored data')

    def merge(self, location, file_list, verbose=False):
        if len(file_list) < 2:
            return
        if verbose:
            print('Attempting to combine:', len(file_list), 'files')
            print('Adding: 0 ->', file_list[0])

        data = ExperienceFactory.__read(path.join(location, file_list[0]))

        for x in range(1, len(file_list)):
            if verbose:
                print('Adding:', x, '->', file_list[x])
            data.append(ExperienceFactory.__read(path.join(location, file_list[x])))

        file_name = ExperienceFactory.__get_name(location, 'merge', self.key, data.get_size())
        if verbose:
            print('New file generated:', file_name)
            print('Storing data')
        
        ExperienceFactory.__write(path.join(location, file_name), data)
        
        if verbose:
            print('Stored data')

    def distinct(self, location, file, verbose=False):
        if verbose:
            print('Loading:', file)

        data = ExperienceFactory.__read(path.join(location, file))

        if verbose:
            print('Loaded', data.get_size(), 'records')

        _, indices = np.unique(data.get_key(), axis=0, return_index=True)

        if verbose:
            print('Found', data.get_size() - len(indices), 'duplicates')

        data.remove_index(indices)

        file_name = ExperienceFactory.__get_name(location, 'distinct', self.key, data.get_size())
        if verbose:
            print('Removed duplicates,', data.get_size(), 'remaining data points')
            print('Writing data to:', file_name)

        ExperienceFactory.__write(file_name, data)

        if verbose:
            print('All data writen')

    @staticmethod
    def __write(file, data):
        with open(file, 'wb') as f:
            f.write(pickle.dumps(data, pickle.HIGHEST_PROTOCOL))

    @staticmethod
    def __read(file):
        with open(file, 'rb') as f:
            return pickle.load(f)

    @staticmethod
    def __get_name(location, name, key, count):
        extension = '.binary'
        existing_files = ExperienceFactory.__get_existing_files(location)

        max_array = np.zeros(len(existing_files) + 1, np.int8)
        for x in range(len(existing_files)):
            current_name = '{0}_{1}_{2}_'.format(key, name, count)
            file_name = existing_files[x]

            if file_name.__contains__(current_name):
                file_name = file_name[len(current_name):]
                file_name = file_name[:-len(extension)]
                max_array[x] = int(file_name)

        min_index = np.amax(max_array) + 1
        return '{0}_{1}_{2}_{3}{4}'.format(key, name, count, min_index, extension)

    @staticmethod
    def __get_existing_files(path):
        for (dirpath, dirnames, filenames) in walk(path):
            return filenames
