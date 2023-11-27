 package com.school.controller;

import java.util.List;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.school.entities.*;
import com.school.service.PaymentService;

import jakarta.persistence.EntityNotFoundException;



@RestController
@RequestMapping("/api")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;
    @GetMapping("/payments/")
    public List<Payments> getAllPayments() {
        return paymentService.getAllPayments();
    }
    
    @GetMapping("/get/{id}")
    public ResponseEntity<Payments> getPaymentById(@PathVariable Long id) {
        Payments payments = paymentService.getPaymentById(id);     
        return ResponseEntity.ok(payments);
        
    }
    
   @PostMapping("/addpayment")
    public ResponseEntity<?> addPayment(@RequestBody Payments payments) {
        if ( payments.getId() <0 || payments.getEvent_id() <0
                || payments.getAmount() == null || payments.getPayment_date() == null
                || payments.getPayment_method() == null) {
            return ResponseEntity.badRequest().body("Invalid input parameters");
        }
        
        Payments savedPayment = paymentService.savePayment(payments);
        if (savedPayment == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save the competition");
        }
       return ResponseEntity.ok(savedPayment);
        //return ResponseEntity.ok("Added Successfully");
    }
    @PutMapping("updatepayment/{id}")
    public ResponseEntity<Payments> updateCompetition(@PathVariable Long id, @RequestBody Payments payments) {
        Payments existingPayment = paymentService.getPaymentById(id);
        existingPayment.setId(payments.getId());
        existingPayment.setEvent_id(payments.getEvent_id());
        existingPayment.setAmount(payments.getAmount());
        existingPayment.setPayment_date(payments.getPayment_date());
        existingPayment.setPayment_method(payments.getPayment_method());
        Payments updatedPayment = paymentService.savePayment(existingPayment);
        return ResponseEntity.ok(updatedPayment);
    }
    
    @DeleteMapping("deletepayment/{id}")
    public ResponseEntity<?> deletePayment(@PathVariable Long id) {
        try {
            paymentService.deletePayment(id);
            return ResponseEntity.ok("Deleted successfully");
            
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete the competition");
        }
    
    }
}