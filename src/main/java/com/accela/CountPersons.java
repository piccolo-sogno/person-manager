package com.accela;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine.Command;

@Command(name = "count", mixinStandardHelpOptions = true, description = "counts number of persons")
public class CountPersons implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(CountPersons.class);
    private PersonManager personManager;

    @Inject
    public CountPersons(PersonManager personManager) {
        this.personManager = personManager;
    }

    public void count() {
        int numberOfPeople = personManager.countPeople();
        logger.info("Total number of people {}", numberOfPeople);
    }

    @Override
    public void run() {
        count();
    }
}
