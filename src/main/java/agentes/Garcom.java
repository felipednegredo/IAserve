package agentes;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class Garcom extends Agent {
    private boolean restauranteAberto = false;
    @Override
    protected void setup() {
        System.out.println("Garçom iniciado.");

        addBehaviour(new CyclicBehaviour() {
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    if (!restauranteAberto) {
                        if ("restaurante aberto".equals(msg.getContent())) {
                            restauranteAberto = true;
                            System.out.println("[Garçom] Restaurante aberto, pronto para trabalhar.");
                        }
                        // Ignora outras mensagens até o restaurante abrir
                        return;
                    }
                    switch (msg.getContent()) {
                        case "fazer pedido":
                            System.out.println("Garçom recebeu pedido do cliente.");
                            ACLMessage toChef = new ACLMessage(ACLMessage.INFORM);
                            toChef.addReceiver(getAID("chef"));
                            toChef.setContent("preparar pedido");
                            send(toChef);
                            break;

                        case "pedido pronto":
                            System.out.println("Garçom recebeu comida do chef.");
                            ACLMessage toCliente = new ACLMessage(ACLMessage.INFORM);
                            toCliente.addReceiver(getAID("cliente"));
                            toCliente.setContent("entregando pedido");
                            send(toCliente);
                            break;

                        case "reclamar":
                            System.out.println("Garçom recebeu reclamação.");
                            ACLMessage refazer = new ACLMessage(ACLMessage.INFORM);
                            refazer.addReceiver(getAID("chef"));
                            refazer.setContent("refazer pedido");
                            send(refazer);
                            break;

                        case "dar gorjeta":
                            System.out.println("Garçom recebeu gorjeta.");
                            ACLMessage gorjetaChef = new ACLMessage(ACLMessage.INFORM);
                            gorjetaChef.addReceiver(getAID("chef"));
                            gorjetaChef.setContent("receber gorjeta");
                            send(gorjetaChef);
                            break;

                        case "pedir conta":
                            System.out.println("Garçom recebeu pedido de conta.");
                            // Simula cobrança
                            break;
                    }
                } else {
                    block();
                }
            }
        });
    }
}
