import numpy as np


class Controller(object):

  def __init__(self):
    self._model = None

  def set_model(self, model):
    self._model = model

  def get_model(self):
    return self._model

  def add_random_tile(self):
    x_pos, y_pos = np.where(self._model.get_state() == 0)
    assert len(x_pos) != 0
    empty_index = np.random.choice(len(x_pos))
    value = np.random.choice([1, 2], p=[0.9, 0.1])

    self._model.set_state_value(x_pos[empty_index], y_pos[empty_index], value)

  def do_action(self, action):
    self._model.rotate(action)
    score = self.__do_action_left()
    self._model.rotate(-action)
    self.add_random_tile()
    return score

  def __do_action_left(self):
    score = 0
    for i in range(self._model.get_size()):
      row = self._model.get_state_row(i)
      row_out, score_out = self.__merge_line(row)

      self._model.set_state_row(i, row_out)
      score += score_out
    return score

  def __merge_line(self, line):
    output = np.zeros(line.size, np.int)
    trim_line = self.__trim_line(line)

    move = 0
    score = 0
    i = 0
    while i < line.size:
      if trim_line[i] == 0:
        break

      current = trim_line[i]
      if i < (line.size - 1) and current == trim_line[i + 1]:
        current += 1
        score += current
        i += 1
      output[move] = current
      move += 1
      i += 1
    return output, score

  def __trim_line(self, line):
    counter = 0
    output = np.zeros(line.size)
    for i in range(line.size):
      val = line[i]
      if val != 0:
        output[counter] = val
        counter += 1
    return output
