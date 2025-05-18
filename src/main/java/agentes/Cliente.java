package agentes;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class Cliente extends Agent {
    private boolean restauranteAberto = false;

    @Override
    protected void setup() {
        System.out.println("Cliente iniciado.");

        addBehaviour(new CyclicBehaviour() {
            public void action() {
                ACLMessage msg = receive();
                if (msg != null && "restaurante aberto".equals(msg.getContent())) {
                    restauranteAberto = true;
                    System.out.println("[Cliente] Restaurante aberto, iniciando atividades.");

                    SequentialBehaviour seq = new SequentialBehaviour();

                    seq.addSubBehaviour(new OneShotBehaviour() {
                        public void action() {
                            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                            msg.addReceiver(getAID("gerente"));
                            msg.setContent("cheguei");
                            send(msg);
                            block(1000);
                        }
                    });

                    seq.addSubBehaviour(new OneShotBehaviour() {
                        public void action() {
                            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                            msg.addReceiver(getAID("garcom"));
                            msg.setContent("fazer pedido");
                            send(msg);
                            block(2000);
                        }
                    });

                    seq.addSubBehaviour(new OneShotBehaviour() {
                        public void action() {
                            boolean gostou = false; // Altere para true para simular gorjeta
                            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                            msg.addReceiver(getAID("garcom"));
                            msg.setContent(gostou ? "dar gorjeta" : "reclamar");
                            send(msg);
                            block(2000);
                        }
                    });

                    seq.addSubBehaviour(new OneShotBehaviour() {
                        public void action() {
                            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                            msg.addReceiver(getAID("garcom"));
                            msg.setContent("pedir conta");
                            send(msg);
                        }
                    });

                    addBehaviour(seq);
                    removeBehaviour(this); // Remove o CyclicBehaviour após iniciar o seq
                } else {
                    block();
                }
            }
        });
    }
}