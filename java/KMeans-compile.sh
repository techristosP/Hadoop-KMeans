#!/bin/bash

# Compile KMeansIteration.java using hadoop-core-1.2.1.jar
javac --release 11 -classpath $(pwd)/hadoop-core-1.2.1.jar -d kmeans-.class KMeansIteration.java KMeans.java

# Create a .jar file with the compiled classes
jar -cvf KMeans.jar -C kmeans-.class/ .