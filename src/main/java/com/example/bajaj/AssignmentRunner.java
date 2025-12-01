package com.example.bajaj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AssignmentRunner implements CommandLineRunner {

    private final WebhookService webhookService;

    @Autowired
    public AssignmentRunner(WebhookService webhookService) {
        this.webhookService = webhookService;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Application started, executing assignment logic...");
        webhookService.executeAssignment();
        System.out.println("Assignment logic finished.");
    }
}