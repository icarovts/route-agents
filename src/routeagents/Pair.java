/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package routeagents;

/**
 *
 * @author Felipe V Nambara
 */
public class Pair {
    private int start;
    private int end;
    private long time;
    private Double interval;

    public Pair(int start, int end, Double interval) {
        this.start = start;
        this.end = end;
        this.interval = interval;
        this.time = System.currentTimeMillis();
        
    }        

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Double getInterval() {
        return interval;
    }

    public void setInterval(Double interval) {
        this.interval = interval;
    }
     
    
}
