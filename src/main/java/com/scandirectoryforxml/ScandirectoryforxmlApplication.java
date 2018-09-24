package com.scandirectoryforxml;

import com.scandirectoryforxml.service.WatchDirService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScandirectoryforxmlApplication implements CommandLineRunner {

    @Autowired
    @Qualifier("source")
    private WatchDirService sourceWatcher;

    @Autowired
    @Qualifier("target")
    private WatchDirService targetWatcher;

    public static void main(String[] args) {
        SpringApplication.run(ScandirectoryforxmlApplication.class, args);
    }

    @Override
    public void run(String... args) {
        // В отдельном потоке сканировать каталог dest/temp
        new Thread(() -> targetWatcher.watchDirectoryForXml()).start();
        sourceWatcher.watchDirectoryForXml();
    }
}
