package es.project.service.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUtil {

    private FileUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static Path getImagePath(Long imageId) {
        return Paths.get("media", "image", imageId.toString());
    }

    public static Path getFilePath(Long imageId, String fileName) {
        return Paths.get("media", "image", imageId.toString(), fileName);
    }

    public static void saveFile(MultipartFile file, Path target) throws IOException {
        createDirectoriesIfNecessary(target);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
    }

    public static void createDirectoriesIfNecessary(Path target) {
        File parentDirectory = target.getParent().toFile();
        if (!parentDirectory.exists()) {
            parentDirectory.mkdirs();
        }
    }
}
