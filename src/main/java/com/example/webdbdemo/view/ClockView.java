package com.example.webdbdemo.view;

import com.example.webdbdemo.model_spring.User;
import com.example.webdbdemo.model_spring.UserRepository;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
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
    private Button pause;

    public ClockView() {
        add(new Html("""
            <div><p id="clock_element">Idő<p><div>
            """));
        add(pause=new Button("Resume",this::pauseOnClick));
    }

    void pauseOnClick(ClickEvent event){
        if(pause.getText().equals("Pause")){
            pause.setText("Resume");
            UI.getCurrent().getPage().executeJs("document.getElementById('clock_element').stopTimer();");
        }else{
            pause.setText("Pause");
            UI.getCurrent().getPage().executeJs("document.getElementById('clock_element').startTimer();");
        }

    }

    @PostConstruct
    public void init(){

        String string = """
                
                remainingTime=10*60*1000; // 10 perc
                startTime=null;
                
                function showTime(){
                    if(startTime==null){
                        actualTime=remainingTime;
                    }else{
                        actualTime=(startTime-new Date())+remainingTime
                    }
                    document.getElementById('clock_element').innerHTML=new Date(0,0,0,0,0,0,actualTime).toLocaleTimeString("it-IT");
                    setTimeout(showTime,1000);
                 }
                 
                 function startTimer(){
                    startTime=new Date();
                 }

                 function stopTimer(){
                    remainingTime=(startTime-new Date())+remainingTime;
                    startTime=null;
                 }
                 
                 clock=document.getElementById('clock_element')
                 clock.startTimer=startTimer
                 clock.stopTimer=stopTimer
                 
                 showTime();
                 """;
        UI.getCurrent().getPage().executeJs(string);
    }
}
