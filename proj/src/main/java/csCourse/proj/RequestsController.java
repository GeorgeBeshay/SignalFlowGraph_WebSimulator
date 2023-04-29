package csCourse.proj;

import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
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
//        System.out.println(numberOfNodes);
//        System.out.println(edgeList[0][0]);
        return true;
    }

    @PostMapping(value = {"fps"})
    public ArrayList<Pair<String,Double>> getFPs(){
        System.out.println("---------------- Separator ----------------");
        System.out.println("Client Requested Forward Paths");;
        return null;
    }

    @PostMapping(value = {"loops"})
    public ArrayList<Pair<String,Double>> getLoops(){
        System.out.println("---------------- Separator ----------------");
        System.out.println("Client Requested All Individual Loops");
        return null;
    }

    @PostMapping(value = {"ntLoops"})
    public ArrayList<ArrayList<Pair<String,Double>>> getNonTouchingLoops(){
        System.out.println("---------------- Separator ----------------");
        System.out.println("Client Requested Non Touching Loops");
        return null;
    }

    @PostMapping(value = {"deltas"})
    public ArrayList<Double>  getDeltas(){
        System.out.println("---------------- Separator ----------------");
        System.out.println("Client Requested Deltas");
        return null;
    }

    @PostMapping(value = {"tf"})
    public Double getTf(){
        System.out.println("---------------- Separator ----------------");
        System.out.println("Client Requested Transfer Function");
        return 0.0;
    }

    @PostMapping(value = {"routh"})
    public int getRouth(){
        System.out.println("---------------- Separator ----------------");
        System.out.println("Client Requested to check for the system stability");
        System.out.println("Checking System Stability Using RH - Criterion");
        return 0;
    }


}
