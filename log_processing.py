import sys

tsTime = 0
tjTime = 0
numOfLine = 0

ts = open("ts_log.txt","r")
tj = open("tj_log.txt","r")

for i in ts.readlines():
    tsTime += int(i)
    numOfLine += 1

for i in tj.readlines():
    tjTime += int(i)

tsavg = round((tsTime / numOfLine)/1000000, 2)
tjavg = round((tjTime / numOfLine)/1000000, 2)

print("TS Average =", tsavg)
print("TJ Average =", tjavg)
