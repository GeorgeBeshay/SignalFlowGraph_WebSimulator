package csCourse.proj;

import java.util.ArrayList;

// Encapsulation of paths and loops
public class Trail {
    private ArrayList<Integer> nodes;
    private double gain;

    public Trail(){
        nodes = new ArrayList<>();
        gain = 1;
    }

    public Trail(ArrayList<Integer> nodes, double gain){
        this.nodes = nodes;
        this.gain = gain;
    }

    public void addNode(int node){
        nodes.add(node);
    }

    public boolean containsNode(int node){
        return nodes.contains(node);
    }

    public void removeNode(Integer node){
        nodes.remove(node);
    }

    public void multiplyGain(double g){
        gain *= g;
    }

    public void divideGain(double g){
        gain /= g;
    }

    public ArrayList<Integer> getNodes(){
        return nodes;
    }

    public double getGain(){
        return gain;
    }

    public Trail clone(){
        return new Trail((ArrayList<Integer>) this.nodes.clone(),this.gain);
    }
}
