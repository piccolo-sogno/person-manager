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
import java.util.Set;

@Command(name = "delete", mixinStandardHelpOptions = true, description = "deletes person with given id")
public class DeletePerson implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(DeletePerson.class);
    private InputValidator inputValidator;
    private PersonManager personManager;

    @Option(names = {"-i", "--id"},
            arity = "1",
            required = true,
            description = "existing id of the person to delete")
    @Min(value = 1, message = "Id should not be less than 1")
    private Long id;

    @Spec
    CommandSpec spec;

    @Inject
    public DeletePerson(PersonManager personManager,
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
            delete();
        }
    }

    public void delete() {
        logger.info("Deleting person with id: {}", id);
        personManager.deletePerson(id);
    }

    public ValidationResult validate() {
        Set<ConstraintViolation<Runnable>> violations = Validation
                .buildDefaultValidatorFactory()
                .getValidator()
                .validate(this);

        inputValidator.validateCLIInput(violations, spec);
        return inputValidator.validatePersonExistById(id);
    }
}
