package com.school_21.fixme.router.database;

import com.school_21.fixme.utils.messages.Message;

import java.sql.*;

public class Database {
    private static final Connection connection = null;

    private static Connection createConnection() throws ClassNotFoundException {
        Connection connection = null;
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String username = "postgres";
        String pass = "1111";
        String driver = "org.postgresql.Driver";
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, pass);
        } catch (Exception e) {
            System.out.println("Connection to database can't be established: " + e.getMessage());
            System.exit(1);
        }
        return connection;
    }

    public static Connection getConnection() throws ClassNotFoundException {
        if (connection == null) {
            return createConnection();
        }
        return connection;
    }

    public static void createTransactionSchema() throws SQLException, ClassNotFoundException {
        String sql = "CREATE TABLE IF NOT EXISTS transactions(" +
                "id serial not null primary key," +
                "msg_length varchar not null," +
                "msg_type varchar not null," +
                "date varchar not null," +
                "username varchar not null," +
                "item_id varchar not null," +
                "amount varchar not null," +
                "price varchar not null," +
                "market_name varchar not null," +
                "check_sum varchar not null)";

        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw e;
        }
    }

    public static void saveTransaction(Message message) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO transactions(msg_length, msg_type, date, username, item_id, amount, price, market_name, check_sum)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection connection = getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, message.get("9"));
            statement.setString(2, message.get("35"));
            statement.setString(3, message.get("52"));
            statement.setString(4, message.get("553"));
            statement.setString(5, message.get("100"));
            statement.setString(6, message.get("101"));
            statement.setString(7, message.get("102"));
            statement.setString(8, message.get("103"));
            statement.setString(9, message.get("10"));
            statement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
