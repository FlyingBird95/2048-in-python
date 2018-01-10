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
        state = model.get_state()
        for row in range(4):
            has_empty = False
            for col in range(4):
                has_empty |= state[row, col] == 0
                if state[row, col] != 0 and has_empty:
                    return True
                if state[row, col] != 0 and col > 0 and state[row, col] == state[row, col - 1]:
                    return True
        return False

    @staticmethod
    def get_possible_moves(model):
        possible_moves = []
        local_model = model.copy()

        for x in range(4):
            local_model.rotate(x)
            if ModelInfo.__is_move_possible(local_model):
                possible_moves.append(x)
