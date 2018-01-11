"""
	Creates the main view for the GUI
"""
import threading

from tkinter import *
from tkinter.scrolledtext import ScrolledText

WIDTH = 800
HEIGHT = 600

TITLE = "2048 GUI"
TITLE_Y = 30
TITLE_FONT = ("Helvetica", 32)

INPUT_FONT = ("Helvetica", 16)
INPUT_LABEL = "Trainings directory:"
INPUT_WIDTH = 50
DEFAULT_VALUE = "../experiments"
INPUT_Y = 100

BUTTON_TRAIN_POS = (50, 200)
BUTTON_TRAIN_LABEL = "Start training"

BUTTON_PLAY_POS = (int(WIDTH/2), 200)
BUTTON_PLAY_LABEL = "Play games"

BUTTON_TENSORBOARD_POS = (WIDTH-50, 200)
BUTTON_TENSORBOARD_LABEL = "Start tensorboard"

CHECKBOX_LABEL = "Show games"
CHECKBOX_POS = (50, 300)

DELAY_LABEL = "Delay per move (in ms):"
DELAY_Y = 300
DELAY_WIDTH = 5

NUM_GAMES_LABEL = "Num games:"
NUM_GAMES_Y = 300
NUM_GAMES_X = WIDTH-150
NUM_GAMES_WIDTH = 5


class Window(Frame):
	def __init__(self, master=None):
		Frame.__init__(self, master)
		self.master = master
		self.show_games = IntVar(value=0)  # checkbox for showing games or not
		self.input_box = None              # input directory for training data
		self.log_text = None               # output log
		self.delay_move = None             # amount of ms that the programs sleep after a move
		self.num_games = None              # amount of games played
		self.init_view()

	def init_view(self):
		self.master.title(TITLE)
		self.pack(fill=BOTH, expand=1)

		# Add all elements
		self.add_title(TITLE, TITLE_Y)
		self.input_box = self.add_input_box(INPUT_LABEL, INPUT_Y, INPUT_WIDTH, DEFAULT_VALUE)
		self.delay_move = self.add_input_box(DELAY_LABEL, DELAY_Y, DELAY_WIDTH, "0")
		self.num_games = self.add_input_box(NUM_GAMES_LABEL, NUM_GAMES_Y, NUM_GAMES_WIDTH, "1", NUM_GAMES_X)

		self.add_button(BUTTON_TRAIN_LABEL, BUTTON_TRAIN_POS, "nw", command=self.command_train)
		self.add_button(BUTTON_PLAY_LABEL, BUTTON_PLAY_POS, "n", command=self.command_play)
		self.add_button(BUTTON_TENSORBOARD_LABEL, BUTTON_TENSORBOARD_POS, "ne", command=self.command_tensorboard)

		self.add_checkbox(CHECKBOX_LABEL, CHECKBOX_POS)

		self.log_text = self.add_log()
		self.log('Program initialised')

	def get_train_dir(self):
		return str(self.input_box.get())

	def get_delay_ms(self):
		return int(self.delay_move.get()) / 1000.0

	def get_num_games(self):
		return int(self.num_games.get())

	def add_title(self, title, y):
		label = Label(self.master, text=title, font=TITLE_FONT)
		label.place(x=WIDTH/2, y=y, anchor="center")

	def add_input_box(self, name, y, width, default_value, x=0):
		label = Label(self.master, text=name, font=INPUT_FONT)
		if x == 0:
			label.place(x=WIDTH / 2, y=y, anchor="center")
		else:
			label.place(x=x, y=y, anchor="nw")

		value = StringVar(self, default_value)
		input_box = Entry(self.master, width=width, font=INPUT_FONT, textvariable=value)
		if x == 0:
			input_box.place(x=WIDTH/2, y=y+30, anchor="center")
		else:
			input_box.place(x=x, y=y + 30, anchor="nw")
		return input_box

	def add_button(self, text, position, anchor, command):
		button = Button(self, text=text, command=command, font=INPUT_FONT)
		x, y = position
		button.place(x=x, y=y, anchor=anchor)

	def add_checkbox(self, label, position):
		checkbox = Checkbutton(self.master, text=label, variable=self.show_games, font=INPUT_FONT)
		x, y = position
		checkbox.place(x=x, y=y)

	def add_log(self):
		label = Label(self.master, text="Log:", font=INPUT_FONT)
		label.place(x=10, y=HEIGHT/2+70)

		log_text = ScrolledText(self.master, width=111, height=12)
		log_text.configure(state="disabled")
		log_text.place(x=0, y=HEIGHT/2+100)
		return log_text

	def checked(self):
		""" Returns true if the checkbox is selected """
		return self.show_games.get() == 1

	def log(self, text):
		""" add a line to the log """
		from tkinter.constants import END
		self.log_text.configure(state="normal")
		self.log_text.insert(END, text + '\n')
		self.log_text.see('end')
		self.log_text.configure(state="disabled")

	def command_train(self):
		""" command for starting the training """
		self.log('Starting with the training')

		from rl_2048.learning.learning import run_training
		run_training(self.get_train_dir())

	def command_play(self):
		""" command for playing a number of games """
		self.log('Playing ' + str(self.get_num_games()) + ' game(s).')

		from rl_2048.play_game import average_score, make_greedy_strategy
		score = average_score(make_greedy_strategy(self.get_train_dir(), self.get_num_games()))
		self.log('Average score: ' + str(score))

	def command_tensorboard(self):
		""" command for starting the tensorboard server """
		self.log('Starting tensorboard')

		def launch_tensor_board():
			import os
			os.system('tensorboard --logdir=' + self.get_train_dir())
			return

		threading.Thread(target=launch_tensor_board).start()


""" Create the view """
root = Tk()
root.geometry(str(WIDTH) + "x" + str(HEIGHT))
root.resizable(False, False)
window = Window(root)
root.mainloop()  # blocking function (i.e. function never returns)
