package org.jailbreak.service.db.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class RowCountMapper implements ResultSetMapper<Integer> {	
	public Integer map(int index, ResultSet r, StatementContext ctx) throws SQLException {
		return Integer.valueOf(r.getInt("count"));
	}
}
