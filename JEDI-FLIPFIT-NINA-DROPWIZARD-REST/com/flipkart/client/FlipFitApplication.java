package com.flipkart.client;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

/**
 * FlipFitApplication - Dropwizard entry point
 */
public class FlipFitApplication extends Application<FlipFitConfiguration> {

    public static void main(String[] args) throws Exception {
        new FlipFitApplication().run(args);
    }

    @Override
    public void run(FlipFitConfiguration configuration, Environment environment) {
        environment.jersey().register(new GymAdminMenu());
        environment.jersey().register(new GymCustomerMenu());
        environment.jersey().register(new GymOwnerMenu());
    }
}