package com.example.demo.cache;

import com.example.demo.model.task.Task;
import com.example.demo.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class RedisClient {
    private final RedisTemplate<String, Object> template;

    @Autowired
    public RedisClient(RedisTemplate<String, Object> template) {
        this.template = template;
    }

    public void cacheUser(User userToSave) {
        StringBuilder sbKey = new StringBuilder();
        String key = sbKey.append("user_cache::").append(userToSave.getId()).toString();
        template.opsForValue().set(key, userToSave);
        template.expire(key,10, TimeUnit.MINUTES);
    }

    public void cacheTask(Task newTask) {
        StringBuilder sbKey = new StringBuilder();
        String key = sbKey.append("task_cache::").append(newTask.getId()).toString();
        template.opsForValue().set(key, newTask);
        template.expire(key,10, TimeUnit.MINUTES);
    }
}
