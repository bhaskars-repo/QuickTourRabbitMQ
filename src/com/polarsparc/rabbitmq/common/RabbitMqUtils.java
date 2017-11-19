/*
 *
 *  Name:        RabbitMqUtils
 *  
 *  Description: Common utility methods used by producers as well as consumers
 *  
 */

package com.polarsparc.rabbitmq.common;

import java.util.logging.Logger;
import java.util.logging.Level;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

public final class RabbitMqUtils {
	private static  Logger _logger = Logger.getLogger(RabbitMqUtils.class.getName());
	
	private static Connection _connection = null;
	
	private RabbitMqUtils() {
	}
	
	public static void initRabbitMq(String host, String user, String pass) 
			throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(host);
		factory.setUsername(user);
		factory.setPassword(pass);
		
		_connection = factory.newConnection();
	}
	
	public static Channel getRabbitMqChannel() 
			throws Exception {
		if (_connection == null) {
			throw new Exception("RabbitMQ not initialized!");
		}
		
		return _connection.createChannel();
	}
	
	public static void closeRabbitMqChannel(final Channel channel) {
		try {
			if (channel != null) {
				channel.close();
			}
		}
		catch (Exception ex) {
			_logger.log(Level.SEVERE, "Error on channel close!", ex);
		}
	}
	
	public static void cleanRabbitMq() {
		try {
			if (_connection != null) {
				_connection.close();
				_connection = null;
			}
		}
		catch (Exception ex) {
			_logger.log(Level.SEVERE, "Error on connection close!", ex);
		}
	}
}
