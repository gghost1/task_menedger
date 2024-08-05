package com.example.entity.user;

import com.example.dto.PaginationDto;
import com.example.dto.UserDto;
import com.example.entity.task.RpTask;
import com.example.security.SecurityConfig;
import com.jcabi.jdbc.JdbcSession;
import com.jcabi.jdbc.SingleOutcome;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class RpUser {

    public final DataSource dataSource;

    public RpUser(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public User add(UserDto user) throws SQLException {
        JdbcSession jdbcSession = new JdbcSession(dataSource);
        String password = new BCryptPasswordEncoder().encode(user.password());
        UUID id = jdbcSession.sql("""
                        INSERT INTO users (email, password)
                        VALUES (?, ?)
                        """)
                .set(user.email())
                .set(password)
                .insert(new SingleOutcome<>(UUID.class));
        return new UserEntity(
                id,
                user.email(),
                password,
                dataSource
        );
    }

    public Optional<User> get(UUID id) throws SQLException {
        JdbcSession jdbcSession = new JdbcSession(dataSource);
        return jdbcSession
                .sql("""
                        SELECT id, email, password FROM users
                        WHERE id = ?
                        """)
                .set(id)
                .select((rset, stmt) -> {
                    if (rset.next()) {
                        return Optional.of(new UserEntity(
                                UUID.fromString(rset.getString("id")),
                                rset.getString("email"),
                                rset.getString("password"),
                                dataSource
                        ));
                    } else {
                        return Optional.empty();
                    }
                });
    }
    public List<User> getAll(PaginationDto paginationDto) throws SQLException {
        JdbcSession jdbcSession = new JdbcSession(dataSource);
        return jdbcSession
                .sql("""
                        SELECT id, email, password FROM users
                        LIMIT ? OFFSET ?
                        """)
                .set(paginationDto.size())
                .set(paginationDto.offset())
                .select((rset, stmt) -> {
                    List<User> users = new ArrayList<>();
                    while (rset.next()) {
                        users.add(new UserEntity(
                                UUID.fromString(rset.getString("id")),
                                rset.getString("email"),
                                rset.getString("password"),
                                dataSource
                        ));
                    }
                    return users;
                });
    }
    public Optional<User> getByEmail(String email) throws SQLException {
        JdbcSession jdbcSession = new JdbcSession(dataSource);
        return jdbcSession
                .sql("""
                        SELECT id, email, password FROM users
                        WHERE email = ?
                        """)
                .set(email)
                .select((rset, stmt) -> {
                    if (rset.next()) {
                        return Optional.of(new UserEntity(
                                UUID.fromString(rset.getString("id")),
                                rset.getString("email"),
                                rset.getString("password"),
                                dataSource
                        ));
                    } else {
                        return Optional.empty();
                    }
                });
    }
}
