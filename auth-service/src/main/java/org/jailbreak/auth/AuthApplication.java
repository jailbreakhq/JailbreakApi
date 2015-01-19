package org.jailbreak.auth;

import org.jailbreak.auth.auth.ApiTokenAuthenticator;
import org.jailbreak.auth.representations.Representations.User;

import io.dropwizard.Application;
import io.dropwizard.auth.basic.BasicAuthProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import com.hubspot.dropwizard.guice.GuiceBundle;
import com.hubspot.jackson.datatype.protobuf.ProtobufModule;


public class AuthApplication extends Application<AuthConfiguration> {
	
	public static void main(String[] args) throws Exception {
        new AuthApplication().run(args);
    }

    @Override
    public String getName() {
        return "JailbreakApi";
    }

    @Override
    public void initialize(Bootstrap<AuthConfiguration> bootstrap) {   	
    	bootstrap.addBundle(migrations);
        bootstrap.addBundle(guiceBundle);
        bootstrap.getObjectMapper().registerModule(new ProtobufModule()); // jackson serializer for protobuf objects
    }

    @Override
    public void run(AuthConfiguration configuration, Environment environment) {
    	// because we're using dropwizard-guice
        // we don't need to add resources, tasks, healthchecks or providers
        // we must get our health checks inherit from InjectableHealthCheck in order for them to be injected
    	ApiTokenAuthenticator apiTokenAuth = this.guiceBundle.getInjector().getInstance(ApiTokenAuthenticator.class);
    	environment.jersey().register(new BasicAuthProvider<User>(apiTokenAuth, "AUTH"));
    }
    
    private final MigrationsBundle<AuthConfiguration> migrations = new MigrationsBundle<AuthConfiguration>() {
        @Override
        public DataSourceFactory getDataSourceFactory(AuthConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };
    
    private final GuiceBundle<AuthConfiguration> guiceBundle = GuiceBundle.<AuthConfiguration>newBuilder()
	    .addModule(new AuthModule())
	    .enableAutoConfig(getClass().getPackage().getName())
	    .setConfigClass(AuthConfiguration.class)
	    .build();
    
}