package Models;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

public class Account {
    private static final String TabelName = "account";

    private long Id;
    private long Sum;
    private boolean Access;
    private long PersId;
    private String Ip;

    public static Account getAccount(long id) throws  SQLException {
        var resultSet = Tools.getResult(Tools.getSelectQuery(TabelName, "Id", Long.toString(id)));
        if(resultSet.next()){
            var sum = resultSet.getInt("Sum");
            var access = resultSet.getBoolean("Access");
            var persId = resultSet.getInt("PersId");
            var ip = resultSet.getString("Ip");
            return new Account(id, sum, access, persId, ip);
        }
        return null;
    }

    public static Account insertAccount(long persId, int startCapital) throws SQLException {
        HashMap<String, Object> args = new HashMap<>();
        args.put("PersId", persId);
        args.put("Sum", startCapital);
        var id = Tools.insert(TabelName, args);
        return getAccount(id);
    }

    public static void transfer(long ownerAccountId, long targetAccountId, long sum) throws  SQLException {
        var conn = Tools.getConnection();
        conn.beginRequest();

        var ownerAccount = getAccount(ownerAccountId);
        var targetAccount = getAccount(targetAccountId);
        if (ownerAccount.getSum() > sum && sum > 0) {
            var a = Tools.getUpdateQuery(TabelName,"Sum", Long.toString(ownerAccount.getSum() - sum), "Id", Long.toString(ownerAccountId));
            Tools.getStatement().executeUpdate(
                    a);
            Tools.getStatement().executeUpdate(
                    Tools.getUpdateQuery(TabelName,"Sum", Long.toString(targetAccount.getSum() + sum), "Id", Long.toString(targetAccountId)));
        }
        conn.endRequest();
    }

    public Account(long id, int sum, boolean access, long persId, String ip) {
        Id = id;
        Sum = sum;
        Access = access;
        PersId = persId;
        Ip = ip;
    }

    public long getId() {
        return Id;
    }

    public long getSum() {
        return Sum;
    }

    public boolean isAccess() {
        return Access;
    }

    public long getPersId() {
        return PersId;
    }

    public String getIp() {
        return Ip;
    }
}