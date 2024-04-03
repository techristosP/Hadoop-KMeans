import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.conf.*;

public class KMeans {
    // private static List<double[]> centers = new ArrayList<>();
    private static boolean converged = false;

    public static void main(String[] args) throws Exception{

        if (args.length != 3) {
            System.err.println("Usage: KMeans [input/data path] [init centers path] [output path]");
            System.exit(-1);
        }

        // initCenters(args[1]);
        clearPreviousOutput(args[2]);

        int iter = 1;
        while (!converged) {
            runIter(args, iter);
            checkConvergence(args[2], iter);
                
            iter++;
        }

        System.exit(0);
    }

    private static void runIter(String[] args, int iter) throws Exception{
        // Run a single iteration of the KMeans algorithm
        String inputPath = args[0];     // File containing the data points
        String centersPath = (iter == 1) ? args[1] : (args[2]+"/out_iter_"+(iter-1)+"/part-00000");
        String outputPath = args[2]+"/out_iter_"+iter;

        String[] iterArgs = {inputPath, centersPath, outputPath};
         
        // for (String arg : iterArgs) {
        //     System.out.println(arg);
        // }

        KMeansIteration.main(iterArgs);
         
        while(!checkFileExistance(outputPath))
            System.out.println("Waiting for output file to be created...");
    }

    private static void checkConvergence(String output, int iter) throws Exception{
        // Check if the algorithm has converged
        String newCentersPath = output+"/out_iter_"+iter+"/part-00000";
 
        // if (checkFileExistance(output+"/out_iter_"+iter)) {
        //     System.err.println("Path: "+newCentersPath+"\nOutput file exists");
        // }
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);
        FSDataInputStream inputStream = fs.open(new Path(newCentersPath));
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        int centersConverged = 0;
        while ((line = reader.readLine()) != null) {
            String[] centers = line.split("[\t\\s]+");
            double oldX = Double.parseDouble(centers[0]);
            double oldY = Double.parseDouble(centers[1]);
            double newX = Double.parseDouble(centers[2]);
            double newY = Double.parseDouble(centers[3]);

            double dist = Math.sqrt(Math.pow(oldX-newX, 2) + Math.pow(oldY-newY, 2));
            if (dist < 0.01) {
                centersConverged++;
            }
        }

        if (centersConverged == 3) {
            converged = true;
        }
        reader.close();
        inputStream.close();
        fs.close();
    }

    private static boolean checkFileExistance(String outputPath) throws Exception{
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);

        String outputFilePath = outputPath + "/part-00000";
        Path path = new Path(outputFilePath);

        boolean exists = fs.exists(path); 
        fs.close();

        return exists;
    }

    private static void clearPreviousOutput(String output) throws Exception{
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);

        Path folder = new Path(output);
        if (fs.exists(folder)) {
            FileStatus[] files = fs.listStatus(folder);
            if (files != null) {
                for (FileStatus file : files) {
                    fs.delete(file.getPath(), true);
                }
            }         
        }
        fs.close();
    }

    // private static void initCenters(String centersPath) {
    //     // Set the centers for the next iteration
    //     BufferedReader reader = new BufferedReader(new FileReader(centersPath));
    //     String line;
    //     while ((line = reader.readLine()) != null) {
    //         String[] points = line.split(" ");
    //         double x = Double.parseDouble(points[0]);
    //         double y = Double.parseDouble(points[1]);
    //         centers.add(new double[]{x, y});
    //     }

    // }
}