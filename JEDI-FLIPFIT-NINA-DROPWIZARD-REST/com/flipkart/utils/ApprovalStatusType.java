package com.flipkart.utils;

/**
 * ApprovalStatusType - Status of gym center/owner approval
 * 
 * From Activity Diagram (Admin):
 * - PENDING: Waiting for admin approval
 * - APPROVED: Admin approved
 * - REJECTED: Admin rejected (cascading delete)
 */
public enum ApprovalStatusType {
    PENDING,
    APPROVED,
    REJECTED
}
