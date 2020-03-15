package Commands;

import Models.Account;
import Models.InMemoryData;

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
        if(params.getOwner_id() == null){
            response.setMessage("Введите id вашего счета");
            response.setState(CommandState.REQUEST_PARAMETER);
            return false;
        }
        try{
            Long.parseLong(params.getOwner_id());
        }
        catch(Exception e){
            response.setMessage("OwnerId - не корректное значение");
            response.setState(CommandState.FAIL);
            return false;
        }
        if(params.getTarget_id() == null)
        {
            response.setMessage("Введите id счета для перевода");
            response.setState(CommandState.REQUEST_PARAMETER);
            return false;
        }
        try{
            Long.parseLong(params.getTarget_id());
        }
        catch(Exception e){
            response.setMessage("TargetId - не корректное значение");
            response.setState(CommandState.FAIL);
            return false;
        }
        if(params.getAmount() == null)
        {
            response.setMessage("Введите сумму перевода");
            response.setState(CommandState.REQUEST_PARAMETER);
            return false;
        }
        try{
            Long.parseLong(params.getAmount());
        }
        catch(Exception e){
            response.setMessage("Amount - не корректное значение");
            response.setState(CommandState.FAIL);
            return false;
        }
        return true;
    }

    @Override
    protected void executeCommand(TransferCommandParameters params) throws SQLException {
        Long leftAmount = Long.parseLong(params.getAmount());

        Long other_id = Long.parseLong(params.getOwner_id());
        Long target_id = Long.parseLong(params.getTarget_id());

        var owner = Account.getAccount(other_id);
        var target = Account.getAccount(target_id);
        if(owner == null){
            response.setMessage("Неизвесный номер счета отправителя");
            response.setState(CommandState.FAIL);
            return;
        }
        if(target == null){
            response.setMessage("Неизвесный номер счета получателя");
            response.setState(CommandState.FAIL);
            return;
        }

        if (leftAmount > owner.getSum()) {
            response.setMessage("Недостаточно средств");
            response.setState(CommandState.FAIL);
            return;
        } else
            {
            Account.transfer(other_id, target_id, leftAmount);
            response.setMessage("Успех");
            response.setState(CommandState.SUCCESS);
            return;
        }
    }
}

