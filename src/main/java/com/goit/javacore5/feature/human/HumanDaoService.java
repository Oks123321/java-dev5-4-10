package com.goit.javacore5.feature.human;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HumanDaoService {
    private PreparedStatement createSt;
    private PreparedStatement getByIdSt;
    private PreparedStatement selectMaxIdSt;
    private PreparedStatement getAllSt;
    private PreparedStatement updateSt;
    private PreparedStatement deleteByIdSt;
    private PreparedStatement existsByIdSt;
    private PreparedStatement clearSt;
    private PreparedStatement searchSt;

    public HumanDaoService(Connection connection) throws SQLException {
        createSt = connection.prepareStatement(
                "INSERT INTO human (name, birthday, gender) VALUES(?, ?, ?)"
        );

        getByIdSt = connection.prepareStatement(
                "SELECT name, birthday, gender FROM human WHERE id = ?"
        );

        selectMaxIdSt = connection.prepareStatement(
                "SELECT max(id) AS maxId FROM human"
        );

        getAllSt = connection.prepareStatement(
                "SELECT id, name, birthday, gender FROM human"
        );

        updateSt = connection.prepareStatement(
                "UPDATE human SET name = ?, birthday = ?, gender = ? WHERE id = ?"
        );

        deleteByIdSt = connection.prepareStatement(
                "DELETE FROM human WHERE id = ?"
        );

        existsByIdSt = connection.prepareStatement(
                "SELECT count(*) > 0 AS humanExists FROM human WHERE id = ?"
        );

        clearSt = connection.prepareStatement(
                "DELETE FROM human"
        );

        searchSt = connection.prepareStatement(
                "SELECT id, name, birthday, gender FROM human WHERE name LIKE ?"
        );
    }

    public long create(Human human) throws SQLException {
        createSt.setString(1, human.getName());
        createSt.setString(2,
                human.getBirthday() == null ? null : human.getBirthday().toString());
        createSt.setString(3,
                human.getGender() == null ? null : human.getGender().name());
        createSt.executeUpdate();

        long id;

        try(ResultSet rs = selectMaxIdSt.executeQuery()) {
            rs.next();
            id = rs.getLong("maxId");
        }

        return id;
    }

    public Human getById(long id) throws SQLException {
        getByIdSt.setLong(1, id);

        try(ResultSet rs = getByIdSt.executeQuery()) {
            if (!rs.next()) {
                return null;
            }

            Human result = new Human();
            result.setId(id);
            result.setName(rs.getString("name"));

            String birthday = rs.getString("birthday");
            if (birthday != null) {
                result.setBirthday(LocalDate.parse(birthday));
            }

            String gender = rs.getString("gender");
            if (gender != null) {
                result.setGender(Human.Gender.valueOf(gender));
            }

            return result;
        }
    }

    public List<Human> getAll() throws SQLException {
        return getHumans(getAllSt);
    }

    public void update(Human human) throws SQLException {
        updateSt.setString(1, human.getName());
        updateSt.setString(2, human.getBirthday().toString());
        updateSt.setString(3, human.getGender().name());
        updateSt.setLong(4, human.getId());

        updateSt.executeUpdate();
    }

    public void deleteById(long id) throws SQLException {
        deleteByIdSt.setLong(1, id);
        deleteByIdSt.executeUpdate();
    }

    public boolean exists(long id) throws SQLException {
        existsByIdSt.setLong(1, id);
        try(ResultSet rs = existsByIdSt.executeQuery()) {
            rs.next();

            return rs.getBoolean("humanExists");
        }
    }

    public long save(Human human) throws SQLException {
        if (exists(human.getId())) {
            update(human);
            return human.getId();
        }

        return create(human);
    }

    public void clear() throws SQLException {
        clearSt.executeUpdate();
    }

    public List<Human> searchByName(String query) throws SQLException {
        searchSt.setString(1, "%" + query + "%");

        return getHumans(searchSt);
    }

    private List<Human> getHumans(PreparedStatement st) throws SQLException {
        try(ResultSet rs = st.executeQuery()) {
            List<Human> result = new ArrayList<>();

            while (rs.next()) {
                Human human = new Human();
                human.setId(rs.getLong("id"));
                human.setName(rs.getString("name"));

                String birthday = rs.getString("birthday");
                if (birthday != null) {
                    human.setBirthday(LocalDate.parse(birthday));
                }

                String gender = rs.getString("gender");
                if (gender != null) {
                    human.setGender(Human.Gender.valueOf(gender));
                }

                result.add(human);
            }

            return result;
        }
    }
}
