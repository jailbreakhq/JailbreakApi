package org.jailbreak.service.tests.db;

import static org.fest.assertions.api.Assertions.assertThat;

import org.jailbreak.service.db.SimplestSqlBuilder;
import org.jailbreak.service.db.SimplestSqlBuilder.OrderBy;
import org.junit.Test;

public class SimplestSqlBuilderTest {
	
	@Test
	public void testSimpleSelect() {
		String expected = "SELECT * FROM teams";
		
		SimplestSqlBuilder builder = new SimplestSqlBuilder("teams");
		
		assertThat(builder.build()).isEqualTo(expected);
	}
	
	@Test
	public void testColumnSelect() {
		String expected = "SELECT name, avatar FROM teams";
		
		SimplestSqlBuilder builder = new SimplestSqlBuilder("teams")
			.addColumn("name")
			.addColumn("avatar");
		
		assertThat(builder.build()).isEqualTo(expected);
	}
	
	@Test
	public void testSingleWhereSelect() {
		String expected = "SELECT * FROM donations WHERE team_id = :team_id";
		
		SimplestSqlBuilder builder = new SimplestSqlBuilder("donations")
			.addWhere("team_id = :team_id");
		
		assertThat(builder.build()).isEqualTo(expected);
	}
	
	@Test
	public void testWhereSelect() {
		String expected = "SELECT * FROM donations WHERE team_id = :team_id AND time > :since_time";
		
		SimplestSqlBuilder builder = new SimplestSqlBuilder("donations")
			.addWhere("team_id = :team_id")
			.addWhere("time > :since_time");
		
		assertThat(builder.build()).isEqualTo(expected);
	}
	
	@Test
	public void testLimit() {
		String expected = "SELECT * FROM donations LIMIT 10";
		
		SimplestSqlBuilder builder = new SimplestSqlBuilder("donations")
			.limit(10);
		
		assertThat(builder.build()).isEqualTo(expected);
	}
	
	@Test
	public void testOrderBySelect() {
		String expected = "SELECT * FROM donations WHERE team_id = :team_id ORDER BY time DESC";
		
		SimplestSqlBuilder builder = new SimplestSqlBuilder("donations")
			.addWhere("team_id = :team_id")
			.addOrderBy("time", OrderBy.DESC);
		
		assertThat(builder.build()).isEqualTo(expected);
	}
	
	@Test
	public void testMutlipleOrderBySelect() {
		String expected = "SELECT * FROM donations ORDER BY team_id, time DESC";
		
		SimplestSqlBuilder builder = new SimplestSqlBuilder("donations")
			.addOrderBy("team_id")
			.addOrderBy("time", OrderBy.DESC);
		
		assertThat(builder.build()).isEqualTo(expected);
	}
	
	@Test
	public void testOrderByOffset() {
String expected = "SELECT * FROM donations ORDER BY time OFFSET 40";
		
		SimplestSqlBuilder builder = new SimplestSqlBuilder("donations")
			.addOrderBy("time")
			.offset(40);
		
		assertThat(builder.build()).isEqualTo(expected);
	}
	
	@Test
	public void testAll() {
		String expected = "SELECT amount, team_id, name FROM donations WHERE time = :time AND name = :name ORDER BY team_id ASC, time DESC LIMIT 25 OFFSET 50";
		
		SimplestSqlBuilder builder = new SimplestSqlBuilder("donations")
			.addColumn("amount")
			.addColumn("team_id")
			.addColumn("name")
			.addWhere("time = :time")
			.addWhere("name = :name")
			.addOrderBy("team_id", OrderBy.ASC)
			.addOrderBy("time", OrderBy.DESC)
			.limit(25)
			.offset(50);
		
		assertThat(builder.build()).isEqualTo(expected);
	}

}
