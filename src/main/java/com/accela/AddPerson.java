package com.accela;

import com.google.inject.Inject;
import lombok.Data;
import picocli.CommandLine.Spec;
import javax.validation.constraints.*;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Model.CommandSpec;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Data
@Command(name = "add", mixinStandardHelpOptions = true, description = "adds new person with name and surname")
public class AddPerson implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(AddPerson.class);
    private final PersonManager personManager;
    private final InputValidator inputValidator;

    @Option(names = {"-i", "--id"},
            arity = "1",
            required = true,
            description = "unique id of the person")
    @NotNull(message = "Id cannot be null")
    @Min(value = 1, message = "Id should not be less than 1")
    private Long id;

    @Option(names = {"-n", "--name"},
            arity = "1",
            required = true,
            description = "first name of the person")
    @NotNull(message = "Name cannot be null")
    @Size(min = 2, max = 25, message = "Name length must be between 2 and 25 characters")
    private String name;

    @Option(names = {"-s", "--surname"},
            arity = "1",
            required = true,
            description = "surname of the person")
    @NotNull(message = "Surname cannot be null")
    @Size(min = 2, max = 25, message = "Surname length must be between 2 and 25 characters")
    private String surname;

    @Spec
    CommandSpec spec;

    @Inject
    public AddPerson(PersonManager personManager,
               InputValidator inputValidator) {
        this.personManager = personManager;
        this.inputValidator = inputValidator;
    }

    @Override
    public void run() {
        ValidationResult result = validate();
        if (!result.isResult()) {
            logger.warn("Validation error: {}", result.getMessage());
        }
        else {
            add();
        }
    }

    private void add() {
        Person person = Person.builder()
                .name(name)
                .surname(surname)
                .id(id)
                .build();

        logger.info("Adding person {}", person.toString());
        personManager.addPerson(person);
    }

    private ValidationResult validate() {
        Set<ConstraintViolation<Runnable>> violations = Validation
                .buildDefaultValidatorFactory()
                .getValidator()
                .validate(this);
        inputValidator.validateCLIInput(violations, spec);
        inputValidator.validatePersonInformation(name, surname, spec);

        return inputValidator.validatePersonDoesNotExist(id);
    }
}
