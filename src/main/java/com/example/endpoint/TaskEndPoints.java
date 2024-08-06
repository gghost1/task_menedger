package com.example.endpoint;


import com.example.data.request.CommentEntityRequest;
import com.example.data.request.PaginationEntityRequest;
import com.example.data.entity.comment.RpComment;
import com.example.data.entity.elements.Priority;
import com.example.data.entity.elements.Status;
import com.example.data.entity.task.RpTask;
import com.example.data.entity.task.Task;
import com.example.data.entity.user.RpUser;
import com.example.configuration.exception.NoDataException;
import com.example.configuration.exception.NotAvailableException;
import com.example.data.response.BasicResponse;
import com.example.data.response.entityResponse.TaskEntityResponse;
import com.example.configuration.security.JwtUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
@AllArgsConstructor
@Tag(name = "Tasks", description = "API for task operations")
public class TaskEndPoints {

    RpTask rpTask;
    RpUser rpUser;
    JwtUtils jwtUtils;
    RpComment rpComment;

    @GetMapping("/")
    @Operation(summary = "Get all tasks with pagination", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> getTasks(@Valid PaginationEntityRequest paginationEntityRequest, HttpServletRequest request) throws SQLException {
        String jwt = jwtUtils.getJwtToken(request);

        return ResponseEntity.ok(new BasicResponse<>(jwt, TaskEntityResponse.from(rpTask.getAll(paginationEntityRequest))));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get specified task by id", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> getTask(@PathVariable UUID id, HttpServletRequest request) throws SQLException, NoDataException {
        String jwt = jwtUtils.getJwtToken(request);

        Optional<Task> task = rpTask.get(id);
        if (task.isEmpty()) {
            throw new NoDataException("Task not found");
        }

        return ResponseEntity.ok(new BasicResponse<>(jwt, TaskEntityResponse.from(task.get())));
    }

    @PostMapping("/{id}/addComment")
    @Operation(summary = "Add comment to specified task by id", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> addComment(@PathVariable UUID id, @Valid @RequestBody CommentEntityRequest commentEntityRequest, HttpServletRequest request) throws SQLException, NoDataException {
        String jwt = jwtUtils.getJwtToken(request);
        rpComment.add(commentEntityRequest, UUID.fromString(jwtUtils.getIdFromJwtToken(jwt)), id);
        Optional<Task> task = rpTask.get(id);
        if (task.isEmpty()) {
            throw new NoDataException("Task not found");
        }
        return ResponseEntity.ok(new BasicResponse<>(jwt, TaskEntityResponse.from(task.get())));
    }

    @PostMapping("/{id}/assign")
    @Operation(summary = "Assign user to specified task by id", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> assign(@PathVariable UUID id, @RequestBody UUID userToAssign_id, HttpServletRequest request) throws SQLException, NoDataException, NotAvailableException {
        String jwt = jwtUtils.getJwtToken(request);
        Task task = rpTask.assign(id, userToAssign_id, UUID.fromString(jwtUtils.getIdFromJwtToken(jwt)));
        return ResponseEntity.ok(new BasicResponse<>(jwt, TaskEntityResponse.from(task)));
    }

    @PostMapping("/{id}/editTitle")
    @Operation(summary = "Edit title of specified task by id", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> editTitle(@PathVariable UUID id, @RequestBody String title, HttpServletRequest request) throws SQLException, NoDataException, NotAvailableException {
        String jwt = jwtUtils.getJwtToken(request);
        Task task = rpTask.editTitle(id, title, UUID.fromString(jwtUtils.getIdFromJwtToken(jwt)));
        return ResponseEntity.ok(new BasicResponse<>(jwt, TaskEntityResponse.from(task)));
    }

    @PostMapping("/{id}/editDescription")
    @Operation(summary = "Edit description of specified task by id", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> editDescription(@PathVariable UUID id, @RequestBody String description, HttpServletRequest request) throws SQLException, NoDataException, NotAvailableException {
        String jwt = jwtUtils.getJwtToken(request);
        Task task = rpTask.editDescription(id, description, UUID.fromString(jwtUtils.getIdFromJwtToken(jwt)));
        return ResponseEntity.ok(new BasicResponse<>(jwt, TaskEntityResponse.from(task)));
    }

    @PostMapping("/{id}/editStatus")
    @Operation(summary = "Edit status of specified task by id", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> editStatus(@PathVariable UUID id, @RequestBody String status, HttpServletRequest request) throws SQLException, NoDataException, NotAvailableException {
        String jwt = jwtUtils.getJwtToken(request);
        try {
            Status.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new NoDataException("Status not found");
        }
        Task task = rpTask.editStatus(id, Status.valueOf(status), UUID.fromString(jwtUtils.getIdFromJwtToken(jwt)));
        return ResponseEntity.ok(new BasicResponse<>(jwt, TaskEntityResponse.from(task)));
    }

    @PostMapping("/{id}/editPriority")
    @Operation(summary = "Edit priority of specified task by id", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> editPriority(@PathVariable UUID id, @RequestBody String priority, HttpServletRequest request) throws SQLException, NoDataException, NotAvailableException {
        String jwt = jwtUtils.getJwtToken(request);
        try {
            Priority.valueOf(priority);
        } catch (IllegalArgumentException e) {
            throw new NoDataException("Priority not found");
        }
        Task task = rpTask.editPriority(id, Priority.valueOf(priority), UUID.fromString(jwtUtils.getIdFromJwtToken(jwt)));
        return ResponseEntity.ok(new BasicResponse<>(jwt, TaskEntityResponse.from(task)));
    }

    @PostMapping("/{id}/delete")
    @Operation(summary = "Delete specified task by id", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> delete(@PathVariable UUID id, HttpServletRequest request) throws SQLException, NoDataException, NotAvailableException {
        String jwt = jwtUtils.getJwtToken(request);
        rpTask.delete(id, UUID.fromString(jwtUtils.getIdFromJwtToken(jwt)));
        return ResponseEntity.ok(new BasicResponse<>(jwt, "Task deleted successfully"));
    }
}