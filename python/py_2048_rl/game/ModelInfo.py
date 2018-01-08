import numpy as np


class ModelInfo:

  @staticmethod
  def win(model):
    return model.__contains__(2024)

  @staticmethod
  def lose(model):
    return ModelInfo.get_possible_moves(model) == 0

  @staticmethod
  def __is_move_possible(model):
    for x in range(model.get_size()):
      row = model.get_state_row(x)
      for y in range(model.get_size() - 1):
        if row[y] == row[y + 1]:
          return True
    return False

  @staticmethod
  def get_possible_moves(model):
    if model.__contains__(0):
      return np.array([0, 1, 2, 3])

    horizontal = ModelInfo.__is_move_possible(model)
    model.rotate(1)
    vertical = ModelInfo.__is_move_possible(model)
    model.rotate(-1)

    if vertical and horizontal:
      return np.array([0, 1, 2, 3])

    if vertical:
      return np.array([0, 2])

    if horizontal:
      return np.array([1, 3])

    return np.array([])
