package com.hnn.dao;

import com.hnn.Consts;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

class UsersTest {

    Connection c = null;

    Users usrs;


    @BeforeEach
    void setUp() throws SQLException {
        c = DatabaseOperations.initTables(Consts.TestDB);
        usrs = new Users(c);
    }

    @AfterEach
    void tearDown() throws SQLException {
        c.close();
        boolean isDeleted = DatabaseOperations.deleteDB(Consts.TestDB);
        if(!isDeleted){
            throw new IllegalStateException("could not delete");
        }
    }

    @Test
    void insertNewUser() {
        try {
            usrs.insertNewUser("12","Smulik","Karras","1993","Male","Female","Black","Student");
            assert true;
        } catch (SQLException e) {
            e.printStackTrace();
            assert false;
        }
    }
    @Test
    void insertNewUser2() {
        try {
            usrs.insertNewUser("12","Smulik","Karras","1993","Male","Female","Black","Student");
            usrs.insertNewUser("13","Anna","Alfi","1994","Female","Male","Asian","Bus Driver");
            assert usrs.isRegistered("12");
            assert usrs.isRegistered("13");
        } catch (SQLException e) {
            e.printStackTrace();
            assert false;
        }
    }

    @Test
    void getUserDetails() {

    }

    @Test
    void isRegistered() {
        try {
            usrs.insertNewUser("12","Smulik","Karras","1993","Male","Female","Black","Student");
            assert usrs.isRegistered("12");
        } catch (SQLException e) {
            e.printStackTrace();
            assert false;
        }
    }
    @Test
    void getUserGender(){
        try {
            usrs.insertNewUser("12","Smulik","Karras","1993","Male","Female","Black","Student");
            usrs.insertNewUser("13","Anna","Alfi","1994","Female","Male","Asian","Bus Driver");
            String userGender = usrs.getUserGender("12", c);
            String userGender2 = usrs.getUserGender("13", c);
            Assertions.assertEquals(userGender,"Male");
            Assertions.assertEquals(userGender2,"Female");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}