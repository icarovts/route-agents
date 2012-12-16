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
    public static CopyOnWriteArrayList<Agent> agents = new CopyOnWriteArrayList<Agent>();
    public static double[][] graphRoute = setGraphRoute();
    public static double[][] graphVelocity = setGraphVelocity(graphRoute);

    public static void main(String[] args) throws StaleProxyException {

        jade.core.Runtime runtime = jade.core.Runtime.instance();

        Profile profile = new ProfileImpl();
        profile.setParameter(Profile.MAIN_HOST, "127.0.0.1");
        profile.setParameter(Profile.MAIN_PORT, "1099");

        ContainerController containerController = runtime.createMainContainer(profile);


        for (int i = 0; i < 500; i++) {

            AgentController a = containerController.createNewAgent("car" + i, Car.class.getName(), null);
            a.start();

            while (a.getState().getCode() != 3) {

                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(RouteAgents.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }


    }

    public static double[][] setGraphVelocity(double[][] graphRoute) {

        double[][] graphVelocity = new double[graphRoute.length][graphRoute.length];

        for (int i = 0; i < graphRoute.length; i++) {

            for (int j = 0; j < graphRoute[i].length; j++) {

                if (graphRoute[i][j] > 0) {
                    graphVelocity[i][j] = 5.0 + (Math.random() * 20);
                }

            }

        }
        return graphVelocity;

    }

    public static double[][] setGraphRoute() {

        double[][] graphRoute = new double[4][4];
        
        graphRoute[0][0] = 0;
        graphRoute[0][1] = 100;
        graphRoute[0][2] = 100;
        graphRoute[0][3] = 100;
        
        graphRoute[1][0] = 0;
        graphRoute[1][1] = 0;
        graphRoute[1][2] = 100;
        graphRoute[1][3] = 100;
        
        graphRoute[2][0] = 0;
        graphRoute[2][1] = 100;
        graphRoute[2][2] = 0;
        graphRoute[2][3] = 100;
        
        graphRoute[3][0] = 0;
        graphRoute[3][1] = 0;
        graphRoute[3][2] = 0;
        graphRoute[3][3] = 0;

//        graphRoute[0][0] = 0;
//        graphRoute[0][1] = 300;
//        graphRoute[0][2] = 200;
//        graphRoute[0][3] = 0;
//        graphRoute[0][4] = 250;
//        graphRoute[0][5] = 0;
//        graphRoute[0][6] = 0;
//        graphRoute[0][7] = 0;
//        graphRoute[0][8] = 0;
//
//        graphRoute[1][0] = 0;
//        graphRoute[1][1] = 0;
//        graphRoute[1][2] = 100;
//        graphRoute[1][3] = 600;
//        graphRoute[1][4] = 30;
//        graphRoute[1][5] = 0;
//        graphRoute[1][6] = 100;
//        graphRoute[1][7] = 0;
//        graphRoute[1][8] = 0;
//
//        graphRoute[2][0] = 0;
//        graphRoute[2][1] = 0;
//        graphRoute[2][2] = 0;
//        graphRoute[2][3] = 0;
//        graphRoute[2][4] = 800;
//        graphRoute[2][5] = 1000;
//        graphRoute[2][6] = 0;
//        graphRoute[2][7] = 300;
//        graphRoute[2][8] = 0;
//
//        graphRoute[3][0] = 0;
//        graphRoute[3][1] = 0;
//        graphRoute[3][2] = 0;
//        graphRoute[3][3] = 0;
//        graphRoute[3][4] = 20;
//        graphRoute[3][5] = 0;
//        graphRoute[3][6] = 200;
//        graphRoute[3][7] = 0;
//        graphRoute[3][8] = 0;
//
//        graphRoute[4][0] = 0;
//        graphRoute[4][1] = 0;
//        graphRoute[4][2] = 0;
//        graphRoute[4][3] = 0;
//        graphRoute[4][4] = 0;
//        graphRoute[4][5] = 400;
//        graphRoute[4][6] = 800;
//        graphRoute[4][7] = 900;
//        graphRoute[4][8] = 1000;
//
//        graphRoute[5][0] = 0;
//        graphRoute[5][1] = 0;
//        graphRoute[5][2] = 0;
//        graphRoute[5][3] = 0;
//        graphRoute[5][4] = 0;
//        graphRoute[5][5] = 0;
//        graphRoute[5][6] = 0;
//        graphRoute[5][7] = 20000;
//        graphRoute[5][8] = 0;
//
//        graphRoute[6][0] = 0;
//        graphRoute[6][1] = 0;
//        graphRoute[6][2] = 0;
//        graphRoute[6][3] = 0;
//        graphRoute[6][4] = 0;
//        graphRoute[6][5] = 0;
//        graphRoute[6][6] = 0;
//        graphRoute[6][7] = 200;
//        graphRoute[6][8] = 700;
//
//        graphRoute[7][0] = 0;
//        graphRoute[7][1] = 0;
//        graphRoute[7][2] = 0;
//        graphRoute[7][3] = 0;
//        graphRoute[7][4] = 0;
//        graphRoute[7][5] = 0;
//        graphRoute[7][6] = 200;
//        graphRoute[7][7] = 0;
//        graphRoute[7][8] = 500;
//
//        graphRoute[8][0] = 0;
//        graphRoute[8][1] = 0;
//        graphRoute[8][2] = 0;
//        graphRoute[8][3] = 0;
//        graphRoute[8][4] = 0;
//        graphRoute[8][5] = 0;
//        graphRoute[8][6] = 0;
//        graphRoute[8][7] = 0;
//        graphRoute[8][8] = 0;

        return graphRoute;

    }
}
