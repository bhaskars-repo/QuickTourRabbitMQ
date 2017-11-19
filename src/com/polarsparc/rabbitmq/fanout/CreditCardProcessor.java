/*
 *
 *  Name:        CreditCardProcessor
 *  
 *  Description: The hypothetical credit-card processor that publishes credit transaction messages in a JSON
 *               format to a fanout exchange
 *  
 */

package com.polarsparc.rabbitmq.fanout;

import java.util.logging.Logger;
import java.util.logging.Level;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.AMQP;

import com.polarsparc.rabbitmq.common.CreditConstants;
import com.polarsparc.rabbitmq.common.RabbitMqUtils;

public class CreditCardProcessor {
	private static String _CC_TXN_MESSAGE_1 = String.join("\n",
			"{",
			"'cc_no': '1234-5678-9012-3456'",
			"'txn_id': '0000000001'",
			"'txn_dt': '11/01/2017 10:25:34'",
			"'txn_amt': '112.75'",
			"'merchant_id': '123'",
			"}"
		);
	private static String _CC_TXN_MESSAGE_2 = String.join("\n",
			"{",
			"'cc_no': '9876-5432-2109-8765'",
			"'txn_id': '0000000002'",
			"'txn_dt': '11/02/2017 16:44:21'",
			"'txn_amt': '33.09'",
			"'merchant_id': '456'",
			"}"
		);
	
	public static void main(String[] args) {
		if (args.length != 3) {
			System.out.printf("Usage: java com.polarsparc.rabbitmq.fanout.CreditCardProcessor <host> <user> <password>\n");
			System.exit(1);
		}
		
		Logger _logger = Logger.getLogger(CreditCardProcessor.class.getName());
		
		try {
			String exchange = CreditConstants.CREDIT_EXCHANGE;
			String routingKey = CreditConstants.ROUTING_KEY;
			
			RabbitMqUtils.initRabbitMq(args[0], args[1], args[2]);
			
			_logger.log(Level.INFO, "Ready to create a communication channel ...");
			
			Channel channel = RabbitMqUtils.getRabbitMqChannel();
			
			_logger.log(Level.INFO, "Ready to create a fanout exchange " + exchange);
			
			channel.exchangeDeclare(exchange, BuiltinExchangeType.FANOUT);
			
			AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
					.appId("CreditCardProcessor")
					.contentType("text/json")
					.priority(1)
					.build();
			
			_logger.log(Level.INFO, "Ready to publish test credit transaction messages");
			
			channel.basicPublish(exchange, routingKey, properties, _CC_TXN_MESSAGE_1.getBytes());
			channel.basicPublish(exchange, routingKey, properties, _CC_TXN_MESSAGE_2.getBytes());
			
			_logger.log(Level.INFO, "Ready to close the communication channel");
			
			RabbitMqUtils.closeRabbitMqChannel(channel);
			
			_logger.log(Level.INFO, "Credit-card processing done !!!");
		}
		catch (Exception ex) {
			_logger.log(Level.SEVERE, "Error in CreditCardProcessor!", ex);
		}
		finally {
			RabbitMqUtils.cleanRabbitMq();
		}
	}
}
