package com.example.webdbdemo.layout;

import com.example.webdbdemo.model_spring.User;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

public class UserForm extends FormLayout {
    TextField name = new TextField("Name");
    EmailField email = new EmailField("Email");
    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    Binder<User> binder = new BeanValidationBinder<>(User.class);
    User user;

    public UserForm() {
        binder.bindInstanceFields(this);

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

        HorizontalLayout hz= new HorizontalLayout(save, delete, close);

        add(name,email,hz);

    }

    public static class Event extends ComponentEvent<UserForm> {
        private User user;

        Event(UserForm source) {
            super(source, false);
            this.user=source.user;
        }

        public User getUser(){
            return user;
        }
    }

    public static class SaveEvent extends Event{
        SaveEvent(UserForm source){
            super(source);
        }
    }

    public static class DeleteEvent extends Event{
        DeleteEvent(UserForm source){
            super(source);
        }
    }

    public static class CloseEvent extends Event{
        CloseEvent(UserForm source){
            super(source);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

    public void setUser(User user){
        this.user=user;
        binder.readBean(user);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(user);
            fireEvent(new SaveEvent(this));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }
}
