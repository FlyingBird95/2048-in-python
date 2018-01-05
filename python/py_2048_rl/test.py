import tensorflow as tf
import sys

hello = tf.constant('xxxHello, TensorFlow!')
sess = tf.Session()

output = sess.run(hello)
print(output)



xxx = sys.path
