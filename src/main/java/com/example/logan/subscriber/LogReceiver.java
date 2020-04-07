package com.example.logan.subscriber;

import com.example.logan.conf.ApplicationConfiguration;
import com.example.logan.conf.RabbitMQConfiguration;
import com.example.logan.domain.Log;
import com.example.logan.repository.FileRepository;
import com.rabbitmq.client.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.concurrent.TimeoutException;

@Service
public class LogReceiver {

    private final FileRepository<Log> repository;
    private final ApplicationConfiguration configuration;
    private final ConnectionFactory factory;

    public LogReceiver(FileRepository<Log> repository,
                       ApplicationConfiguration configuration,
                       ConnectionFactory factory) {
        this.repository = repository;
        this.configuration = configuration;
        this.factory = factory;
        init();
    }

    private void init() {
        try {
            String queueName = RabbitMQConfiguration.QUEUE_NAME;
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.queueDeclare(queueName, false, false, false, null);

            Consumer consumer = new DefaultConsumer(channel) {

                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                           byte[] body) throws IOException {
                    String log = new String(body, StandardCharsets.UTF_8);
                    System.out.println(log);
                    try {
                        repository.write(Paths.get(configuration.getLogStorage()), Log.parse(log));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            channel.basicConsume(queueName, true, consumer);
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }
}
