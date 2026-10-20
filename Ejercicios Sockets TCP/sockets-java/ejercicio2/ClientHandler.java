import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class ClientHandler implements Runnable {
    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        String cliente = String.valueOf(socket.getRemoteSocketAddress());
        System.out.println("[" + Thread.currentThread().getName() + "] conectado: " + cliente);
        try (Socket s = socket;
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(s.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter out = new PrintWriter(
                     new OutputStreamWriter(s.getOutputStream(), StandardCharsets.UTF_8), true)) {

            String linea;
            while ((linea = in.readLine()) != null) {
                if ("CHAO".equalsIgnoreCase(linea.trim())) break;
                out.println("ECO: " + linea);
            }
        } catch (IOException e) {
            System.err.println("Cliente desconectado: " + e.getMessage());
        }
        System.out.println("[" + Thread.currentThread().getName() + "] cerrado: " + cliente);
    }
}
