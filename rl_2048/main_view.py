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
DEFAULT_VALUE = "/experiments/train"
INPUT_Y = 100

BUTTON_TRAIN_POS = (50, 200)
BUTTON_TRAIN_LABEL = "Start training"

BUTTON_PLAY_POS = (300, 200)
BUTTON_PLAY_LABEL = "Play a single game"

BUTTON_TENSORBOARD_POS = (550, 200)
BUTTON_TENSORBOARD_LABEL = "Start tensorboard"


class Window(Frame):
	def __init__(self, master=None):
		Frame.__init__(self, master)
		self.master = master
		self.input_box = None
		self.log_text = None
		self.init_view()

	def init_view(self):
		self.master.title(TITLE)
		self.pack(fill=BOTH, expand=1)

		# Add all elements
		self.add_title(TITLE, TITLE_Y)
		self.input_box = self.add_input_box(INPUT_LABEL, INPUT_Y, DEFAULT_VALUE)

		self.add_button(BUTTON_TRAIN_LABEL, BUTTON_TRAIN_POS, command=self.command_train)
		self.add_button(BUTTON_PLAY_LABEL, BUTTON_PLAY_POS, command=self.command_play)
		self.add_button(BUTTON_TENSORBOARD_LABEL, BUTTON_TENSORBOARD_POS, command=self.command_tensorboard)

		self.log_text = self.add_log()

	def get_train_dir(self):
		return str(self.input_box.get())

	def add_title(self, title, y):
		label = Label(self.master, text=title, font=TITLE_FONT)
		label.place(x=WIDTH/2, y=y, anchor="center")

	def add_input_box(self, name, y, default_value):
		label = Label(self.master, text=name, font=INPUT_FONT)
		label.place(x=WIDTH / 2, y=y, anchor="center")

		import os
		dir_path = os.path.dirname(os.path.realpath(__file__))
		folders = dir_path.split('/')
		folders = folders[1:len(folders)-1]

		value = StringVar(self, '/'.join(folders) + default_value)
		input_box = Entry(self.master, width=50, font=INPUT_FONT, textvariable=value )
		input_box.place(x=WIDTH/2, y=y+30, anchor="center")
		return input_box

	def add_button(self, text, position, command):
		button = Button(self, text=text, command=command, font=INPUT_FONT)
		x, y = position
		button.place(x=x, y=y)

	def add_log(self):
		label = Label(self.master, text="Log:", font=INPUT_FONT)
		label.place(x=10, y=HEIGHT/2+50)

		log_text = ScrolledText(self.master, width=WIDTH, height=HEIGHT/2-100)
		log_text.configure(state="disabled")
		log_text.place(x=0, y=HEIGHT/2+100)
		return log_text

	def log(self, text):
		""" add a line to the log """
		from tkinter.constants import END
		# self.log_text.config(state=NORMAL)
		self.log_text.insert(END, text)
		# self.log_text.config(state=DISABLED)

	def command_train(self):
		""" command for starting the training """

		from rl_2048.learning.learning import run_training
		run_training(self.get_train_dir())

	def command_play(self):
		""" command for playing a single game """

		from rl_2048.play_game import play_single_game
		play_single_game(self.get_train_dir())

	def command_tensorboard(self):
		""" command for starting the tensorboard server """

		def launch_tensor_board():
			import os
			os.system('tensorboard --logdir=' + self.get_train_dir())
			return

		threading.Thread(target=launch_tensor_board).start()


def main():
	""" Create the view """
	root = Tk()
	root.geometry(str(WIDTH) + "x" + str(HEIGHT))
	Window(root)
	root.mainloop()


if __name__ == '__main__':
	main()
