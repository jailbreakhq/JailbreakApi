package org.jailbreak.service.db;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * Why in god's name are there no super simple java SQL select query 
 * builder out there?
 * 
 * sql-squiggle is the only one with the simple philosphy I want but it
 * isn't on maven making it annoying to incorporate into a project
 * 
 * All I want are zero or a small number of dependencies.
 * String builder with nice function names using a sane Java syntax. 
 * I can add prepared statements placeholder into the query to be 
 * evaluate later
 * 
 * I don't want a SQL query builder that knows about by database layer
 * or knows about my objects. I want a SQL builder script is easy to call
 * and returns string. Type-safe SQL is a pipe dream that I don't need in 
 * my life right now.
 * 
 * Cross-database support -- who needs that. I'm writing SQL I can't change 
 * quickly anyway.
 */
public class SimplestSqlBuilder {
	
	public enum OrderBy {
		DESC, ASC;
	}
	
	private final String table;
	private final List<String> columns = Lists.newArrayList();
	private final List<String> wheres = Lists.newArrayList();
	private final List<String> orderBys = Lists.newArrayList();
	
	public SimplestSqlBuilder(String table) {
		this.table = table;
	}
	
	public SimplestSqlBuilder addColumn(String column) {
		this.columns.add(column);
		
		return this;
	}
	
	public SimplestSqlBuilder addWhere(String condition) {
		this.wheres.add(condition);
		
		return this;
	}
	
	public SimplestSqlBuilder addOrderBy(String value) {
		// default direction is ASC
		this.orderBys.add(value);
	
		return this;
	}
	
	public SimplestSqlBuilder addOrderBy(String value, OrderBy orderBy) {
		if (orderBy == OrderBy.DESC) {
			this.orderBys.add(value + " DESC");
		} else {
			this.orderBys.add(value + " ASC");
		}
	
		return this;
	}
	
	public String build() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("SELECT ");
		if (columns.size() == 0) {
			builder.append("*");
		} else {
			for (int i = 0; i < columns.size(); i++) {
				builder.append(columns.get(i));
				if (i != columns.size()-1) {
					builder.append(", ");
				}
			}
		}
		builder.append(" FROM " + table);
		if (wheres.size() > 0) {
			builder.append(" WHERE ");
			for (int i = 0; i < wheres.size(); i++) {
				builder.append(wheres.get(i));
				if (i != wheres.size()-1) {
					builder.append(" AND ");
				}
			}
		}
		
		if (orderBys.size() > 0) {
			builder.append(" ORDER BY ");
			for (int i = 0; i < orderBys.size(); i++) {
				builder.append(orderBys.get(i));
				if (i != orderBys.size()-1) {
					builder.append(", ");
				}
			}
		}
		
		return builder.toString().trim();
	}
	
	public String toString() {
		return this.build();
	}

}
