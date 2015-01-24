package org.jailbreak.service;

import org.jailbreak.api.representations.Representations.User;
import org.jailbreak.service.auth.ApiTokenAuthenticator;

import io.dropwizard.Application;
import io.dropwizard.auth.basic.BasicAuthProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import com.hubspot.dropwizard.guice.GuiceBundle;
import com.hubspot.jackson.datatype.protobuf.ProtobufModule;


public class ServiceApplication extends Application<ServiceConfiguration> {
	
	public static void main(String[] args) throws Exception {
        new ServiceApplication().run(args);
    }

    @Override
    public String getName() {
        return "JailbreakApi";
    }

    @Override
    public void initialize(Bootstrap<ServiceConfiguration> bootstrap) {   	
    	bootstrap.addBundle(migrations);
        bootstrap.addBundle(guiceBundle);
        bootstrap.getObjectMapper().registerModule(new ProtobufModule()); // jackson serializer for protobuf objects
    }

    @Override
    public void run(ServiceConfiguration configuration, Environment environment) {
    	// because we're using dropwizard-guice
        // we don't need to add resources, tasks, healthchecks or providers
        // we must get our health checks inherit from InjectableHealthCheck in order for them to be injected
    	ApiTokenAuthenticator apiTokenAuth = this.guiceBundle.getInjector().getInstance(ApiTokenAuthenticator.class);
    	environment.jersey().register(new BasicAuthProvider<User>(apiTokenAuth, "AUTH"));
    }
    
    private final MigrationsBundle<ServiceConfiguration> migrations = new MigrationsBundle<ServiceConfiguration>() {
        @Override
        public DataSourceFactory getDataSourceFactory(ServiceConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };
    
    private final GuiceBundle<ServiceConfiguration> guiceBundle = GuiceBundle.<ServiceConfiguration>newBuilder()
	    .addModule(new ServiceModule())
	    .enableAutoConfig(getClass().getPackage().getName())
	    .setConfigClass(ServiceConfiguration.class)
	    .build();
    
}