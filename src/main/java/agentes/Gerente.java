package agentes;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class Gerente extends Agent {
    // --- CONFIGURAÇÃO DE CICLO ---
    private static final int INTERVALO_TICK     = 50;    // ms por tick → 20 TPS
    private static final int CICLO_DIA          = 24000; // ticks por dia completo (20 min reais)
    private static final int OFFSET_HORA        = 10;    // faz o tick 0 corresponder a 10:00
    private static final int HORA_ABERTURA      = 10;    // 10:00
    private static final int HORA_FECHAMENTO    = 13;    // 13:00
    private static final int MONITOR_INTERVALO  = INTERVALO_TICK * 20; // 1s real entre logs de monitor

    // capacidade do restaurante
    private static final int CAPACIDADE_TOTAL = 5;

    // estado do restaurante
    private volatile boolean restauranteAberto;
    private volatile int     lugaresDisponiveis;
    private volatile int     currentTick;

    @Override
    protected void setup() {
        System.out.println("Gerente iniciado.");

        // 1) Abertura / fechamento baseado em ticks
        addBehaviour(new TickerBehaviour(this, INTERVALO_TICK) {
            private boolean abriuHoje, fechouHoje;

            @Override
            protected void onTick() {
                int tick     = getTickCount();
                currentTick  = tick;
                int dayTime  = tick % CICLO_DIA;

                // reset no início de cada dia
                if (dayTime == 0) {
                    abriuHoje         = false;
                    fechouHoje        = false;
                    restauranteAberto = false;
                }

                int horaJogo   = (dayTime / 1000 + OFFSET_HORA) % 24;
                int minutoJogo = (dayTime % 1000) * 60 / 1000;
                String horario = formatHorario(tick);

                // abre às 10:00
                if (!abriuHoje && horaJogo == HORA_ABERTURA && minutoJogo == 0) {
                    abriuHoje           = true;
                    restauranteAberto   = true;
                    lugaresDisponiveis  = CAPACIDADE_TOTAL;
                    System.out.printf("[%s] Restaurante ABERTO! Lugares: %d%n",
                            horario, lugaresDisponiveis);
                    broadcast("restaurante aberto");
                }

                // fecha às 13:00
                if (!fechouHoje && horaJogo == HORA_FECHAMENTO && minutoJogo == 0) {
                    fechouHoje          = true;
                    restauranteAberto   = false;
                    System.out.printf("[%s] Restaurante FECHADO!%n", horario);
                    broadcast("restaurante fechado");
                }
            }

            private void broadcast(String content) {
                for (String nome : new String[]{"garcom","cliente","chef"}) {
                    ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                    msg.addReceiver(getAID(nome));
                    msg.setContent(content);
                    send(msg);
                }
            }
        });

        // 2) Recepção de clientes — atende vários enquanto aberto
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = receive();
                if (msg != null && "cheguei".equals(msg.getContent()) && restauranteAberto) {
                    String horario = formatHorario(currentTick);
                    if (lugaresDisponiveis > 0) {
                        lugaresDisponiveis--;
                        System.out.printf("[%s] Cliente recebido. Lugares restantes: %d%n",
                                horario, lugaresDisponiveis);
                        ACLMessage reply = msg.createReply();
                        reply.setContent("sente-se por favor");
                        send(reply);
                    } else {
                        System.out.printf("[%s] Restaurante cheio!%n", horario);
                        ACLMessage reply = msg.createReply();
                        reply.setContent("restaurante cheio");
                        send(reply);
                    }
                } else {
                    // bloqueia até nova mensagem ou timeout pequeno
                    block(100);
                }
            }
        });

        // 3) Monitoramento de espaço — em loop contínuo
        addBehaviour(new TickerBehaviour(this, MONITOR_INTERVALO) {
            @Override
            protected void onTick() {
                if (restauranteAberto) {
                    String horario = formatHorario(currentTick);
                    int ocupados    = CAPACIDADE_TOTAL - lugaresDisponiveis;
                    System.out.printf("[%s] Monitor: %d/%d ocupados.%n",
                            horario, ocupados, CAPACIDADE_TOTAL);
                }
            }
        });
    }

    @Override
    protected void takeDown() {
        System.out.println("Gerente encerrando expediente.");
    }

    // formata "Dia X - HH:MM"
    private String formatHorario(int tick) {
        int dayTime    = tick % CICLO_DIA;
        int diaCount   = tick / CICLO_DIA + 1;
        int horaJogo   = (dayTime / 1000 + OFFSET_HORA) % 24;
        int minutoJogo = (dayTime % 1000) * 60 / 1000;
        return String.format("Dia %d - %02d:%02d", diaCount, horaJogo, minutoJogo);
    }
}
