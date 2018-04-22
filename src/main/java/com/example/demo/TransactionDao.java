package com.example.demo;

import org.springframework.stereotype.Controller;

import java.sql.*;
import java.util.ArrayList;

@Controller
public class TransactionDao {
    private static final String URL = "jdbc:mysql://localhost:3306/home";
    private static final String USER = "root";
    private static final String PASS = "root";
    private Connection connection;

    public TransactionDao() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASS);
        } catch (ClassNotFoundException e) {
            System.out.println("No driver found");
        } catch (SQLException e) {
            System.out.println("Could not establish connection");
        }
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addTransaction(Transaction transaction) {
        final String sql = "insert into home_budget (id, type, description, amount, day, month, year) values(?,?,?,?,?,?,?)";
        try {
            PreparedStatement prepStmt = connection.prepareStatement(sql);
            prepStmt.setLong(1, transaction.getId());
            prepStmt.setString(2, transaction.getType());
            prepStmt.setString(3, transaction.getDescription());
            prepStmt.setDouble(4, transaction.getAmount());
            prepStmt.setInt(5, transaction.getDay());
            prepStmt.setInt(6, transaction.getMonth());
            prepStmt.setInt(7, transaction.getYear());
            prepStmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Nie można zapisać transakcji");
            e.printStackTrace();
        }
    }


    public ArrayList<Transaction> readTransaction(boolean positive) {  // to dodać
        String sql;
        if (positive) {
            sql = "select * from home_budget where type='przychod'";
        } else {
            sql = "select * from home_budget where type='wydatek'";
        }
        try {
            PreparedStatement prepStmt = connection.prepareStatement(sql);
            return read(prepStmt);
        } catch (SQLException e) {
            System.out.println("Could not get data");
            return new ArrayList<>();
        }
    }


    public ArrayList<Transaction> read(PreparedStatement prepStmt) { // to dodać
        ArrayList<Transaction> transactions = new ArrayList<>();
        try {
            ResultSet result = prepStmt.executeQuery();
            while (result.next()) {
                Transaction transaction = new Transaction();
                transaction.setId(result.getInt("id"));
                transaction.setType(result.getString("type"));
                transaction.setDescription(result.getString("description"));
                transaction.setAmount(result.getDouble("amount"));
                transaction.setYear(result.getInt("year"));
                transaction.setMonth(result.getInt("month"));
                transaction.setDay(result.getInt("day"));
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            System.out.println("Could not get data");
        }
        return transactions;
    }


    public ArrayList<Transaction> readAboveAmount(double amount) { // to dodać
        String sql = "select * from home_budget where amount > ?";
        try {
            PreparedStatement prepStmt = connection.prepareStatement(sql);
            prepStmt.setDouble(1, amount);
            return read(prepStmt);
        } catch (SQLException e) {
            System.out.println("Could not get data");
            return new ArrayList<>();
        }
    }

}