package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.AbstractNamedEntity;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepositoryImpl implements UserRepository {

    private ResultSetExtractor<List<User>> ROW_MAPPER = rs -> {
        Map<Integer, User> map = new HashMap<>();
        while (rs.next()) {
            User user = new User(rs.getInt("id"), rs.getString("name"), rs.getString("email"),
                    rs.getString("password"), rs.getInt("calories_per_day"),
                    rs.getBoolean("enabled"), rs.getDate("registered"),
                    EnumSet.of(Role.valueOf(rs.getString(5))));
            map.putIfAbsent(user.getId(), user);
            if (map.containsKey(user.getId()))
                map.get(user.getId()).addRoles(user.getRoles());
        }
        List<User> list = new ArrayList<>(map.values());
        list.sort(Comparator.comparing(AbstractNamedEntity::getName));
        return list;
    };

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepositoryImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

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
            for (Role role : user.getRoles())
                jdbcTemplate.update("INSERT INTO user_roles (user_id, role) VALUES (?, ?)", user.getId(), role.name());

        } else if (namedParameterJdbcTemplate.update(
                "UPDATE users SET name=:name, email=:email, password=:password, " +
                        "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource) == 0) {
            for (Role role : user.getRoles())
                jdbcTemplate.update("UPDATE user_roles SET user_id = ?, role = ? WHERE user_id = ?", user.getId(), role.name(), user.getId());
            return null;
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
        List<User> users = jdbcTemplate.query("SELECT id,email,name,enabled,user_roles.role,calories_per_day,registered,password " +
                "FROM users LEFT JOIN user_roles ON user_roles.user_id=users.id WHERE id=?", ROW_MAPPER, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT id,email,name,enabled,user_roles.role,calories_per_day,registered,password " +
                "FROM users LEFT JOIN user_roles ON user_roles.user_id=users.id WHERE email=?", ROW_MAPPER, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT id,email,name,enabled,user_roles.role,calories_per_day,registered,password " +
                "FROM users LEFT JOIN user_roles ON user_roles.user_id=users.id ORDER BY name, email", ROW_MAPPER);
    }
}
