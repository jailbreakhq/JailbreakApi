package org.jailbreak.service;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration.Dynamic;

import net.kencochrane.raven.Raven;

import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.jailbreak.api.representations.Representations.User;
import org.jailbreak.service.auth.ApiTokenAuthenticator;
import org.jailbreak.service.errors.RuntimeExceptionMapper;

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
    	
    	// Custom Exception Mapper
    	Raven raven = this.guiceBundle.getInjector().getInstance(Raven.class);
    	environment.jersey().register(new RuntimeExceptionMapper(raven));
    	
    	// Enable Open CORS headers
    	Dynamic filter = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
    	filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
    	filter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,PUT,POST,DELETE,OPTIONS,HEAD");
        filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        filter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
        filter.setInitParameter("allowedHeaders", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");
        filter.setInitParameter("allowedCredentials", "true");
        filter.setInitParameter("exposedHeaders", "X-Total-Count"); // stupid syntax - don't change
        
        // request mandatory environment variables - causes runtime errors early if missing
        configuration.getEnvironmentSettings().requestAllManadtory();
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