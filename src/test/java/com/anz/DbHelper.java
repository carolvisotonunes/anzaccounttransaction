package com.anz;

import com.anz.dao.DatabaseConstants;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.SQLException;

public class DbHelper {
    private final BasicDataSource ds;

    public DbHelper() {
        ds = new BasicDataSource();
        ds.setUrl(DatabaseConstants.JDBC_URL);
        ds.setUsername(DatabaseConstants.USERNAME);
        ds.setPassword(DatabaseConstants.PASSWORD);
        ds.setMinIdle(5);
        ds.setMaxIdle(10);
        ds.setMaxOpenPreparedStatements(100);
    }

    public void truncate() throws SQLException {
        ds.getConnection().prepareStatement("TRUNCATE TABLE account").execute();
        ds.getConnection().prepareStatement("TRUNCATE TABLE transaction").execute();
    }
}
