package com.scandirectoryforxml.service;

import com.scandirectoryforxml.config.WatcherConfig;
import com.scandirectoryforxml.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@Service
public class SourceXmlHandler {

    private Logger logger = LoggerFactory.getLogger(SourceXmlHandler.class);

    private Executor executor = Executors.newFixedThreadPool(8);

    public Consumer<Path> pathConsumer() {
        return source -> {
            // При обнаружении xml файлов произвести их копирование (многопоточно) в другой каталог – dest/temp.
            executor.execute(() -> {
                logger.debug("Copying file " + source.toString() + " to " + WatcherConfig.TARGET_TEMP_DIR);
                FileUtil.copyFile(source, WatcherConfig.TARGET_TEMP_DIR);
            });
        };
    }
}
