package com.example.webdbdemo.view;

import com.example.webdbdemo.model_spring.User;
import com.example.webdbdemo.model_spring.UserRepository;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@PageTitle("User List")
@Route(value = "user", layout = MainLayout.class)
@Secured("ROLE_Admin")
public class UserView extends HorizontalLayout {

    private Grid<User> userGrid;
    private Text message;
    @Autowired
    private UserRepository userRepo;

    public UserView() {
        add(userGrid = new Grid<>(User.class));
    }

    @PostConstruct
    public void init(){
        Iterable<User> users=userRepo.findAll();
        List<User> userList=new ArrayList<>();
        users.forEach(userList::add);
        userGrid.removeColumnByKey("address");
        userGrid.setItems(userList);
    }
}
