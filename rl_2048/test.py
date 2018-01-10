from rl_2048.experience.ExperienceFactory import ExperienceFactory

ef = ExperienceFactory('T') # change to your own letter, just so we can see who generated what

# for x in range(1): # generate 10 files, with 100000 entries each, just incase something goes wrong in the mean time
#     ef.generate('../experiments', 1000, False)

location = '../experiments'
file_list = ['T_experience_1000_1.binary', 'T_experience_1000_2.binary']

# ef.merge(location, file_list, True)

ef.distinct(location, 'T_experience_1000_1.binary', True)
