import commands.AccountsCommand;
import commands.CommandState;
import commands.LoginCommand;
import models.User;

import java.sql.SQLException;

public class ConsoleMain {
    public static void main(String[] args) {
        User user;
        try {
            var command = new LoginCommand();
            command.setParameters(new String[]{"/login", "Admin", "Shadow"}, 0);
            var resp = command.execute();
            System.out.println(resp.getMessage());

            var command1 = new AccountsCommand();
            command1.setParameters(new String[]{"Admin", "Shadow"}, 0);
            resp = command1.execute();
            System.out.println(resp.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
