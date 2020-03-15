package Models;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;

public class Session {
    private static final String TabelName = "session";

    private long userId;
    private long chatId;
    private Date lastAuth;
    private String mode;

    public Session(long userId, long chatId, Date lastAuth, String mode) {
        this.userId = userId;
        this.chatId = chatId;
        this.lastAuth = lastAuth;
        this.mode = mode;
    }

    public static Session insertSession(long userId, long chatId) throws SQLException {
        HashMap<String, Object> args = new HashMap<String, Object>();
        args.put("UserId", userId);
        args.put("ChatId", chatId);
        args.put("LastAuth", new Date());
        Tools.insert(TabelName, args);
        return getSession(chatId);
    }

    public static Session getSession(long chatId) {
        try{
        var resultSet = Tools.getResult(Tools.getSelectQuery(TabelName, "ChatId", Long.toString(chatId)));
        if(resultSet.next()){
            var userId = resultSet.getLong("UserId");
            var lastAuth = resultSet.getDate("LastAuth");
            var mode = resultSet.getString("Mode");
            return new Session(userId, chatId, lastAuth, mode);
        }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return null;
    }

    public static void updateSession(long chatId, String mode) {
        var a = Tools.getUpdateQuery(TabelName,
                "Mode", mode, "ChatId", Long.toString(chatId));
        try {
            Tools.getStatement().executeUpdate(
                    a);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateSession(long chatId, long userId) throws SQLException{
            var a = Tools.getUpdateQuery(TabelName,
                "UserId", Long.toString(userId), "ChatId", Long.toString(chatId));
        Tools.getStatement().executeUpdate(
                a);
    }

    public long getUserId() {
        return userId;
    }

    public long getChatId() {
        return chatId;
    }

    public Date getLastAuth() {
        return lastAuth;
    }

    public String getMode() {
        return mode;
    }

}
