package com.example.webdbdemo;

import com.example.webdbdemo.model_spring.User;
import com.example.webdbdemo.model_spring.UserRepository;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;

/* A spring annotációk biztosítják a tesztkörnyezet inicilizálását, részletesebben:
 https://www.codejava.net/frameworks/spring-boot/junit-tests-for-spring-data-jpa
*/

@DataJpaTest
@Rollback(false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {
    @Autowired
    private TestEntityManager entityManager; // Spring automatikusan hozza létre az objektumot

    @Autowired
    private UserRepository ur; // Spring automatikusan hozza létre az objektumot

    @Test
    void findById() {
        Optional<User> u=ur.findById(1);
        u.ifPresent(System.out::println);
    }

    @Test
    void findAll() {
        Iterable<User> ul=ur.findAll();
        for(User u: ul){
            System.out.println(u.toString());

        }
    }

    @Test
    void delete() {
        Optional<User> u=ur.findById(1);
        u.ifPresent(user -> ur.delete(user));
    }

    @Test
    void save_update() {
        Optional<User> u=ur.findById(1);
        if(u.isPresent()) {
            u.get().setEmail("huhu@huhu.hu");
            ur.save(u.get());
        }
    }

    @Test
    void save_insert() {
        for(int i=0;i<10;i++) {
            User u = new User(null, "Maci Laci" + i, "hehe@hehe.he" + i);
            ur.save(u);
        }
    }
}
