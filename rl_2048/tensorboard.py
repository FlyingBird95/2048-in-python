import threading

path = '/home/patrick/git/2048-in-java/python/experiments/train_0_3'


def launch_tensor_board():
    import os
    os.system('tensorboard --logdir=' + path)
    return


t = threading.Thread(target=launch_tensor_board, args=([]))
t.start()

