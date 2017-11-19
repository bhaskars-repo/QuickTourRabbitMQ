/*
 *
 *  Name:        PaymentVerifier
 *  
 *  Description: RabbitMQ Mastercard or Visa (consumer) that receives the hypothetical payment messages
 *  
 */

package com.polarsparc.rabbitmq.direct;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP;

import com.polarsparc.rabbitmq.common.PaymentConstants;
import com.polarsparc.rabbitmq.common.RabbitMqUtils;

public class PaymentVerifier {
	public static void main(String[] args) {
		if (args.length != 4) {
			System.out.printf("Usage: java com.polarsparc.rabbitmq.direct.PaymentVerifier <host> <user> <password> <queue>\n");
			System.exit(1);
		}
		
		Logger _logger = Logger.getLogger(PaymentVerifier.class.getName());
		
		try {
			String queue = PaymentConstants.VISA_QUEUE_NAME;
			if (args[3].equalsIgnoreCase(PaymentConstants.MASTERCARD_QUEUE_NAME)) {
				queue = PaymentConstants.MASTERCARD_QUEUE_NAME;
						
			}
			RabbitMqUtils.initRabbitMq(args[0], args[1], args[2]);
			
			_logger.log(Level.INFO, "Ready to create communication channel for " + queue);
			
			Channel channel = RabbitMqUtils.getRabbitMqChannel();
			
			_logger.log(Level.INFO, "Ready to create a queue for " + queue);
			
			channel.queueDeclare(queue, false, false, false, null);
			
			_logger.log(Level.INFO, "Ready to create a consumer for " + args[3]);
			
			Consumer consumer = new DefaultConsumer(channel) {
				@Override
				public void handleDelivery(String consumerTag, 
						Envelope envelope,
						AMQP.BasicProperties properties,
						byte[] body) throws IOException {
					String msg = new String(body, "UTF-8");
					_logger.log(Level.INFO, "Received message: " + msg);
				}
			};
			
			_logger.log(Level.INFO, "Ready to consume test messages for " + args[3]);
			
			channel.basicConsume(queue, true, consumer);
		}
		catch (Exception ex) {
			_logger.log(Level.SEVERE, "Error in PaymentVerifier!", ex);
		}
	}
}
