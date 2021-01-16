package com.accela;

import com.google.inject.*;
import picocli.CommandLine;
import picocli.CommandLine.IFactory;

public class GuiceFactory implements IFactory {
    private final Injector injector = Guice.createInjector(new AppModule());

    @Override
    public <K> K create(Class<K> cls) throws Exception {
        try {
            return injector.getInstance(cls);
        } catch (ConfigurationException ex) { // no implementation found in Guice configuration
            return CommandLine.defaultFactory().create(cls); // fallback if missing
        }
    }

    static class AppModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(PersonManager.class);
            bind(InputValidator.class);
            bind(JsonUtils.class);
        }
    }
}