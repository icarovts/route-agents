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
}
