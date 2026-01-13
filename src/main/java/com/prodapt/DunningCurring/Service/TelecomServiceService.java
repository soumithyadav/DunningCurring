package com.prodapt.DunningCurring.Service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prodapt.DunningCurring.DAO.TelecomServiceRepository;
import com.prodapt.DunningCurring.Entity.TelecomService;

@Service
public class TelecomServiceService {

	 @Autowired
    private final TelecomServiceRepository telecomServiceRepository;

    public TelecomServiceService(TelecomServiceRepository telecomServiceRepository) {
        this.telecomServiceRepository = telecomServiceRepository;
    }
    public List<TelecomService> getServicesByCustomer(Long customerId) {
        return telecomServiceRepository.findByCustomerId(customerId);
    }

    public TelecomService getService(Long serviceId) {
        return telecomServiceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found"));
    }

    public void updateOverdue(Long serviceId, BigDecimal overdueAmount) {
        TelecomService service = getService(serviceId);
        service.setCurrentOverdueAmount(overdueAmount);
        telecomServiceRepository.save(service);
    }

    public void updateStatus(TelecomService service, String status) {
        service.setStatus(status);
        telecomServiceRepository.save(service);
    }

    public boolean isPaymentAllowed(TelecomService service) {
        return !"BLOCKED".equals(service.getStatus());
    }
    

    public TelecomService createService(TelecomService service) {
        if (service.getStatus() == null) {
            service.setStatus("ACTIVE");
        }
        return telecomServiceRepository.save(service);
    } 

    public TelecomService updateServiceStatus(Long serviceId, String newStatus) {
        TelecomService service = getService(serviceId); 
        service.setStatus(newStatus);
        
        if ("ACTIVE".equalsIgnoreCase(newStatus)) {            
            service.setCuringStatus("NORMAL");
        } 
        else if ("BLOCKED".equalsIgnoreCase(newStatus)) {
            service.setCuringStatus("BLOCKED");
        }

        return telecomServiceRepository.save(service);
    }
}
