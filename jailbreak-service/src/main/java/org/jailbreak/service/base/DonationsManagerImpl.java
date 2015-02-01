package org.jailbreak.service.base;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.jailbreak.api.representations.Representations.Donation;
import org.jailbreak.api.representations.Representations.Donation.DonationsFilters;
import org.jailbreak.service.core.DonationsManager;
import org.jailbreak.service.db.DonationsDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

public class DonationsManagerImpl implements DonationsManager {
	
	private final DonationsDAO dao;
	private final Connection conn;
	private final Logger LOG = LoggerFactory.getLogger(DonationsManagerImpl.class);
	
	@Inject
	public DonationsManagerImpl(DonationsDAO dao, Connection conn) {
		this.dao = dao;
		this.conn = conn;
	}

	@Override
	public Optional<Donation> getDonation(int id) {
		return this.dao.getDonation(id);
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
		int newId = this.dao.insert(donation);
		
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
	public List<Donation> getDonations() {
		return this.dao.getDonations();
	}
	
	@Override
	public List<Donation> getDonations(DonationsFilters filters) {
		// TODO: fix this mess
		try {
			return this.dao.getFilteredDonations(conn, filters);
		} catch (SQLException e) {
			LOG.error("SQL Error in getFilteredDonations: " + e.getMessage());
		}
		return Lists.newArrayList();
	}

	@Override
	public boolean deleteDonation(int id) {
		int result = this.dao.delete(id);
		if (result == 1)
			return true;
		else
			return false;
	}

}
