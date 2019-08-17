package com.example.myapplication;

import java.util.ArrayList;
import java.util.List;

public class ArrayOperation {
    String[][] finalDataArray;
    ArrayList<User> users;
    public ArrayList<User> remakeArray(List<List<String>> selData){

        finalDataArray = new String[selData.size()][];
        for (int i = 0; i < selData.size(); i++) {
            ArrayList<String> row = (ArrayList<String>) selData.get(i);
            finalDataArray[i] = row.toArray(new String[row.size()]);
        }

        users = new ArrayList<User>();

        for (int i = 0; i < finalDataArray.length; i++) {
            users.add(new User(finalDataArray[i][1], finalDataArray[i][2], finalDataArray[i][5]));
        }
    return users;
    }
}
