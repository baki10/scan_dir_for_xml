package com.scandirectoryforxml.service;

import com.scandirectoryforxml.config.WatcherConfig;
import com.scandirectoryforxml.model.XmlFile;
import com.scandirectoryforxml.repository.XmlFileRepository;
import com.scandirectoryforxml.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

@Service
public class TargetXmlHandler {

    private Logger logger = LoggerFactory.getLogger(TargetXmlHandler.class);

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private XmlFileRepository xmlFileRepository;

    public Consumer<Path> pathConsumer() {
        return path -> {
            try {
                // при получении файла записать содержимое файла в БД.
                logger.debug("saving file to the database: " + path.toString());
                saveToDb(path);

                // После сохранения информации в БД, отправить сообщение в active mq.
                logger.debug("sending message to the activemq broker: " + path.toString());
                sendToActiveMq(path);

                // После обработки, файл переместить в каталог - dest/archive
                logger.debug("moving file to the archive directory: " + WatcherConfig.TARGET_ARCHIVE_DIR);
                moveToArchive(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

    private void moveToArchive(Path path) {
        FileUtil.move(path, WatcherConfig.TARGET_ARCHIVE_DIR);
    }

    private void sendToActiveMq(Path path) throws IOException {
        List<String> lines = Files.readAllLines(path);
        String content = String.join("\n", lines);
        String name = String.valueOf(path.getFileName());
        // В теле сообщения передать содержимое файла,
        // дополнительно установить свойство сообщения Name, в котором указать имя файла.
        jmsTemplate.convertAndSend("xmlMessage", content, message -> {
            message.setStringProperty("Name", name);
            return message;
        });
    }

    private void saveToDb(Path path) throws IOException {
        XmlFile xmlFile = new XmlFile();
        xmlFile.setFileName(String.valueOf(path.getFileName()));
        xmlFile.setXmlText(Files.readAllBytes(path));
        xmlFileRepository.save(xmlFile);
    }
}
