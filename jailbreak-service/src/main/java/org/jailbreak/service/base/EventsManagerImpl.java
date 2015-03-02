package org.jailbreak.service.base;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.jailbreak.api.representations.Representations.Checkin;
import org.jailbreak.api.representations.Representations.Event;
import org.jailbreak.api.representations.Representations.Youtube;
import org.jailbreak.api.representations.Representations.Event.EventType;
import org.jailbreak.api.representations.Representations.Event.EventsFilters;
import org.jailbreak.api.representations.Representations.Team;
import org.jailbreak.service.core.CheckinsManager;
import org.jailbreak.service.core.EventsManager;
import org.jailbreak.service.core.TeamsManager;
import org.jailbreak.service.core.YoutubesManager;
import org.jailbreak.service.db.EventsDAO;
import org.jailbreak.service.errors.AppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.newrelic.deps.com.google.common.collect.Sets;

public class EventsManagerImpl implements EventsManager {

	private final EventsDAO dao;
	private final TeamsManager teamsManager;
	private final CheckinsManager checkinsManager;
	private final YoutubesManager youtubesManager;
	private final Logger LOG = LoggerFactory.getLogger(EventsManagerImpl.class);
	
	@Inject
	public EventsManagerImpl(EventsDAO dao,
			TeamsManager teamsManager,
			CheckinsManager checkinsManager,
			YoutubesManager youtubesManager) {
		this.dao = dao;
		this.teamsManager = teamsManager;
		this.checkinsManager = checkinsManager;
		this.youtubesManager = youtubesManager;
	}
	
	@Override
	public List<Event> getEvents(int limit, EventsFilters filters) {
		try {
			List<Event> rawEvents = dao.getFilteredEvents(limit, filters);
			return annotateEvents(rawEvents);
		} catch (SQLException e) {
			throw new AppException("Database error retrieving events", e);
		}
	}
	
	@Override
	public List<Event> filterPrivateFields(List<Event> events) {
		List<Event> filtered = Lists.newArrayListWithCapacity(events.size());
		for (Event event : events) {
			Event.Builder builder = event.toBuilder();
			
			builder.clearObjectId();
			
			filtered.add(builder.build());
		}
		return filtered;
	}
	
	private List<Event> annotateEvents(List<Event> events) {
		// Divide them up into their event types
		List<Event> youtubeEvents = Lists.newArrayList();
		List<Event> linkEvents = Lists.newArrayList();
		List<Event> checkinEvents = Lists.newArrayList();
		
		for (Event event : events) {
			if (event.getType() == EventType.YOUTUBE) {
				youtubeEvents.add(event);
			} else if (event.getType() == EventType.LINK) {
				linkEvents.add(event);
			} else if (event.getType() == EventType.CHECKIN) {
				checkinEvents.add(event);
			}
		}
		
		// Go fetch object data and annotate event
		List<Event.Builder> youtubeBuilders = annotateYoutubeEvents(youtubeEvents);
		List<Event.Builder> linkBuilders = annotateLinkEvents(linkEvents);
		List<Event.Builder> checkinBuilders = annotateCheckinEvents(checkinEvents);
		
		// Combine all the events types together again and sort by event time
		List<Event.Builder> combinedEvents = Lists.newArrayList();
		
		combinedEvents.addAll(youtubeBuilders);
		combinedEvents.addAll(linkBuilders);
		combinedEvents.addAll(checkinBuilders);
		
		Collections.sort(combinedEvents, new Comparator<Event.Builder>() {
			@Override
			public int compare(Event.Builder e1, Event.Builder e2) {
				Long time1 = Long.valueOf(e1.getTime());
				Long time2 = Long.valueOf(e2.getTime());
				return time2.compareTo(time1);
			}
		});
		
		// Annotate any events with a team id with their team data
		HashMap<Integer, Team> teamsMap = teamsManager.getLimitedTeams(getTeamIds(events));
		
		for(Team team : teamsMap.values()) {
			LOG.info("Team " + team.getId() + " " + team.getNames());
		}
		
		for (Event.Builder event : combinedEvents) {
			if (event.hasTeamId()) {
				int teamId = event.getTeamId();
						
				if (event.getType() == EventType.YOUTUBE) {
					Youtube.Builder youtube = event.getYoutube().toBuilder();
					youtube.setTeamId(teamId);
					if (teamsMap.containsKey(teamId)) {
						youtube.setTeam(teamsMap.get(teamId));
					}
					event.setYoutube(youtube);
				} else if (event.getType() == EventType.CHECKIN) {
					Checkin.Builder checkin = event.getCheckin().toBuilder();
					checkin.setTeamId(teamId);
					if (teamsMap.containsKey(teamId)) {
						checkin.setTeam(teamsMap.get(teamId));
					}
					event.setCheckin(checkin);
				}
			}
		}
		
		return buildEvents(combinedEvents);
	}
	
	private List<Event.Builder> annotateYoutubeEvents(List<Event> events) {
		HashMap<Integer, Youtube> youtubes = youtubesManager.getYoutubes(getObjectIds(events));
		List<Event.Builder> builders =  Lists.newArrayListWithCapacity(events.size());
		for (Event event : events ) {
			if (youtubes.containsKey(event.getObjectId())) {
				Event.Builder builder = event.toBuilder().setYoutube(youtubes.get(event.getObjectId()));
				builders.add(builder);
			}
			// if no matching youtube object ignore whole object
		}
		return builders;
	}
	
	private List<Event.Builder> annotateLinkEvents(List<Event> events) {
		return Lists.newArrayList();
	}
	
	private List<Event.Builder> annotateCheckinEvents(List<Event> events) {
		HashMap<Integer, Checkin> checkins = checkinsManager.getCheckins(getObjectIds(events));
		List<Event.Builder> builders =  Lists.newArrayListWithCapacity(events.size());
		for (Event event : events ) {
			if (checkins.containsKey(event.getObjectId())) {
				Event.Builder builder = event.toBuilder().setCheckin(checkins.get(event.getObjectId()));
				builders.add(builder);
			}
			// if no matching checkin object ignore whole object
		}
		return builders;
	}
	
	private List<Event> buildEvents(List<Event.Builder> builders) {
		List<Event> events = Lists.newArrayListWithCapacity(builders.size());
		for (Event.Builder builder : builders) {
			events.add(builder.build());
		}
		return events;
	}
	
	private Set<Integer> getTeamIds(List<Event> events) {
		Set<Integer> ids = Sets.newHashSet();
		for (Event event : events) {
			if (event.hasTeamId()) {
				ids.add(event.getTeamId());
			}
		}
		return ids;
	}
	
	private Set<Integer> getObjectIds(List<Event> events) {
		Set<Integer> ids = Sets.newHashSet();
		for (Event event : events) {
			ids.add(event.getObjectId());
		}
		return ids;
	}

}
