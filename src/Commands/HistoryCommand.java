package commands;

import models.Account;
import models.Session;
import models.Transaction;

import java.sql.SQLException;

@CommandName("/history")
public class HistoryCommand extends Command<HistoryCommandParameters> {

    private HistoryCommandParameters params;

    public void setParameters(HistoryCommandParameters params) {
        this.params = params;
    }

    public void setParameters(String[] params, long chatId) {
        this.params = new HistoryCommandParameters(chatId);
        this.params.parse(params);
    }

    @Override
    public HistoryCommandParameters getParameters() {
        return params;
    }

    @Override
    protected boolean check() {
        var session = Session.getSession(params.chat_id);
        if(session == null){
            response.setMessage("Для данной операции необходимо авторизоваться");
            response.setState(CommandState.REQUEST_AUTHORIZATION);
            return false;
        }
        if(session.getUserId() == 0) {
            response.setMessage("Для данной операции необходимо авторизоваться");
            response.setState(CommandState.REQUEST_AUTHORIZATION);
            return false;
        }
        return true;
    }

    @Override
    protected void executeCommand(HistoryCommandParameters params) throws SQLException {
        var session = Session.getSession(params.chat_id);
        var transactions = Transaction.getTransactionByUser(session.getUserId());
        var builder = new StringBuilder();
        if(transactions.size() <= 0)
        {
            response.setMessage("С вашими счетами операция не было");
            response.setState(CommandState.SUCCESS);
            return;
        }
        for(Transaction transaction: transactions){
            builder.append("Номер счета отправителя: ");
            builder.append(transaction.getSourceAccountId());
            builder.append(System.lineSeparator());
            builder.append("Номер счета получателя: ");
            builder.append(transaction.getTargetAccountId());
            builder.append(System.lineSeparator());
            builder.append("  Сумма: ");
            builder.append(transaction.getAmount());
            builder.append(System.lineSeparator());
            builder.append(System.lineSeparator());
        }

        response.setMessage(builder.toString());
        response.setState(CommandState.SUCCESS);
        return;
    }
}


