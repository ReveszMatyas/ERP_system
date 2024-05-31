package DB;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.tinylog.Logger;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


/**
 * Contains methods to implement CRUD operations on Json files with Jackson.
 */
@Data
public class JacksonIO {

    public static final ObjectMapper MAPPER = getObjectMapper();

    /**
     *
     * @return the Object Mapper of the {@link JacksonIO} class.
     */

    private static ObjectMapper getObjectMapper(){
        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

    /**
     * Method to serialize the data in the given file into a list of objects.
     * @param path The path where the json file is located.
     * @param clazz The class to serialize into
     * @return A list of objects from the class
     * @throws IOException when the repo file is not found or not available.
     */
    public static <T> List<T> getObjectListFromJson(String path, Class<T> clazz) throws IOException {
        return MAPPER.readValue(new File(path), MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
    }

    /**
     * Appends an object to the given json repository.
     * @param path The path where the json file is located.
     * @param object Object to insert into the repo.
     * @param <T> The class of the object.
     * @throws IOException when the repo file is not found or not available.
     */
    public static <T> void appendToJsonFile(String path, T object) throws IOException{
        try {
            List<T> objects = (List<T>) getObjectListFromJson(path, object.getClass());
            objects.add(object);
            updateJsonFile(path, objects);
        } catch (ClassCastException e){
            Logger.error(e.getMessage(), e);
        }

    }
    /*
        public static <T> void appendToJsonFile(String path, T object) throws IOException{
        try {
            List<T> objects = (List<T>) getObjectListFromJson(path, Object.class);
            objects.add(object);
            updateJsonFile(path, objects);
        } catch (RuntimeException e){
            Logger.error(e.getMessage(), e);
        }
     */

    /**
     * Deserializes the given objects into the repository file.
     * @param path The path where the json file is located.
     * @param objects Objects to insert into the repo.
     * @param <T> The class of the object.
     * @throws IOException when the repo file is not found or not available.
     */
    public static <T> void updateJsonFile(String path, List<T> objects) throws IOException{
        createBackup(path); // todo: delete. There for testing purposes.
        MAPPER.writerWithDefaultPrettyPrinter().writeValue(new File(path), objects);
    }

    /**
     * Creates a backup of the given repository.
     * @param path The path where the json file is located.
     * @throws IOException when the repo file is not found or not available.
     */
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

        System.out.println("Backup created at: " + backupFilePath);
    }
}
