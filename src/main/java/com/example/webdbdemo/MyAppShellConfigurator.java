package com.example.webdbdemo;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import org.springframework.context.annotation.Configuration;

@Configuration
@PWA(name = "WebDbDemo", shortName = "WebDBDemo")
public class MyAppShellConfigurator implements AppShellConfigurator {
}
