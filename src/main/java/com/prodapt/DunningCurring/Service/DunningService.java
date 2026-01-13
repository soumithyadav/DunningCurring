package com.prodapt.DunningCurring.Service;



import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prodapt.DunningCurring.DAO.*;
import com.prodapt.DunningCurring.Entity.*;

@Service
public class DunningService {

	 @Autowired
    private final TelecomServiceRepository telecomServiceRepository;
	 @Autowired
    private final DunningRuleRepository ruleRepository;
	 @Autowired
    private final DunningLogRepository logRepository;

	    @Autowired
	    private final NotificationService notificationService;

   
    public DunningService(TelecomServiceRepository telecomServiceRepository,
                          DunningRuleRepository ruleRepository,
                          DunningLogRepository logRepository,
                          NotificationService notificationService) {
        this.telecomServiceRepository = telecomServiceRepository;
        this.ruleRepository = ruleRepository;
        this.logRepository = logRepository;
        this.notificationService=notificationService;
    }


    public DunningRule createRule(DunningRule rule) {
        return ruleRepository.save(rule);
    }

    public void runDunning() {

        List<DunningRule> rules = ruleRepository.findByActiveTrueOrderByOverdueDaysAsc();
        List<TelecomService> services = telecomServiceRepository.findAll();

        for (TelecomService service : services) {

            if (service.getNextDueDate() == null) continue;

            long overdueDays = ChronoUnit.DAYS.between(
                    service.getNextDueDate(), LocalDate.now()
            );

            for (DunningRule rule : rules) {
                if (overdueDays >= rule.getOverdueDays()) {
                    applyRule(service, rule);
                }
            }
        }
    }

    private void applyRule(TelecomService service, DunningRule rule) {

        String oldStatus = service.getStatus();

        if ("BLOCK".equals(rule.getAction())) {
            service.setStatus("BLOCKED");
        } else if ("RESTRICT".equals(rule.getAction())) {
            service.setStatus("RESTRICTED");
        }
        // notificaions to the rules mentioned in DunningRules
        telecomServiceRepository.save(service);
        
        String message="Alert: your acccount is"+rule.getOverdueDays()+"days overdue Action"+rule.getAction();
        notificationService.send(service, rule.getChannel(), message);
        
        DunningLog log = new DunningLog();
        log.setService(service);
        
        log.setRule(rule);
        log.setActionTaken(rule.getAction());
        log.setStatusBefore(oldStatus);
        log.setStatusAfter(service.getStatus());

        logRepository.save(log);
        
    }
    
}
