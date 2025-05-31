package frontend;

import java.io.*;
import java.net.Socket;

public class SocketClient {
    private static final String HOST = "localhost";
    private static final int PORT = 5000;

    public void sendMessage(String message) {
        try (Socket socket = new Socket(HOST, PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println(message); // Envia mensagem ao servidor
            String response = in.readLine(); // Recebe resposta
            System.out.println("Resposta do servidor: " + response);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}