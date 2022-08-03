package com.goit.javacore5.feature.human;

import com.goit.javacore5.feature.storage.Storage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class HumanService {
    private Storage storage;
    private Statement statement;

    public HumanService(Storage storage) throws SQLException {
        this.storage = storage;

        statement = storage.getConnection().createStatement();
    }

    public String getHumanInfo(long id) {
        String sql = "SELECT name, birthday FROM human WHERE id = " + id;

        try(ResultSet rs = statement.executeQuery(sql)) {
            if (rs.next()) {
                String name = rs.getString("name");
                String birthday = rs.getString("birthday");

                return "name: " + name + ", birthday: " + birthday;
            } else {
                return null;
            }
        } catch (Exception ex) {
            return null;
        }
    }

    public void printHumanIds() {
        try(Statement st = storage.getConnection().createStatement()) {
            try(ResultSet rs = st.executeQuery("SELECT id FROM human")) {
                while(rs.next()) {
                    long id = rs.getLong("id");
                    System.out.println("ID: " + id);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void createNewHuman(String name, LocalDate birthday) {
        String insertSql = String.format(
                "INSERT INTO human (name, birthday) VALUES ('%s', '%s')",
                name,
                birthday
        );

        storage.executeUpdate(insertSql);
    }
}
