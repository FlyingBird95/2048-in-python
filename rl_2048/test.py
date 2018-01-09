from rl_2048.game.model import Model
from rl_2048.game.controller import Controller
from rl_2048.experience.ExperienceFactory import ExperienceFactory

m = Model(None, 1)
m.set_state_value(0, 0, 1)

c = Controller()
c.set_model(m)


ef = ExperienceFactory('T')

location = 'C:/Users/T/Documents/GitHub/2048-in-python/docs'
test = ef.generate(location, 10000, False)


# files = ['T_experience_1000_1.binary',
#          'T_experience_1000_2.binary',
#          'T_experience_1000_3.binary']
#
# zzz = ef.merge(location, files, True)

ttt = 5

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

yyy = 16
