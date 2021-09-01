package com.example.webdbdemo.view;

import com.example.webdbdemo.model_spring.User;
import com.example.webdbdemo.model_spring.UserRepository;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@PageTitle("Clock")
@Route(value = "clock", layout = MainLayout.class)
public class ClockView extends HorizontalLayout {


    public ClockView() {
        add(new Html("""
            <div><p id="clock_element">Idő<p><div>
            """));
    }

    @PostConstruct
    public void init(){

        String string = """
                function showTime(){
                    date=new Date().toLocaleTimeString("it-IT");
                    document.getElementById('clock_element').innerHTML=date;
                    setTimeout(showTime,1000);
                 }
                 showTime();""";
        UI.getCurrent().getPage().executeJs(string);
    }
}
