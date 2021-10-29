package com.example.webdbdemo.view;

import ch.qos.logback.core.Layout;
import com.example.webdbdemo.layout.MainLayout;
import com.example.webdbdemo.layout.UserForm;
import com.example.webdbdemo.model_spring.User;
import com.example.webdbdemo.model_spring.UserRepository;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@PageTitle("User List")
@Route(value = "user", layout = MainLayout.class)
@Secured("ROLE_Admin")
@Component
public class UserView extends HorizontalLayout {

    private Grid<User> userGrid;
    private Button addUser;
    private UserForm userForm;

    @Autowired
    private UserRepository userRepo;

    public UserView() {
        userGrid = new Grid<>(User.class);
        userGrid.removeColumnByKey("address");
        userGrid.asSingleSelect().addValueChangeListener(event -> editUser(event.getValue()));
        userGrid.setWidth("50em");

        addUser=new Button("Add user");
        addUser.addClickListener(event -> adduser());

        userForm=new UserForm();

        userForm.setWidth("25em");
        userForm.addListener(UserForm.SaveEvent.class, this::saveUser);
        userForm.addListener(UserForm.DeleteEvent.class, this::deleteUser);
        userForm.addListener(UserForm.CloseEvent.class, e -> closeEditor());
        userForm.setVisible(false);

        VerticalLayout vl = new VerticalLayout(addUser,new HorizontalLayout(userGrid,userForm));

        add(vl);
    }

    @PostConstruct
    public void init(){
        updateList();
    }

    public void editUser(User user) {
        if (user == null) {
            closeEditor();
        } else {
            userForm.setUser(user);
            userForm.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        userForm.setUser(null);
        userForm.setVisible(false);
        removeClassName("editing");
    }

    private void adduser() {
        userGrid.asSingleSelect().clear();
        editUser(new User());
    }

    private void updateList() {
        Iterable<User> users=userRepo.findAll();
        List<User> userList=new ArrayList<>();
        users.forEach(userList::add);
        userGrid.setItems(userList);
    }

    private void saveUser(UserForm.SaveEvent event) {
        userRepo.save(event.getUser());
        updateList();
        closeEditor();
    }

    private void deleteUser(UserForm.DeleteEvent event) {
        userRepo.delete(event.getUser());
        updateList();
        closeEditor();
    }
}
