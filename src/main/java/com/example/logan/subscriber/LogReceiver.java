package com.example.logan.subscriber;

import com.example.logan.conf.ApplicationConfiguration;
import com.example.logan.conf.RabbitMQConfiguration;
import com.example.logan.domain.Log;
import com.example.logan.repository.FileRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;

@Service
public class LogReceiver {

    private final FileRepository<Log> repository;
    private final ApplicationConfiguration configuration;

    public LogReceiver(FileRepository<Log> repository,
                       ApplicationConfiguration configuration) {
        this.repository = repository;
        this.configuration = configuration;
    }

    @RabbitListener(queues = RabbitMQConfiguration.QUEUE_NAME)
    public void listen(String log) {
        System.out.println(log);
        try {
            repository.write(Paths.get(configuration.getLogStorage()), Log.parse(log));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
