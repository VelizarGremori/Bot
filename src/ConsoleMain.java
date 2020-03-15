import Models.User;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.sql.SQLException;

public class ConsoleMain {
    public static void main(String[] args) {
        User user;
        try {
            user = User.getUser(2);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
