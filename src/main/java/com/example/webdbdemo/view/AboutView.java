package com.example.webdbdemo.view;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.example.webdbdemo.layout.MainLayout;

@PageTitle("About")
@Route(value = "about", layout = MainLayout.class)
public class AboutView extends Div {

    public AboutView() {
        add(new Text("Ez egy egyszerű Vaadin - Spring Boot stacket bemutató alkalmazás"));
    }
}

