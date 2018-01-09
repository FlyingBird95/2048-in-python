import numpy as np
from rl_2048.game.game import Game, ACTION_NAMES
from rl_2048.experience.Experience import Experience


class ExperienceGenerator(object):

    def __init__(self, experience_binary):
        self.experience_binary = experience_binary

    def play(self, strategy, verbose=False):

        while not self.experience_binary.is_full():

            game = Game()

            state = game.state().copy()
            game_over = game.game_over()

            while not game_over:
                if verbose:
                    print("Score:", game.score())
                    game.print_state()

                old_state = state
                next_action = ExperienceGenerator.strategy_random(game.available_actions())

                if game.is_action_available(next_action):

                    reward = game.do_action(next_action)
                    state = game.state().copy()
                    game_over = game.game_over()

                    if verbose:
                        print("Action:", ACTION_NAMES[next_action])
                        print("Reward:", reward)

                    self.experience_binary.add(Experience(old_state, next_action, reward, state, game_over, False,
                                                          game.available_actions()))
                else:
                    self.experience_binary.add(Experience(state, next_action, 0, state, False, True,
                                                          game.available_actions()))

                if self.experience_binary.is_full():
                    break

            if verbose:
                print("Score:", game.score())
                game.print_state()
                print("Game over.")

    @staticmethod
    def strategy_random(actions):
        return np.random.choice(actions)

    @staticmethod
    def strategy_preference(actions):
        return min(actions)

    @staticmethod
    def strategy_greedy(state, actions):
        sorted_actions = np.sort(actions)[::-1]
        rewards = map(lambda action: Game(np.copy(state)).do_action(action), sorted_actions)
        action_index = np.argsort(rewards, kind="mergesort")[-1]
        return sorted_actions[action_index]

    @staticmethod
    def strategy_greedy_epsilon(state, actions): pass
