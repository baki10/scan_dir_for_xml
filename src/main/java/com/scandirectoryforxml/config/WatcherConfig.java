package com.scandirectoryforxml.config;

import com.scandirectoryforxml.model.WatcherConfigEntity;
import com.scandirectoryforxml.repository.WatcherConfigRepository;
import com.scandirectoryforxml.service.SourceXmlHandler;
import com.scandirectoryforxml.service.TargetXmlHandler;
import com.scandirectoryforxml.service.WatchDirService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

@Configuration
public class WatcherConfig {

    private static final String DEFAULT_SOURCE_DIR = "source";
    public static final String TARGET_TEMP_DIR = "dest/tmp";
    public static final String TARGET_ARCHIVE_DIR ="dest/archive";

    @Autowired
    private WatcherConfigRepository watcherConfigRepository;
    @Autowired
    private SourceXmlHandler sourceHandler;
    @Autowired
    private TargetXmlHandler targetHandler;

    @Bean
    @Qualifier("source")
    public WatchDirService watchSource() throws IOException {
        // register directory and process its events
        WatcherConfigEntity configEntity = watcherConfigRepository.findById(1000L)
                .orElse(new WatcherConfigEntity(DEFAULT_SOURCE_DIR));
        return watcher(configEntity.getSource(), sourceHandler.pathConsumer());
    }

    @Bean
    @Qualifier("target")
    public WatchDirService watchTarget() throws IOException {
        return watcher(TARGET_TEMP_DIR, targetHandler.pathConsumer());
    }

    private WatchDirService watcher(String path, Consumer<Path> handler) throws IOException {
        File file = new File(path);
        if (file.mkdirs()) {
            System.out.println("created: " + file);
        }
        Path sourceDir = Paths.get(path);
        return new WatchDirService(sourceDir, handler);
    }
}
