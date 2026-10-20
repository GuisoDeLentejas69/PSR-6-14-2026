import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class RoomManager {

    private final Map<String, List<ClientHandler>> salas = new ConcurrentHashMap<>();

    private final Map<String, ClientHandler> usuarios = new ConcurrentHashMap<>();

    private final Set<ClientHandler> conexiones = ConcurrentHashMap.newKeySet();

    public void agregarConexion(ClientHandler c) { conexiones.add(c); }
    public void quitarConexion(ClientHandler c)  { conexiones.remove(c); }

    public boolean registrarUsuario(String nombre, ClientHandler c) {
        return usuarios.putIfAbsent(nombre, c) == null;
    }
    public void quitarUsuario(String nombre) {
        if (nombre != null) usuarios.remove(nombre);
    }

    public void unirseASala(String nombreSala, ClientHandler cliente) {

        salas.compute(nombreSala, (k, lista) -> {
            if (lista == null) lista = new CopyOnWriteArrayList<>();
            if (!lista.contains(cliente)) lista.add(cliente);
            return lista;
        });
    }

    public void salirDeSala(String nombreSala, ClientHandler cliente) {
        if (nombreSala == null) return;
        salas.computeIfPresent(nombreSala, (k, lista) -> {
            lista.remove(cliente);
            return lista.isEmpty() ? null : lista;
        });
    }

    public void broadcast(String nombreSala, String mensaje, ClientHandler emisor) {
        List<ClientHandler> miembros = salas.get(nombreSala);
        if (miembros != null) {
            for (ClientHandler miembro : miembros) {
                if (miembro != emisor) {
                    miembro.enviarMensajeDirecto(mensaje);
                }
            }
        }
    }

    public void cerrarTodos(String aviso) {
        for (ClientHandler c : conexiones) {
            c.cerrarConAviso(aviso);
        }
    }
}
