package Logic;

import DB.JacksonIO;

import java.io.IOException;
import java.util.List;


/**
 * An interface that contains static methods utility methods.
 */
public interface ILogic {

    /**
     * Utility method to retrieve the repository as a list of objects.
     *
     * @param path is the path of the repository (json file)
     * @param clazz the class type of the objects in the repository
     * @param <T> the type of objects in the repository
     * @return a list of objects of the specified class type
     * @throws IOException if the repo does not exist or not accessible.
     */
    static <T> List<T> getAllFromRepoAsList(String path, Class<T> clazz) throws IOException {
        return JacksonIO.getObjectListFromJson(path, clazz);
    }

    /**
     * Utility method to add a new object to a repository.
     *
     * @param path is the path of the repository (json file)
     * @param object is an object to append to the json file.
     * @param <T> is the class of the object to write
     * @throws IOException if the repo does not exist or not accessible.
     */
    static <T> void addObjectToRepo(String path, T object) throws IOException {
        JacksonIO.appendToJsonFile(path, object);
    }

    /**
     * Utility method to update repository files.
     * @param path is the path of the repository.
     * @param objects is a List of objects to deserialize into the repo json file
     * @param <T> is the class of the objects to write
     * @throws IOException if the repo does not exist or not accessible.
     */
    static <T> void updateRepo(String path, List<T> objects) throws IOException {
        JacksonIO.updateJsonFile(path, objects);
    }

    /**
     * Examines whether a string is parsable to integer or double.
     *
     * @param strNum is a string containing the number.
     * @return a boolean indicating whether the string is parsable.
     */
    static boolean isNumeric(String strNum) {
        if (strNum == null || strNum.trim().isEmpty()) {
            return false;
        }

        String regex = "[+-]?([0-9]*[.])?[0-9]+";

        strNum = strNum.trim();

        if (!strNum.matches(regex)) {
            return false;
        }

        try {
            Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /**
     * Checks if a string is null or empty.
     *
     * @param str the string to check
     * @return true if the string is null or empty, false otherwise
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

}
