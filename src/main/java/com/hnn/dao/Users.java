package com.hnn.dao;

import com.hnn.dto.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class Users extends Dao {
    public Users(Connection c) {
        super(c);
    }
    public void insertNewUser(String user_id, String fname, String lname, String dob, String gender, String sexual_orientation, String race, String profession) throws SQLException {
        Statement stmt = c.createStatement();
        String sql = String.format("INSERT INTO USERS(USER_ID,FNAME,LNAME,DOB,GENDER,SEXUAL_ORIENTATION, RACE,PROFESSION) VALUES " +
                "('%s','%s','%s','%s','%s','%s','%s','%s');",
                user_id,
                fname,
                lname,
                dob,
                gender,
                sexual_orientation,
                race,
                profession);
        stmt.execute(sql);
        stmt.close();
    }

    public void insertNewUser_debug(String user_id, String gender) throws SQLException {
        insertNewUser(user_id,null,null,null, gender, null, null, null);
    }
    public HashMap<String,String> getUserDetails(String user_id) throws SQLException {
        Statement stmt = c.createStatement();
        String sql = String.format("SELECT * FROM USERS WHERE USER_ID = '%s'", user_id);
        ResultSet rs = stmt.executeQuery(sql);
        HashMap<String, String> hashMap = new HashMap<>();
        while(rs.next()){
            hashMap.put("USER_ID",rs.getString("USER_ID"));
            hashMap.put("FNAME",rs.getString("FNAME"));
            hashMap.put("LNAME",rs.getString("LNAME"));
            hashMap.put("DOB",rs.getString("DOB"));
            hashMap.put("GENDER",rs.getString("GENDER"));
            hashMap.put("SEXUAL_ORIENTATION",rs.getString("SEXUAL_ORIENTATION"));
            hashMap.put("RACE",rs.getString("RACE"));
            hashMap.put("PROFESSION",rs.getString("PROFESSION"));
        }
        stmt.close();
        return hashMap;
    }
    public boolean isRegistered(String user_id) throws SQLException {
        Statement stmt = c.createStatement();
        String sql = String.format("SELECT USER_ID FROM USERS WHERE USER_ID = '%s'", user_id);
        ResultSet rs = stmt.executeQuery(sql);

        String id = null;
        while(rs.next()){
            id = rs.getString("USER_ID");
        }
        stmt.close();
        return id != null;

    }
    public static String getUserGender(String user_id, Connection c) throws SQLException {
        String userGenderSql = String.format("SELECT GENDER FROM USERS WHERE USER_ID = %s;",user_id);
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery(userGenderSql);
        String userGender = null;
        while(rs.next()){
            userGender = rs.getString("GENDER");
        }
        stmt.close();
        return userGender;
    }
    // insert new user
    // fetch user details
}
