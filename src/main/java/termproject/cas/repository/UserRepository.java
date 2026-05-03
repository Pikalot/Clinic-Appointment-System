package termproject.cas.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import termproject.cas.assembler.UserAssembler;
import termproject.cas.model.User;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<User> findAll() {
        String sql = SQL.FIND_ALL_USERS;
        Map<Long, User> userMap = new HashMap<>();

        jdbcTemplate.query(sql, res -> {
            User user = UserAssembler.fromResultSet(res);
            userMap.put(user.getId(), user);
        });

        return new ArrayList<>(userMap.values());
    }

    public Optional<User> findById(Long userId) {
        String sql = SQL.FIND_USER_BY_ID;

        return jdbcTemplate.query(sql, res -> {
            if (!res.next()) return Optional.empty();
            return Optional.of(UserAssembler.fromResultSet(res));
        });
    }

    public Optional<User> findByUsername(String username) {
        String sql = SQL.FIND_USER_BY_USERNAME;

        return jdbcTemplate.query(sql, res -> {
            if (!res.next()) return Optional.empty();
            return Optional.of(UserAssembler.fromResultSet(res));
        }, username);
    }

    public Optional<User> findByUsernameAndPassword(String username, String password) {
        String sql = SQL.FIND_USER_BY_USERNAME_PASSWORD;

        return jdbcTemplate.query(sql, res -> {
            if (!res.next()) return  Optional.empty();
            return Optional.of(UserAssembler.fromResultSet(res));
        }, username, password);
    }

    public String findRoleById(Long userId) {
        String sql = SQL.FIND_ROLE_BY_ID;
        String[] result = {"Unknown"};

        jdbcTemplate.query(sql, res -> {
            String staffRole = res.getString("Staff_Role");
            Long providerId = res.getLong("Provider_ID");

            if (staffRole != null) result[0] = staffRole;      // "Admin" or "Staff"
            else if (providerId != 0) result[0] = "Provider";
        }, userId);

        return result[0];
    }


    // [TO DO]
    public boolean update(User user) {
        return true;
    }

}
