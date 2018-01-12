from tkinter import StringVar, IntVar


DEFAULT_TRAIN_DIR = '../experiments/train_0_4'


class Config(object):
	"""
		All options that are set in the Main GUI (see window.py) can be accessed in this class.
	"""
	def __init__(self, master):
		self.master = master

		self.train_dir_obj = StringVar(master=master, value=DEFAULT_TRAIN_DIR)
		self.delay_in_ms_obj = StringVar(master=master, value='0')
		self.num_games_obj = StringVar(master=master, value='1')
		self.show_games_obj = IntVar(master=master, value=0)

	def get_train_dir(self):
		return str(self.train_dir_obj.get())

	def get_delay_in_sec(self):
		return int(self.delay_in_ms_obj.get()) / 1000.0

	def get_num_games(self):
		return int(self.num_games_obj.get())

	def show_games(self):
		return self.show_games_obj.get() == 1

	def print(self):
		print('Train directory: ' + self.get_train_dir())
		print('Delay in ms: ' + str(self.get_delay_in_sec()))
		print('Num games: ' + str(self.get_num_games()))
		print('Show games: ' + str(self.show_games()))
