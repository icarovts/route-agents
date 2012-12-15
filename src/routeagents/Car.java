/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package routeagents;

import jade.content.OntoACLMessage;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Felipe V Nambara
 */
public class Car extends Agent {

    int current = 0;
    boolean wait = false;
    ArrayList<Pair> pairs = new ArrayList<Pair>(); // route done by car
    ArrayList<Pair> ways = new ArrayList<Pair>();

    @Override
    protected void setup() {

        super.setup();

        RouteAgents.agents.add(this);

        CarBehavior carBehavior = new CarBehavior(this);
        // aciiona o comportamento ciclico para ficar recebendo as solicitações dos demais agentes
        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                String[] parse;
                ACLMessage rec = receive();

                if (rec != null) {

                    //System.out.println("sou o agente " + getAID().getLocalName() + " e recebi a solicitacao do agente " + rec.getSender().getLocalName());

                    parse = rec.getContent().split("\n");

                    if (pairs.size() > 0 && parse[0].equals("01")) {

                        String[] pair;
                        StringBuffer buffer = new StringBuffer();

                        buffer.append("02\n");

                        for (int i = 1; i < parse.length; i++) {

                            pair = parse[i].split(";");

                            for (Pair p : pairs) {

                                if (p.getStart() == Integer.valueOf(pair[0]) && p.getEnd() == Integer.valueOf(pair[1])) {

                                    buffer.append(pair[0]);
                                    buffer.append(";");
                                    buffer.append(pair[1]);
                                    buffer.append(";");
                                    buffer.append(p.getTime());
                                    buffer.append(";");
                                    buffer.append(p.getInterval());
                                    buffer.append("\n");

                                }

                            }

                        }


                        if (!buffer.toString().isEmpty()) {

                            ACLMessage response = new OntoACLMessage(ACLMessage.INFORM);

                            response.addReceiver(new AID(rec.getSender().getLocalName(), AID.ISLOCALNAME));

                            response.setContent(buffer.toString());

                            send(response);

                        }
                    }

                    if (parse[0].equals("02")) {

                        for (int i = 1; i < parse.length; i++) {

                            String[] pair = parse[i].split(";");

                            Pair p = new Pair(Integer.parseInt(pair[0]), Integer.parseInt(pair[1]), Double.parseDouble(pair[2]));

                            //System.out.println("sou o agente " + getAID().getLocalName() + " e recebi a resposta do agente " + rec.getSender().getLocalName());                            
                            
                            ways.add(p);


                        }

                    }

                }

                block();

            }
        });

        addBehaviour(carBehavior);

    }

    void startWay() {

        while (this.current != RouteAgents.graphRoute.length - 1) {

            ArrayList<Integer> neibhgours = getNeighbours();

            StringBuffer message = new StringBuffer();

            message.append("01\n");

            for (int n : neibhgours) {

                message.append(this.current);
                message.append(";");
                message.append(n);
                message.append(";");
                message.append("0");
                message.append(";");
                message.append("0");
                message.append("\n");

            }

            ways.removeAll(null);


            for (Agent a : RouteAgents.agents) {

                if (!a.getAID().getLocalName().equals(this.getAID().getLocalName())) {

                    ACLMessage msg = new OntoACLMessage(ACLMessage.INFORM);

                    msg.addReceiver(new AID(a.getLocalName(), AID.ISLOCALNAME));

                    msg.setContent(message.toString());

                    this.send(msg);

                }

            }

            boolean withoutOptions = true;

            int loops = 0;

            while (ways.size() == 0 && loops <= 10) {


                for (Pair p : ways) {
                    withoutOptions = withoutOptions && neibhgours.indexOf(p.getEnd()) == -1;
                }

                System.out.println("agente " + this.getAID().getLocalName() + " aguardando resposta...");

                if (ways.size() > 0) {
                    System.out.println("agente " + this.getAID().getLocalName() + " encontrou outros agentes que fizeram este caminho !!!!!!!!!!!!!!!!!!!!!!!!!");
                }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Car.class.getName()).log(Level.SEVERE, null, ex);
                }

                loops++;
            }

            double[] prob = new double[neibhgours.size()];

            if (withoutOptions) {

                for (int i = 0; i < prob.length; i++) {

                    prob[i] = 100.00 / prob.length;

                }
            }

            int v = 0;

            double x = Math.random() * 100;

            double y = 0;

            for (int i = 0; i < prob.length; i++) {

                y += prob[i];

                if (x < y) {

                    v = i;

                    break;

                }

            }

            System.out.println("agente " + this.getAID().getLocalName() + " saiu do vértice " + this.current + " para o " + neibhgours.get(v));

            moveTo(this.current, neibhgours.get(v));
           
        }

        System.out.println("agente " + this.getAID().getLocalName() + " finalizando caminho" );
    }

    ArrayList<Integer> getNeighbours() {

        ArrayList<Integer> neighbours = new ArrayList<Integer>();

        for (int i = 0; i < RouteAgents.graphRoute[current].length; i++) {

            if (RouteAgents.graphRoute[current][i] == 1) {

                neighbours.add(i);

            }

        }

        return neighbours;
    }

    void moveTo(int start, int end) {

        Pair pair = new Pair(start, end, calculateInterval(start, end));

        pairs.add(pair);

        this.current = end;

    }

    private Double calculateInterval(int start, int end) {
        Double interval;

        interval = RouteAgents.graphRoute[start][end] / RouteAgents.graphVelocity[start][end];

        return interval;
    }
}
