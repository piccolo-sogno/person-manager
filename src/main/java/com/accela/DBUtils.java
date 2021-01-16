package com.accela;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.postgres.PostgresPlugin;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

public class DBUtils {
    private static final String DB_NAMESPACE = "jdbc:postgresql://localhost:5432/sample";

    public static PersonDao bindAndGetDao() {
        Jdbi jdbi = Jdbi.create(DB_NAMESPACE, "postgres", "postgres")
                .installPlugin(new PostgresPlugin())
                .installPlugin(new SqlObjectPlugin());
        return jdbi.onDemand(PersonDao.class);
    }
}
