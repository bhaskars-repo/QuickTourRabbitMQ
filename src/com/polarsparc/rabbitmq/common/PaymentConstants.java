/*
 *
 *  Name:        PaymentConstants
 *  
 *  Description: Common constants used between the Payment Processor (publisher) and the payment
 *               verifiers for Mastercard and Visa (consumers)
 *  
 */

package com.polarsparc.rabbitmq.common;

public interface PaymentConstants {
	public final static String DEFAULT_EXCHANGE = "";
	public final static String MASTERCARD_QUEUE_NAME = "mastercard";
	public final static String VISA_QUEUE_NAME       = "visa";
}
