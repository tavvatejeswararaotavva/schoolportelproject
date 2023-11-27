package com.school.entities;

import java.math.BigDecimal;
import java.util.Date;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="paymentdetails")
public class Payments {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable=false)

    private int id;
    private int event_id;
    private int student_id;
    private BigDecimal amount;
    private Date payment_date;
    private String payment_method;
	public Payments(int id, int event_id, BigDecimal amount, Date payment_date, String payment_method) {
		super();
		this.id = id;
		this.event_id = event_id;
		this.amount = amount;
		this.payment_date = payment_date;
		this.payment_method =payment_method;
	}
	
	public Payments() {
		super();
		
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getEvent_id() {
		return event_id;
	}
	public void setEvent_id(int event_id) {
		this.event_id = event_id;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public Date getPayment_date() {
		return payment_date;
	}
	public void setPayment_date(Date payment_date) {
		this.payment_date = payment_date;
	}
	public String getPayment_method() {
		return payment_method;
	}
	public void setPayment_method(String method) {
		this.payment_method = method;
	}
	
	public int getStudent_id() {
		return student_id;
	}

	public void setStudent_id(int student_id) {
		this.student_id = student_id;
	}

	@Override
	public String toString() {
		return "Payments [id=" + id + ", event_id=" + event_id + ", student_id=" + student_id + ", amount=" + amount
				+ ", payment_date=" + payment_date + ", payment_method=" + payment_method + "]";
	}

	
    
}