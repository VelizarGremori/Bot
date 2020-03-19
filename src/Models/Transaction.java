package models;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Transaction {
    private static final String TabelName = "transaction";

    private long Id;
    private long SourceAccountId;
    private long TargetAccountId;
    private long Amount;
    private Date DateTime;

    public Transaction(long id, long sourceAccountId, long targetAccountId, long amount, Date dateTime) {
        Id = id;
        SourceAccountId = sourceAccountId;
        TargetAccountId = targetAccountId;
        Amount = amount;
        DateTime = dateTime;
    }


    public static Transaction insertTransaction(long sourceAccountId, long targetAccountId, long amount, Date dateTime) throws SQLException {
        HashMap<String, Object> args = new HashMap<String, Object>();
        args.put("SourceAccountId", sourceAccountId);
        args.put("TargetAccountId", targetAccountId);
        args.put("Amount", amount);
        args.put("DateTime", dateTime);
        var id = Tools.insert(TabelName, args);
        return getTransaction(id);
    }

    public static Transaction getTransaction(long id) throws SQLException {
        var resultSet = Tools.getResult(Tools.getSelectQuery(TabelName, "Id", Long.toString(id)));
        if(resultSet.next()){
            var sourceAccountId = resultSet.getLong("SourceAccountId");
            var targetAccountId = resultSet.getLong("TargetAccountId");
            var amount = resultSet.getLong("Amount");
            var dateTime = resultSet.getDate("DateTime");

            return new Transaction(id, sourceAccountId, targetAccountId, amount, dateTime);
        }
        return null;
    }

    public static ArrayList<Transaction> getTransactionByAccount(long accountId) throws SQLException{
        var transactions = new ArrayList<Transaction>();
        transactions.addAll(getTransactionBySourceAccount(accountId));
        transactions.addAll(getTransactionByTargetAccount(accountId));
        return transactions;
    }

    public static ArrayList<Transaction> getTransactionBySourceAccount(long sourceAccountId) throws SQLException{
        var transactions = new ArrayList<Transaction>();
        var resultSet = Tools.getResult(Tools.getSelectQuery(TabelName, "SourceAccountId", Long.toString(sourceAccountId)));
        while (resultSet.next()){
            var id = resultSet.getLong("Id");
            var targetAccountId = resultSet.getLong("TargetAccountId");
            var amount = resultSet.getLong("Amount");
            var dateTime = resultSet.getDate("DateTime");

            transactions.add( new Transaction(id, sourceAccountId, targetAccountId, amount, dateTime));
        }
        return transactions;
    }

    public static ArrayList<Transaction> getTransactionByTargetAccount(long targetAccountId) throws SQLException{
        var transactions = new ArrayList<Transaction>();
        var resultSet = Tools.getResult(Tools.getSelectQuery(TabelName, "TargetAccountId", Long.toString(targetAccountId)));
        while (resultSet.next()){
            var id = resultSet.getLong("Id");
            var sourceAccountId = resultSet.getLong("SourceAccountIds");
            var amount = resultSet.getLong("Amount");
            var dateTime = resultSet.getDate("DateTime");

            transactions.add( new Transaction(id, sourceAccountId, targetAccountId, amount, dateTime));
        }
        return transactions;
    }

    public long getId() {
        return Id;
    }

    public long getSourceAccountId() {
        return SourceAccountId;
    }

    public long getTargetAccountId() {
        return TargetAccountId;
    }

    public long getAmount() {
        return Amount;
    }

    public Date getDateTime() {
        return DateTime;
    }
}
