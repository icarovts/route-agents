/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package routeagents;

import jade.core.Agent;
import java.util.ArrayList;

/**
 *
 * @author Felipe V Nambara
 */
public class Car extends Agent {

    int current = 0;
    int[][] graphRoute;
    int[][] graphVelocity;
    ArrayList<Pair> pairs = new ArrayList<Pair>(); // route done by car
    
    @Override
    protected void setup() {

        super.setup();

        Object[] args = getArguments();

        if (args != null && args.length > 1) {

            this.graphRoute = (int[][]) args[0];
            
            this.graphVelocity = (int[][]) args[1];

        }
        
    }

    ArrayList<Integer> getNeighbours() {

        ArrayList<Integer> neighbours = new ArrayList<Integer>();

        for (int i = 0; i < graphRoute[current].length; i++) {

            if (graphRoute[current][i] == 1) {

                neighbours.add(i);

            }

        }

        return neighbours;
    }
    
    
    void move(int start, int end){                
        
        Pair pair = new Pair(start, end, calculateInterval(start, end));
        
        pairs.add(pair);
        
        this.current = end;
        
    }
    
    private Double calculateInterval(int start, int end){
        Double interval;
        
        interval = ( (Integer) this.graphRoute[start][end] ).doubleValue() / ( (Integer) this.graphVelocity[start][end] ).doubleValue();
        
        return interval;
    }
}
