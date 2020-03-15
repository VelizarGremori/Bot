package Commands;

public abstract class CommandParameters {
    public CommandParameters(long chat_id) {
        this.chat_id = chat_id;
    }

    protected long chat_id;

    abstract void parse(String[] params);

    public long getChat_id() {
        return chat_id;
    }
}
