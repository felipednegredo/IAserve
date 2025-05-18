package agentes;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class Cliente extends Agent {
    private boolean restauranteAberto = false;

    // Estados do cliente para controlar o fluxo
    private enum Estado {
        AGUARDANDO_ABERTURA,   // antes de receber "restaurante aberto"
        AGUARDANDO_SENTAR,     // enviou "cheguei", aguardando "sente-se por favor" ou "restaurante cheio"
        AGUARDANDO_COMER,      // sentado, enviou "fazer pedido", aguardando "entregando pedido"
        AGUARDANDO_FEEDBACK,   // comida entregue, vai "reclamar" ou "dar gorjeta"
        AGUARDANDO_CONTA       // feedback enviado, vai "pedir conta"
    }

    private Estado estado = Estado.AGUARDANDO_ABERTURA;
    private boolean gostou;  // define se deixará gorjeta ou não

    @Override
    protected void setup() {
        System.out.println("Cliente iniciado.");

        // 1) CyclicBehaviour para processar todas as mensagens recebidas
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    String conteudo = msg.getContent();
                    switch (conteudo) {
                        case "restaurante aberto":
                            restauranteAberto = true;
                            estado = Estado.AGUARDANDO_SENTAR;
                            System.out.println("[Cliente] Restaurante abriu — vou pedir para sentar.");
                            break;

                        case "restaurante cheio":
                            if (estado == Estado.AGUARDANDO_SENTAR) {
                                System.out.println("[Cliente] Restaurante cheio — aguardo e tento de novo.");
                            }
                            break;

                        case "sente-se por favor":
                            if (estado == Estado.AGUARDANDO_SENTAR) {
                                estado = Estado.AGUARDANDO_COMER;
                                System.out.println("[Cliente] Fui acomodado — hora de pedir comida.");
                            }
                            break;

                        case "entregando pedido":
                            if (estado == Estado.AGUARDANDO_COMER) {
                                estado = Estado.AGUARDANDO_FEEDBACK;
                                System.out.println("[Cliente] Recebi meu pedido — comendo agora.");
                            }
                            break;

                        case "restaurante fechado":
                            restauranteAberto = false;
                            estado = Estado.AGUARDANDO_ABERTURA;
                            System.out.println("[Cliente] Restaurante fechou — encerrando minhas atividades.");
                            break;

                        default:
                            // outras mensagens ignoradas
                            break;
                    }
                } else {
                    block();
                }
            }
        });

        // 2) TickerBehaviour para disparar ações em sequência, enquanto aberto
        addBehaviour(new TickerBehaviour(this, 2000) { // a cada 2 s real
            @Override
            protected void onTick() {
                if (!restauranteAberto) {
                    return; // não faz nada fora do expediente
                }
                switch (estado) {
                    case AGUARDANDO_SENTAR:
                        // pede para sentar
                        ACLMessage msg1 = new ACLMessage(ACLMessage.INFORM);
                        msg1.addReceiver(getAID("gerente"));
                        msg1.setContent("cheguei");
                        send(msg1);
                        System.out.println("[Cliente] Pedi para sentar.");
                        break;

                    case AGUARDANDO_COMER:
                        // faz o pedido
                        ACLMessage msg2 = new ACLMessage(ACLMessage.INFORM);
                        msg2.addReceiver(getAID("garcom"));
                        msg2.setContent("fazer pedido");
                        send(msg2);
                        System.out.println("[Cliente] Pedi meu pedido.");
                        break;

                    case AGUARDANDO_FEEDBACK:
                        // decide se gostou e envia feedback
                        gostou = Math.random() < 0.5;
                        ACLMessage msg3 = new ACLMessage(ACLMessage.INFORM);
                        msg3.addReceiver(getAID("garcom"));
                        msg3.setContent(gostou ? "dar gorjeta" : "reclamar");
                        send(msg3);
                        System.out.println(gostou
                                ? "[Cliente] Gostei! Deixei gorjeta."
                                : "[Cliente] Não gostei. Reclamei.");
                        estado = Estado.AGUARDANDO_CONTA;
                        break;

                    case AGUARDANDO_CONTA:
                        // pede a conta e reinicia ciclo
                        ACLMessage msg4 = new ACLMessage(ACLMessage.INFORM);
                        msg4.addReceiver(getAID("garcom"));
                        msg4.setContent("pedir conta");
                        send(msg4);
                        System.out.println("[Cliente] Pedi a conta — até a próxima!");
                        estado = Estado.AGUARDANDO_SENTAR;
                        break;

                    default:
                        break;
                }
            }
        });
    }
}
