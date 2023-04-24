package csCourse.proj;

import org.springframework.data.util.Pair;

import java.util.ArrayList;

public class ControlSystem implements SignalFlowIF{
    ArrayList<Edge>[] graph;
    ArrayList<Trail> paths; /** We will use paths[0] = null as a dummy value for ease of delta computation */
    ArrayList<Trail> loops;
    ArrayList<ArrayList<ArrayList<Integer>>> nonTouchingLoops; /** nonTouchingLoops[i] = the indices (in the loops array) of (i+2)-groups non-touching loops */
    ArrayList<Double> pathDeltas; /** pathDeltas[0] = overall delta, pathDeltas[i] = delta of paths[i] */

    // Constructor that initializes the system using the number of vertices & the edge list
    public ControlSystem(int vertices, double[][] edgeList){
        // Graph adjacency list initialization
        int from,to;
        graph = new ArrayList[vertices];
        for (int i=0 ; i<vertices ; i++) graph[i] = new ArrayList<>();

        for (int i=0 ; i<edgeList.length ; i++){
            from = (int)Math.round(edgeList[i][0]);
            to = (int)Math.round(edgeList[i][1]);
            graph[from].add(new Edge(to, edgeList[i][2]));
        }

        /** We could initialize the forward paths using registerPaths method here */
        /** We could initialize the individual loops using getLoops method here */
        /** We could initialize the non-touching loops here */
        /** We could initialize the path deltas here */
//        registerPaths();
//        registerLoops();
//        nonTouchingLoops = getNonTouchingLoops(this.loops);
//        registerDeltas();
    }


    public void registerPaths(){
        paths = new ArrayList<>();
        paths.add(null); // Dummy value to facilitate delta computation
        //TODO Fill up the paths field with forward paths (Trail objects)
    }

    public void registerLoops(){
        paths = new ArrayList<>();
        //TODO Fill up the loops field with individual loops (Trail objects)
    }

    // We can pass two loops or a loop and a path here to see if they are touching or not.
    private boolean isTouching(Trail t1, Trail t2){
        for (int node : t1.getNodes()) if (t2.getNodes().contains(node)) return true;
        return false;
    }

    /** Method that takes a group of loops and returns the non-touching loop indices in that group */
    public ArrayList<ArrayList<ArrayList<Integer>>> getNonTouchingLoops(ArrayList<Trail> loops){
        //TODO seeb David ye3melha 3ashan mkasel yeshra7 eh elly hayet3emel XD
        return null;
    }

    public void registerDeltas(){
        pathDeltas = new ArrayList<>();
        //TODO Find the delta for each path, pathDeltas[0] = overall delta , and pathDeltas[i] = delta of paths[i]

        /**
         * Loop through paths list, for each path, get a list of loops that are not touching that path.
         * If you are at paths[0], use the list of loops = this.loops (original individual loops)
         * Initialize pathDeltas[i] = 1, add to it all individual loops.
         * get the list of non-touching loops for the individual loops you just got using getNonTouchingLoops.
         * loop through the non-touching loops, where for each index:
         *      if index is even, loop through the list at that index, subtracting the gain product of each group
         *      if index is odd, loop through the list at that index, adding the gain product of each group
         * */
    }








    public ArrayList<Pair<String, Double>> forwardPaths() {
        //TODO
        return null;
    }

    public ArrayList<Pair<String, Double>> loops() {
        //TODO
        return null;
    }

    public ArrayList<ArrayList<Pair<String, Double>>> nonTouchingLoops() {
        //TODO
        return null;
    }

    public ArrayList<Double> delta() {
        //TODO
        return null;
    }

    public Double transferFunction() {
        //TODO
        return null;
    }

    public int routh() {
        //TODO
        return 0;
    }
}
