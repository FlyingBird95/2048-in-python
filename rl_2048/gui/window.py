from tkinter import *
from tkinter.scrolledtext import ScrolledText
from rl_2048.gui.root import WIDTH
from rl_2048.gui.config import Config
from tkinter.constants import END
import threading

TITLE_FONT = ("Helvetica", 32)
DEFAULT_FONT = ("Helvetica", 14)

TRAIN_DIR_POS = (WIDTH / 2, 60)
DELAY_MS_POS = (WIDTH / 2, 100)
NUM_GAMES_POS = (WIDTH / 2, 140)

TRAIN_BUTTON_POS = (50, 300)
PLAY_BUTTON_POS = (300, 300)
TENSOR_BUTTON_POS = (580, 300)

SHOW_GAMES_CHECKBOX_POS = (WIDTH / 2, 180)


class Window(Frame):
    """
        Main window for the GUI.
        Creates all components in the __init__-function.
    """

    def __init__(self, master):
        Frame.__init__(self, master=master)

        # attributes
        self.master = master
        self.config = Config(master)

        # create the view
        title = Label(self.master, text="2048 GUI", font=TITLE_FONT)
        title.place(x=WIDTH / 2, y=30, anchor="center")

        self.add_input_box("Trainings directory", TRAIN_DIR_POS, 30, self.config.train_dir_obj)
        self.add_input_box("Delay per move (in ms)", DELAY_MS_POS, 5, self.config.delay_in_ms_obj)
        self.add_input_box("Number of games", NUM_GAMES_POS, 5, self.config.num_games_obj)

        self.add_button("Start training", TRAIN_BUTTON_POS, self.start_training)
        self.add_button("Start playing", PLAY_BUTTON_POS, self.start_playing)
        self.add_button("Start tensorboard", TENSOR_BUTTON_POS, self.start_tensorboard)

        self.add_checkbox("Show games", SHOW_GAMES_CHECKBOX_POS, self.config.show_games_obj)

        # Add the log
        Label(self.master, text="Log:", font=DEFAULT_FONT).place(x=10, y=370)

        self.log_obj = ScrolledText(self.master, width=111, height=12)
        self.log_obj.configure(state="disabled")
        self.log_obj.place(x=0, y=400)

        self.log('Program initialised')

    def add_input_box(self, label, pos, input_width, value):
        """
            Place an input box, together with a label.
            :param label: a string with the text of the label
            :param pos: the position (consists of a tuple) with the x and y location
            :param input_width: how many characters the input box is width
            :param value: reference to attribute in the config-obj
        """
        x, y = pos
        label_widget = Label(self.master, text=label + ':', font=DEFAULT_FONT)
        label_widget.place(x=x - 5, y=y, anchor="ne")

        input_box = Entry(self.master, width=input_width, font=DEFAULT_FONT, textvariable=value)
        input_box.place(x=x + 5, y=y, anchor="nw")

    def add_button(self, text, position, command):
        """
            :param text: Label for the button
            :param position: the position (consists of a tuple) with the x and y location
            :param command: function reference to whatever is being executed when the button is clicked
        """

        button = Button(self.master, text=text, command=command, font=DEFAULT_FONT)
        x, y = position
        button.place(x=x, y=y, anchor="nw")

    def add_checkbox(self, label, position, value):
        """
            :param label: Name of the checkbox
            :param position: the position (consists of a tuple) with the x and y location
            :param value: reference to attribute in the config-obj
            """
        checkbox = Checkbutton(self.master, text=label, variable=value, font=DEFAULT_FONT)
        x, y = position
        checkbox.place(x=x, y=y, anchor="nw")

    def log(self, text):
        """
            Add a line of text to the log-obj
            :param text: the text (without the newline) to be added
        """
        self.log_obj.configure(state="normal")
        self.log_obj.insert(END, text + '\n')
        self.log_obj.see('end')
        self.log_obj.configure(state="disabled")

    # Methods below are button actions
    def start_training(self):
        """
            Start with the training (gathering experiences)
        """

        def train():
            self.log('Starting with the training')
            from rl_2048.learning.learning import run_training
            run_training(self.config.get_train_dir())

        threading.Thread(target=train).start()

    def start_playing(self):
        """
            Start playing a number of games
        """

        def play():
            self.log('Playing ' + str(self.config.get_num_games()) + ' game(s).')

            from rl_2048.play_game import average_score, make_greedy_strategy
            score = average_score(make_greedy_strategy(self.config.get_train_dir()), window=self)
            self.log('Average score: ' + str(score))

        threading.Thread(target=play).start()

    def start_tensorboard(self):
        """
            Start the tensorboard server (using the command line)
        """

        def launch_tensor_board():
            self.log('Starting tensorboard')
            import os
            os.system('tensorboard --logdir=' + self.config.get_train_dir())
            return

        threading.Thread(target=launch_tensor_board).start()
