package models;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Map;
import java.util.Properties;

public class Tools {

    public static Connection getConnection() throws SQLException{
        Properties props = new Properties();
        try(InputStream in = Files.newInputStream(Paths.get("src/database.properties"))){
            props.load(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String url = props.getProperty("url");
        String username = props.getProperty("username");
        String password = props.getProperty("password");

        return DriverManager.getConnection(url, username, password);
    }

    public static ResultSet getResult(String query) throws SQLException {
        var conn = getConnection();
        Statement statement = conn.createStatement();
        return statement.executeQuery(query);
    }
    

    public static String getSelectQuery(String tableName, String keyColumn, String key)
    {
        return  "SELECT * FROM anthill." + tableName +" WHERE " + keyColumn+" = " + key;
    }

    public static Statement getStatement() throws SQLException {
        return getConnection().createStatement();
    }

    public static String getUpdateQuery(String tableName, String updateColumn, String value, String keyColumn, String key)
    {
        return  "UPDATE anthill." + tableName + " SET " + updateColumn + " =  " +  value + " WHERE "+ keyColumn +" = " + key;
    }

    public static long insert(String tableName, Map<String, Object> args) throws SQLException {
        var stringBuilder = new StringBuilder("INSERT anthill.");
        stringBuilder.append(tableName);

        stringBuilder.append("( ");

        var keys = args.keySet().toArray();

        for (var key:
                keys) {
            stringBuilder.append(key);
            stringBuilder.append(", ");
        }
        stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length() - 1);
        stringBuilder.append(") Values( ");

        for(var i = 1; i < keys.length; i++){
            stringBuilder.append("?,");
        }
        long id = 0;

        stringBuilder.append("?)");

        try (var conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(stringBuilder.toString(),
                     Statement.RETURN_GENERATED_KEYS)) {
            for (var i = 0; i < keys.length; i++){
                var value = args.get(keys[i]);

                if (value instanceof String) {
                    pstmt.setString(i + 1, (String)value);
                } else if (value instanceof Integer) {
                    pstmt.setInt(i + 1, (int) value);
                }else if (value instanceof Long){
                    pstmt.setLong(i + 1, (long)value);
                }else if (value instanceof Boolean){
                    pstmt.setBoolean(i + 1, (boolean)value);
                }else
                    pstmt.setObject(i + 1, value);

            }
            int affectedRows = pstmt.executeUpdate();
            // check the affected rows
            if (affectedRows > 0) {
                // get the ID back
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        id = rs.getLong(1);
                    }
                }
        }
        return  id;
        }
    }
}
