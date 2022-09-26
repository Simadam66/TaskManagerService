package com.example.demo.task;

import com.example.demo.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;

    @Autowired 
    public TaskService(TaskRepository taskRepository, UserService userService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    public List<Task> getUserTasks(Long userId){
        return userService.getUser(userId).getTasks();
    }

    // TODO: atnez
    public Task getTask(Long userId, Long taskId) {
        Optional<List<Task>> UserTasks = Optional.of(getUserTasks(userId));

        taskRepository.findById(taskId)
                .orElseThrow( () -> new IllegalStateException("task with id " + taskId + " does not exist"));

        Optional<Task> task = UserTasks.get().stream().filter( t -> t.getId().equals(taskId)).findFirst();
        if (task.isEmpty()){
            throw new IllegalStateException("this user does not have a task with id: " + taskId);
        }

        return task.get();
    }

    public Task addNewTask(Long userId, TaskRequest taskRequest) {
        Task newTask = Task.of(taskRequest);
        userService.getUser(userId).addTask(newTask);
        taskRepository.save(newTask);
        return newTask;
    }

    // TODO: atnez
    public void deleteTask(Long userId, Long taskId) {
        Task task = getTask(userId, taskId);
        userService.removeUserTask(userId, task);
        taskRepository.deleteById(taskId);
    }

    // TODO: transactional userre megvaltoztat
    @Transactional
    public Task updateTask(Long userId, Long taskId, TaskRequest taskRequest) {
        Task taskToUpdate = getTask(userId, taskId);
        taskToUpdate.update(taskRequest);
        taskRepository.save(taskToUpdate);
        return taskToUpdate;
    }

}
