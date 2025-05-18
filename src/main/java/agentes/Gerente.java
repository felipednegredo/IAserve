package agentes;

import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;

public class Gerente extends Agent {
    @Override
    protected void setup() {
        System.out.println("Gerente iniciado.");

        SequentialBehaviour rotina = new SequentialBehaviour();

        // Abertura do restaurante após 2 segundos
        rotina.addSubBehaviour(new WakerBehaviour(this, 2000) {
            protected void onWake() {
                String hora = java.time.LocalTime.now().toString();
                System.out.println("[" + hora + "] Restaurante ABERTO!");

                // Envia mensagem de abertura para todos os agentes relevantes
                String[] agentes = {"garcom", "cliente", "chef"};
                for (String nome : agentes) {
                    ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                    msg.addReceiver(getAID(nome));
                    msg.setContent("restaurante aberto");
                    send(msg);
                }
            }
        });

        // Comportamento para recepcionar clientes enquanto o restaurante está aberto
        rotina.addSubBehaviour(new CyclicBehaviour() {
            public void action() {
                ACLMessage msg = receive();
                if (msg != null && "cheguei".equals(msg.getContent())) {
                    ACLMessage reply = msg.createReply();
                    reply.setContent("sente-se por favor");
                    send(reply);
                    System.out.println("Gerente recepcionou cliente.");
                } else {
                    block();
                }
            }
        });

        // Fechamento do restaurante após 20 segundos
        rotina.addSubBehaviour(new WakerBehaviour(this, 20000) {
            protected void onWake() {
                String hora = java.time.LocalTime.now().toString();
                System.out.println("[" + hora + "] Restaurante FECHADO!");
                doDelete();
            }
        });

        addBehaviour(rotina);
    }

    @Override
    protected void takeDown() {
        System.out.println("Gerente encerrando expediente.");
    }
}