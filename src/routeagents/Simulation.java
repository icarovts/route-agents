package routeagents;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

class Simulation implements Runnable{

    @Override
    public void run() {
        start();
    }

    public static final int FRAMERATE = 60;
    public static boolean finished = false;

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

        while (!finished) {
            // Always call Window.update(), all the time - it does some behind the
            // scenes work, and also displays the rendered output


            // Check for close requests
            if (Display.isCloseRequested()) {
                finished = true;
            } // The window is in the foreground, so we should play the game
            else if (Display.isActive()) {

                render();
                Display.update();
                Display.sync(FRAMERATE);
            } // The window is not in the foreground, so we can allow other stuff to run and
            // infrequently update
            else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }


                // Only bother rendering if the window is visible or dirty
                if (Display.isVisible() || Display.isDirty()) {
                    
                    render();
                    Display.update();
                }
            }
        }

    }


    private static void render() {

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
        GL11.glPointSize(10);
        GL11.glBegin(GL11.GL_POINTS);

        for (Car car : RouteAgents.agents) {
            if(car.simX != 0 || car.simY != 0){
                GL11.glColor3f(0.0f, 0.0f, 1.0f);
                GL11.glVertex2f(car.simX, car.simY);
            }
        }
        GL11.glEnd();
        GL11.glPopMatrix();
    }

    private static void cleanup() {
        // Close the window
        Display.destroy();
    }
}
