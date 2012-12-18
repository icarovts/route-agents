package routeagents;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

class Simulation {

    public static final int FRAMERATE = 60;

    void start() {

        // Build a graphic graphRoute
        try {
            initDisplay(false);
            runGraph();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            cleanup();
        }


    }

    private static void initDisplay(boolean fullscreen) throws LWJGLException {
        // Create a fullscreen window with 1:1 orthographic 2D projection (default)
        Display.setTitle("Route Agents - Ícaro & Felipe");
        Display.setFullscreen(fullscreen);

        // Enable vsync if we can (due to how OpenGL works, it cannot be guarenteed to always work)
        Display.setVSyncEnabled(true);

        Display.create();
    }

    private static void runGraph() {

        for (Car car : RouteAgents.agents) {

            for (Pair pair : car.pairs) {

                int x = RouteAgents.graphRoute[pair.getStart()][pair.getEnd()].getStartX();
                int y = RouteAgents.graphRoute[pair.getStart()][pair.getEnd()].getStartY();
                int x2 = RouteAgents.graphRoute[pair.getStart()][pair.getEnd()].getEndX();
                int y2 = RouteAgents.graphRoute[pair.getStart()][pair.getEnd()].getEndY();

                double velocity = RouteAgents.graphRoute[pair.getStart()][pair.getEnd()].getAvarageVelocity();

                while (x != x2 || y != y2) {                   
                            
                    int toIterate = ((int) velocity/10) ;
                                                            
                    if(toIterate == 0){
                        toIterate = 1;
                    }
                    
                    if (x < x2) {
                        x += toIterate;
                    } else {
                        if (x > x2) {
                            x -= toIterate;
                        }
                    }

                    if (y < y2) {
                        y += toIterate;
                    } else {
                        if (y > y2) {
                            y -= toIterate;
                        }
                    }

                    render(x, y);

                    Display.update();

                }
            }

            Display.sync(FRAMERATE);
        }

    }

    private static void render(int x, int y) {

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, Display.getDisplayMode().getWidth(), 0, Display.getDisplayMode().getHeight(), -1, 1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);

        // clear the screen
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_STENCIL_BUFFER_BIT);

        // center square according to screen size
        GL11.glPushMatrix();
        GL11.glTranslatef(Display.getDisplayMode().getWidth() / 2, Display.getDisplayMode().getHeight() / 2, 0.0f);

        // Routes
        GL11.glBegin(GL11.GL_LINES);
        GL11.glColor3f(5.0f, 5.0f, 1.0f);

        for (int i = 0; i < RouteAgents.graphRoute.length; i++) {

            for (int j = 0; j < RouteAgents.graphRoute[i].length; j++) {

                Route route = RouteAgents.graphRoute[i][j];

                if (route != null) {

                    GL11.glVertex2f(route.getStartX(), route.getStartY());
                    GL11.glVertex2f(route.getEndX(), route.getEndY());

                }

            }
        }


        GL11.glEnd();

        // Carro
        GL11.glPointSize(40);
        GL11.glBegin(GL11.GL_POINTS);
        GL11.glPopName();
        GL11.glColor3f(0.0f, 0.0f, 1.0f);
        GL11.glVertex2f(x, y);
        GL11.glEnd();


        GL11.glPopMatrix();
    }

    private static void cleanup() {
        // Close the window
        Display.destroy();
    }
}
