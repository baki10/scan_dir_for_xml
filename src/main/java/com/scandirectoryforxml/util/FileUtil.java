package com.scandirectoryforxml.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {

    public static void copyFile(Path source, String directory) {
        try {
            Path target = Paths.get(directory, String.valueOf(source.getFileName()));
            Files.copy(source, target);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void move(Path source, String directory) {
        try {
            if (new File(directory).mkdirs()) {
                System.out.println("created new directory: " + directory);
            }
            Path target = Paths.get(directory, String.valueOf(source.getFileName()));
            Files.move(source, target);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
