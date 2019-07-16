package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MyActivity";
    private EditText nick_inp, mail_inp, pwd_inp;
    private Button ins_btn, sel_btn;
    public Connection dbConnection;
    private ListView mailList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nick_inp = findViewById(R.id.nick);
        mail_inp = findViewById(R.id.mail);
        pwd_inp = findViewById(R.id.pwd);

        ins_btn = findViewById(R.id.btn_ins);
        sel_btn = findViewById(R.id.btn_sel);

        ins_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new addUserTask().execute();
                 recreate();
                new showUsersTask().execute();
            }
        });
        sel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new showUsersTask().execute();
            }
        });
    }

//-----------------------------------------------------------------------

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

//=========================================================================================

    private class addUserTask extends AsyncTask<Void, Void, Void> {
        String insertUser = "INSERT INTO users (nick, mail, pwd) VALUES ('"
                + nick_inp.getText().toString() + "', '"
                + mail_inp.getText().toString() + "', '"
                + pwd_inp.getText().toString() + "')";

        @Override
        protected Void doInBackground(Void... voids) {
            makeConnectivity();
            try {
                if (dbConnection != null) {
                    PreparedStatement addUserrr = dbConnection.prepareStatement(insertUser);
                    addUserrr.executeUpdate();
                    dbConnection.close();
                    Log.d(TAG, "User inserted successful!");

                } else {
                    Toast.makeText(MainActivity.this, "Insertion was FAILED!", Toast.LENGTH_SHORT).show();
                }

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                Log.d(TAG, "Insert Completed...");
            }
            return null;
        }
    }

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    private class showUsersTask extends AsyncTask<Void, Void, Void> {
        String selectUser = "SELECT * FROM users ORDER BY id DESC";
        List<List<String>> selData = new ArrayList<List<String>>();
        String[][] finalDataArray;
        ArrayList<User> users;

        @Override
        protected Void doInBackground(Void... voids) {
            makeConnectivity();
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
                    Toast.makeText(MainActivity.this, "Selection was FAILED!", Toast.LENGTH_SHORT).show();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            finalDataArray = new String[selData.size()][];
            for (int i = 0; i < selData.size(); i++) {
                ArrayList<String> row = (ArrayList<String>) selData.get(i);
                finalDataArray[i] = row.toArray(new String[row.size()]);
            }

            users = new ArrayList<User>();

            for (int i = 0; i < finalDataArray.length; i++) {
                users.add(new User(finalDataArray[i][1], finalDataArray[i][2], finalDataArray[i][5]));
            }

            ArrayAdapter<User> adapter = new UserAdapter(MainActivity.this);
            mailList = findViewById(R.id.mainList);
            mailList.setAdapter(adapter);
        }

        private class UserAdapter extends ArrayAdapter<User> {

            public UserAdapter(Context context) {
                super(context, R.layout.item_of_user, users);
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                User user = getItem(position);

                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext())
                            .inflate(R.layout.item_of_user, null);
                }
                ((TextView) convertView.findViewById(R.id.nick))
                        .setText(user.getNick());
                ((TextView) convertView.findViewById(R.id.mail))
                        .setText(user.getMail());
                ((TextView) convertView.findViewById(R.id.pwd))
                        .setText(user.getPwd());
                return convertView;
            }
        }
    }
}