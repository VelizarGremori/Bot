package commands;

import models.Account;
import models.Person;
import models.Session;
import models.User;

import java.sql.SQLException;

@CommandName("/transfer")
public class TransferCommand extends Command<TransferCommandParameters> {

    private TransferCommandParameters params;

    public void setParameters(TransferCommandParameters params) {
        this.params = params;
    }

    public void setParameters(String[] params, long chatId) {
        this.params = new TransferCommandParameters(chatId);
        this.params.parse(params);
    }

    @Override
    public TransferCommandParameters getParameters() {
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

        if(params.getSource_id() == null){
            response.setMessage("Введите id вашего счета");
            response.setState(CommandState.REQUEST_PARAMETER);
            return false;
        }
        long source_id;
        try{
            source_id = Long.parseLong(params.getSource_id());
        }
        catch(Exception e) {
            response.setMessage("SourceId - некорректное значение");
            response.setState(CommandState.FAIL);
            return false;
        }

        if(params.getTarget_id() == null)
        {
            response.setMessage("Введите id счета для перевода");
            response.setState(CommandState.REQUEST_PARAMETER);
            return false;
        }
        long target_id;
        try{
            target_id = Long.parseLong(params.getTarget_id());
        }
        catch(Exception e){
            response.setMessage("TargetId - некорректное значение");
            response.setState(CommandState.FAIL);
            return false;
        }

        if(params.getAmount() == null)
        {
            response.setMessage("Введите сумму перевода");
            response.setState(CommandState.REQUEST_PARAMETER);
            return false;
        }
        long leftAmount;
        try{
            leftAmount = Long.parseLong(params.getAmount());
        }
        catch(Exception e){
            response.setMessage("Amount - некорректное значение");
            response.setState(CommandState.FAIL);
            return false;
        }
        if(leftAmount <= 0)
        {
            response.setMessage("Amount - некорректное значение");
            response.setState(CommandState.FAIL);
            return false;
        }

        Account source;
        Account target;
        User owner;
        try {
            source = Account.getAccount(source_id);
            target = Account.getAccount(target_id);
            var owner_person = Person.getPerson(source.getPersId());
            owner = User.getUser(owner_person.getUserId());
        }catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }

        if(source == null){
            response.setMessage("Неизвесный номер счета отправителя");
            response.setState(CommandState.FAIL);
            return false;
        }
        if(target == null){
            response.setMessage("Неизвесный номер счета получателя");
            response.setState(CommandState.FAIL);
            return false;
        }
        if(session.getUserId() != owner.getId()){
            response.setMessage("Счет списания не принадлежит авторизированному пользователю");
            response.setState(CommandState.FAIL);
            return false;
        }

        if (leftAmount > source.getSum()) {
            response.setMessage("Недостаточно средств");
            response.setState(CommandState.FAIL);
            return false;
        }

        return true;
    }

    @Override
    protected void executeCommand(TransferCommandParameters params) throws SQLException {
            Account.transfer(
                    Long.parseLong(params.getSource_id()),
                    Long.parseLong(params.getTarget_id()),
                    Integer.parseInt(params.getAmount()));
            response.setMessage("Успех");
            response.setState(CommandState.SUCCESS);
            return;

    }
}

