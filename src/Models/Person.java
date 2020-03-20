package models;

import shared.Tools;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class    Person {
    private static final String TabelName = "person";
    
    private long Id;
    private String Name;
    private String History;
    private String Attributes;
    private String Ip;
    private long UserId;


    public static Person insertPerson(String name, long userId) throws SQLException {
        HashMap<String, Object> args = new HashMap<String, Object>();
        args.put("UserId", userId);
        args.put("Name", name);
        var id = Tools.insert(TabelName, args);
        return getPerson(id);
    }

    public static Person getPerson(long id) throws SQLException{
        var resultSet = Tools.getResult(Tools.getSelectQuery(TabelName, "Id", Long.toString(id)));
        if(resultSet.next()){
            var name = resultSet.getString("Name");
            var history = resultSet.getString("History");
            var atributes = resultSet.getString("Atributes");
            var ip = resultSet.getString("Ip");
            var userId = resultSet.getLong("UserID");

            return new Person(id, name, history, atributes, ip, userId);
        }
        return null;
    }
    public static ArrayList<Person> getPersonsByUser(long userId) throws SQLException{
        var pers = new ArrayList<Person>();
        var resultSet = Tools.getResult(Tools.getSelectQuery(TabelName, "UserId", Long.toString(userId)));
        while (resultSet.next()){
            var id = resultSet.getLong("Id");
            var name = resultSet.getString("Name");
            var history = resultSet.getString("History");
            var atributes = resultSet.getString("Atributes");
            var ip = resultSet.getString("Ip");

            pers.add( new Person(id, name, history, atributes, ip, userId));
        }
        return pers;
    }

    public Person(long id, String name, String history, String attributes, String ip, long userId) {
        Id = id;
        Name = name;
        History = history;
        Attributes = attributes;
        Ip = ip;
        UserId = userId;
    }

    public long getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public String getHistory() {
        return History;
    }

    public String getAtributes() {
        return Attributes;
    }

    public String getIp() {
        return Ip;
    }

    public long getUserId() {
        return UserId;
    }
}
