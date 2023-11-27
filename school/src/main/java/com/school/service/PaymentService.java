package com.school.service;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.school.entities.Payments;
import com.school.repository.PaymentRepository;

import jakarta.persistence.EntityNotFoundException;


@Service
public class PaymentService {
    
    @Autowired
    private PaymentRepository paymentRepository;

	
	public List<Payments> getAllPayments() {
        return paymentRepository.findAll();
    }
	
	
    public Payments getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Competition not found with id " + id));
    }

    public Payments createPayment(Payments payments) {
        return paymentRepository.save(payments);
    }

    public Payments updatePayment(Long id, Payments payments) {
        Payments existingPayment= getPaymentById(id);
        existingPayment.setId(payments.getId());
        existingPayment.setEvent_id(payments.getEvent_id());
        existingPayment.setAmount(payments.getAmount());
        existingPayment.setPayment_date(payments.getPayment_date());
        existingPayment.setPayment_method(payments.getPayment_method());
        return paymentRepository.save( existingPayment);
    }

    public void deletePayment(Long id) {
        Payments existingPayment = getPaymentById(id);
        paymentRepository.delete(existingPayment);
    }

	public Payments savePayment(Payments payments) {
		return paymentRepository.save(payments);
	}
}

