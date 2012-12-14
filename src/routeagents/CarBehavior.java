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

        System.out.println("finaliznado");
        this.finished = false;

        
    }

    @Override
    public boolean done() {
        return this.finished;
    }       
    
}
