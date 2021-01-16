package com.accela;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(
        name = "personManager",
        mixinStandardHelpOptions = true,
        subcommands = {
                AddPerson.class,
                EditPerson.class,
                DeletePerson.class,
                CountPersons.class,
                ListPerson.class,
                LoadPerson.class
        },
        version = "personManager 1.0",
        description = "Manages person creation, edition and deletion. Lists all the persons and count the number of persons. Loads the persons from a json file"
)
public class CommandLineProcessor implements Runnable {
    @Override
    public void run() {
        new CommandLine(this).usage(System.out);
    }

    public static void main(String... args) {
        int exitCode = new CommandLine(CommandLineProcessor.class, new GuiceFactory()).execute(args);
        System.exit(exitCode);
    }
}