package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);


    private static final ResultSetExtractor<List<User>> USER_RESULT_EXTRACTOR = new ResultSetExtractor<List<User>>() {

        @Override
        public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {

            LinkedHashMap<Integer, User> userMap = new LinkedHashMap<>();

            while (rs.next()) {
                User user = userMap.computeIfAbsent(rs.getInt("id"), id -> {
                            try {
                                User u = new User();
                                u.setId(id);
                                u.setName(rs.getString("name"));
                                u.setEmail(rs.getString("email"));
                                u.setPassword(rs.getString("password"));
                                u.setRegistered(rs.getDate("registered"));
                                u.setEnabled(rs.getBoolean("enabled"));
                                u.setCaloriesPerDay(rs.getInt("calories_per_day"));
                                return u;
                            } catch (SQLException e) {
                                return null;
                            }
                        }
                );

                String role = rs.getString("role");
                if (role != null) {
                    if (user.getRoles() == null) {
                        user.setRoles(EnumSet.noneOf(Role.class));
                    }
                    user.getRoles().add(Role.valueOf(role));
                } else {
                    user.setRoles(EnumSet.noneOf(Role.class));
                }
            }

            return new ArrayList<>(userMap.values());
        }
    };

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;
    private final SimpleJdbcInsert insertRoles;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        this.insertRoles = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("user_roles");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update(
                "UPDATE users SET name=:name, email=:email, password=:password, " +
                        "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource) == 0) {
            return null;
        } else {
            namedParameterJdbcTemplate.update("delete from user_roles WHERE user_id=:id", parameterSource);
        }
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            SqlParameterSource[] arRoles = user.getRoles().stream()
                    .map(role -> new MapSqlParameterSource()
                            .addValue("user_id", user.getId())
                            .addValue("role", role)).toArray(SqlParameterSource[]::new);
            insertRoles.executeBatch(arRoles);
        }
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users u LEFT JOIN user_roles ur on u.id = ur.user_id WHERE id=?", USER_RESULT_EXTRACTOR, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users u LEFT JOIN user_roles ur on u.id = ur.user_id WHERE email=?", USER_RESULT_EXTRACTOR, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM users u LEFT JOIN user_roles ur on u.id = ur.user_id ORDER BY name, email", USER_RESULT_EXTRACTOR);
    }
}
