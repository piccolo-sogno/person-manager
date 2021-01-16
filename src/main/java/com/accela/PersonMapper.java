package com.accela;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

public class PersonMapper implements RowMapper<Person> {

    @Override
    public Person map(ResultSet rs, StatementContext ctx) throws SQLException {
        return new Person(rs.getLong("id"), rs.getString("name"), rs.getString("surname"));
    }
}