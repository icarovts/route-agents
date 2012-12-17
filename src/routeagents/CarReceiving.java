/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package routeagents;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

/**
 *
 * @author Felipe V Nambara
 */
public class CarReceiving extends CyclicBehaviour {

    Car a;

    public CarReceiving(Car a) {
        super(a);
        this.a = a;
    }

    @Override
    public void action() {
        String[] parse;
        ACLMessage rec = this.a.receive();

        if (rec != null) {

            System.out.println("sou o agente " + this.a.getAID().getLocalName() + " e recebi a solicitacao do agente " + rec.getSender().getLocalName());

            parse = rec.getContent().split("\n");

            if (this.a.pairs.size() > 0 && parse[0].equals("01")) {

                String[] pair;
                StringBuffer buffer = new StringBuffer();

                buffer.append("02\n");

                for (int i = 1; i < parse.length; i++) {

                    pair = parse[i].split(";");

                    for (Pair p : this.a.pairs) {

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
                    
                    System.out.println("sou o agente " + this.a.getAID().getLocalName() + " e vou enviar a resposta para o agente " + rec.getSender().getLocalName());
                    
                    ACLMessage response = new ACLMessage(ACLMessage.REQUEST);

                    response.addReceiver(new AID(rec.getSender().getLocalName(), AID.ISLOCALNAME));

                    response.setContent(buffer.toString());

                    this.a.send(response);

                }
            }


        }

        this.block();
    }
}
