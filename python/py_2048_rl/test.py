from py_2048_rl.game.model import Model
from py_2048_rl.game.controller import Controller
from py_2048_rl.game.modelInfo import ModelInfo
import numpy as np

m = Model(None, 1)
m.set_state_value(0, 0, 1)

c = Controller()
c.set_model(m)

m.set_state_value(0, 0, 0)
m.set_state_value(0, 1, 0)
m.set_state_value(0, 2, 0)
m.set_state_value(0, 3, 0)

m.set_state_value(1, 0, 1)
m.set_state_value(1, 1, 1)
m.set_state_value(1, 2, 1)
m.set_state_value(1, 3, 1)

m.set_state_value(2, 0, 1)
m.set_state_value(2, 1, 1)
m.set_state_value(2, 2, 1)
m.set_state_value(2, 3, 1)

m.set_state_value(3, 0, 1)
m.set_state_value(3, 1, 2)
m.set_state_value(3, 2, 3)
m.set_state_value(3, 3, 4)

m.print_state()

# w = m.get_state_row(0)
# x = m.get_state_row(1)
# y = m.get_state_row(2)
# z = m.get_state_row(3)
#
# wm, ws = c.merge_line(w)
# xm, xs = c.merge_line(x)
# ym, ys = c.merge_line(y)
# zm, zs = c.merge_line(z)
#
# m.set_state_row(0, wm)
# m.set_state_row(1, xm)
# m.set_state_row(2, ym)
# m.set_state_row(3, zm)
#
# m.print_state()

# c.do_action()
c.do_action(2)

c.get_model().print_state()




xxx = ModelInfo.win(m)
zzz = ModelInfo.lose(m)
yyy = 16
