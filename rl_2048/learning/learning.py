"""Learning algorithms."""

from __future__ import absolute_import
from __future__ import division
from __future__ import print_function

import sys
import os
import tensorflow as tf
import numpy as np

from rl_2048.experience.ExperienceReplay import ExperienceReplay
from rl_2048.learning.experience_batcher import ExperienceBatcher
from rl_2048.learning.model import FeedModel
from rl_2048.learning.Strategies import Strategies
from rl_2048.game.play import Play

STATE_NORMALIZE_FACTOR = 1.0 / 15.0
EXPERIENCE_FILE_NAME = 'EXPERIENCE_FILE_NAME.binary'
REFERENCE_FILE_NAME = 'REFERENCE_FILE_NAME.binary'


def make_run_inference(session, model):
    """Make run_inference() function for given session and model."""

    def run_inference(state_batch):
        """Run inference on a given state_batch. Returns a q value batch."""
        return session.run(model.q_values, feed_dict={model.state_batch_placeholder: state_batch})

    return run_inference


def make_get_q_values(session, model):
    """Make get_q_values() function for given session and model."""

    run_inference = make_run_inference(session, model)

    def get_q_values(state):
        """Run inference on a single (4, 4) state matrix."""
        state_vector = state.flatten() * STATE_NORMALIZE_FACTOR
        state_batch = np.array([state_vector])
        q_values_batch = run_inference(state_batch)
        return q_values_batch[0]

    return get_q_values


def run_training(train_dir):
    """Run training"""

    resume = os.path.exists(train_dir)

    with tf.Graph().as_default():
        model = FeedModel()
        saver = tf.train.Saver(save_relative_paths=True)
        session = tf.Session()
        summary_writer = tf.summary.FileWriter(train_dir, graph_def=session.graph_def, flush_secs=10)

        if resume:
            print("Resuming: ", train_dir)
            saver.restore(session, tf.train.latest_checkpoint(train_dir))
            experience_replay = ExperienceReplay().load(os.path.join(train_dir, EXPERIENCE_FILE_NAME))
            experience_reference = ExperienceReplay().load(os.path.join(train_dir, REFERENCE_FILE_NAME))
        else:
            print("Starting new training: ", train_dir)
            session.run(model.init)

            experience_replay = ExperienceReplay()
            experience_replay.generate(100000, Strategies.random_strategy, True)
            experience_replay.save(os.path.join(train_dir, EXPERIENCE_FILE_NAME))

            experience_reference = ExperienceReplay()
            experience_reference.generate(100000, Strategies.random_strategy, True)
            experience_reference.save(os.path.join(train_dir, REFERENCE_FILE_NAME))

        run_inference = make_run_inference(session, model)
        get_q_values = make_get_q_values(session, model)

        batcher = ExperienceBatcher(experience_replay, run_inference, get_q_values, STATE_NORMALIZE_FACTOR)

        for state_batch, targets, actions in batcher.get_batches_stepwise():
            global_step, _ = session.run([model.global_step, model.train_op],
                                         feed_dict={
                                           model.state_batch_placeholder: state_batch,
                                           model.targets_placeholder: targets,
                                           model.actions_placeholder: actions,
                                           model.score_placeholder: np.asarray([]),
                                           model.max_value_placeholder: np.asarray([]), })

            if global_step % 10000 == 0 and global_step != 0:
                saver.save(session, train_dir + "/checkpoint", global_step=global_step)
                loss = write_summaries(session, batcher, model, experience_reference.get_experience(), get_q_values, summary_writer)
                experience_replay.save(os.path.join(train_dir, EXPERIENCE_FILE_NAME))
                experience_reference.save(os.path.join(train_dir, REFERENCE_FILE_NAME))
                print("Step:", global_step, "Loss:", loss)
            if global_step % 1000000 == 0 and global_step != 0:
                break


def test(q_values, games):
    strategy = Strategies.make_greedy_strategy(q_values)
    scores = []
    tiles = []
    for _ in range(games):
        score, max_tile, _ = Play.play_game(strategy)
        scores.append(score)
        tiles.append(max_tile)
    return scores, tiles


def write_summaries(session, batcher, model, test_experiences, get_q_values, summary_writer):
    """Writes summaries by running the model on test_experiences. Returns loss."""

    score, tiles = test(get_q_values, 25)

    state_batch, targets, actions = batcher.experiences_to_batches(test_experiences)
    state_batch_p, targets_p, actions_p, score_p, max_p = model.placeholders
    summary_str, global_step, loss = session.run(
      [model.summary_op, model.global_step, model.loss],
      feed_dict={
        state_batch_p: state_batch,
        targets_p: targets,
        actions_p: actions,
        score_p: np.asarray(score),
        max_p: np.asarray(tiles), })
    summary_writer.add_summary(summary_str, global_step)
    tf.summary.merge_all()
    return loss


def main(args):
    """Main function."""

    if len(args) != 2:
        print("Usage: %s train_dir" % args[0])
        sys.exit(1)

    run_training(args[1])


if __name__ == '__main__':
    tf.app.run()
