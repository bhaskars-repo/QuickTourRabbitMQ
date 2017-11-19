/*
 *
 *  Name:        FraudVerifier
 *  
 *  Description: A hypothetical credit card fraud verifier
 *  
 */

package com.polarsparc.rabbitmq.fanout;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP;

import com.polarsparc.rabbitmq.common.CreditConstants;
import com.polarsparc.rabbitmq.common.RabbitMqUtils;

public class FraudVerifier {
	public static void main(String[] args) {
		if (args.length != 3) {
			System.out.printf("Usage: java com.polarsparc.rabbitmq.fanout.FraudVerifier <host> <user> <password>\n");
			System.exit(1);
		}
		
		Logger _logger = Logger.getLogger(FraudVerifier.class.getName());
		
		try {
			String queue = CreditConstants.FRAUD_QUEUE_NAME;
			String exchange = CreditConstants.CREDIT_EXCHANGE;
			String routingKey = CreditConstants.ROUTING_KEY;
			
			RabbitMqUtils.initRabbitMq(args[0], args[1], args[2]);
			
			_logger.log(Level.INFO, "Ready to create communication channel for " + queue);
			
			Channel channel = RabbitMqUtils.getRabbitMqChannel();
			
			_logger.log(Level.INFO, "Ready to create a fanout exchange " + exchange);
			
			channel.exchangeDeclare(exchange, BuiltinExchangeType.FANOUT);
			
			_logger.log(Level.INFO, "Ready to bind the queue " + queue + " to exchange " + exchange);
			
			channel.queueDeclare(queue, false, false, false, null);
			channel.queueBind(queue, exchange, routingKey);
			
			_logger.log(Level.INFO, "Ready to create a consumer for " + queue);
			
			Consumer consumer = new DefaultConsumer(channel) {
				@Override
				public void handleDelivery(String consumerTag, 
						Envelope envelope,
						AMQP.BasicProperties properties,
						byte[] body) throws IOException {
					String msg = new String(body, "UTF-8");
					_logger.log(Level.INFO, "Received credit-card message (properties): " + properties);
					_logger.log(Level.INFO, "Received credit-card message (body): " + msg);
					channel.basicAck(envelope.getDeliveryTag(), false);
				}
			};
			
			_logger.log(Level.INFO, "Ready to consume test credit-card messages for " + queue);
			
			channel.basicConsume(queue, false, consumer);
		}
		catch (Exception ex) {
			_logger.log(Level.SEVERE, "Error in FraudVerifier!", ex);
		}
	}
}
