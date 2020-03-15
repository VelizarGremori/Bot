package Models;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

public class User {
    private static final String TabelName = "user";

    private long Id;
    private String Login;
    private String Name;
    private int Role;

    private User(long id, String login, String name, int role) {
        Id = id;
        Login = login;
        Name = name;
        Role = role;
    }

    public static User insertUser(String login, String password, String name) throws SQLException {
        HashMap<String, Object> args = new HashMap<String, Object>();
        args.put("Login", login);
        args.put("Password", password);
        args.put("Name", name);
        var id = Tools.insert(TabelName, args);
        return getUser(id);
    }

    public static User getUser(long id) throws SQLException {
        var resultSet = Tools.getResult(Tools.getSelectQuery(TabelName, "Id", Long.toString(id)));
        if(resultSet.next()){
            var login = resultSet.getString("Login");
            var name = resultSet.getString("Name");
            var role = resultSet.getInt("Role");

            return new User(id, login, name, role);
        }
        return null;
    }

    public static User getUser(String login, String password) throws SQLException {
        var resultSet = Tools.getResult(Tools.getSelectQuery(TabelName, "Login", "'" + login + "'"));
        if(resultSet.next()){
            var pass = resultSet.getString("password");
            if(!password.equals(pass))
                return null;
            var id = resultSet.getInt(1);
            var name = resultSet.getString("Name");
            var role = resultSet.getInt("Role");
            var persId = resultSet.getInt("PersId");

            return new User(id, login, name, role);
        }
        return null;
    }

    public long getId() {
        return Id;
    }

    public String getLogin() {
        return Login;
    }

    public String getName() {
        return Name;
    }

    public int getRole() {
        return Role;
    }
}
