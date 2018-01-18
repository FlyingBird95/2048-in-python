import matplotlib.pyplot as plt
import numpy as np


def show_plot(scores, iteration=None):

    y_mean = [np.mean(scores)] * len(scores)

    fig, ax = plt.subplots()

    # Plot the data
    data_line = ax.plot(range(len(scores)), scores, label='Scores')

    # Plot the average line
    mean_line = ax.plot(range(len(scores)), y_mean, label='Mean', linestyle='--')

    # Make a legend
    legend = ax.legend(loc='upper right')

    plt.show()
    filename = 'figure'
    if iteration:
        filename += str(iteration)

    plt.savefig(filename + '.png')
    print('Showing plot')

