package com.flipkart.bean;

import com.flipkart.utils.PaymentStatusType;
import com.flipkart.utils.PaymentType;

public class Payment {
    private Long paymentId;
    private Long bookingId;
    private PaymentType paymentType;
    private Long payerId;
    private PaymentStatusType paymentStatus;

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public Long getPayerId() {
        return payerId;
    }

    public void setPayerId(Long payerId) {
        this.payerId = payerId;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public PaymentStatusType getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatusType paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
