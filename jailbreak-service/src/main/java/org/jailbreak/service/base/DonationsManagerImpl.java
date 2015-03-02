package org.jailbreak.service.base;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.jailbreak.api.representations.Representations.Donation;
import org.jailbreak.api.representations.Representations.Team;
import org.jailbreak.api.representations.Representations.Donation.DonationType;
import org.jailbreak.api.representations.Representations.Donation.DonationsFilters;
import org.jailbreak.service.core.DonationsManager;
import org.jailbreak.service.core.TeamsManager;
import org.jailbreak.service.db.DonationsDAO;
import org.jailbreak.service.errors.AppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Inject;

public class DonationsManagerImpl implements DonationsManager {
	
	private final DonationsDAO dao;
	private final TeamsManager teamsManager;
	private final Logger LOG = LoggerFactory.getLogger(DonationsManagerImpl.class);
	
	@Inject
	public DonationsManagerImpl(DonationsDAO dao, TeamsManager teamsManager) {
		this.dao = dao;
		this.teamsManager = teamsManager;
	}

	@Override
	public Optional<Donation> getDonation(int id) {
		Optional<Donation> donation = this.dao.getDonation(id);
		
		if (donation.isPresent()) {
			List<Donation> donationList = Lists.newArrayList(donation.get());
			List<Donation> annotatedList = annotateDonationsWithTeams(donationList);
			donation = Optional.of(annotatedList.get(0));
		}
		
		return donation;
	}

	@Override
	public boolean updateDonation(Donation donation) {
		int result = this.dao.update(donation);
		if (result == 1)
			return true;
		else
			return false;
	}
	
	@Override
	public Donation createDonation(Donation donation) {
		LOG.info("Creating donatino for amount " + donation.getAmount());
		if (!donation.hasTime()) {
			donation = donation.toBuilder()
					.setTime(System.currentTimeMillis()/1000L)
					.build();
		}
		if (!donation.hasType()) {
			donation = donation.toBuilder()
					.setType(DonationType.ONLINE)
					.build();
		}
		int newId = this.dao.insert(donation);
		
		
		// update the count on the teams object
		if (donation.hasTeamId()) {
			Optional<Team> maybeTeam = teamsManager.getTeam(donation.getTeamId());
			if (maybeTeam.isPresent()) {
				Team team = maybeTeam.get();
				LOG.info("Old amount raised online: " + team.getAmountRaisedOnline()/100 + " euro");
				LOG.info("Updating team " + team.getId() + " amount +" + donation.getAmount()/100 + " euro");
				team = team.toBuilder()
					.setAmountRaisedOnline(team.getAmountRaisedOnline() + donation.getAmount())
					.build();
				LOG.info("New amount raised online: " + team.getAmountRaisedOnline()/100 + " euro");
				teamsManager.updateTeam(team);
			}
		}
		
		return this.dao.getDonation(newId).get(); // return full object with defaults set by DB
	}
	
	@Override
	public Optional<Donation> patchDonation(Donation donation) {
		Optional<Donation> maybeCurrent = dao.getDonation(donation.getId());
		if (maybeCurrent.isPresent()) {
			Donation merged = maybeCurrent.get().toBuilder().mergeFrom(donation).build();
			dao.update(merged);
			return Optional.of(merged);
		}
		return Optional.absent();
	}

	@Override
	public List<Donation> getDonations(int limit) {
		return annotateDonationsWithTeams(this.dao.getDonations(limit));
	}
	
	@Override
	public List<Donation> getDonations(int limit, DonationsFilters filters) {
		try {
			List<Donation> donations = this.dao.getFilteredDonations(limit, filters);
			
			return annotateDonationsWithTeams(donations);
		} catch (SQLException e) {
			throw new AppException("Database error retrieving donations count", e);
		}
	}
	
	@Override
	public int getTotalCount(DonationsFilters filters) {
		try {
			return this.dao.countFilteredDonations(filters);
		} catch (SQLException e) {
			throw new AppException("Database error retrieving donations count", e);
		}
	}

	@Override
	public boolean deleteDonation(int id) {
		int result = this.dao.delete(id);
		if (result == 1)
			return true;
		else
			return false;
	}
	
	@Override
	public int getTotalRaised() {
		return dao.getDonationsTotalAmount();
	}

	@Override
	public List<Donation> filterPrivateFields(List<Donation> donations) {
		// Useful method that removes private fields from objects so that we don't leak
		// sensitive information over the public API
		List<Donation> filtered = Lists.newArrayListWithCapacity(donations.size());
		for (Donation donation : donations) {
			Donation.Builder builder = donation.toBuilder();
			
			if (donation.hasEmail()) {
				builder.clearEmail();
			}
			
			filtered.add(builder.build());
		}
		return filtered;
	}
	
	private List<Donation> annotateDonationsWithTeams(List<Donation> donations) {
		Set<Integer> ids = teamIds(donations);
		HashMap<Integer, Team> map = teamsManager.getLimitedTeams(ids);
		
		List<Donation> newDonations = Lists.newArrayListWithCapacity(donations.size());
		for (Donation donation : donations) {
			if (donation.hasTeamId()) {
				int teamId = donation.getTeamId();
				if (map.containsKey(teamId)) {
					newDonations.add(donation.toBuilder().setTeam(map.get(teamId)).build());
				}
			} else {
				newDonations.add(donation);
			}
		}
		
		return newDonations;
	}
	
	private Set<Integer> teamIds(List<Donation> donations) {
		Set<Integer> ids = Sets.newHashSetWithExpectedSize(donations.size());
		for (Donation donation : donations) {
			if (donation.hasTeamId()) {
				ids.add(donation.getTeamId());
			}
		}
		return ids;
	}

}
