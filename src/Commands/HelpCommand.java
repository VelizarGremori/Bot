package commands;

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
    protected void executeCommand(HelpCommandParameters params){
        response.setMessage(
                "Вы можете использовать следующие команды: \n\r" +
                "/login Login Password \n\r" +
                "/accounts - получить список ваших счетов (accountId : balance) \n\r" +
                "/transfer SourceId TargetId Amount - перевод денег другому пользователю." +
                        "SourceId - Номер вашего счета (accountId) TargetId - Номер счета получателя (accountId), Amount - Сумма для перевода");
        response.setState(CommandState.SUCCESS);
    }
}

