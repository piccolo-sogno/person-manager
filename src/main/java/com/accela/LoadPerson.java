package com.accela;

import com.google.inject.Inject;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine.Spec;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Model.CommandSpec;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Command(name = "load", mixinStandardHelpOptions = true, description = "adds persons from a json file")
public class LoadPerson implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(LoadPerson.class);
    private final InputValidator inputValidator;
    private final JsonUtils jsonUtils;
    private final PersonManager personManager;

    @Option(names = {"-f", "--filePath"},
            arity = "1",
            required = true,
            description = "the file path of the persons to be added")
    @NotNull(message = "File path cannot be null")
    @Size(min = 2, max = 100, message = "File path length must be between 2 and 100 characters")
    private String filePath;

    @Spec
    CommandSpec spec;

    @Inject
    public LoadPerson(InputValidator inputValidator,
                JsonUtils jsonUtils,
                PersonManager personManager) {
        this.inputValidator = inputValidator;
        this.jsonUtils = jsonUtils;
        this.personManager = personManager;
    }

    public void load() {
        List<Person> persons = jsonUtils.parseFile(filePath);
        storePersons(persons);
    }

    private void validate() {
        Set<ConstraintViolation<Runnable>> violations = Validation
                .buildDefaultValidatorFactory()
                .getValidator()
                .validate(this);
        inputValidator.validateCLIInput(violations, spec);

    }

    private void storePersons(List<Person> persons) {
        persons.forEach(person -> validateAndStorePerson(person));
    }

    private void validateAndStorePerson(Person person) {
        List<ValidationResult> validationResults = new ArrayList<>();

        validationResults.add(inputValidator.validatePersonDoesNotExist(person.getId()));
        validationResults.add(inputValidator.validatePersonInformation(person.getName(), person.getSurname()));

        if (validationResults.stream().anyMatch(e -> !e.isResult())) {
            Set<String> invalidMessages = validationResults.stream()
                    .filter(result -> !result.isResult())
                    .map(ValidationResult::getMessage)
                    .collect(Collectors.toSet());
            logger.warn("Validation error: {}", Strings.join(invalidMessages, '\n'));
        }
        else {
            logger.info("Adding person {}", person.toString());
            personManager.addPerson(Person.builder()
                    .name(person.getName())
                    .surname(person.getSurname())
                    .id(person.getId())
                    .build());
        }
    }

    @Override
    public void run() {
        validate();
        load();
    }
}
