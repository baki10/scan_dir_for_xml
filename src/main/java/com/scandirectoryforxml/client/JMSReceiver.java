package com.scandirectoryforxml.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class JMSReceiver {

    private Logger logger = LoggerFactory.getLogger(JMSReceiver.class);

    @JmsListener(destination = "xmlMessage")
    public void receive(String message,
                        @Header(name = "Name", defaultValue = "defaultName") String name) {
        logger.info("JMS received " + name + ":\n" + message);
    }
}
