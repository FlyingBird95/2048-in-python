from rl_2048.experience.ExperienceFactory import ExperienceFactory

ef = ExperienceFactory('T') # change to your own letter, just so we can see who generated what

for x in range(10): # generate 10 files, with 100000 entries each, just incase something goes wrong in the mean time