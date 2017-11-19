/*
 *
 *  Name:        CreditConstants
 *  
 *  Description: Common constants used between the CreditCard Processor (publisher) and the two consumers
 *               Fraud Verifier and Analytics Engine
 *  
 */

package com.polarsparc.rabbitmq.common;

public interface CreditConstants {
	public final static String CREDIT_EXCHANGE      = "credit";
	public final static String ROUTING_KEY          = "";
	public final static String ANALYTICS_QUEUE_NAME = "analytics";
	public final static String FRAUD_QUEUE_NAME     = "fraud";
}
