package agentes;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class Chef extends Agent {
    private boolean restauranteAberto = false;

    @Override
    protected void setup() {
        System.out.println("Chef iniciado.");

        addBehaviour(new CyclicBehaviour() {
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    if (!restauranteAberto) {
                        if ("restaurante aberto".equals(msg.getContent())) {
                            restauranteAberto = true;
                            System.out.println("[Chef] Restaurante aberto, pronto para trabalhar.");
                        }
                        // Ignora outras mensagens até o restaurante abrir
                        return;
                    }
                    if ("preparar pedido".equals(msg.getContent()) || "refazer pedido".equals(msg.getContent())) {
                        System.out.println("Chef está preparando pedido.");
                        ACLMessage ready = new ACLMessage(ACLMessage.INFORM);
                        ready.addReceiver(getAID("garcom"));
                        ready.setContent("pedido pronto");
                        send(ready);
                    } else if ("receber gorjeta".equals(msg.getContent())) {
                        System.out.println("Chef recebeu gorjeta!");
                    }
                } else {
                    block();
                }
            }
        });
    }
}