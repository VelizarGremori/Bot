package Commands;

public enum CommandState {
    SUCCESS (0),
    FAIL (-1),
    REQUEST_PARAMETER (1),
    REQUEST_AUTHORIZATION(2);

    private int code;

    public int getCode() {
        return code;
    }

    CommandState(int code) {
        this.code = code;
    }
}
