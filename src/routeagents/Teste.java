/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package routeagents;

import jade.content.OntoACLMessage;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Felipe V Nambara
 */
public class Teste implements Runnable {

    Agent t;

    public Teste(Agent t) {
        this.t = t;
    }

    @Override
    public void run() {

        while (true) {

            System.out.println("thread");

            ACLMessage msg = new OntoACLMessage(ACLMessage.INFORM);

            String nomeAgente;

            if (t.getAID().getName().equals("car1")) {
                nomeAgente = "car2";
            } else {
                nomeAgente = "car1";
            }

            System.out.println(nomeAgente);
                    
            msg.addReceiver(new AID(nomeAgente, AID.ISLOCALNAME));
            msg.setContent("projeto sma");
            this.t.send(msg);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Teste.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }
}
