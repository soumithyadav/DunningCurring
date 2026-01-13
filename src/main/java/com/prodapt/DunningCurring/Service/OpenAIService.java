package com.prodapt.DunningCurring.Service;

import com.prodapt.DunningCurring.DAO.CustomerRepository;
import com.prodapt.DunningCurring.DAO.TelecomServiceRepository;
import com.prodapt.DunningCurring.Entity.Customer;
import com.prodapt.DunningCurring.Entity.TelecomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OpenAIService {

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.model}")
    private String model;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TelecomServiceRepository serviceRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getChatResponse(Long customerId, String userMessage) {
        
        //  Fetch Customer Context
        Customer customer = customerRepository.findById(customerId).orElse(null);
        String contextInfo = "User is anonymous.";
        
        if (customer != null && !customer.getServices().isEmpty()) {
            TelecomService service = customer.getServices().get(0); 
            
            // Handle Null Dates gracefully
            String dueDate = (service.getNextDueDate() != null) 
                             ? service.getNextDueDate().toString() 
                             : "Not Scheduled";
            contextInfo = String.format(
                "Context Data:\n" +
                "- Customer Name: %s\n" +
                "- Service Status: %s\n" +
                "- Current Overdue Amount: $%s\n" +
                "- Service Valid Until / Next Bill Date: %s\n", 
                customer.getName(),
                service.getStatus(),
                service.getCurrentOverdueAmount(),
                dueDate
            );
            
            if ("BLOCKED".equals(service.getStatus())) {
                contextInfo += "NOTE: User is BLOCKED. Advise them to pay immediately.\n";
            }
        }

        String systemPrompt = "You are a helpful Telecom Support Assistant for 'Prodapt Connect'.\n" 
                            + "Use the following live account details to answer the user's questions accurately:\n" 
                            + contextInfo 
                            + "\nIf asked about 'service end' or 'expiry', refer to the 'Service Valid Until' date."
                            + "\nKeep answers short and professional.";

        return callOpenAI(systemPrompt, userMessage);
    }

    private String callOpenAI(String systemPrompt, String userMessage) {
        String url = "https://api.openai.com/v1/chat/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> body = new HashMap<>();
        body.put("model", model);	
        body.put("messages", List.of(
            Map.of("role", "system", "content", systemPrompt),
            Map.of("role", "user", "content", userMessage)
        ));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
            Map<String, Object> responseBody = response.getBody();
            List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
            return (String) message.get("content");
        } catch (Exception e) {
            e.printStackTrace();
            return "I am currently experiencing high traffic. Please try again later.";
        }
    }
}