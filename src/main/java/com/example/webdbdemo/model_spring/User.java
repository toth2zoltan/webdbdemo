package com.example.webdbdemo.model_spring;

import javax.persistence.*;
import java.util.List;

// Az annotációkról részletesen: https://docs.oracle.com/javaee/7/tutorial/persistence-intro001.htm
// https://www.tutorialspoint.com/spring/spring_jdbc_framework.htm

@Entity
public class User {
    @Id // ez az attributum az azonosító
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id; // Objektum típus lehet csak
    @Column(length=60)
    private String name;
    private String email;
    @OneToMany(fetch=FetchType.EAGER)
    private List<Address> address;

    public User(){ // no args constructor kell
    }

    public User(Integer id, String name, String email){
        this.id=id;
        this.name=name;
        this.email=email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString(){
      return Integer.toString(id) + ','+ name + ','+email;
    }

    public List<Address> getAddress() {
        return address;
    }

    public void setAddress(List<Address> address) {
        this.address = address;
    }
}
