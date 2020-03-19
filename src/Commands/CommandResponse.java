package commands;

public class CommandResponse {
    private String message;
    private CommandState state;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CommandState getState() {
        return state;
    }

    public void setState(CommandState state) {
        this.state = state;
    }
}
