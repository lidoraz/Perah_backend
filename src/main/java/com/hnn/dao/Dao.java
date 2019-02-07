package com.hnn.dao;

import java.sql.Connection;

public abstract class Dao {
    protected Connection c = null;

    public Dao(Connection c) {
        this.c = c;
    }

}
