""" For creating a new window, see root.py
    Construct a Main View (see window.py)
"""
from rl_2048.gui.root import new_root
from rl_2048.gui.window import Window


def start():
    root = new_root()
    Window(root)  # create a main window

    # start the main window
    root.mainloop()  # blocking function (i.e. function never returns)
