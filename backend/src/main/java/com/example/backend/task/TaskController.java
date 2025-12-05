package com.example.backend.task;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "http://localhost:3000") // React からのアクセスを許可
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // 一覧取得: GET /api/tasks
    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAll();
    }

    // 追加: POST /api/tasks  { "title": "xxxx" }
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task request) {
        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Task created = taskService.createTask(request.getTitle().trim());
        return ResponseEntity.ok(created);
    }

    // 更新: PUT /api/tasks/{id}  { "id": 1, "title": "xxx", "done": true }
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task request) {
        return taskService.updateTask(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 削除: DELETE /api/tasks/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
