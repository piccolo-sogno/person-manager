package com.accela;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Spec;
import picocli.CommandLine.Model.CommandSpec;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Command(name = "edit", mixinStandardHelpOptions = true, description = "edits person name and surname with given id")
public class EditPerson implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(EditPerson.class);
    private final InputValidator inputValidator;
    private final PersonManager personManager;

    @Option(names = {"-i", "--id"},
            arity = "1",
            required = true,
            description = "existing id of the person to be edited")
    @Min(value = 1, message = "Id should not be less than 1")
    private Long id;

    @Option(names = {"-n", "--name"},
            arity = "1",
            required = true,
            description = "updated first name of the person")
    @NotNull(message = "Name cannot be null")
    @Size(min = 2, max = 25, message = "Name length must be between 2 and 25 characters")
    private String name;

    @Option(names = {"-s", "--surname"},
            arity = "1",
            required = true,
            description = "updated surname of the person")
    @NotNull(message = "Surname cannot be null")
    @Size(min = 2, max = 25, message = "Surname length must be between 2 and 25 characters")
    private String surname;

    @Spec
    CommandSpec spec;

    @Inject
    public EditPerson(PersonManager personManager,
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
            edit();
        }
    }

    public void edit() {
        logger.info("Editing person with id: {} with name: {} and surname: {}", id, name, surname);
        personManager.editPerson(Person.builder()
                .id(id)
                .name(name)
                .surname(surname)
                .build());
    }

    public ValidationResult validate() {
        Set<ConstraintViolation<Runnable>> violations = Validation
                .buildDefaultValidatorFactory()
                .getValidator()
                .validate(this);

        inputValidator.validateCLIInput(violations, spec);
        inputValidator.validatePersonInformation(name, surname, spec);

        return inputValidator.validatePersonExists(id);
    }

}
