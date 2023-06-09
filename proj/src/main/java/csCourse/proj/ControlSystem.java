package csCourse.proj;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Arrays;

@Service
public class ControlSystem implements SignalFlowIF{

    private int vertices;
    private ArrayList<Edge>[] graph;
    private ArrayList<Trail> paths = new ArrayList<>(); /** We will use paths[0] = null as a dummy value for ease of delta computation */
    private ArrayList<Trail> loops = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<Integer>>> nonTouchingLoops; /** nonTouchingLoops[i] = the indices (in the loops array) of (i+2)-groups non-touching loops */
    private ArrayList<Double> pathDeltas; /** pathDeltas[0] = overall delta, pathDeltas[i] = delta of paths[i] */
    // Auxiliary helper fields for path & loop finding algorithms
    private ArrayList<String> loopsStrings = new ArrayList<>();
    private Trail t = new Trail();

    public ControlSystem(){}
    // Constructor that initializes the system using the number of vertices & the edge list
    public ControlSystem(int vertices, double[][] edgeList){
        this.vertices = vertices;
        // Graph adjacency list initialization
        int from,to;
        boolean exists;
        graph = new ArrayList[vertices];
        for (int i=0 ; i<vertices ; i++) graph[i] = new ArrayList<>();

        for (double[] edge : edgeList) {
            from = (int) Math.round(edge[0]);
            to = (int) Math.round(edge[1]);
            exists = false;
            for(Edge e : graph[from]){
                if (e.to == to) {
                    e.gain += edge[2];
                    exists = true;
                }
            }
            if(!exists) graph[from].add(new Edge(to, edge[2]));
        }

        // Initialize forward paths, individual loops, combinations of non-touching loops,
        // overall delta, and delta of each path.
        registerPaths();
        registerLoops();
        nonTouchingLoops = getNonTouchingLoops(this.loops);
        registerDeltas();
    }


    public void registerPaths(){
        paths.add(null); // Dummy value to facilitate delta computation
        recursiveForwardPaths(0,this.vertices-1);
    }

    public void recursiveForwardPaths(int s,int e) {
        t.addNode(s);
        for (Edge edge : this.graph[s]) {
            if( (edge.to != e) && !(t.containsNode(edge.to)) ){
                t.multiplyGain(edge.gain);
                recursiveForwardPaths(edge.to, e);
                t.divideGain(edge.gain);
            }
            else if (edge.to == e) {
                t.addNode(e);
                t.multiplyGain(edge.gain);
                this.paths.add(t.clone());
                t.divideGain(edge.gain);
                t.removeNode(e);
            }
        }
        t.removeNode(s);
    }

    public void registerLoops(){
        // Fill up the loops field with individual loops (Trail objects)
        for(int i=1; i<this.vertices-1; i++){ recursiveLoops(i,i);}
    }

    private boolean checkIfExist(String str){
        boolean flag;
        for (String exist: this.loopsStrings) {
            flag = false;
            if(exist.length()==str.length()){
                for(int i=0;i<str.length(); i++){
                    if(exist.indexOf(str.charAt(i)) < 0){
                        flag = true;
                        break;
                    }
                }
                if(!flag){return true;}
            }
        }
        return false;
    }
    public void recursiveLoops(int s, int e) {
        t.addNode(s);
        for (Edge edge : this.graph[s]) {
            if( (edge.to != e) && !(t.containsNode(edge.to)) ){
                t.multiplyGain(edge.gain);
                recursiveLoops(edge.to, e);
                t.divideGain(edge.gain);
            }
            else if (edge.to == e) {
                if(checkIfExist(t.getNodes().toString())){continue;}
                t.multiplyGain(edge.gain);
                t.addNode(e); // Add the node we got back to to the list
                this.loops.add(t.clone());
                t.getNodes().remove(t.getNodes().size()-1); // Remove the node again to ensure correctness
                this.loopsStrings.add(t.getNodes().toString());
                t.divideGain(edge.gain);
            }
        }
        t.removeNode(s);
    }

