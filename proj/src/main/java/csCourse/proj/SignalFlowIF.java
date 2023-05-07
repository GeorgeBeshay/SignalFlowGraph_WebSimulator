package csCourse.proj;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.ArrayList;

public interface SignalFlowIF {

    // Get all the forward paths with the respective gains.
    ArrayList<Pair<String,Double>> forwardPaths();

    // Get all the loops in the given graph with the respective gains.
    ArrayList<Pair<String,Double>> loops();

    // Returns a list of groups of non-touching loops with their respective gain products.
    // List at index-i represents a list of i+2 non-touching loop groups.
    // Group loops are represented as a string (LxLyLz) and a double (product gain).
    ArrayList<ArrayList<Pair<String,Double>>> nonTouchingLoops();

    // Get the deltas for all paths (deltas[i] = delta of path[i]).
    // Deltas[0] is the overall delta.
    ArrayList<Double> delta();

    // Calculate the overall transfer function.
    double transferFunction();

    // Return the number of the solutions on the right hand side.
    // Returns zero if the system is stable.
    int routh(double[] coefficients);
}
