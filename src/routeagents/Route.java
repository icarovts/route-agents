/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package routeagents;

/**
 *
 * @author icarovts
 */
public class Route {

    private int startX;
    private int startY;
    private int endX;
    private int endY;
    private Double avarageVelocity;

    public Route(int startX, int startY, int endX, int endY, Double avarageVelocity) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.avarageVelocity = avarageVelocity;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getEndX() {
        return endX;
    }

    public int getEndY() {
        return endY;
    }

    public Double getAvarageVelocity() {
        return avarageVelocity;
    }

    public Double getLength() {
        
        double dsx = Math.abs((double) startX);
        double dsy = Math.abs((double) startY);
        double dex = Math.abs((double) endX);
        double dey = Math.abs((double) endY);

        double distX = dex - dsx;
        double distY = dey - dsy;
        
        return Math.sqrt(distX * distX + distY * distY);
    }
}
