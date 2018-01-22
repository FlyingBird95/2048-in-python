from tkinter import Tk

WIDTH = 800
HEIGHT = 600


def new_root():
    """ Returns a new Tk instance """
    root = Tk()
    root.title('2048 GUI')
    root.geometry(str(WIDTH) + "x" + str(HEIGHT))
    root.resizable(False, False)
    return root
