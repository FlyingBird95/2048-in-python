import numpy as np
from rl_2048.game.game import Game


class Strategies(object):

    @staticmethod
    def random_strategy(_, actions):
        """Strategy that always chooses actions at random."""
        return np.random.choice(actions)

    @staticmethod
    def static_preference_strategy(_, actions):
        """Always prefer left over up over right over top."""
        return min(actions)

    @staticmethod
    def highest_reward_strategy(state, actions):
        """
        Strategy that always chooses the action of highest immediate reward.
        If there are any ties, the strategy prefers left over up over right over down.
        """

        sorted_actions = np.sort(actions)[::-1]
        rewards = map(lambda action: Game(np.copy(state)).do_action(action),
                      sorted_actions)
        action_index = np.argsort(rewards, kind="mergesort")[-1]
        return sorted_actions[action_index]

    @staticmethod
    def make_greedy_strategy(get_q_values):
        """Makes greedy_strategy."""

        def greedy_strategy(state, actions):
            """Strategy that always picks the action of maximum Q(state, action)."""
            q_values = get_q_values(state)
            sorted_actions = np.argsort(q_values)
            action = [a for a in sorted_actions if a in actions][-1]
            return action

        return greedy_strategy

    @staticmethod
    def make_epsilon_greedy_strategy(get_q_values, epsilon):
        """Makes epsilon_greedy_strategy."""

        greedy_strategy = Strategies.make_greedy_strategy(get_q_values)

        def epsilon_greedy_strategy(state, actions):
            """Picks random action with prob. epsilon, otherwise greedy_strategy."""
            do_random_action = np.random.choice([True, False], p=[epsilon, 1 - epsilon])
            if do_random_action:
                return Strategies.random_strategy(state, actions)
            return greedy_strategy(state, actions)

        return epsilon_greedy_strategy
