/*
 *
 *  Name:        DealAlerter
 *  
 *  Description: The hypothetical deals alerting system that will publish deal alert messages
 *               as a string in a CSV format to a topic exchange
 *  
 */

package com.polarsparc.rabbitmq.topic;

import java.util.logging.Logger;
import java.util.logging.Level;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.AMQP;
import com.polarsparc.rabbitmq.common.DealsConstants;
import com.polarsparc.rabbitmq.common.RabbitMqUtils;

public class DealAlerter {
	private static String[][] _DEAL_ALERTS = {
		{ "deal.tech.mobile", "iPhone 8 64GB" , "699.99" , "T-Mobile" },
		{ "deal.tech.laptop" , "Dell 15.6 i7 16GB" , "899.99", "Best Buy" },
		{ "deal.furniture.desk", "Bush Computer Desk", "249.99", "Amazon" },
		{ "deal.airfare", "Round-trip NY JFK", "79.99", "CheapAir" }
	};
	
	public static void main(String[] args) {
		if (args.length != 3) {
			System.out.printf("Usage: java com.polarsparc.rabbitmq.topic.DealAlerter <host> <user> <password>\n");
			System.exit(1);
		}
		
		Logger _logger = Logger.getLogger(DealAlerter.class.getName());
		
		try {
			String exchange = DealsConstants.DEALS_EXCHANGE;
			
			RabbitMqUtils.initRabbitMq(args[0], args[1], args[2]);
			
			_logger.log(Level.INFO, "Ready to create a communication channel ...");
			
			Channel channel = RabbitMqUtils.getRabbitMqChannel();
			
			_logger.log(Level.INFO, "Ready to create a topic exchange " + exchange);
			
			channel.exchangeDeclare(exchange, BuiltinExchangeType.TOPIC);
			
			_logger.log(Level.INFO, "Ready to publish test deal alert messages");
			
			AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
					.appId("DealAlerter")
					.contentType("text/plain")
					.expiration("5000")
					.build();
			
			for (String[] deal : _DEAL_ALERTS) {
				String msg = String.join(",", deal);
				channel.basicPublish(exchange, deal[0], properties, msg.getBytes());
			}
			
			_logger.log(Level.INFO, "Ready to close the communication channel");
			
			RabbitMqUtils.closeRabbitMqChannel(channel);
			
			_logger.log(Level.INFO, "Deal alerts processing done !!!");
		}
		catch (Exception ex) {
			_logger.log(Level.SEVERE, "Error in DealAlerter!", ex);
		}
		finally {
			RabbitMqUtils.cleanRabbitMq();
		}
	}
}
