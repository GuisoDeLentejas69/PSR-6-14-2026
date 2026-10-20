public class ParserDemo {
    public static void main(String[] args) {
        String[] entradas = {
            "CONNECT alice", "connect", "CONNECT mal nombre", "JOIN #general", "JOIN",
            "MSG Hola a todos!", "MSG", "QUIT", "QUIT ya", "FOO bar", "", null
        };
        for (String e : entradas) {
            System.out.printf("%-22s -> %s%n", e == null ? "null" : "\"" + e + "\"", ProtocolParser.parse(e));
        }
    }
}
