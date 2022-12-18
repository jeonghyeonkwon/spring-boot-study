package com.jeonghyeon.springbootstudy;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;

public class SimpleListener implements ApplicationListener<ApplicationStartedEvent> {
    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        System.out.println("========================");
        System.out.println("Application is starting");
        System.out.println("========================");
    }
}
