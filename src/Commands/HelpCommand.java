package Commands;

import java.sql.SQLException;

@CommandName("/help")
public class HelpCommand extends Command<HelpCommandParameters>{
    @Override
    public void setParameters(String[] params, long chatId) {
    }

    @Override
    public void setParameters(HelpCommandParameters commandParameters) {
    }

    @Override
    public HelpCommandParameters getParameters() {
        return null;
    }

    @Override
    protected boolean check() {
        return true;
    }

    @Override
    protected void executeCommand(HelpCommandParameters params) throws SQLException {
        response.setMessage(
                "You can use these commands: \n\r" +
                "/login Login Password" +
                "/accounts - get list your accounts (accountId : balance) \n\r" +
                "/transfer OwnerId TargetId Amount - transfer money to another user." +
                        "OwnerId - your accountId TargetId - target accountId, Amount - amount for transfer");
        response.setState(CommandState.SUCCESS);
    }
}

