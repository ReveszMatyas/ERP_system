package DB;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
public class JacksonIO {
    public static final ObjectMapper MAPPER = getObjectMapper();

    private static ObjectMapper getObjectMapper(){
        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

    public static <T> List<T> getObjectListFromJson(String path, Class<T> clazz) throws IOException {
        return MAPPER.readValue(new File(path), MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
    }


    public static <T> void appendToJsonFile(String path, T object) throws IOException{
        List<T> objects = (List<T>)getObjectListFromJson(path, Object.class);

        objects.add(object);

        updateJsonFile(path, objects);
    }

    public static <T> void updateJsonFile(String path, List<T> objects) throws IOException{
        createBackup(path);
        MAPPER.writerWithDefaultPrettyPrinter().writeValue(new File(path), objects);
    }

    public static void createBackup(String path) throws IOException {
        // Define the source file path
        Path sourcePath = Paths.get(path);

        // Define the backup directory path
        Path backupDir = Paths.get(sourcePath.getParent().toString(), "backups");

        // Create the backup directory if it doesn't exist
        if (!Files.exists(backupDir)) {
            Files.createDirectories(backupDir);
        }

        // Get the file name and extension
        String fileName = sourcePath.getFileName().toString();
        String fileExtension = "";
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex > 0) {
            fileExtension = fileName.substring(dotIndex);
            fileName = fileName.substring(0, dotIndex);
        }

        // Format the current date and time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String formattedDateTime = LocalDateTime.now().format(formatter);

        // Define the backup file name
        String backupFileName = fileName + "_" + formattedDateTime + fileExtension;

        // Define the backup file path
        Path backupFilePath = backupDir.resolve(backupFileName);

        // Copy the file to the backup directory
        Files.copy(sourcePath, backupFilePath, StandardCopyOption.REPLACE_EXISTING);

        System.out.println("Backup created at: " + backupFilePath.toString());
    }
}
