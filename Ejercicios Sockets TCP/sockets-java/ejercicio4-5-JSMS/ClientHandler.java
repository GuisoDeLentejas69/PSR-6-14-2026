import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class ClientHandler implements Runnable {
    private static final int TIMEOUT_MS = 30_000;

    private final Socket socket;
    private final RoomManager rooms;
    private volatile PrintWriter out;
    private volatile String username;
    private volatile String sala;
    private volatile boolean limpiado = false;

    public ClientHandler(Socket socket, RoomManager rooms) {
        this.socket = socket;
        this.rooms = rooms;
    }

    @Override
    public void run() {
        rooms.agregarConexion(this);
        try (BufferedReader in = new BufferedReader(
                     new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter writer = new PrintWriter(
                     new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true)) {

            this.out = writer;
            socket.setSoTimeout(TIMEOUT_MS);
            socket.setKeepAlive(true);

            enviarMensajeDirecto("JSMS listo. Comandos: CONNECT <user> | JOIN <sala> | MSG <texto> | QUIT");

            String linea;
            while ((linea = in.readLine()) != null) {
                Command cmd = ProtocolParser.parse(linea);
                if (!procesar(cmd)) break;
            }
        } catch (SocketTimeoutException e) {
            enviarMensajeDirecto("408 TIMEOUT Inactividad de " + (TIMEOUT_MS / 1000) + "s, cerrando conexion");
            System.out.println("Timeout: " + descripcion());
        } catch (SocketException e) {
            System.out.println("Conexion cerrada/abortada (" + descripcion() + "): " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error de E/S (" + descripcion() + "): " + e.getMessage());
        } finally {
            limpiar();
        }
    }

    private boolean procesar(Command cmd) {
        switch (cmd.getType()) {
            case CONNECT:
                if (username != null) {
                    enviarMensajeDirecto("401 ERROR Ya estas conectado como " + username);
                } else if (!rooms.registrarUsuario(cmd.getArgument(), this)) {
                    enviarMensajeDirecto("401 ERROR El usuario '" + cmd.getArgument() + "' ya esta en uso");
                } else {
                    username = cmd.getArgument();
                    enviarMensajeDirecto("200 OK");
                }
                return true;

            case JOIN:
                if (username == null) {
                    enviarMensajeDirecto("401 ERROR Primero debes hacer CONNECT <username>");
                    return true;
                }
                String nueva = cmd.getArgument();
                String anterior = sala;
                if (anterior != null) {
                    rooms.salirDeSala(anterior, this);
                    rooms.broadcast(anterior, "* " + username + " salio de " + anterior, this);
                }
                rooms.unirseASala(nueva, this);
                sala = nueva;
                enviarMensajeDirecto("JOINED " + nueva);
                rooms.broadcast(nueva, "* " + username + " se unio a " + nueva, this);
                return true;

            case MSG:
                if (username == null) {
                    enviarMensajeDirecto("401 ERROR Primero debes hacer CONNECT <username>");
                } else if (sala == null) {
                    enviarMensajeDirecto("400 ERROR Primero debes hacer JOIN <sala>");
                } else {
                    rooms.broadcast(sala, "MSG " + username + ": " + cmd.getArgument(), this);
                    enviarMensajeDirecto("200 OK");
                }
                return true;

            case QUIT:
                enviarMensajeDirecto("200 BYE");
                return false;

            default:
                enviarMensajeDirecto("400 ERROR " + cmd.getError());
                return true;
        }
    }

    public void enviarMensajeDirecto(String mensaje) {
        PrintWriter o = out;
        if (o != null) o.println(mensaje);
    }

    public void cerrarConAviso(String aviso) {
        enviarMensajeDirecto(aviso);
        try { socket.close(); } catch (IOException ignored) {}
    }

    private void limpiar() {
        if (limpiado) return;
        limpiado = true;
        String s = sala;
        String u = username;
        if (s != null) {
            rooms.salirDeSala(s, this);
            rooms.broadcast(s, "* " + u + " salio de " + s, this);
        }
        rooms.quitarUsuario(u);
        rooms.quitarConexion(this);
        try { socket.close(); } catch (IOException ignored) {}
        System.out.println("Cliente liberado: " + descripcion());
    }

    private String descripcion() {
        return (username != null ? username : "anonimo") + "@" + socket.getRemoteSocketAddress();
    }
}
