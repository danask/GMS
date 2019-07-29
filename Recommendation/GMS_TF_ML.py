import tensorflow as tf
import mysql.connector


x1_input = 25.0;
x2_input = 50.0;

mydb = mysql.connector.connect(
    host = "localhost",
    user = "root",
    passwd = "admin1010",
    database= "gms"
)

mycursor = mydb.cursor()

mycursor.execute("SELECT sensorTemp, sensorHumid FROM sensor "+
                 "where description = 'demo'")
myresult = mycursor.fetchall()

for rows in myresult:
    x1_input = float(rows[0])
    x2_input = float(rows[1])
    print(x1_input)



# data
x1_data = [21., 22., 23., 24., 25., 26., 27., 28., 29., 30.] # room_temp
x2_data = [70., 65., 60., 55., 50., 45., 40., 35., 30., 25.] # room_humidity
#x3_data = [75., 93., 90., 100., 70.]
y_data = [1.6, 1.7, 1.8, 1.9, 2.0, 2.1, 2.2, 2.3, 2.4, 2.5]
 
# variables
x1 = tf.placeholder(tf.float32)
x2 = tf.placeholder(tf.float32)
#x3 = tf.placeholder(tf.float32)
Y = tf.placeholder(tf.float32)

w1 = tf.Variable(tf.random_normal([1]), name='weight1')
w2 = tf.Variable(tf.random_normal([1]), name='weight2')
#w3 = tf.Variable(tf.random_normal([1]), name='weight3')
b = tf.Variable(tf.random_normal([1]), name='bias')
 
# hypothesis
hypothesis = x1*w1 + x2*w2 + b #x3*w3 + b
 
# cost/loss function
cost = tf.reduce_mean(tf.square(hypothesis - Y))
# minimize cost function 
optimizer = tf.train.GradientDescentOptimizer(learning_rate=1e-5)
train = optimizer.minimize(cost)
 
# Session
with tf.Session() as sess:
    
    sess.run(tf.global_variables_initializer())
 
    for step in range(5001):
        cost_val, hy_val, _ = sess.run([cost, hypothesis, train],
                                       feed_dict={x1: x1_data, x2: x2_data, Y: y_data})
                                       #feed_dict={x1: x1_data, x2: x2_data, x3: x3_data, Y: y_data})
        #if step % 500 == 0:
        #    print(step, "Cost: ", cost_val, "\nPrediction:\n", hy_val)
    result = sess.run(hypothesis, feed_dict={x1: [x1_input], x2: [x2_input]})
    print(result)

    sql = "UPDATE sensor SET description = '" + str(result[0]) + "' WHERE description = 'demo'"
    #print (sql)
    mycursor.execute(sql)
    mydb.commit()
    print(mycursor.rowcount, "record(s) affected")
