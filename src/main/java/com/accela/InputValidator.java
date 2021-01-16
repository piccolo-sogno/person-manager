package com.accela;

import picocli.CommandLine;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.ParameterException;

import javax.validation.ConstraintViolation;
import java.util.Set;
import java.util.regex.Pattern;

public class InputValidator {
    private static String NAME_SURNAME_REGEX = "^[a-zA-Z]+$";
    private static final String INVALID_STRING = "Invalid %s: %s";

    private final PersonDao personDao;

    public InputValidator() {
        personDao = DBUtils.bindAndGetDao();
    }

    public ValidationResult validatePersonExists(long id) {
        if (!personDao.getUserById(id).isPresent()) {
            return ValidationResult.builder().result(false).message(String.format("A person with provided id %s doesn't exist", id)).build();
        }
        return ValidationResult.builder().build();
    }

    public ValidationResult validatePersonDoesNotExist(long id) {
        if (personDao.getUserById(id).isPresent()) {
            return ValidationResult.builder().result(false).message(String.format("A person with provided id %s already exists", id)).build();
        }
        return ValidationResult.builder().build();
    }

    public ValidationResult validatePersonExistById(long id) {
        if (personDao.getUserById(id).isEmpty()) {
            return ValidationResult.builder().result(false).message(String.format("A person with provided id %s does not exist", id)).build();
        }
        return ValidationResult.builder().build();
    }

    public ValidationResult validatePersonInformation(String name, String surname) {
        if (!validateString(name)) {
            return ValidationResult.builder().result(false).message(String.format(INVALID_STRING, "name", name)).build();
        }
        if (!validateString(surname)) {
            return ValidationResult.builder().result(false).message(String.format(INVALID_STRING, "surname", surname)).build();
        }
        return ValidationResult.builder().build();
    }

    public void validatePersonInformation(String name, String surname, CommandSpec spec) {
        if (!validateString(name)) {
            throw new ParameterException(spec.commandLine(),
                    String.format(INVALID_STRING, "name", name));
        }
        if (!validateString(surname)) {
            throw new ParameterException(spec.commandLine(),
                    String.format(INVALID_STRING, "surname", surname));
        }
    }

    public void validateCLIInput(Set<ConstraintViolation<Runnable>> violations, CommandSpec spec){
        if (!violations.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            violations.forEach(violation -> stringBuilder.append(violation.getMessage()).append("\n"));
            throw new CommandLine.ParameterException(spec.commandLine(), stringBuilder.toString());
        }
    }

    private boolean validateString(String input) {
        return Pattern.matches(NAME_SURNAME_REGEX, input);
    }

}
