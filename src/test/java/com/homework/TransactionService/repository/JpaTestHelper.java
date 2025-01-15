package com.homework.TransactionService.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import jakarta.persistence.EntityManager;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public abstract class JpaTestHelper {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private DataSource dataSource;

    public EntityManager getEntityManager() {
        return entityManager;
    }

    private void executeSql(String sql) throws SQLException {
        Connection db = dataSource.getConnection();
        db.createStatement().executeUpdate(sql);
    }

    @BeforeEach
    void setUpBeforeEach() throws SQLException {
        executeSql(loadResource("prepare_testdata.sql"));
        entityManager.flush();
    }

    @AfterEach
    void clearAfterEach() throws SQLException {
        getEntityManager().clear();
        executeSql(loadResource("delete_testdata.sql"));
        entityManager.flush();
    }

    private String loadResource(String fileName) {
        try{
            return new String(Files.readAllBytes(Paths.get("src/test/resources/" + fileName))).trim();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
