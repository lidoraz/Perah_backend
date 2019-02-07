package com.hnn;

import com.hnn.dao.DatabaseOperations;
import com.hnn.dao.Ratings;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sun.plugin.dom.exception.InvalidStateException;

import java.io.IOException;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

class DatabaseOperationsTest {

    Connection c = null;
    Ratings ratings;

    @BeforeEach
    void setUp() throws SQLException {
        c = DatabaseOperations.initTables(Consts.TestDB);
        ratings = new Ratings(c);
    }

    @AfterEach
    void tearDown() throws SQLException {
        c.close();
        boolean isDeleted = DatabaseOperations.deleteDB(Consts.TestDB);
        if (!isDeleted) {
            throw new InvalidStateException("could not delete");
        }
    }

    @Test
    void initTables() throws SQLException {
        // list how many tables are in db:
        Statement stmp = c.createStatement();
        DatabaseMetaData md = c.getMetaData();
        ResultSet rs = md.getTables(null, null, "%", null);

        final int TABLECOUNT = 4;
        Set<String> tables = new HashSet();
        tables.add("PHOTOS");
        tables.add("RATINGS_ATTRACTIVENESS");
        tables.add("RATINGS_LOAN_WILLINGNESS");
        tables.add("USERS");
        String[] tablesFromDB = new String[4];
        int tcounter = 0;
        while (rs.next()) {
            tablesFromDB[tcounter] = rs.getString(3);
            tcounter++;
        }
        assert tcounter == TABLECOUNT;
        for (String tname : tablesFromDB) {
            assert tables.contains(tname);
        }

    }

    @Test
    void initPhotosTest() throws SQLException, IOException {
        DatabaseOperations.initPhotosTest(c,Consts.TestDB);
        assert true;
    }

    @Test
    void deleteDB() {
    }
}