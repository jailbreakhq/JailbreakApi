package org.jailbreak.service.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.newrelic.deps.com.google.common.collect.Lists;

/**
 * Handy quick and dirty script for adding named parameter support
 * to JDBC PreparedStatements
 * 
 * Source: http://stackoverflow.com/a/20644736
 */
public class NamedParamStatement {
	
	private PreparedStatement prepStmt;
    private List<String> fields = Lists.newArrayList();
    
    public NamedParamStatement(Connection conn, String sql) throws SQLException {
        int pos;
        while((pos = sql.indexOf(":")) != -1) {
            int end = sql.substring(pos).indexOf(" ");
            if (end == -1)
                end = sql.length();
            else
                end += pos;
            fields.add(sql.substring(pos+1,end));
            sql = sql.substring(0, pos) + "?" + sql.substring(end);
        }       
        prepStmt = conn.prepareStatement(sql);
    }

    public PreparedStatement getPreparedStatement() {
        return prepStmt;
    }
    
    public ResultSet executeQuery() throws SQLException {
        return prepStmt.executeQuery();
    }
    
    public void close() throws SQLException {
        prepStmt.close();
    }

    public void bind(String name, Object value) throws SQLException {        
        prepStmt.setObject(getIndex(name), value);
    }

    private int getIndex(String name) {
        return fields.indexOf(name)+1;
    }
}
