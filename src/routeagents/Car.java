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

                    parse = rec.getContent().split("\n");

                    if (parse[0].equals("01")) {

                        String[] pair;
                        StringBuffer buffer = new StringBuffer();

                        for (int i = 1; i < parse.length; i++) {

                            pair = parse[i].split(";");

                            buffer.append("02\n");

                            for (Pair p : pairs) {

                                if (p.getStart() == Integer.valueOf(pair[0]) && p.getEnd() == Integer.valueOf(pair[1])) {

                                    buffer.append("{");
                                    buffer.append(pair[0]);
                                    buffer.append(";");
                                    buffer.append(pair[1]);
                                    buffer.append(";");
                                    buffer.append(p.getTime());
                                    buffer.append(";");
                                    buffer.append(p.getInterval());
                                    buffer.append("}");
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

                            ways.add(new Pair(Integer.parseInt(pair[0]), Integer.parseInt(pair[1]), Double.parseDouble(pair[2])));

                        }

                    }

                }

                block();

            }
        });

        addBehaviour(carBehavior);

    }

    void startWay() {

        ArrayList<Integer> neibhgours = getNeighbours();

        StringBuffer message = new StringBuffer();

        message.append("01\n");

        for (int n : neibhgours) {

            message.append("{");
            message.append(this.current);
            message.append(";");
            message.append(n);
            message.append(";");
            message.append("0");
            message.append(";");
            message.append("0");
            message.append("}");
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

//        while (true) {
//
//            if (!ways.isEmpty()) {
//
//                for (Pair p : ways) {
//                    System.out.println(p.toString());
//                }
//
//                break;
//                
//            }
//
//        }

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

        interval = 0.0; //((Integer) RouteAgents.graphRoute[start][end]).doubleValue() / ((Integer)RouteAgents.graphVelocity[start][end]).doubleValue();

        return interval;
    }
}
