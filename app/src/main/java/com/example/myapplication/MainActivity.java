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
    private Connection dbConnection;
    private ListView mailList;
    private DataBaseOperation dataBaseOperation = new DataBaseOperation(dbConnection);


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
                Toast.makeText(MainActivity.this, "User added", Toast.LENGTH_SHORT).show();
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

    private class addUserTask extends AsyncTask<Void, Void, Void> {
        String insertUser = "INSERT INTO users (nick, mail, pwd) VALUES ('"
                + nick_inp.getText().toString() + "', '"
                + mail_inp.getText().toString() + "', '"
                + pwd_inp.getText().toString() + "')";

        @Override
        protected Void doInBackground(Void... voids) {
            dataBaseOperation.add(insertUser);
            return null;
        }
    }

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    private class showUsersTask extends AsyncTask<Void, Void, Void> {
        String selectUser = "SELECT * FROM users ORDER BY id DESC";
        List<List<String>> selData = new ArrayList<>();

        @Override
        protected Void doInBackground(Void... voids) {
            dataBaseOperation.show(selectUser, selData);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ArrayOperation arrayOperation = new ArrayOperation();
            ArrayList<User> finalUsers = arrayOperation.remakeArray(selData);

            ArrayAdapter<User> adapter = new UserAdapter(MainActivity.this, finalUsers);
            mailList = findViewById(R.id.mainList);
            mailList.setAdapter(adapter);
        }
    }
}