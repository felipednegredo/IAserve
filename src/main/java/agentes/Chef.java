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
            @Override
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    String conteudo = msg.getContent();

                    // Abre o expediente
                    if ("restaurante aberto".equals(conteudo)) {
                        restauranteAberto = true;
                        System.out.println("[Chef] Restaurante aberto, pronto para trabalhar.");
                        return;
                    }
                    // Fecha o expediente
                    if ("restaurante fechado".equals(conteudo)) {
                        restauranteAberto = false;
                        System.out.println("[Chef] Restaurante fechado, encerrando atividades.");
                        return;
                    }
                    // Se não estiver aberto, ignora o restante
                    if (!restauranteAberto) {
                        return;
                    }

                    // Processa pedidos e gorjeta enquanto aberto
                    switch (conteudo) {
                        case "preparar pedido":
                        case "refazer pedido":
                            System.out.println("[Chef] Preparando pedido.");
                            ACLMessage ready = new ACLMessage(ACLMessage.INFORM);
                            ready.addReceiver(getAID("garcom"));
                            ready.setContent("pedido pronto");
                            send(ready);
                            break;

                        case "receber gorjeta":
                            System.out.println("[Chef] Recebi gorjeta, obrigado!");
                            break;

                        default:
                            // Mensagem desconhecida: ignora
                            break;
                    }
                } else {
                    block();
                }
            }
        });
    }

    @Override
    protected void takeDown() {
        System.out.println("Chef encerrando expediente.");
    }
}
