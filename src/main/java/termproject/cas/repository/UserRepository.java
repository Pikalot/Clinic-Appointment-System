package termproject.cas.repository;

import org.springframework.stereotype.Repository;
import termproject.cas.assembler.UserAssembler;
import termproject.cas.model.Slot;
import termproject.cas.model.User;

import javax.sql.DataSource;
import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class UserRepository {
    private final DataSource dataSource;
    private long nextId = 1L;

    public UserRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<User> findAll() {
        String sql = SQL.FIND_ALL_USERS;
        Map<Long, User> userMap = new HashMap<>();

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet res = statement.executeQuery()
        ) {
            while (res.next()) {
                User user = UserAssembler.fromResultSet(res);
                userMap.put(user.getId(), user);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch all users", e);
        }
        return new ArrayList<>(userMap.values());
    }

    public User findById(Long userId) {
        String sql = SQL.FIND_USER_BY_ID;

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setLong(1, userId);
            ResultSet res = statement.executeQuery();
            if (res.next()) {
                return UserAssembler.fromResultSet(res);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch user by id", e);
        }
        return null;
    }

    public Optional<User> findByUsername(String username) {
        String sql = SQL.FIND_USER_BY_USERNAME;

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, username);
            ResultSet res = statement.executeQuery();
            if (res.next()) {
                return Optional.of(UserAssembler.fromResultSet(res));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch user by username", e);
        }
        return Optional.empty();
    }

    public Optional<User> findByUsernameAndPassword(String username, String password) {
        String sql = SQL.FIND_USER_BY_USERNAME_PASSWORD;

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet res = statement.executeQuery();
            if (res.next()) {
                return Optional.of(UserAssembler.fromResultSet(res));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch user by username and password", e);
        }
        return Optional.empty();
    }

    public String findRoleById(Long userId) {
        String sql = SQL.FIND_ROLE_BY_ID;

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ) {
            statement.setLong(1, userId);
            ResultSet res = statement.executeQuery();

            if (res.next()) {
                String staffRole = res.getString("Staff_Role");
                Long providerId = res.getLong("Provider_ID");

                if (staffRole != null) return staffRole;      // "Admin" or "Staff"
                if (providerId != 0) return "Provider";       // provider found
            }
        }
        catch (SQLException e) {
            throw new RuntimeException("Failed to get role", e);
        }
        return "Unknown";
    }


    // [TO DO]
    public boolean update(User user) {
        return true;
    }

}
