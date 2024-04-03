import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;

public class KMeansIteration {

    public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text> {
        private List<double[]> centers = new ArrayList<>();

        public void configure(JobConf job) {
            try {
                Path[] cacheFiles = DistributedCache.getLocalCacheFiles(job);
                if (cacheFiles != null && cacheFiles.length > 0) {
                    BufferedReader reader = new BufferedReader(new FileReader(cacheFiles[0].toString()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] points = line.split("[\t\\s]+");
                        double x = Double.parseDouble((points.length == 2) ? points[0] : points[2]);
                        double y = Double.parseDouble((points.length == 2) ? points[1] : points[3]);
                        centers.add(new double[]{x, y});
                    }
                    reader.close();
                }
            } catch (IOException e) {
                System.err.println("Error reading centers file: \n" + e.getMessage());                
            }
        }

        public void map(LongWritable key, Text value, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
            String[] point = value.toString().split(" ");
            double x = Double.parseDouble(point[0]);
            double y = Double.parseDouble(point[1]);

            double minDistance = Double.MAX_VALUE;
            double[] closestCenter = null;
            for (double[] center : centers) {
                double dist = Math.sqrt(Math.pow(x-center[0], 2) + Math.pow(y-center[1], 2));
                if (dist < minDistance) {
                    minDistance = dist;
                    closestCenter = center;
                }
            }

            output.collect(new Text(closestCenter[0] + " " + closestCenter[1]), new Text(point[0] + " " + point[1]));
        }
    }

    public static class Reduce extends MapReduceBase implements Reducer<Text, Text, Text, Text> {
        
        public void reduce(Text key, Iterator<Text> values, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
            double X = 0;
            double Y = 0;
            double count = 0;
            while (values.hasNext()) {
                count++;
                String[] point = values.next().toString().split(" ");
                X += Double.parseDouble(point[0]);
                Y += Double.parseDouble(point[1]);
            }
            X /= count;
            Y /= count;
            output.collect(key, new Text(X + " " + Y));
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.err.println("Usage: KMeansIteration [input path] [centers path] [output path]");
            System.exit(-1);
        }

        JobConf conf = new JobConf(KMeansIteration.class);
        conf.setJobName("KMeansIteration");

        conf.setMapperClass(Map.class);
        conf.setReducerClass(Reduce.class);

        conf.setOutputKeyClass(Text.class);
        conf.setOutputValueClass(Text.class);

        conf.setInputFormat(TextInputFormat.class);
        conf.setOutputFormat(TextOutputFormat.class);

        FileInputFormat.setInputPaths(conf, new Path(args[0]));
        DistributedCache.addCacheFile(new URI(args[1]), conf);
        FileOutputFormat.setOutputPath(conf, new Path(args[2]));

        JobClient.runJob(conf);

    }
}