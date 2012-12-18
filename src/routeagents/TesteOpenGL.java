// http://www.lwjgl.org/download.php
// Propriedades > Executar > Opções de VM
// -Djava.library.path=/Users/icarovts/Downloads/lwjgl-2.8.5/native/macosx

package routeagents;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.Display;

/**
 * Basic testeopengl
 *
 * @author Name <email>
 * @version 1.0
 */
class TesteOpenGL {

    /**
     * Game title
     */
    public static final String GAME_TITLE = "My Game";
    /**
     * Desired frame time
     */
    private static final int FRAMERATE = 190;
    /**
     * Exit the game
     */
    private static boolean finished;
    /**
     * Angle of rotating square
     */
    private static float angle;

    /**
     * Application init
     *
     * @param args Commandline args
     */
    public static void main(String[] args) {
        boolean fullscreen = (args.length == 1 && args[0].equals("-fullscreen"));
        
        try {
            init(fullscreen);
            run();
        } catch (Exception e) {
            e.printStackTrace(System.err);
            
        } finally {
            cleanup();
        }
        System.exit(0);
    }

    /**
     * Initialise the game
     *
     * @throws Exception if init fails
     */
    private static void init(boolean fullscreen) throws Exception {
        // Create a fullscreen window with 1:1 orthographic 2D projection (default)
        Display.setTitle(GAME_TITLE);
        Display.setFullscreen(fullscreen);

        // Enable vsync if we can (due to how OpenGL works, it cannot be guarenteed to always work)
        Display.setVSyncEnabled(true);

        // Create default display of 640x480
        Display.create();
    }

    /**
     * Runs the game (the "main loop")
     */
    private static void run() {
        
        while (!finished) {
            // Always call Window.update(), all the time - it does some behind the
            // scenes work, and also displays the rendered output
            Display.update();

            // Check for close requests
            if (Display.isCloseRequested()) {
                finished = true;
            } // The window is in the foreground, so we should play the game
            else if (Display.isActive()) {
                logic();
                render();
                Display.sync(FRAMERATE);
            } // The window is not in the foreground, so we can allow other stuff to run and
            // infrequently update
            else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
                logic();

                // Only bother rendering if the window is visible or dirty
                if (Display.isVisible() || Display.isDirty()) {
                    render();
                }
            }
        }
    }

    /**
     * Do any game-specific cleanup
     */
    private static void cleanup() {
        // Close the window
        Display.destroy();
    }

    /**
     * Do all calculations, handle input, etc.
     */
    private static void logic() {
        // Example input handler: we'll check for the ESC key and finish the game instantly when it's pressed
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            finished = true;
        }

        // Rotate the square
        angle += 6;
    }

    /**
     * Render the current frame
     */
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

        
        
        
        // Retas
        GL11.glBegin(GL11.GL_LINES);
        GL11.glColor3f(5.0f, 5.0f, 1.0f);
        GL11.glVertex2f(0, 300);
        GL11.glVertex2f(0, 0);
       
        GL11.glVertex2f(0, 300);
        GL11.glVertex2f(300, 0);
        
        GL11.glVertex2f(0, 300);
        GL11.glVertex2f(-300, 0);
        
        GL11.glVertex2f(-300, 0);
        GL11.glVertex2f(0, 0);
        
        GL11.glVertex2f(300, 0);
        GL11.glVertex2f(0, 0);
        
        GL11.glVertex2f(-300, 0);
        GL11.glVertex2f(0, -300);
        
        GL11.glVertex2f(0, 0);
        GL11.glVertex2f(0, -300);
        
        GL11.glVertex2f(300, 0);
        GL11.glVertex2f(0, -300);
        
        GL11.glEnd();
        
        
        // Carro
        GL11.glPointSize(10);
//        GL11.glRotatef(angle, 0.0f, 0.0f, 1.0f);
        GL11.glBegin(GL11.GL_POINTS);
        GL11.glColor3f(0.0f, 0.0f, 1.0f);
        GL11.glVertex2f(0, angle);
        GL11.glEnd();
      
        
        GL11.glPopMatrix();
    }
}
