package csCourse.proj;

import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/server/")
@RestController
public class RequestsController {

    @Autowired
    private SignalFlowIF solver;

    @PostMapping(value = {"init/{numberOfNodes}"})
    public boolean init_system(@PathVariable int numberOfNodes, @RequestBody double[][] edgeList){
        System.out.println("---------------- Separator ----------------");
        System.out.println("Client Requested to initialize the system");
        // Initializing system solver
        this.solver = new ControlSystem(numberOfNodes, edgeList);
        // -------------------------- Separator --------------------------
        System.out.println("System Initialized with the following specifications:");
        System.out.println("Number of nodes:" + numberOfNodes);
        System.out.println("Edges:");
        for(double[] tempArr : edgeList)
            System.out.println("From: " + tempArr[0] + ", To: " + tempArr[1] +
                    ", Weight: " + tempArr[2]);
        // -------------------------- Separator --------------------------
        return true;
    }

    @PostMapping(value = {"fps"})
    public ArrayList<Pair<String,Double>> getFPs(){
        System.out.println("---------------- Separator ----------------");
        System.out.println("Client Requested Forward Paths");
        ArrayList<Pair<String, Double>> temp = this.solver.forwardPaths();
        // -------------------------- Separator --------------------------
        System.out.println("Forward paths are:");
        for(Pair<String, Double> tempPair : temp) {
            System.out.println("Path: " + tempPair.getKey());
            System.out.println("Gain: " + tempPair.getValue());
        }
        // -------------------------- Separator --------------------------
        return this.solver.forwardPaths();
    }

    @PostMapping(value = {"loops"})
    public ArrayList<Pair<String,Double>> getLoops(){
        System.out.println("---------------- Separator ----------------");
        System.out.println("Client Requested All Individual Loops");
        // -------------------------- Separator --------------------------
        ArrayList<Pair<String,Double>> temp = this.solver.loops();
        System.out.println("All Systems loops are:");
        for(Pair<String, Double> tempPair : temp) {
            System.out.println("Loop Path: " + tempPair.getKey());
            System.out.println("Loop Gain: " + tempPair.getValue());
        }
        // -------------------------- Separator --------------------------
        return temp;
    }

    @PostMapping(value = {"ntLoops"})
    public ArrayList<ArrayList<Pair<String,Double>>> getNonTouchingLoops(){
        System.out.println("---------------- Separator ----------------");
        System.out.println("Client Requested Non Touching Loops");
        // -------------------------- Separator --------------------------
        ArrayList<ArrayList<Pair<String,Double>>> temp = this.solver.nonTouchingLoops();
        int counter = 2;
        System.out.println("All Systems loops are:");
        for(ArrayList<Pair<String, Double>> tempList : temp) {
            System.out.println(counter + " Non touching Loops: ");
            counter++;
            for(Pair<String, Double> tempPair : tempList){
                System.out.println("\t" + "Loop Path: " + tempPair.getKey());
                System.out.println("\t" + "Loop Gain: " + tempPair.getValue());
            }
        }
        // -------------------------- Separator --------------------------
        return temp;
    }

    @PostMapping(value = {"deltas"})
    public ArrayList<Double>  getDeltas(){
        System.out.println("---------------- Separator ----------------");
        System.out.println("Client Requested Deltas");
        // -------------------------- Separator --------------------------
        ArrayList<Double> temp = this.solver.delta();
        System.out.println("System over all delta: " + temp.get(0));
        System.out.println("System Deltas:");
        for(int i = 1 ; i < temp.size() ; i++)
            System.out.println(temp.get(i) + ", ");
        // -------------------------- Separator --------------------------
        return temp;
    }

    @PostMapping(value = {"tf"})
    public Double getTf(){
        System.out.println("---------------- Separator ----------------");
        System.out.println("Client Requested Transfer Function");
        // -------------------------- Separator --------------------------
        Double temp = this.solver.transferFunction();
        System.out.println("System transfer function = " + temp);
        // -------------------------- Separator --------------------------
        return temp;
    }

    @PostMapping(value = {"routh"})
    public int getRouth(@RequestBody double[] coefficients){
        System.out.println("---------------- Separator ----------------");
        System.out.println("Client Requested to check for the system stability");
        System.out.println("Checking System Stability Using RH - Criterion");
        // -------------------------- Separator --------------------------
        int temp = this.solver.routh(coefficients);
        System.out.println("Number of roots that are on the RHS of the Imaginary Axis = " + temp);
        // -------------------------- Separator --------------------------
        return temp;
    }


}
