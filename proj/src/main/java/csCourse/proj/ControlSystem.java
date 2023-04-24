package csCourse.proj;

import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;

public class ControlSystem implements SignalFlowIF{

    int vertices;
    ArrayList<Edge>[] graph;
    ArrayList<Trail> paths; /** We will use paths[0] = null as a dummy value for ease of delta computation */
    ArrayList<Trail> loops = new ArrayList<>();
    ArrayList<ArrayList<ArrayList<Integer>>> nonTouchingLoops; /** nonTouchingLoops[i] = the indices (in the loops array) of (i+2)-groups non-touching loops */
    ArrayList<Double> pathDeltas; /** pathDeltas[0] = overall delta, pathDeltas[i] = delta of paths[i] */

    // Constructor that initializes the system using the number of vertices & the edge list
    public ControlSystem(int vertices, double[][] edgeList){
        this.vertices = vertices;
        // Graph adjacency list initialization
        int from,to;
        graph = new ArrayList[vertices];
        for (int i=0 ; i<vertices ; i++) graph[i] = new ArrayList<>();

        for (double[] edge : edgeList) {
            from = (int) Math.round(edge[0]);
            to = (int) Math.round(edge[1]);
            graph[from].add(new Edge(to, edge[2]));
        }

        /** We could initialize the forward paths using registerPaths method here */
        /** We could initialize the individual loops using getLoops method here */
        /** We could initialize the non-touching loops here */
        /** We could initialize the path deltas here */
        registerPaths();
//        registerLoops();
//        nonTouchingLoops = getNonTouchingLoops(this.loops);
//        registerDeltas();
    }


    public void registerPaths(){
        paths = new ArrayList<Trail>();
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
        ArrayList<ArrayList<ArrayList<Integer>>> NTL = new ArrayList<>();
        boolean done;
        int index = 0, i, j, k, size;
        do{
            done = true;
            NTL.add(new ArrayList<>());
            k = 0;
            if (index == 0){ // n=2
                size = loops().size();
                // Add all non-touching pairs
                for(i=0 ; i<size-1 ; i++){
                    for (j=i+1 ; j<size ; j++){
                        if(!isTouching(loops.get(i),loops.get(j))){
                            NTL.get(0).add(new ArrayList<>(Arrays.asList(i,j)));
                            k++;
                            done = false;
                        }
                    }
                }
                // Done adding all non-touching pairs
            }else{ // n non-touching loops, where n>2 and index+2 = n
                ArrayList<ArrayList<Integer>> pairs = NTL.get(0);
                ArrayList<Integer> group;
                // Loop over all groups of n-1 touching loop groups
                for(i=0 ; i<NTL.get(index-1).size() ; i++){
                    group = NTL.get(index-1).get(i); // for example, group might be L0L1L4
                    for(j=0 ; j<loops.size() ; j++){
                        // If there is an additional loop in the individual loop group
                        // that doesn't touch every loop in the group, we add it
                        // to the group and add the new group to the new n-groups.
                        if(hasPairs(j , pairs , group)){
                            NTL.get(index).add(new ArrayList<>());
                            NTL.get(index).get(k).addAll(group); // Add the whole group
                            NTL.get(index).get(k).add(j); // Add the additional element
                            k++;
                            done = false;
                        }
                    }
                }
            }
            index++;
        }while(!done);
        if (NTL.get(0).isEmpty()) return null;
        return NTL;
    }

    private boolean hasPairs(int test, ArrayList<ArrayList<Integer>> pairs, ArrayList<Integer> group){
        /**
         * testing an element test, we want to look through the group and non-touching pairs
         * we will get element i of the group, if there are no pairs with test & element i, we will return false
         * if there is indeed a pair the iteration passes, and we look for a pair of test with the next element.
         */
        boolean noMatches;
        for(int i : group){
            noMatches = true;
            for (ArrayList<Integer> pair : pairs){
                if (pair.contains(test) && pair.contains(i)){
                    noMatches = false;
                    break;
                }
            }
            if(noMatches) return false;
        }
        return true;
    }

    public void registerDeltas(){
        pathDeltas = new ArrayList<>();
        double product, pathDelta;
        ArrayList<Trail> loopsNotTouchingPath = loops;
        ArrayList<ArrayList<ArrayList<Integer>>> nonTouchingLoopsOfPath = nonTouchingLoops;
        // Loop through paths list.
        for(Trail path : paths){
            pathDelta = 1;
            // If path is null, it's the first index, and we use the original individual
            // Loops and non-touching loops.
            if(path != null){
                loopsNotTouchingPath.clear();
                for(Trail loop : loops){
                    if (!isTouching(path,loop)) loopsNotTouchingPath.add(loop);
                }
                nonTouchingLoopsOfPath = getNonTouchingLoops(loopsNotTouchingPath);
            }
            // Add gains of individual loops
            for (Trail loop : loopsNotTouchingPath) pathDelta += loop.getGain();
            // Add/subtract gain products of non-touching loops
            for(int i=0 ; i<nonTouchingLoopsOfPath.size() ; i++){
                if(i%2==0){
                    for (ArrayList<Integer> nLoops : nonTouchingLoopsOfPath.get(i)) {
                        product = 1;
                        for (int loopIndex : nLoops) product *= loopsNotTouchingPath.get(loopIndex).getGain();
                        pathDelta -= product;
                    }
                }else{
                    for (ArrayList<Integer> nLoops : nonTouchingLoopsOfPath.get(i)) {
                        product = 1;
                        for (int loopIndex : nLoops) product *= loopsNotTouchingPath.get(loopIndex).getGain();
                        pathDelta += product;
                    }
                }
            }
            pathDeltas.add(pathDelta);
        }
    }


    Trail t = new Trail();

    public void forwardPaths(){
        forwardPaths(0,this.vertices-1);
    }
    public void forwardPaths(int s,int e) {
        //TODO
        t.addNode(s);
        for (Edge edge : this.graph[s]) {
            if( (edge.to != e) && !(t.containsNode(edge.to)) ){
                t.multiplyGain(edge.weight);
                forwardPaths(edge.to, e);
                t.divideGain(edge.weight);
            }
            else if (edge.to == e) {
                t.addNode(e);
                t.multiplyGain(edge.weight);
//                System.out.println("the path is " + t.getNodes().toString());
//                System.out.println("the gain is " + t.getGain());
                this.paths.add(t.clone());
                t.divideGain(edge.weight);
                t.removeNode(e);
            }
        }
        t.removeNode(s);
    }

    public void loops(){
        for(int i=1; i<this.vertices-1; i++){
            loops(i,i);
        }
    }
    public void loops(int s, int e) {
        //TODO

            t.addNode(s);
            for (Edge edge : this.graph[s]) {
                if( (edge.to != e) && !(t.containsNode(edge.to)) ){
                    t.multiplyGain(edge.weight);
                    loops(edge.to, e);
                    t.divideGain(edge.weight);
                }
                else if (edge.to == e) {
                    t.multiplyGain(edge.weight);
//                    System.out.println("the loop is " + t.getNodes().toString());
//                    System.out.println("the gain is " + t.getGain());
                    this.loops.add(t.clone());
                    t.divideGain(edge.weight);
                }
            }
            t.removeNode(s);
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
