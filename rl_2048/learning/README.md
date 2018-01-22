# Learning

This directory contains all code that has to do with the Deep Q-Learning algorithm itself.
Here's a comprehensive list of the modules:

- `replay_memory` implements the Replay Memory. Main methods are `add()` to add an experience and `sample()` to sample a number of experiences.
- `experience_collector` implements a `collect(strategy, num_games)` function that plays a number of games, deduplicates & undersamples the experiences, and returns them.
- `target_batch_computer` is responsible for computing the target batch that is passed to the network.
- `experience_batcher` uses the `ReplayMemory`, `ExperienceCollector` and `TargetBatchComputer` to generate training batches for the neural network.
- `model` defines the Neural Network architecture and its training parameters (e.g. Learning Rate).
- `learning` glues everything together to implement the Deep Q-Learning algorithm.

## Run Training

There is some work to do in setting the right parameters:
- The `GAMMA` value in `target_value_computer.py`
- The `INIT_LEARNING_RATE` or `HIDDEN_SIZES` in `model.py`
- The `MIN_EPSILON` in `experience_batcher.py`
- ...
