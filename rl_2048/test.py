from rl_2048.game.model import Model
from rl_2048.game.controller import Controller
from rl_2048.ExperienceFactory import ExperienceFactory
import numpy as np
import random
import pickle

m = Model(None, 1)
m.set_state_value(0, 0, 1)

c = Controller()
c.set_model(m)


ef = ExperienceFactory()

xxx = 'C:/Users/T/Documents/GitHub/2048-in-python/docs'
test = ef.generate(xxx, 'T', 1000, True)


# class Dump(object):
#   def __init__(self):
#     self.experience_count = 10
#     self.s_key = np.zeros((self.experience_count,16), np.int8)
#     self.m_key = np.zeros((self.experience_count,1), np.int8)
#     self.ns_key = np.zeros((self.experience_count,16), np.int8)
#     self.value = np.zeros((self.experience_count,1), np.int8)
#
#   def fill(self):
#     for x in range(self.experience_count):
#       self.m_key[x] = random.randint(0, 3)
#       for y in range(16):
#         self.s_key[x, y] = random.randint(0, 10)
#         self.ns_key[x, y] = random.randint(0, 10)
#
#
# myDump = Dump()
# myDump.fill()
#
# with open("super.file", "wb") as f:
#     pickle.dump(myDump, f, pickle.HIGHEST_PROTOCOL)
#
# myDump = None
#
# with open("super.file", "rb") as f:
#   myDump = pickle.load(f)
#
# yyy = 16
