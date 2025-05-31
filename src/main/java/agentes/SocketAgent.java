package agentes;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketAgent extends Agent {
    private static final int PORT = 5000;

    @Override
    protected void setup() {
        System.out.println("SocketAgent iniciado.");

        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                try (ServerSocket serverSocket = new ServerSocket(PORT)) {
                    System.out.println("SocketAgent escutando na porta " + PORT);

                    while (true) {
                        Socket clientSocket = serverSocket.accept();
                        new Thread(() -> handleClient(clientSocket)).start();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String message = in.readLine();
            System.out.println("Mensagem recebida do front-end: " + message);

            // Envia mensagem ACL para outros agentes
            ACLMessage aclMessage = new ACLMessage(ACLMessage.INFORM);
            aclMessage.addReceiver(getAID("gerente")); // Exemplo: envia ao gerente
            aclMessage.setContent(message);
            send(aclMessage);

            // Responde ao front-end
            out.println("Mensagem enviada ao agente JADE: " + message);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}