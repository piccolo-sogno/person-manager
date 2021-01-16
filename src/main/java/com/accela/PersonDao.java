package com.accela;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.BindMethods;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.Optional;
import java.util.Set;

public interface PersonDao {

    @SqlUpdate("INSERT INTO persons (id, name, surname, created_on, updated_on) values (:getId, :getName, :getSurname, now(), now())")
    void insert(@BindMethods Person person);

    @SqlUpdate("UPDATE persons SET name = :getName, surname = :getSurname, updated_on = now() WHERE id = :getId")
    void upsert(@BindMethods Person person);

    @SqlUpdate("DELETE FROM persons WHERE id = :id")
    void delete(@Bind("id") Long id);

    @SqlQuery("SELECT COUNT(*) from persons")
    int countPeople();

    @SqlQuery("SELECT * from persons")
    @RegisterRowMapper(PersonMapper.class)
    Set<Person> getAllUsers();

    @SqlQuery("SELECT * from persons WHERE id = :id")
    @RegisterRowMapper(PersonMapper.class)
    Optional<Person> getUserById(@Bind("id") Long id);

    @SqlQuery("SELECT * from persons WHERE nmme = :name AND surname = :surname")
    @RegisterRowMapper(PersonMapper.class)
    Optional<Person> getUserByNameSurname(@Bind("name") String name, @Bind("surname") String surname);
}
