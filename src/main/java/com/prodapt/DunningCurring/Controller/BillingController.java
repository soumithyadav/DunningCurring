package com.prodapt.DunningCurring.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.prodapt.DunningCurring.Entity.Bill;
import com.prodapt.DunningCurring.Service.BillingService;

@RestController
@RequestMapping("/api/bills")
public class BillingController {

    @Autowired
    private BillingService billingService;

    @GetMapping("/unpaid/{serviceId}")
    public List<Bill> getUnpaidBills(@PathVariable Long serviceId) {
        return billingService.getUnpaidBills(serviceId);
    }
    
}