    // We can pass two loops or a loop and a path here to see if they are touching or not.
    private boolean isNotTouching(Trail t1, Trail t2){
        for (int node : t1.getNodes()) if (t2.getNodes().contains(node)) return false;
        return true;
    }

    /** Method that takes a group of loops and returns the non-touching loop indices in that group */
    public ArrayList<ArrayList<ArrayList<Integer>>> getNonTouchingLoops(ArrayList<Trail> loops){
        ArrayList<ArrayList<ArrayList<Integer>>> NTL = new ArrayList<>();
        ArrayList<ArrayList<Integer>> pairs;
        boolean done;
        int index = 0, i, j, k, size, start;
        do{
            done = true;
            NTL.add(new ArrayList<>());
            k = 0;
            if (index == 0){ // n=2
                size = loops.size();
                // Add all non-touching pairs
                for(i=0 ; i<size-1 ; i++){
                    for (j=i+1 ; j<size ; j++){
                        if(isNotTouching(loops.get(i), loops.get(j))){
                            NTL.get(0).add(new ArrayList<>(Arrays.asList(i,j)));
                            k++;
                            done = false;
                        }
                    }
                }
                // Done adding all non-touching pairs
            }else{ // n non-touching loops, where n>2 and index+2 = n
                pairs = NTL.get(0);
                ArrayList<Integer> group;
                // Loop over all groups of n-1 touching loop groups
                for(i=0 ; i<NTL.get(index-1).size() ; i++){
                    group = NTL.get(index-1).get(i); // for example, group might be L0L1L4
                    // Groups are always sorted, so start checking nodes from
                    // right after the greatest (last) element of the group.
                    start = group.get(group.size()-1);
                    for(j=start+1 ; j<loops.size() ; j++){
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
        /*
          testing an element test, we want to look through the group and non-touching pairs
          we will get element i of the group, if there are no pairs with test & element i, we will return false
          if there is indeed a pair the iteration passes, and we look for a pair of test with the next element.
         */
        boolean noMatches;
        for(int i : group){
            noMatches = true;
            for (ArrayList<Integer> pair : pairs){
                // test is always greater than all numbers in the group, and pairs are sorted
                // if there exists a pair, it would have to be i first then test.
                if (pair.get(0) == i && pair.get(1) == test){
                    noMatches = false;
                    break;
                }
                // if we went past pairs starting with higher than i, then there
                // is no more elements starting with i, which means we don't
                // need to look further.
                if (pair.get(0) > i) return false;
            }
            if(noMatches) return false;
        }
        return true;
    }

    public void registerDeltas(){
        pathDeltas = new ArrayList<>();
        double product, pathDelta;
        boolean nonTouchingExists = (nonTouchingLoops != null);
        ArrayList<Trail> loopsNotTouchingPath = new ArrayList<>(loops);
        ArrayList<ArrayList<ArrayList<Integer>>> nonTouchingLoopsOfPath = (nonTouchingExists)? new ArrayList<>(nonTouchingLoops) : null;
        // Loop through paths list.
        for(Trail path : paths){
            pathDelta = 1;
            // If path is null, it's the first index, and we use the original individual
            // Loops and non-touching loops.
            if(path != null){
                loopsNotTouchingPath.clear();
                for(Trail loop : loops){
                    if (isNotTouching(path, loop)) loopsNotTouchingPath.add(loop);
                }
                if(nonTouchingExists) nonTouchingLoopsOfPath = getNonTouchingLoops(loopsNotTouchingPath);
            }
            // Subtract gains of individual loops
            for (Trail loop : loopsNotTouchingPath) pathDelta -= loop.getGain();
            // If non-touching loops of the path do exist:
            // Add/subtract gain products of non-touching loops
            if (nonTouchingLoopsOfPath != null){
                for (int i = 0; i < nonTouchingLoopsOfPath.size(); i++) {
                    if (i % 2 == 0) {
                        for (ArrayList<Integer> nLoops : nonTouchingLoopsOfPath.get(i)) {
                            product = 1;
                            for (int loopIndex : nLoops) product *= loopsNotTouchingPath.get(loopIndex).getGain();
                            pathDelta += product;
                        }
                    } else {
                        for (ArrayList<Integer> nLoops : nonTouchingLoopsOfPath.get(i)) {
                            product = 1;
                            for (int loopIndex : nLoops) product *= loopsNotTouchingPath.get(loopIndex).getGain();
                            pathDelta -= product;
                        }
                    }
                }
            }
            pathDeltas.add(pathDelta);
        }
    }

    /** -------------------------------------------- INTERFACE METHODS -------------------------------------------- */

    public ArrayList<Pair<String,Double>> forwardPaths(){
        ArrayList<Pair<String,Double>> ret = new ArrayList<>();
        for(int i=1 ; i<paths.size() ; i++)
            ret.add(new Pair<>(
                    "P" + i + ": " + paths.get(i).getNodes().toString(),
                    paths.get(i).getGain()
            ));
        return ret;
    }

    public ArrayList<Pair<String,Double>> loops(){
        ArrayList<Pair<String,Double>> ret = new ArrayList<>();
        for(int i=0 ; i<loops.size() ; i++)
            ret.add(new Pair<>(
                            "L" + i + ": " + loops.get(i).getNodes().toString(),
                            loops.get(i).getGain()
                    ));
        return ret;
    }

    public ArrayList<ArrayList<Pair<String, Double>>> nonTouchingLoops() {
        if (nonTouchingLoops == null){
            return new ArrayList<>();
        }
        // nonTouching[i] = a list of groups of i+2 non-touching loops.
        ArrayList<ArrayList<Pair<String,Double>>> nonTouching = new ArrayList<>();
        StringBuilder group;
        double product;
        int index = 0;
        for(ArrayList<ArrayList<Integer>> nLoopGroups : nonTouchingLoops){
            nonTouching.add(new ArrayList<>());
            for (ArrayList<Integer> nLoopGroup : nLoopGroups){
                group = new StringBuilder();
                product = 1;
                for(int loopIndex : nLoopGroup){
                    group.append("L").append(loopIndex);
                    product *= loops.get(loopIndex).getGain();
                }
                nonTouching.get(index).add(new Pair<>(group.toString(), product));
            }
            index++;
        }
        return nonTouching;
    }

    public ArrayList<Double> delta() {
        return pathDeltas;
    }

    public double transferFunction() {
        double tf = 0;
        for(int i=1 ; i<paths.size() ; i++)
            tf += paths.get(i).getGain() * pathDeltas.get(i);
        return tf / pathDeltas.get(0);
    }


    public int routh(double[] coefficients) {
        final double EPS = 0.0001;
        int len = coefficients.length;
        int n = Math.round((float) len / (float) 2.0);
        int f = 0, s = 0;
        double[][] table = new double[len][n];

        for (int i = 0; i < len; i++) {
            if (i%2 == 0)
                table[0][f++] = coefficients[i];
            else
                table[1][s++] = coefficients[i];
        }

        for (int i = 2; i < len; i++) {

            if (table[i - 1][0] == 0) table[i - 1][0] = EPS;

            for (int j = 0; j < n - 1; j++)
                table[i][j] = ((table[i-1][0] * table[i-2][j+1]) - (table[i-2][0] * table[i-1][j+1]))
                                                / table[i-1][0];

            boolean flag = false;
            for (int k = 0; k < n; k++) {
                if (table[i][k] != 0) {
                    flag = true;
                    break;
                }
            }

            if (!flag) {
                int Order = len - i;
                int c = 0;
                for (int j = 0; j < n - 1; j++) {
                    table[i][j] = (Order - c) * table[i - 1][j];
                    c += 2;
                }
            }
        }
        System.out.println("Routh");
        for (double[] doubles : table) {
            for (double aDouble : doubles) System.out.println(aDouble + " ");
            System.out.println();
        }
        int rightPoles = 0;
        for (int i = 0; i < len - 1; i++) {
            if ((table[i][0] * table[i + 1][0]) < 0) {
                rightPoles++;
            }
        }

        if(rightPoles>0)
            System.out.println("System is Unstable");
        else
            System.out.println("System is Stable");
        return rightPoles;
    }
}