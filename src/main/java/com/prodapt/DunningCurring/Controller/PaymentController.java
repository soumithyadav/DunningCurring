package com.prodapt.DunningCurring.Controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.prodapt.DunningCurring.DAO.UserRepository;
import com.prodapt.DunningCurring.Entity.User;
import com.prodapt.DunningCurring.Service.ActivityLogService;
import com.prodapt.DunningCurring.Service.PaymentService;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private ActivityLogService activityLogService;

	@Autowired
	private UserRepository userRepository;

	@PostMapping("/pay/{billId}")
	public String payBill(
			@PathVariable Long billId, 
			@RequestParam BigDecimal amount, 
			@RequestParam String mode) {
		paymentService.processPayment(billId, amount, mode);
		User systemUser = userRepository.findByUsername("admin_super").orElse(null);
		if (systemUser != null) {
			String logDescription = "Bill #" + billId + " Paid via " + mode + ". Service Restored.";
			activityLogService.log(systemUser, "PAYMENT_RECEIVED", "Bill", billId);
		}

		return "Payment successful. Service restored.";
	}

}
