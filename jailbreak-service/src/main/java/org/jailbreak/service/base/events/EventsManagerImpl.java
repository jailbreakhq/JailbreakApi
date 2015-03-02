package org.jailbreak.service.base.events;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.jailbreak.api.representations.Representations.Checkin;
import org.jailbreak.api.representations.Representations.Donate;
import org.jailbreak.api.representations.Representations.Event;
import org.jailbreak.api.representations.Representations.Facebook;
import org.jailbreak.api.representations.Representations.Instagram;
import org.jailbreak.api.representations.Representations.Link;
import org.jailbreak.api.representations.Representations.Twitter;
import org.jailbreak.api.representations.Representations.Vine;
import org.jailbreak.api.representations.Representations.Youtube;
import org.jailbreak.api.representations.Representations.Event.EventType;
import org.jailbreak.api.representations.Representations.Event.EventsFilters;
import org.jailbreak.api.representations.Representations.Team;
import org.jailbreak.service.core.CheckinsManager;
import org.jailbreak.service.core.TeamsManager;
import org.jailbreak.service.core.events.DonateEventsManager;
import org.jailbreak.service.core.events.EventsManager;
import org.jailbreak.service.core.events.FacebookEventsManager;
import org.jailbreak.service.core.events.InstagramEventsManager;
import org.jailbreak.service.core.events.LinkEventsManager;
import org.jailbreak.service.core.events.TwitterEventsManager;
import org.jailbreak.service.core.events.VineEventsManager;
import org.jailbreak.service.core.events.YoutubeEventsManager;
import org.jailbreak.service.db.dao.events.EventsDAO;
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
	private final DonateEventsManager donatesManager;
	private final LinkEventsManager linksManager;
	private final YoutubeEventsManager youtubesManager;
	private final FacebookEventsManager facebookManager;
	private final TwitterEventsManager twitterManager;
	private final InstagramEventsManager instagramManager;
	private final VineEventsManager vineManager;
	private final Logger LOG = LoggerFactory.getLogger(EventsManagerImpl.class);
	
	@Inject
	public EventsManagerImpl(EventsDAO dao,
			TeamsManager teamsManager,
			CheckinsManager checkinsManager,
			YoutubeEventsManager youtubesManager,
			DonateEventsManager donatesManager,
			LinkEventsManager linksManager,
			FacebookEventsManager facebookManager,
			TwitterEventsManager twitterManager,
			InstagramEventsManager instagramManager,
			VineEventsManager vineManager) {
		this.dao = dao;
		this.teamsManager = teamsManager;
		this.checkinsManager = checkinsManager;
		this.youtubesManager = youtubesManager;
		this.donatesManager = donatesManager;
		this.linksManager = linksManager;
		this.facebookManager = facebookManager;
		this.twitterManager = twitterManager;
		this.instagramManager = instagramManager;
		this.vineManager = vineManager;
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
		List<Event.Builder> builders = annotateEventsObjects(events);
		
		Collections.sort(builders, new Comparator<Event.Builder>() {
			@Override
			public int compare(Event.Builder e1, Event.Builder e2) {
				Long time1 = Long.valueOf(e1.getTime());
				Long time2 = Long.valueOf(e2.getTime());
				return time2.compareTo(time1);
			}
		});
		
		// Annotate any events with a team id with their team data
		HashMap<Integer, Team> teamsMap = teamsManager.getLimitedTeams(getTeamIds(events));
		List<Event> finalEvents = Lists.newArrayListWithCapacity(events.size());
		
		LOG.info("Annotating " + events.size() + " events with the data from " + teamsMap.size() + " teams.");
		
		for (Event.Builder event : builders) {
			if (event.hasTeamId()) {
				int teamId = event.getTeamId();
						
				switch(event.getType().getNumber()) {
				case EventType.CHECKIN_VALUE:
					Checkin.Builder checkin = event.getCheckin().toBuilder();
					if (teamsMap.containsKey(teamId)) {
						checkin.setTeam(teamsMap.get(teamId));
					}
					event.setCheckin(checkin);
					break;
					
				case EventType.DONATE_VALUE:
					Donate.Builder donate = event.getDonate().toBuilder();
					if (teamsMap.containsKey(teamId)) {
						donate.setTeam(teamsMap.get(teamId));
					}
					event.setDonate(donate);
					break;
					
				case EventType.TWITTER_VALUE:
					Twitter.Builder twitter = event.getTwitter().toBuilder();
					if (teamsMap.containsKey(teamId)) {
						twitter.setTeam(teamsMap.get(teamId));
					}
					event.setTwitter(twitter);
					break;
					
				case EventType.FACEBOOK_VALUE:
					Facebook.Builder facebook = event.getFacebook().toBuilder();
					if (teamsMap.containsKey(teamId)) {
						facebook.setTeam(teamsMap.get(teamId));
					}
					event.setFacebook(facebook);
					break;
					
				case EventType.VINE_VALUE:
					Vine.Builder vine = event.getVine().toBuilder();
					if (teamsMap.containsKey(teamId)) {
						vine.setTeam(teamsMap.get(teamId));
					}
					event.setVine(vine);
					break;
					
				case EventType.INSTAGRAM_VALUE:
					Instagram.Builder insta = event.getInstagram().toBuilder();
					if (teamsMap.containsKey(teamId)) {
						insta.setTeam(teamsMap.get(teamId));
					}
					event.setInstagram(insta);
					break;
			
				case EventType.YOUTUBE_VALUE:
					Youtube.Builder youtube = event.getYoutube().toBuilder();
					if (teamsMap.containsKey(teamId)) {
						youtube.setTeam(teamsMap.get(teamId));
					}
					event.setYoutube(youtube);
					break;
				}
			}
			
			finalEvents.add(event.build());
		}
		
		return finalEvents;
	}
	
	private List<Event.Builder> annotateEventsObjects(List<Event> events) {
		LOG.info("Annotating " + events.size() + " events with their full object data");
		
		HashMap<Integer, Checkin> checkins = checkinsManager.getCheckins(getObjectIds(events, EventType.CHECKIN));
		HashMap<Integer, Donate> donates = donatesManager.getDonateEvents(getObjectIds(events, EventType.DONATE));
		HashMap<Integer, Link> links = linksManager.getLinkEvents(getObjectIds(events, EventType.LINK));
		HashMap<Integer, Facebook> facebooks = facebookManager.getFacebookEvents(getObjectIds(events, EventType.FACEBOOK));
		HashMap<Integer, Twitter> twitters = twitterManager.getTwitterEvents(getObjectIds(events, EventType.TWITTER));
		HashMap<Integer, Instagram> instagrams = instagramManager.getInstagramEvents(getObjectIds(events, EventType.INSTAGRAM));
		HashMap<Integer, Vine> vines = vineManager.getVineEvents(getObjectIds(events, EventType.VINE));
		HashMap<Integer, Youtube> youtubes = youtubesManager.getYoutubeEvents(getObjectIds(events, EventType.YOUTUBE));
		
		List<Event.Builder> builders =  Lists.newArrayListWithCapacity(events.size());
		for (Event event : events) {
			switch(event.getType().getNumber()) {
			case EventType.LINK_VALUE:
				if (links.containsKey(event.getObjectId())) {
					Event.Builder builder = event.toBuilder().setLink(links.get(event.getObjectId()));
					builders.add(builder);
				}
				break;
				
			case EventType.CHECKIN_VALUE:
				if (checkins.containsKey(event.getObjectId())) {
					Event.Builder builder = event.toBuilder().setCheckin(checkins.get(event.getObjectId()));
					builders.add(builder);
				}
				break;
				
			case EventType.DONATE_VALUE:
				if (donates.containsKey(event.getObjectId())) {
					Event.Builder builder = event.toBuilder().setDonate(donates.get(event.getObjectId()));
					builders.add(builder);
				}
				break;
				
			case EventType.TWITTER_VALUE:
				if (twitters.containsKey(event.getObjectId())) {
					Event.Builder builder = event.toBuilder().setTwitter(twitters.get(event.getObjectId()));
					builders.add(builder);
				}
				break;
				
			case EventType.FACEBOOK_VALUE:
				if (facebooks.containsKey(event.getObjectId())) {
					Event.Builder builder = event.toBuilder().setFacebook(facebooks.get(event.getObjectId()));
					builders.add(builder);
				}
				break;
				
			case EventType.VINE_VALUE:
				if (vines.containsKey(event.getObjectId())) {
					Event.Builder builder = event.toBuilder().setVine(vines.get(event.getObjectId()));
					builders.add(builder);
				}
				break;
				
			case EventType.INSTAGRAM_VALUE:
				if (instagrams.containsKey(event.getObjectId())) {
					Event.Builder builder = event.toBuilder().setInstagram(instagrams.get(event.getObjectId()));
					builders.add(builder);
				}
				break;
		
			case EventType.YOUTUBE_VALUE:
				if (youtubes.containsKey(event.getObjectId())) {
					Event.Builder builder = event.toBuilder().setYoutube(youtubes.get(event.getObjectId()));
					builders.add(builder);
				}
				break;
				
			default:
				builders.add(event.toBuilder());
			}
			
		}
		return builders;
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
	
	private Set<Integer> getObjectIds(List<Event> events, EventType type) {
		Set<Integer> ids = Sets.newHashSet();
		for (Event event : events) {
			if (event.getType() == type) {
				ids.add(event.getObjectId());
			}
		}
		return ids;
	}

}
