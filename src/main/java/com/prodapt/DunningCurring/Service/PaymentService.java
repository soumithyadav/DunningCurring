package com.prodapt.DunningCurring.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prodapt.DunningCurring.DAO.*;
import com.prodapt.DunningCurring.Entity.*;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private TelecomServiceRepository telecomServiceRepository;
    @Autowired
    private BillRepository billRepository;
    @Autowired
    private CureEventRepository cureEventRepository;

    @Transactional 
    public void processPayment(Long billId, BigDecimal amount, String mode) {

        System.out.println("DEBUG: Starting Payment Process for Bill " + billId);

        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found with ID: " + billId));

        TelecomService service = bill.getService();
        String oldStatus = service.getStatus(); 

        bill.setPaid(true); 
        billRepository.saveAndFlush(bill); 
//        System.out.println("DEBUG: Bill " + billId + " marked as PAID.");

        Payment payment = new Payment();
        payment.setBill(bill);
        payment.setService(service);
        payment.setAmount(amount);
        payment.setPaymentMode(mode);
        payment.setPaymentStatus("SUCCESS");
        payment.setPaymentDate(LocalDateTime.now()); 
        paymentRepository.save(payment);


        service.setCurrentOverdueAmount(BigDecimal.ZERO);
        service.setStatus("ACTIVE");
        service.setCuringStatus("CURED"); 
        

        LocalDate today = LocalDate.now();
        service.setLastPaymentDate(today);
        
        service.setNextDueDate(today.plusDays(30));

        telecomServiceRepository.save(service);
//        System.out.println("DEBUG: Service Dates Updated. Next Due: " + service.getNextDueDate());
        	CureEvent cure = new CureEvent();
        cure.setService(service);
        cure.setOverdueAmount(amount); 
        cure.setPreviousStatus(oldStatus);
        cure.setNewStatus("ACTIVE");
        cure.setCureDate(LocalDateTime.now());
        cureEventRepository.save(cure);
    }
}