/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package routeagents;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Hashtable;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author Felipe V Nambara
 */
public class Car extends Agent {

    int current = 0;
    int simX;
    int simY;
    boolean wait = false;
    ArrayList<Pair> pairs = new ArrayList<Pair>(); // route done by car
    double totalInterval = 0;
    CarReceiving carrec;
    CarBehavior carbeh;

    @Override
    protected void setup() {

        super.setup();

        RouteAgents.agents.add(this);

        this.carrec = new CarReceiving(this);

        this.carbeh = new CarBehavior(this);

        addBehaviour(carrec);

        addBehaviour(carbeh);


    }

    void startWay() {

        while (this.current != RouteAgents.graphRoute.length - 1) {

            ArrayList<Integer> neighbors = getNeighbours();

            //this.carrec.block();

            askForWays(neighbors);

            ArrayList<Pair> ways = receiveWays();
           

            Hashtable prob = setProbabilities(neighbors, ways);

            int v = chooseRoute(neighbors, prob);

            System.out.println("agente " + this.getAID().getLocalName() + " saiu do vértice " + this.current + " para o " + v);

            moveTo(this.current, v);

        }

        System.err.println("agente " + this.getAID().getLocalName() + " finalizando caminho (" + this.totalInterval + " segundos)");
    }

    ArrayList<Integer> getNeighbours() {

        ArrayList<Integer> neighbours = new ArrayList<Integer>();

        for (int i = 0; i < RouteAgents.graphRoute[current].length; i++) {

            if (RouteAgents.graphRoute[current][i] != null) {

                neighbours.add(i);

            }

        }

        return neighbours;
    }

    void moveTo(int start, int end) {

        System.out.println("sou o agente " + this.getAID().getLocalName() + " e estou percorrendo o caminho de " + start + " para " + end + "...");

        double interval = calculateInterval(start, end);

        Route route = RouteAgents.graphRoute[start][end];
        int x = route.getStartX();
        int y = route.getStartY();
        int x2 = route.getEndX();
        int y2 = route.getEndY();

        double velocity = route.getAvarageVelocity();
        Pair pair = new Pair(start, end, interval);

        pairs.add(pair);

        while (x != x2 || y != y2) {

            try {
                Thread.sleep(70);
            } catch (InterruptedException ex) {
                Logger.getLogger(Car.class.getName()).log(Level.SEVERE, null, ex);
            }            
            
            int toIterate = ((int) velocity / 10);

            if (toIterate == 0) {
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

            this.simX = x;
            this.simY = y;



        }



        this.totalInterval += interval;


        this.current = end;

    }

    private Double calculateInterval(int start, int end) {
        Double interval;

        Route route = RouteAgents.graphRoute[start][end];

        interval = route.getLength() / route.getAvarageVelocity();

        return interval;
    }

    private Hashtable setProbabilities(ArrayList<Integer> neighbors, ArrayList<Pair> ways) {

        Hashtable prob = new Hashtable();

        boolean hasOptions = true;
        ArrayList<Pair> correctWays = new ArrayList<Pair>();

        for (int i : neighbors) {

            boolean hasOneOption = false;

            for (Pair p : ways) {

                if (i == p.getEnd()) {

                    correctWays.add(p);
                    hasOneOption = true;

                }

            }

            hasOptions = hasOneOption && hasOptions;

        }


        // Probabilities are the same if doesn't have options 
        // or are proportional to the routes's intervals
        if (hasOptions) {
            
            double inverseTotal = 0;
            Hashtable inverses = new Hashtable();

            for (int n : neighbors) {

                double totalInterval = 0;
                int count = 0;

                for (Pair pair : correctWays) {

                    if (pair.getEnd() == n) {
                        totalInterval += pair.getInterval();
                        count++;
                    }

                }

                double avarageInterval = totalInterval / count;

                double inverse = 1 / avarageInterval;
                inverses.put(n, inverse);
                inverseTotal += inverse;

            }

            for (int n : neighbors) {

                double p = ((Double) inverses.get(n)) * 100.00 / inverseTotal;
                prob.put(n, p);

            }
           

        } else {

            for (int n : neighbors) {


                prob.put(n, 100.00 / neighbors.size());

            }

        }

        return prob;
    }

    // Heuristic route choice based on the probabilities
    private int chooseRoute(ArrayList<Integer> neighbors, Hashtable prob) {
        int v = 0;
        double x = Math.random() * 100;
        double y = 0;

        for (int n : neighbors) {

            y += (Double) prob.get(n);
            if (x < y) {
                v = n;
                break;
            }

            System.out.println("agente " + this.getAID().getLocalName() + " finalizando caminho");

        }

        return v;

    }

    private ArrayList<Pair> receiveWays() {
        ArrayList<Pair> ways = new ArrayList<Pair>();

        String[] parse;
        ACLMessage rec;

        while ((rec = receive()) != null) {

            if (rec != null) {

                parse = rec.getContent().split("\n");

                if (parse[0].equals("02")) {

                    for (int i = 1; i < parse.length; i++) {

                        String[] pair = parse[i].split(";");

                        Pair p = new Pair(Integer.parseInt(pair[0]), Integer.parseInt(pair[1]), Double.parseDouble(pair[2]));

                        ways.add(p);
                        
                        //System.out.println("entrou " + p.getEnd());

                    }

                }

            }

            System.out.println("sou o agente " + this.getAID().getLocalName() + " e estou aguardando a resposta...");

        }

        return ways;
    }

    private void askForWays(ArrayList<Integer> neighbors) {
        // Creating message to ask for route options
        StringBuffer message = new StringBuffer();

        message.append("01\n");

        for (int n : neighbors) {

            message.append(this.current);
            message.append(";");
            message.append(n);
            message.append(";");
            message.append("0");
            message.append(";");
            message.append("0");
            message.append("\n");

        }

        // Ask antoher agents for options
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);

        for (Agent a : RouteAgents.agents) {

            if (!a.getAID().getLocalName().equals(this.getAID().getLocalName())) {

                msg.addReceiver(new AID(a.getLocalName(), AID.ISLOCALNAME));

            }

        }

        msg.setContent(message.toString());

        this.send(msg);
    }

    public void setSimX(int simX) {
        this.simX = simX;
    }

    public void setSimY(int simY) {
        this.simY = simY;
    }
}
