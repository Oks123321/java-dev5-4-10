package com.goit.javacore5.feature.human;

import com.goit.javacore5.feature.storage.DatabaseInitService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class HumanDaoServiceTests {
    private Connection connection;
    private HumanDaoService daoService;

    @BeforeEach
    public void beforeEach() throws SQLException {
        final String connectionUrl = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
        new DatabaseInitService().initDb(connectionUrl);
        connection = DriverManager.getConnection(connectionUrl);
        daoService = new HumanDaoService(connection);
        daoService.clear();
    }

    @AfterEach
    public void afterEach() throws SQLException {
        connection.close();
    }

    @Test
    public void testThatHumanCreatedCorrectly() throws SQLException {
        List<Human> originalHumans = new ArrayList<>();

        //Set up
        Human fullValueHuman = new Human();
        fullValueHuman.setName("TestName");
        fullValueHuman.setBirthday(LocalDate.now());
        fullValueHuman.setGender(Human.Gender.male);
        originalHumans.add(fullValueHuman);

        Human nullBirthdayHuman = new Human();
        nullBirthdayHuman.setName("TestName 1");
        nullBirthdayHuman.setBirthday(null);
        nullBirthdayHuman.setGender(Human.Gender.male);
        originalHumans.add(nullBirthdayHuman);

        Human nullGenderHuman = new Human();
        nullGenderHuman.setName("TestName 1");
        nullGenderHuman.setBirthday(LocalDate.now());
        nullGenderHuman.setGender(null);
        originalHumans.add(nullGenderHuman);

        for (Human original : originalHumans) {
            //Do
            long id = daoService.create(original);
            Human saved = daoService.getById(id);

            //Assert
            Assertions.assertEquals(id, saved.getId());
            Assertions.assertEquals(original.getName(), saved.getName());
            Assertions.assertEquals(original.getBirthday(), saved.getBirthday());
            Assertions.assertEquals(original.getGender(), saved.getGender());
        }
    }

    @Test
    public void getAllTest() throws SQLException {
        //Set up
        Human expected = new Human();
        expected.setName("TestName");
        expected.setBirthday(LocalDate.now());
        expected.setGender(Human.Gender.male);

        long id = daoService.create(expected);
        expected.setId(id);

        List<Human> expectedHumans = Collections.singletonList(expected);
        List<Human> actualHumans = daoService.getAll();

        Assertions.assertEquals(expectedHumans, actualHumans);
    }

    @Test
    public void testUpdate() throws SQLException {
        //TODO test with birthday=null, gender=null
        //Set up
        Human original = new Human();
        original.setName("TestName");
        original.setBirthday(LocalDate.now());
        original.setGender(Human.Gender.male);

        long id = daoService.create(original);
        original.setId(id);

        //Update
        original.setName("New Name");
        original.setBirthday(LocalDate.now().plusDays(1));
        original.setGender(Human.Gender.female);
        daoService.update(original);

        //Get by id and compare
        Human updated = daoService.getById(id);
        Assertions.assertEquals(id, updated.getId());
        Assertions.assertEquals("New Name", updated.getName());
        Assertions.assertEquals(LocalDate.now().plusDays(1), updated.getBirthday());
        Assertions.assertEquals(Human.Gender.female, updated.getGender());
    }

    @Test
    public void testDelete() throws SQLException {
        //Set up
        Human expected = new Human();
        expected.setName("TestName");
        expected.setBirthday(LocalDate.now());
        expected.setGender(Human.Gender.male);

        long id = daoService.create(expected);
        daoService.deleteById(id);

        Assertions.assertNull(daoService.getById(id));
    }

    @Test
    public void testExists() throws SQLException {
        Human expected = new Human();
        expected.setName("TestName");
        expected.setBirthday(LocalDate.now());
        expected.setGender(Human.Gender.male);

        long id = daoService.create(expected);
        Assertions.assertTrue(daoService.exists(id));
    }

    @Test
    public void testThatExistsReturnsFalseForNonExistingHuman() throws SQLException {
        Assertions.assertFalse(daoService.exists(-1));
    }

    @Test
    public void testSaveOnNewUser() throws SQLException {
        Human newHuman = new Human();
        newHuman.setName("TestName");
        newHuman.setBirthday(LocalDate.now());
        newHuman.setGender(Human.Gender.male);

        long id = daoService.save(newHuman);
        Assertions.assertTrue(daoService.exists(id));
    }

    @Test
    public void testSaveOnExistingUser() throws SQLException {
        Human newHuman = new Human();
        newHuman.setName("TestName");
        newHuman.setBirthday(LocalDate.now());
        newHuman.setGender(Human.Gender.male);

        long id = daoService.save(newHuman);
        newHuman.setId(id);

        newHuman.setName("New Name");
        daoService.save(newHuman);

        Human updated = daoService.getById(id);
        Assertions.assertEquals("New Name", updated.getName());
    }

    @Test
    public void testSearchOnEmpty() throws SQLException {
        Assertions.assertEquals(
                Collections.emptyList(),
                daoService.searchByName("name")
        );
    }

    @Test
    public void testSearchOnFilledDb() throws SQLException {
        Human newHuman = new Human();
        newHuman.setName("TestName");
        newHuman.setBirthday(LocalDate.now());
        newHuman.setGender(Human.Gender.male);

        long id = daoService.save(newHuman);

        List<Human> actual = daoService.searchByName("Test");
        Assertions.assertEquals(1, actual.size());
        Assertions.assertEquals(id, actual.get(0).getId());
    }
}