package org.jailbreak.service.views;

import io.dropwizard.views.View;

import java.util.List;

import liquibase.util.StringUtils;

import org.assertj.core.util.Lists;
import org.jailbreak.api.representations.Representations.Team;
import org.jailbreak.api.representations.Representations.Team.TeamsFilters;

public class TeamsView extends View {
	
	private final List<Team> teams;
	private final int teamsCount;
	private final int page;
	private final int numberPages;
	private final TeamsFilters filters;
	
	public TeamsView(
			List<Team> teams,
			int teamsCount,
			int page,
			int numberPages,
			TeamsFilters filters) {
		super("teams.ftl");
		this.teams = teams;
		this.teamsCount = teamsCount;
		this.page = page;
		this.numberPages = numberPages;
		this.filters = filters;
	}
	
	public List<Team> getTeams() {
		return teams;
	}

	public int getTeamsCount() {
		return teamsCount;
	}
	
	public int getPage() {
		return page;
	}
	
	public int getNumberPages() {
		return numberPages;
	}
	
	public TeamsFilters getFilters() {
		return filters;
	}
	
	public String getUniversity() {
		if (filters.hasUniversity()) {
			return filters.getUniversity().name();
		} else {
			return "ALL";
		}
	}
	
	public String getOrderBy() {
		if (filters.hasOrderBy()) {
			return filters.getOrderBy().name();
		} else {
			return "";
		}
	}
	
	public List<Integer> getPageRange() {
		List<Integer> results = Lists.newArrayList();
		
		for(int i = 1; i <= this.numberPages; i++) {
			results.add(i);
		}
		
		return results;
	}
	
	public String getFiltersParams() {
		List<String> params = Lists.newArrayList();
		
		if (filters.hasUniversity()) {
			params.add("university=" + filters.getUniversity());
		}
		
		if (filters.hasOrderBy()) {
			params.add("orderBy=" + filters.getOrderBy());
		}
		
		String joined = StringUtils.join(params, "&");
		if (!joined.isEmpty()) {
			return "&" + joined;
		}
		return "";
	}

}
