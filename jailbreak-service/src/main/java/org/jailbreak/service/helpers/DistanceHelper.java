package org.jailbreak.service.helpers;

import org.jailbreak.api.representations.Representations.Checkin;
import org.jailbreak.service.ServiceConfiguration;

import com.google.inject.Inject;

public class DistanceHelper {
	
	private final double finalLat;
	private final double finalLon;
	
	@Inject
	public DistanceHelper(ServiceConfiguration config) {
		this.finalLat = config.getJailbreakSettings().getFinalLat();
		this.finalLon = config.getJailbreakSettings().getFinalLon();
	}
	
	public double distanceToX(double lat, double lon) {
		return this.distance(lat, lon, finalLat, finalLon);
	}
	
	public double distanceToX(Checkin checkin) {
		return this.distance(checkin.getLat(), checkin.getLon(), finalLat, finalLon);
	}
	
	public double distance(double lat1, double lon1, double lat2, double lon2) {
	  double theta = lon1 - lon2;
	  double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
	  dist = Math.acos(dist);
	  dist = rad2deg(dist);
	  dist = dist * 60 * 1.1515;
	  dist = dist * 1.609344;
	  return (dist);
	}

	private double deg2rad(double deg) {
	  return (deg * Math.PI / 180.0);
	}

	private double rad2deg(double rad) {
	  return (rad * 180 / Math.PI);
	}

}
