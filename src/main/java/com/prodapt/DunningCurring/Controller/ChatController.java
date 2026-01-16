package com.prodapt.DunningCurring.Controller;

import com.prodapt.DunningCurring.DAO.UserRepository;
import com.prodapt.DunningCurring.Entity.User;
import com.prodapt.DunningCurring.Service.OpenAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/chat")
//@CrossOrigin(origins = "http://localhost:5173")
public class ChatController {

    @Autowired
    private OpenAIService openAIService;
    
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/ask")
    public Map<String, String> askAssistant(@RequestBody Map<String, String> payload, Authentication auth) {

        String username = auth.getName();
        User user = userRepository.findByUsername(username).orElseThrow();
        Long customerId = user.getCustomer().getId();
        String userMessage = payload.get("message");
        String aiResponse = openAIService.getChatResponse(customerId, userMessage);

        return Map.of("response", aiResponse);
    }
}