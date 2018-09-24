package com.scandirectoryforxml.client;

import com.scandirectoryforxml.model.XmlFileMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class JMSReceiver {

    private Logger logger = LoggerFactory.getLogger(JMSReceiver.class);

    @JmsListener(destination = "xmlMessage")
    public void receive(XmlFileMessage message){
        logger.info("JMS received: " + message);
    }
}
