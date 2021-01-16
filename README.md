# Person Manager Application

## Overview
This application is a sample CLI Java application to manage person data.
It includes some CRUD operations such as adding, deleting, editing, listing and counting persons data.
The data is persisted into Postgres database.

Based on an input from the command line the application provides the following functionality;

1. Provides help and usage
2. Provides the version of the app
3. Adds Person (`id`, `name`, `surname`)
4. Edits Person (`id`, `name`, `surname`)
5. Deletes Person (`id`)
6. Counts Number of Persons
7. Lists Persons
8. Loads Persons from a JSON file
9. Based on Maven Build, Test and Packaging
10. Runs the application using Docker container which is based on executable jars.

## External Dependencies

Please refer to `pom.xml` file to see external Java library dependencies to build and run the application.

## Details

### CLI Options

The following is the CLI options for the application:

```
Usage: personManager [-hV] [COMMAND]
Manages person creation, edition and deletion. Lists all the persons and count
the number of persons. Loads the persons from a json file
  -h, --help      Show this help message and exit.
  -V, --version   Print version information and exit.
Commands:
  add     adds new person with name and surname
  edit    edits person name and surname with given id
  delete  deletes person with given id
  count   counts number of persons
  list    lists all persons
  load    adds persons from a json file
```

#### `--help` Command
`--help` command shows the above options information for the application.

#### `--version` Command
`--version` command shows the current version of the application.

#### `add` Command Options
`add` command requires `id`, `name` and `surname` information.

```
Usage: personManager add [-hV] -i=<id> -n=<name> -s=<surname>
adds new person with name and surname
-h, --help                Show this help message and exit.
-i, --id=<id>             unique id of the person
-n, --name=<name>         first name of the person
-s, --surname=<surname>   surname of the person
-V, --version             Print version information and exit.
```

There are some validation steps to verify the given data within the code.
For example, missing any options above will lead an error message, and the application quits:

```
Missing required option: '--id=<id>'
```

#### `edit` Command Options
`edit` command requires `id`, `name` and `surname` information.
It updates the person data if the given `id` exists in the database.
If not, it shows a `warning` message and exists without doing anything.

```
Usage: personManager edit [-hV] -i=<id> -n=<name> -s=<surname>
edits person name and surname with given id
  -h, --help                Show this help message and exit.
  -i, --id=<id>             existing id of the person to be edited
  -n, --name=<name>         updated first name of the person
  -s, --surname=<surname>   updated surname of the person
  -V, --version             Print version information and exit.
```

#### `delete` Command Options
`delete` command requires `id` information.
It deletes the person data if the given `id` exists in the database.
If not, it shows a `warning` message and exists without doing anything.

```
Usage: personManager delete [-hV] -i=<id>
deletes person with given id
  -h, --help      Show this help message and exit.
  -i, --id=<id>   existing id of the person to delete
  -V, --version   Print version information and exit.
```

#### `count` and `list` Commands

The `count` and `list` commands does not require any options to pass.

#### `load` Command Options

The `load` command requires `filePath` as an option.
The file should be `json` file, and the `filePath` could be either absolute path, or
the relative path.

```
Usage: personManager load [-hV] -f=<filePath>
adds persons from a json file
  -f, --filePath=<filePath>
                  the file path of the persons to be added
  -h, --help      Show this help message and exit.
  -V, --version   Print version information and exit.
```

Sample `json` file content:
```
[
    {
        "name": "TestNameA",
        "surname": "TestSurnameA",
        "id": 1
    },
    {
        "name": "TestNameB",
        "surname": "TestSurnameB",
        "id": 2
    }
]
```


## How to build and run the application

This application can be containerized via Docker. Please refer to the following steps in order to run the application.
Two Docker containers needed in order to run the application successfully.
The first one will be the Postgres container which will include Postgres database.
The second one will be the actual application container.
The actual application container connects to the database container to persist the data.

### 1. Run Postgres Docker Container

**Prerequisites**: 
1. To run `Docker` containers, `Docker` should be installed in the local machine.
2. The following commands should be executed under `postgres` folder.

#### Steps

* Run `postgres` container:
```
docker run --rm --name sample-postgres -d -p 5432:5432 -e POSTGRES_PASSWORD=postgres postgres:alpine
```

* Copy `sql` script into docker container to create `sample` database, and `persons` table.
The `sql` script can be found in the `setup-db-table.sql` file in this repo.
```
docker cp setup-db-table.sql sample-postgres:/tmp/file.sql
```

* Execute the `sql` script using the following command:
```
docker exec -it sample-postgres psql -U postgres -f /tmp/file.sql
```

### 2. Run Application Docker Container

**Prerequisites**:
1. To run `Docker` containers, `Docker` should be installed in the local machine.
2. The following commands should be executed under repo's `root`.
3. Running Postgres docker container is mandatory for running the application container.
   Otherwise, the application will throw an exception.


#### Steps
* Build docker image.
  It builds the docker image based on `maven` commands and create executable `jar` file within the container.
```
docker build -t personmanager .
```

* Some sample commands to run docker container based on `personmanager` 
```bash
# Shows version
docker run --net host --rm personmanager:latest --version

# Shows help for delete command
docker run --net host --rm personmanager:latest delete --help

# adds person into the database
docker run --net host --rm personmanager:latest add --id=1 --name=Jack --surname=Brown
```