import matplotlib.pyplot as plt


def show_plot(scores):
    plt.plot(scores)
    plt.ylabel('Simple plot')
    plt.show()  # Doesn't work, but figure is correctly saved
    plt.savefig('figure.png')
    print('Showing plot')

