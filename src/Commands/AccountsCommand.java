package commands;

import models.Account;
import models.Session;

import java.sql.SQLException;

@CommandName("/accounts")
public class AccountsCommand extends Command<AccountsCommandParameters> {

    private AccountsCommandParameters params;

    public void setParameters(AccountsCommandParameters params) {
        this.params = params;
    }

    public void setParameters(String[] params, long chatId) {
        this.params = new AccountsCommandParameters(chatId);
        this.params.parse(params);
    }

    @Override
    public AccountsCommandParameters getParameters() {
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

        return true;
    }

    @Override
    protected void executeCommand(AccountsCommandParameters params) throws SQLException {
        var session = Session.getSession(params.chat_id);
        var accounts = Account.getAccountsByUser(session.getUserId());
        var builder = new StringBuilder();
        if(accounts.size() <= 0)
        {
            response.setMessage("У вас пока нет счетов");
            response.setState(CommandState.SUCCESS);
            return;
        }
        for(Account account: accounts){
            builder.append("Номер счета: ");
            builder.append(account.getId());
            builder.append("  Остаток: ");
            builder.append(account.getSum());
            builder.append(System.lineSeparator());
        }

        response.setMessage(builder.toString());
        response.setState(CommandState.SUCCESS);
        return;
    }
}

