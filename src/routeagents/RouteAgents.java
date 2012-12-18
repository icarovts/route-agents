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
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

/**
 *
 * @author icarovts
 */
public class RouteAgents {

    /**
     * @param args the command line arguments
     */
    public static CopyOnWriteArrayList<Agent> agents = new CopyOnWriteArrayList<Agent>();
    public static Route[][] graphRoute = setGraphRoute();

    public static void main(String[] args) throws StaleProxyException {

        // Build a graphic graphRoute
        try {
            initDisplay(true);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            cleanup();
        }
        
        // Init JADE configs and some agents
        initJADE();
        
    }

    private static void initDisplay(boolean fullscreen) throws LWJGLException {
        // Create a fullscreen window with 1:1 orthographic 2D projection (default)
        Display.setTitle("Route Agents - Ícaro & Felipe");
        Display.setFullscreen(fullscreen);

        // Enable vsync if we can (due to how OpenGL works, it cannot be guarenteed to always work)
        Display.setVSyncEnabled(true);

        Display.create();
    }

    private static void cleanup() {
        // Close the window
        Display.destroy();
    }

    private static void initJADE() throws StaleProxyException {
        // JADE configs...
        jade.core.Runtime runtime = jade.core.Runtime.instance();

        Profile profile = new ProfileImpl();
        profile.setParameter(Profile.MAIN_HOST, "127.0.0.1");
        profile.setParameter(Profile.MAIN_PORT, "1099");

        // Init some car agents
        ContainerController containerController = runtime.createMainContainer(profile);

        for (int i = 0; i < 10; i++) {

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

    public static Route[][] setGraphRoute() {

        Route[][] graphRoute = new Route[5][5];

        graphRoute[0][1] = new Route(0, 300, 0, 0, 5.0 + (Math.random() * 20));
        graphRoute[0][2] = new Route(0, 300, 300, 0, 5.0 + (Math.random() * 20));
        graphRoute[0][3] = new Route(0, 300, -300, 0, 5.0 + (Math.random() * 20));
        graphRoute[3][1] = new Route(-300, 0, 0, 0, 5.0 + (Math.random() * 20));
        graphRoute[2][1] = new Route(300, 0, 0, 0, 5.0 + (Math.random() * 20));
        graphRoute[3][4] = new Route(-300, 0, 0, -300, 5.0 + (Math.random() * 20));
        graphRoute[1][4] = new Route(0, 0, 0, -300, 5.0 + (Math.random() * 20));
        graphRoute[2][4] = new Route(300, 0, 0, -300, 5.0 + (Math.random() * 20));

        return graphRoute;

    }
}
