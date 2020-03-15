package Commands;

import Models.Session;
import Models.User;
import org.apache.commons.cli.*;

import java.sql.SQLException;

//"/login Login Password"
@CommandName("/login")
public class LoginCommand extends Command<LoginCommandParameters> {

    protected LoginCommandParameters params;

    public void setParameters(LoginCommandParameters params) {
        this.params = params;
    }

    public void setParameters(String[] params, long chatId) {
        this.params = new LoginCommandParameters(chatId);
        this.params.parse(params);
    }

    public LoginCommandParameters getParameters()
    {
        return params;
    }

    @Override
    protected boolean check()
    {
        if(params.getLogin() == null){
            response.setMessage("Введите логин");
            response.setState(CommandState.REQUEST_PARAMETER);
            return false;
        }
        if(params.getPassword() == null)
        {
            response.setMessage("Введите пароль");
            response.setState(CommandState.REQUEST_PARAMETER);
            return false;
        }
        return true;
    }

    @Override
    protected void executeCommand(LoginCommandParameters params) throws SQLException {
        var session = Session.getSession(params.chat_id);
        var user = User.getUser(params.getLogin(), params.getPassword());
        if(user != null){
            if(session != null){
                if(session.getUserId() == user.getId()){
                    response.setMessage("Вход уже выполнен");
                }
                else{
                    Session.updateSession(params.chat_id, user.getId());
                    response.setMessage("Добро пожаловать, " + user.getName());
                }
            }
            else{
                Session.insertSession(params.chat_id, user.getId());
                response.setMessage("Добро пожаловать, " + user.getName());
            }
            response.setState(CommandState.SUCCESS);
        }
        else {
            response.setMessage("Неверный логин или пароль");
            response.setState(CommandState.FAIL);
        }
    }
}
