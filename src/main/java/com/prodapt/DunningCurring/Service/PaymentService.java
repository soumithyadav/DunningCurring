package com.prodapt.DunningCurring.Service;

import java.math.BigDecimal;
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

//    @Transactional 
	public void processPayment(Long billId, BigDecimal amount, String mode) {

		System.out.println("DEBUG: Starting Payment Process for Bill " + billId);
		//  Find the Bill
		Bill bill = billRepository.findById(billId)
				.orElseThrow(() -> new RuntimeException("Bill not found with ID: " + billId));

		TelecomService service = bill.getService();
		String oldStatus = service.getStatus(); 

		// Create Payment Record
		try {
			Payment payment = new Payment();
			payment.setBill(bill);
			payment.setService(service);
			payment.setAmount(amount);
			payment.setPaymentMode(mode);
			payment.setPaymentStatus("SUCCESS");
			payment.setPaymentDate(LocalDateTime.now()); 
			paymentRepository.saveAndFlush(payment);
			System.out.println("DEBUG: Payment Record saved");
		} catch (Exception e) {
			System.err.println("ERROR: Failed to save Payment Record: " + e.getMessage());
			e.printStackTrace();
		}

		
		try {
			bill.setPaid(true);
			billRepository.saveAndFlush(bill);
			System.out.println("DEBUG: Bill set to PAID successfully");
		} catch (Exception e) {
			System.err.println("ERROR: Failed to update Bill: " + e.getMessage());
			e.printStackTrace();
			return;
		}

		try {
			service.setCurrentOverdueAmount(BigDecimal.ZERO);
			service.setStatus("ACTIVE");
			service.setCuringStatus("CURED"); 
			telecomServiceRepository.saveAndFlush(service);
			System.out.println("DEBUG: Service restored and overdue zeroed");
		} catch (Exception e) {
			System.err.println("ERROR: Failed to update Service: " + e.getMessage());
			e.printStackTrace();
		}

		try {
			CureEvent cure = new CureEvent();
			cure.setService(service);
			cure.setOverdueAmount(amount); 
			cure.setPreviousStatus(oldStatus);
			cure.setNewStatus("ACTIVE");
			cure.setCureDate(LocalDateTime.now());
			cureEventRepository.saveAndFlush(cure);
			System.out.println("DEBUG: Cure Event saved");
		} catch (Exception e) {
			System.err.println("ERROR: Failed to save Cure Event: " + e.getMessage());
			e.printStackTrace();
		}
	}
}