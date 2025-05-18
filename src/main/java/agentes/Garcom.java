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
            @Override
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    String conteudo = msg.getContent();

                    // Controle de abertura/fechamento
                    if ("restaurante aberto".equals(conteudo)) {
                        restauranteAberto = true;
                        System.out.println("[Garçom] Restaurante aberto, pronto para trabalhar.");
                        return;
                    }
                    if ("restaurante fechado".equals(conteudo)) {
                        restauranteAberto = false;
                        System.out.println("[Garçom] Restaurante fechado, encerrando turno.");
                        return;
                    }

                    // Se não estiver aberto, ignora tudo até abrir
                    if (!restauranteAberto) {
                        return;
                    }

                    // Fluxo de trabalho do garçom enquanto aberto
                    switch (conteudo) {
                        case "fazer pedido":
                            System.out.println("[Garçom] Recebi pedido do cliente.");
                            ACLMessage toChef = new ACLMessage(ACLMessage.INFORM);
                            toChef.addReceiver(getAID("chef"));
                            toChef.setContent("preparar pedido");
                            send(toChef);
                            break;

                        case "pedido pronto":
                            System.out.println("[Garçom] Pedido pronto, levando ao cliente.");
                            ACLMessage toCliente = new ACLMessage(ACLMessage.INFORM);
                            toCliente.addReceiver(getAID("cliente"));
                            toCliente.setContent("entregando pedido");
                            send(toCliente);
                            break;

                        case "reclamar":
                            System.out.println("[Garçom] Cliente reclamou, comunicando chef.");
                            ACLMessage refazer = new ACLMessage(ACLMessage.INFORM);
                            refazer.addReceiver(getAID("chef"));
                            refazer.setContent("refazer pedido");
                            send(refazer);
                            break;

                        case "dar gorjeta":
                            System.out.println("[Garçom] Recebi gorjeta, informando chef.");
                            ACLMessage gorjetaChef = new ACLMessage(ACLMessage.INFORM);
                            gorjetaChef.addReceiver(getAID("chef"));
                            gorjetaChef.setContent("receber gorjeta");
                            send(gorjetaChef);
                            break;

                        case "pedir conta":
                            System.out.println("[Garçom] Cliente pediu a conta.");
                            // Aqui poderia ser disparada alguma rotina de pagamento
                            break;

                        default:
                            // Mensagem desconhecida: ignora ou loga, se desejar
                            break;
                    }
                } else {
                    block();
                }
            }
        });
    }
}
