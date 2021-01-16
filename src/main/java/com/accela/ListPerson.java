package com.accela;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine.Command;

import java.util.Set;

@Command(name = "list", mixinStandardHelpOptions = true, description = "lists all persons")
public class ListPerson implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(ListPerson.class);
    private final PersonManager personManager;

    @Inject
    public ListPerson(PersonManager personManager) {
        this.personManager = personManager;
    }

    @Override
    public void run() {
        list();
    }

    public Set<Person> list() {
        Set<Person> people = personManager.listPeople();
        people.forEach(person -> logger.info("Current person {}", person.toString()));
        return people;
    }
}
