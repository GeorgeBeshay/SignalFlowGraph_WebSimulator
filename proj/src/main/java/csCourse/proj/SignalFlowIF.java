package csCourse.proj;
import java.awt.*;
import java.util.ArrayList;

public interface SignalFlowIF {

    //get all the forward paths with the gains
    ArrayList<Pair<String,Double>> forwardPaths();

    //get all the loops in the given graph with the gains
    ArrayList<Pair<String,Double>> loops();

    /**
     [
        2 Non touching:
                 [(L1L2,G1G2), (L3L4,G3G4), (L5L6,G5G6)]
        3 Non touching:
                []
     ]
     */
    ArrayList<ArrayList<Pair<String,Double>>> nonTouchingLoops();

    // index[0] is the over all delta
    ArrayList<Double> delta();

    //calculate the over all transfer function
    double transferFunction();

    //return the number of the solutions in the right hand side
    // if the system is stable return zero
    int routh(double[] coefficients);
}
