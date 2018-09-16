package org.brijframework.util.runtime;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.List;

public class Touch implements Runnable {

    private Path touchPath;

    public Touch(Path touchPath) {
        this.touchPath = touchPath;
    }

    public static void touch(Path file) throws IOException {
        long timestamp = System.currentTimeMillis();
        touch(file, timestamp);
    }

    public static void touch(Path file, long timestamp) throws IOException {
        if (Files.exists(file)) {
            FileTime ft = FileTime.fromMillis(timestamp);
            Files.setLastModifiedTime(file, ft);
        }
    }

    List<Path> listFiles(Path path) throws IOException {
        final List<Path> files = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            for (Path entry : stream) {
                if (Files.isDirectory(entry)) {
                    files.addAll(listFiles(entry));
                }
                files.add(entry);
            }
        }
        return files;
    }

    @Override
    public void run() {
        while (true) {
            try {
                for (Path path : listFiles(touchPath)) {
                    touch(path);
                }
            } catch (IOException e) {
                System.out.println("Exception: " + e);
            }

            try {
                Thread.sleep(10000L);
            } catch (InterruptedException e) {
                System.out.println("Exception: " + e);
            }
        }

    }

}