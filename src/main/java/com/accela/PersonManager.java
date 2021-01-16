package com.accela;

import java.util.Set;

public class PersonManager {
    private final PersonDao personDao = DBUtils.bindAndGetDao();

    public void addPerson(Person person) {
        personDao.insert(person);
    }

    public void editPerson(Person person) {
        personDao.upsert(person);
    }

    public void deletePerson(Long id) {
        personDao.delete(id);
    }

    public Set<Person> listPeople() {
        return personDao.getAllUsers();
    }

    public int countPeople() {
        return personDao.countPeople();
    }
}
