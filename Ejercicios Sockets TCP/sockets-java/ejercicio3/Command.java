public final class Command {
    public enum Type { CONNECT, JOIN, MSG, QUIT, INVALID }

    private final Type type;
    private final String argument;
    private final String error;

    private Command(Type type, String argument, String error) {
        this.type = type;
        this.argument = argument;
        this.error = error;
    }

    public static Command of(Type type, String argument) { return new Command(type, argument, null); }
    public static Command invalid(String error)          { return new Command(Type.INVALID, "", error); }

    public Type getType()       { return type; }
    public String getArgument() { return argument; }
    public String getError()    { return error; }

    @Override
    public String toString() {
        return type == Type.INVALID ? "INVALID(" + error + ")" : type + "(" + argument + ")";
    }
}
