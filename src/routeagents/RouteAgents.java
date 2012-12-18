/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package routeagents;

import jade.core.Agent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author icarovts
 */
public class RouteAgents {

    /**
     * @param args the command line arguments
     */
    public static CopyOnWriteArrayList<Car> agents = new CopyOnWriteArrayList<Car>();
    public static Route[][] graphRoute = setGraphRoute();

    public static void main(String[] args) throws StaleProxyException {

        // Init JADE configs and some agents
        initJADE();

    }

    private static void initJADE() throws StaleProxyException {
        // JADE configs...
        jade.core.Runtime runtime = jade.core.Runtime.instance();

        Profile profile = new ProfileImpl();
        profile.setParameter(Profile.MAIN_HOST, "127.0.0.1");
        profile.setParameter(Profile.MAIN_PORT, "1099");

        // Init some car agents
        ContainerController containerController = runtime.createMainContainer(profile);

        Simulation simulation = new Simulation();
        Thread tSim = new Thread(simulation);
        tSim.start();
        
        for (int i = 0; i < 200; i++) {

            AgentController a = containerController.createNewAgent("car" + i, Car.class.getName(), null);

            a.start();

//            while (a.getState().getCode() != 3) {

            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(RouteAgents.class.getName()).log(Level.SEVERE, null, ex);
            }

//            }

        }

        // initialize graphical simulation after processing jade agents
        

        containerController.kill();
        runtime.setCloseVM(true);


    }

    public static Route[][] setGraphRoute() {

        Route[][] graphRoute = new Route[5][5];

        graphRoute[0][1] = new Route(0, 300, 0, 0, 1.0);
        graphRoute[0][2] = new Route(0, 300, 300, 0, 3.0);
        graphRoute[0][3] = new Route(0, 300, -300, 0, 100.0);
        graphRoute[3][1] = new Route(-300, 0, 0, 0, 60.0);
        graphRoute[2][1] = new Route(300, 0, 0, 0, 1.0);
        graphRoute[3][4] = new Route(-300, 0, 0, -300, 40.0);
        graphRoute[1][4] = new Route(0, 0, 0, -300, 10.0);
        graphRoute[2][4] = new Route(300, 0, 0, -300, 5.0);

        return graphRoute;

    }
}
