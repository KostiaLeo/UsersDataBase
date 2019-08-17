package com.example.myapplication;

import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class DataBaseOperation {
    private Connection dbConnection;

    public DataBaseOperation(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public void makeConnectivity() {
        String url = "jdbc:mysql://remotemysql.com:3306/fyrqY4YUuY";
        String username = "fyrqY4YUuY";
        String password = "xuBvaZEooo";

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            dbConnection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //---------------------------------------------------------------------

    public void show(String selectUser, List<List<String>> selData){
        makeConnectivity();
        System.out.println(dbConnection);
        try {
            System.out.println(dbConnection);
            if (dbConnection != null) {
                PreparedStatement users = dbConnection.prepareStatement(selectUser);
                ResultSet result = users.executeQuery();
                ResultSetMetaData rsmd = result.getMetaData();

                int Cols = rsmd.getColumnCount();
                int Rows = result.getRow();
                while (result.next()) {
                    List<String> rData = new ArrayList<String>();
                    int j = 0;
                    while (j < Cols) {
                        rData.add(result.getString(j + 1));
                        j++;
                    }
                    selData.add(rData);
                }
                result.close();
            } else {
                //Toast.makeText(MainActivity.this, "Selection was FAILED!", Toast.LENGTH_SHORT).show();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //---------------------------------------------------------------------

    public void add(String insertUser){
        makeConnectivity();
        try {
            if (dbConnection != null) {
                PreparedStatement addUserrr = dbConnection.prepareStatement(insertUser);
                addUserrr.executeUpdate();
                dbConnection.close();
                Log.d(TAG, "User inserted successful!");

            } else {
               // Toast.makeText(MainActivity.this, "Insertion was FAILED!", Toast.LENGTH_SHORT).show();
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Log.d(TAG, "Insert Completed...");
        }
    }
}
