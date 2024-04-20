HADOOP - MAPREDUCE
==================
This project is a simple implementation of KMeans algorithm using Hadoop MapReduce. 
Hadoop version used is 3.2.4.

### Steps
To start the Hadoop daemons in a hadoop-ready linux environment, run the following commands:
```
start-dfs.sh
start-yarn.sh
```

To compile and create the jar file, run the following commands in /java directory:
```
chmod +x KMeans-compile.sh
KMeans-compile.sh
``` 

To execute KMeans algorith in Hadoop, run the following commands in project directory (Enter only the names of the files located inside the /input directory, not the path):
```
chmod +x KMeans-exec-hadoop.sh
KMeans-exec-hadoop.sh <datapoints_file> <init_centers_file> 
```

###### You can find the results in the output directory of the project with the latest date in the name.

To create your own data points file, run the following command in project directory:
```
python3 createPoints.py
```
You can specify the number of data points, the centers and the standard deviation of the data points inside the file.
