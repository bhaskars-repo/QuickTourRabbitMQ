/*
 *
 *  Name:        DealListener
 *  
 *  Description: A hypothetical deal alert messages listener that will use routing key
 *               wildcard patterns when binding queues
 *  
 */

package com.polarsparc.rabbitmq.topic;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP;
import com.polarsparc.rabbitmq.common.DealsConstants;
import com.polarsparc.rabbitmq.common.RabbitMqUtils;

public class DealListener {
	public static void main(String[] args) {
		if (args.length != 4) {
			System.out.printf("Usage: java com.polarsparc.rabbitmq.topic.DealListener <host> <user> <password> 'all|tech'\n");
			System.exit(1);
		}
		
		Logger _logger = Logger.getLogger(DealListener.class.getName());
		
		try {
			String queue = "all";
			if (args[3].equalsIgnoreCase("tech")) {
				queue = args[3];
			}
			String exchange = DealsConstants.DEALS_EXCHANGE;
			String routingKey = DealsConstants.ROUTING_KEY_PATTERN_1;
			if (args[3].equalsIgnoreCase("tech")) {
				routingKey = DealsConstants.ROUTING_KEY_PATTERN_2;
			}
			
			RabbitMqUtils.initRabbitMq(args[0], args[1], args[2]);
			
			_logger.log(Level.INFO, "Ready to create communication channel for " + queue);
			
			Channel channel = RabbitMqUtils.getRabbitMqChannel();
			
			_logger.log(Level.INFO, "Ready to create a topic exchange " + exchange);
			
			channel.exchangeDeclare(exchange, BuiltinExchangeType.TOPIC);
			
			_logger.log(Level.INFO, "Ready to bind the queue " + queue + " to exchange " + exchange 
					+ " with routing key " + routingKey);
			
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
					_logger.log(Level.INFO, "Received deal alert message (body): " + msg);
				}
			};
			
			_logger.log(Level.INFO, "Ready to consume test deal alert messages for " + routingKey);
			
			channel.basicConsume(queue, true, consumer);
		}
		catch (Exception ex) {
			_logger.log(Level.SEVERE, "Error in DealListener!", ex);
		}
	}
}
