package com.example.backend.task;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    @Transactional
    public Task createTask(String title) {
        Task task = new Task(title);
        return taskRepository.save(task);
    }

    @Transactional
    public Optional<Task> updateTask(Long id, Task updated) {
        return taskRepository.findById(id).map(existing -> {
            existing.setTitle(updated.getTitle());
            existing.setDone(updated.isDone());
            // ここで save() しても良いが、@Transactional なので戻り値をそのまま返してOK
            return existing;
        });
    }

    @Transactional
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}
