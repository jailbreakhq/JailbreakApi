package org.jailbreak.service.health;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.hubspot.dropwizard.guice.InjectableHealthCheck;

public class RootResourceHealthCheck extends InjectableHealthCheck {
    private final long startTime;

    @Inject
    public RootResourceHealthCheck(@Named("jailbreak.startTime") long startTime) {
        this.startTime = startTime;
    }

    @Override
    protected Result check() throws Exception {
        if (!(this.startTime > 0)) {
        	return Result.unhealthy("Root Resource start time doesn't make sense");
        }
        return Result.healthy();
    }

	@Override
	public String getName() {
		return "RootResource";
	}
}