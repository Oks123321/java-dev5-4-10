package com.goit.javacore5.feature.human;

import com.goit.javacore5.feature.storage.Storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HumanServiceV2 {
    private Connection conn;
    private PreparedStatement insertSt;
    private PreparedStatement selectByIdSt;
    private PreparedStatement selectAllSt;
    private PreparedStatement renameSt;

    public HumanServiceV2(Storage storage) throws SQLException {
        conn = storage.getConnection();

        insertSt = conn.prepareStatement(
                "INSERT INTO human (name, birthday) VALUES(?, ?)"
        );
        selectByIdSt = conn.prepareStatement(
                "SELECT name, birthday FROM human WHERE id = ?"
        );
        selectAllSt = conn.prepareStatement("SELECT id FROM human");

        renameSt = conn.prepareStatement("UPDATE human SET name = ? WHERE name = ?");
    }

    public void createNewHumans(String[] names, LocalDate[] birthdays) throws SQLException {
        for (int i = 0; i < names.length; i++) {
            String name = names[i];
            LocalDate birthday = birthdays[i];

            insertSt.setString(1, name);
            insertSt.setString(2, birthday.toString());

            insertSt.addBatch();
        }

        insertSt.executeBatch();
    }

    public boolean createNewHuman(String name, LocalDate birthday) {
        try {
            insertSt.setString(1, name);
            insertSt.setString(2, birthday.toString());
            return insertSt.executeUpdate() == 1;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return false;
    }

    public String getHumanInfo(long id) {
        try {
            selectByIdSt.setLong(1, id);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

        try(ResultSet rs = selectByIdSt.executeQuery()) {
            if (!rs.next()) {
                System.out.println("Human with id " + id + " not found!");
                return null;
            }

            String name = rs.getString("name");
            String birthday = rs.getString("birthday");

            return "name: " + name + ", birthday: " + birthday;
        } catch (Exception ex) {
            return null;
        }
    }

    public List<Long> getIds() {
        List<Long> result = new ArrayList<>();

        try (ResultSet rs = selectAllSt.executeQuery()) {
               while(rs.next()) {
                   result.add(rs.getLong("id"));
               }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return result;
    }


    public void rename(Map<String, String> renameMap) throws SQLException {
        conn.setAutoCommit(false);

        for (Map.Entry<String, String> keyValue : renameMap.entrySet()) {
            renameSt.setString(1, keyValue.getKey());
            renameSt.setString(2, keyValue.getKey());

            renameSt.addBatch();
        }

        try {
            renameSt.executeBatch();

            conn.commit();
        } catch (Exception ex) {
            conn.rollback();
        } finally {
            conn.setAutoCommit(true);
        }
    }
}
