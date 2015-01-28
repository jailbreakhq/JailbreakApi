package org.jailbreak.service;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration.Dynamic;

import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.jailbreak.api.representations.Representations.User;
import org.jailbreak.service.auth.ApiTokenAuthenticator;
import org.jailbreak.service.errors.GenericExceptionMapper;

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
    	environment.jersey().register(new GenericExceptionMapper());
    	
    	// Enable CORS headers
    	Dynamic filter = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
        
    	// Add URL mapping
    	filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
        
    	// Configure CORS parameters
    	filter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,PUT,POST,DELETE,OPTIONS,HEAD");
        filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        filter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
        filter.setInitParameter("allowedHeaders", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");
        filter.setInitParameter("allowCredentials", "true");
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