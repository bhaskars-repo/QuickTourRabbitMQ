/*
 *
 *  Name:        DealsConstants
 *  
 *  Description: Common constants used between the deals alerter (publisher) and the two listeners (consumers)
 *  
 */

package com.polarsparc.rabbitmq.common;

public interface DealsConstants {
	public final static String DEALS_EXCHANGE        = "deals";
	public final static String ROUTING_KEY_PATTERN_1 = "deal.#";
	public final static String ROUTING_KEY_PATTERN_2 = "deal.tech.*";
	public final static String ALL_DEALS_QUEUE_NAME  = "alldeals";
	public final static String TECH_DEALS_QUEUE_NAME = "techdeals";
}
