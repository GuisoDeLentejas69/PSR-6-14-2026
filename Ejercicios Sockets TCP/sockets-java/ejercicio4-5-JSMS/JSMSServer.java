import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.*;

public class JSMSServer {
    private static final int PUERTO = 8080;
    private static final int MAX_HILOS = 10;

    public static void main(String[] args) throws IOException {
        ExecutorService pool = Executors.newFixedThreadPool(MAX_HILOS);
        RoomManager rooms = new RoomManager();
        ServerSocket server = new ServerSocket(PUERTO);
        System.out.println("JSMS escuchando en puerto " + PUERTO + " (max " + MAX_HILOS + " clientes)");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nIniciando apagado controlado del servidor...");
            rooms.cerrarTodos("SERVER SHUTDOWN El servidor se esta apagando");
            try { server.close(); } catch (IOException ignored) {}
            pool.shutdown();
            try {
                if (!pool.awaitTermination(3, TimeUnit.SECONDS)) pool.shutdownNow();
            } catch (InterruptedException e) {
                pool.shutdownNow();
            }
            System.out.println("Servidor detenido.");
        }));

        while (!server.isClosed()) {
            try {
                Socket socket = server.accept();
                pool.execute(new ClientHandler(socket, rooms));
            } catch (SocketException e) {
                if (server.isClosed()) break;
                System.err.println("Error en accept(): " + e.getMessage());
            } catch (RejectedExecutionException e) {
                break;
            }
        }
    }
}
