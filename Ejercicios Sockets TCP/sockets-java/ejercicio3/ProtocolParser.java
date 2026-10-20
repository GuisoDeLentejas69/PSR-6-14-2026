public final class ProtocolParser {
    private ProtocolParser() {}

    public static Command parse(String lineaBruta) {
        if (lineaBruta == null || lineaBruta.trim().isEmpty()) {
            return Command.invalid("Linea vacia");
        }

        String[] partes = lineaBruta.trim().split("\\s+", 2);
        String comando = partes[0].toUpperCase();
        String arg = partes.length > 1 ? partes[1].trim() : "";

        switch (comando) {
            case "CONNECT":
                if (arg.isEmpty())
                    return Command.invalid("Uso: CONNECT <username>");
                if (!arg.matches("[A-Za-z0-9_]{1,20}"))
                    return Command.invalid("Username invalido (1-20 caracteres: letras, numeros o _)");
                return Command.of(Command.Type.CONNECT, arg);

            case "JOIN":
                if (arg.isEmpty())
                    return Command.invalid("Uso: JOIN <nombre_sala>");
                if (arg.contains(" "))
                    return Command.invalid("El nombre de sala no puede tener espacios");
                return Command.of(Command.Type.JOIN, arg);

            case "MSG":
                if (arg.isEmpty())
                    return Command.invalid("Uso: MSG <texto>");
                return Command.of(Command.Type.MSG, arg);

            case "QUIT":
                if (!arg.isEmpty())
                    return Command.invalid("QUIT no lleva argumentos");
                return Command.of(Command.Type.QUIT, "");

            default:
                return Command.invalid("Comando no reconocido: " + comando);
        }
    }
}
