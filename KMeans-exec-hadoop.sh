#!/bin/bash

# Check if the number of arguments is correct
if [ "$#" -ne 2 ]; then
    echo "Usage: $0 <datapoints_file> <init_centers_file> (Just the filename inside input folder, not the path)"
    exit 1
fi

# Create kmeans input and output dirs in HDFS (only if they dont exist)
hdfs dfs -mkdir /kmeans
hdfs dfs -mkdir /kmeans/input 
hdfs dfs -mkdir /kmeans/output

# Upload input files in HDFS
hdfs dfs -put $(pwd)/input/$1 /kmeans/input
hdfs dfs -put $(pwd)/input/$2 /kmeans/input

# Execute Map-Reduce .jar in Hadoop
# hadoop jar /home/popos/Downloads/hadoop-project/java/KMeans.jar KMeans /kmeans/input/$1 /kmeans/input/$2 /kmeans/output
hadoop jar $(pwd)/java/KMeans.jar KMeans /kmeans/input/$1 /kmeans/input/$2 /kmeans/output

# Save the output folder locally
current_datetime=$(date +"%Y-%m-%d-%H-%M-%S")
mkdir "output-$current_datetime"
hdfs dfs -get /kmeans/output $(pwd)/output-$current_datetime
