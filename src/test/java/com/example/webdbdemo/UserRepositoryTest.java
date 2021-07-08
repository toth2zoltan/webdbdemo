package com.example.webdbdemo;

import com.example.webdbdemo.model_jdbc.User;
import com.example.webdbdemo.model_jdbc.UserRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class UserRepositoryTest {
    @Test
    void findById() {
        UserRepository ur=new UserRepository();
        Optional<User> u=ur.findById(1); // Az Optional olyan objektum, ami vagy semmit vagy egy objektumot tartalmaz
        if(u.isPresent()){
            System.out.println(u.get());
        }
    }

    @Test
    void findAll() {
        UserRepository ur=new UserRepository();
        Iterable<User> ul=ur.findAll();
        for(User u: ul){
            System.out.println(u.toString());
        }
    }

    @Test
    void delete() {
        UserRepository ur=new UserRepository();
        Optional<User> u=ur.findById(1);
        if(u.isPresent()) {
            ur.delete(u.get());
        }
    }

    @Test
    void save_update() {
        UserRepository ur=new UserRepository();
        Optional<User> u=ur.findById(1);
        if(u.isPresent()) {
            u.get().setEmail("huhu@huhu.hu");
            ur.save(u.get());
        }
    }

    @Test
    void save_insert() {
        UserRepository ur=new UserRepository();
        for(int i=0;i<10;i++) {
            User u = new User(0, "Maci Laci" + i, "hehe@hehe.he" + i);
            ur.save(u);
        }
    }
}
