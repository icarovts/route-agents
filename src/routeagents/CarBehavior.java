/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package routeagents;

import jade.core.behaviours.SimpleBehaviour;

/**
 *
 * @author Felipe V Nambara
 */
public class CarBehavior extends SimpleBehaviour{
    boolean finished = false;
    Car car;
    
    public CarBehavior(Car car) {
        
        super();
            
        this.car = car;
        
    }
        
    @Override
    public void action() {
              
        this.car.startWay();
        
        this.finished = true;

        
    }

    @Override
    public boolean done() {
        return this.finished;
    }       
    
}
