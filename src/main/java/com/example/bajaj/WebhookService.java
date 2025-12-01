package com.example.bajaj;

import com.example.bajaj.dto.GenerateWebhookRequest;
import com.example.bajaj.dto.GenerateWebhookResponse;
import com.example.bajaj.dto.SubmitQueryRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WebhookService {

    private final RestTemplate restTemplate = new RestTemplate();

    public void executeAssignment() {
        System.out.println("Generating webhook...");

        GenerateWebhookRequest generateRequest = new GenerateWebhookRequest(
                "Mohammed Irshadali Syed",
                "22BKT0021",
                "mohammed.irshadali2022@vitstudent.ac.in"
        );

        String generateUrl = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";
        GenerateWebhookResponse webhookResponse = restTemplate.postForObject(generateUrl, generateRequest, GenerateWebhookResponse.class);

        if (webhookResponse == null || webhookResponse.getWebhook() == null || webhookResponse.getAccessToken() == null) {
            System.err.println("Failed to generate webhook.");
            return;
        }

        String webhookUrl = webhookResponse.getWebhook();
        String accessToken = webhookResponse.getAccessToken().trim();

        System.out.println("Successfully generated webhook and received token.");

        String sqlQuery = """
        WITH RankedPayments AS (SELECT p.AMOUNT, e.FIRST_NAME, e.LAST_NAME, e.DOB, d.DEPARTMENT_NAME, ROW_NUMBER() OVER(PARTITION BY d.DEPARTMENT_NAME ORDER BY p.AMOUNT DESC) as rn FROM PAYMENTS p JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID WHERE EXTRACT(DAY FROM p.PAYMENT_TIME) <> 1) SELECT DEPARTMENT_NAME, AMOUNT AS SALARY, CONCAT(FIRST_NAME, ' ', LAST_NAME) AS EMPLOYEE_NAME, TIMESTAMPDIFF(YEAR, DOB, CURDATE()) AS AGE FROM RankedPayments WHERE rn = 1;
        """;

        System.out.println("Submitting SQL query...");

        try {
            SubmitQueryRequest submitRequest = new SubmitQueryRequest(sqlQuery);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", accessToken);

            HttpEntity<SubmitQueryRequest> entity = new HttpEntity<>(submitRequest, headers);

            String submitResponse = restTemplate.postForObject(webhookUrl, entity, String.class);
            System.out.println("Successfully submitted solution!");
            System.out.println("Response from server: " + submitResponse);
        } catch (Exception e) {
            System.err.println("An error occurred while submitting the solution: " + e.getMessage());
        }
    }
}