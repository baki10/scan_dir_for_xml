package com.scandirectoryforxml.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

public class WatchDirService {

    private Logger logger = LoggerFactory.getLogger(WatchDirService.class);

    private final WatchService watcher;
    private final Map<WatchKey, Path> keys;
    private final Consumer<Path> handler;

    @SuppressWarnings("unchecked")
    private static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>) event;
    }

    /**
     * Register the given directory with the WatchService
     */
    private void register(Path dir) throws IOException {
        WatchKey key = dir.register(watcher, ENTRY_CREATE);
        keys.put(key, dir);
    }

    /**
     * Creates a WatchService and registers the given directory
     */
    public WatchDirService(Path dir, Consumer<Path> xmlHandler) throws IOException {
        this.watcher = FileSystems.getDefault().newWatchService();
        this.keys = new HashMap<>();
        this.handler = xmlHandler;
        register(dir);
    }

    public void watchDirectoryForXml() {
        for (; ; ) {
            logger.debug("waiting for new xml file " + keys.values().toString());
            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException x) {
                return;
            }

            Path dir = keys.get(key);
            if (dir == null) {
                logger.error("WatchKey not recognized!!");
                continue;
            }

            for (WatchEvent<?> event : key.pollEvents()) {

                // Context for directory entry event is the file name of entry
                WatchEvent<Path> ev = cast(event);
                Path name = ev.context();
                Path child = dir.resolve(name);

                if (child.toString().endsWith("xml")) {
                    logger.debug("New XML File: " + child.toString());
                    // handle by given handler
                    handler.accept(child);
                }
            }

            // reset key and remove from set if directory no longer accessible
            boolean valid = key.reset();
            if (!valid) {
                keys.remove(key);

                // all directories are inaccessible
                if (keys.isEmpty()) {
                    break;
                }
            }
        }
    }
}
