package com.example;

import com.example.db.InitializedTestContainer;
import com.example.dto.CommentDto;
import com.example.dto.TaskDto;
import com.example.dto.UserDto;
import com.example.entity.comment.Comment;
import com.example.entity.comment.CommentEntity;
import com.example.entity.comment.RpComment;
import com.example.entity.elements.Priority;
import com.example.entity.elements.Status;
import com.example.entity.task.RpTask;
import com.example.entity.user.RpUser;
import com.example.exceptions.NoDataException;
import com.jcabi.jdbc.JdbcSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class RpCommentTest extends InitializedTestContainer {
    @Autowired
    private DataSource dataSource;
    private RpComment rpComment;
    private RpUser rpUser;
    private RpTask rpTask;

    @BeforeEach
    public void init() {
        rpUser = new RpUser(dataSource);
        rpTask = new RpTask(dataSource);
        rpComment = new RpComment(dataSource);
    }

//    @Test
//    public void addCommentTest() throws SQLException, NoDataException {
//        JdbcSession jdbcSession = new JdbcSession(dataSource);
//        rpUser = new RpUser(dataSource);
//        UUID userId = rpUser.add(new UserDto(
//                null,
//                "test",
//                "test"
//        )).id();
//
//        UUID taskId = rpTask.add(new TaskDto(
//                null,
//                "test",
//                "test",
//                Status.NEW,
//                Priority.low,
//                userId
//        )).id();
//
//        CommentDto commentDto = new CommentDto(
//                null,
//                "test",
//                taskId,
//                userId
//        );
//
//        Comment comment = rpComment.add(commentDto);
//        Comment expected = jdbcSession
//                .sql("SELECT * FROM comments WHERE id = ?")
//                .set(comment.id())
//                .select((rset, stmt) -> {
//                    if (rset.next()) {
//                        return new CommentEntity(
//                                UUID.fromString(rset.getString("id")),
//                                rset.getString("text"),
//                                UUID.fromString(rset.getString("author_id"))
//                        );
//                    } else {
//                        throw new SQLException("No data found for the given ID");
//                    }
//                });
//
//        assertEquals(expected, comment);
//    }
//
//    @Test
//    public void getCommentTest() throws SQLException, NoDataException {
//        UUID userId = rpUser.add(new UserDto(
//                null,
//                "test",
//                "test"
//        )).id();
//
//        UUID taskId = rpTask.add(new TaskDto(
//                null,
//                "test",
//                "test",
//                Status.NEW,
//                Priority.low,
//                userId
//        )).id();
//
//        CommentDto commentDto = new CommentDto(
//                null,
//                "test",
//                taskId,
//                userId
//        );
//
//        Comment comment = rpComment.add(commentDto);
//        Optional<Comment> expected = rpComment.get(comment.id());
//        assertEquals(expected.get(), comment);
//    }
//
//    @Test
//    public void deleteAllForTaskTest() throws SQLException, NoDataException {
//        UUID userId = rpUser.add(new UserDto(
//                null,
//                "test",
//                "test"
//        )).id();
//
//        UUID taskId = rpTask.add(new TaskDto(
//                null,
//                "test",
//                "test",
//                Status.NEW,
//                Priority.low,
//                userId
//        )).id();
//
//        CommentDto commentDto = new CommentDto(
//                null,
//                "test",
//                taskId,
//                userId
//        );
//        rpComment.add(commentDto);
//        rpComment.deleteAllForTask(taskId);
//
//        Optional<Comment> comment = rpComment.get(commentDto.id());
//        assertEquals(Optional.empty(), comment);
//    }

}
