/*
 *
 *  Name:        PaymentProcessor
 *  
 *  Description: RabbitMQ publisher that generates hypothetical payment messages for either Mastercard
 *               or Visa payment 
 *  
 */

package com.polarsparc.rabbitmq.direct;

import java.util.logging.Logger;
import java.util.logging.Level;

import com.rabbitmq.client.Channel;

import com.polarsparc.rabbitmq.common.PaymentConstants;
import com.polarsparc.rabbitmq.common.RabbitMqUtils;

public class PaymentProcessor {
	private static String _MASTERCARD_PAYMENT_MSG = "12345,19.99";
	private static String _VISA_PAYMENT_MSG       = "98765,21.99";
	
	public static void main(String[] args) {
		if (args.length != 3) {
			System.out.printf("Usage: java com.polarsparc.rabbitmq.direct.PaymentProcessor <host> <user> <password>\n");
			System.exit(1);
		}
		
		Logger _logger = Logger.getLogger(PaymentProcessor.class.getName());
		
		try {
			RabbitMqUtils.initRabbitMq(args[0], args[1], args[2]);
			
			_logger.log(Level.INFO, "Ready to create communication channels ...");
			
			Channel channel1 = RabbitMqUtils.getRabbitMqChannel();
			Channel channel2 = RabbitMqUtils.getRabbitMqChannel();
			
			_logger.log(Level.INFO, "Ready to create queues for Mastercard and Visa");
			
			channel1.queueDeclare(PaymentConstants.MASTERCARD_QUEUE_NAME, false, false, false, null);
			channel2.queueDeclare(PaymentConstants.VISA_QUEUE_NAME, false, false, false, null);
			
			_logger.log(Level.INFO, "Ready to publish test messages for Mastercard and Visa");
			
			channel1.basicPublish(PaymentConstants.DEFAULT_EXCHANGE,
					PaymentConstants.MASTERCARD_QUEUE_NAME,
					null,
					_MASTERCARD_PAYMENT_MSG.getBytes());
			channel2.basicPublish(PaymentConstants.DEFAULT_EXCHANGE,
					PaymentConstants.VISA_QUEUE_NAME,
					null,
					_VISA_PAYMENT_MSG.getBytes());
			
			_logger.log(Level.INFO, "Ready to close the communication channels");
			
			RabbitMqUtils.closeRabbitMqChannel(channel1);
			RabbitMqUtils.closeRabbitMqChannel(channel2);
			
			_logger.log(Level.INFO, "Payment processing done !!!");
		}
		catch (Exception ex) {
			_logger.log(Level.SEVERE, "Error in PaymentProcessor!", ex);
		}
		finally {
			RabbitMqUtils.cleanRabbitMq();
		}
	}
}
