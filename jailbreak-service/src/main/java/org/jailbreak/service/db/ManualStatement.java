package org.jailbreak.service.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.google.common.collect.Lists;

public class ManualStatement {
	
	private final NamedParamStatement stmt;
	
	public ManualStatement(Connection conn, String queryString, Map<String, Object> bindValues) throws SQLException {
		// Bind params
		stmt = new NamedParamStatement(conn, queryString);
		for (Map.Entry<String, Object> entry : bindValues.entrySet()) {
			stmt.bind(entry.getKey(), entry.getValue());
		}
	}
	
	public <T> List<T> executeQuery(ResultSetMapper<T> mapper) throws SQLException {
		// execute the query and map the results
		ResultSet rs = stmt.executeQuery();
		List<T> results = Lists.newArrayList();
		int i = 0;
		while(rs.next()) {
			T obj = mapper.map(i, rs, null);
			results.add(obj);
		}
		
		return results;
	}

}
