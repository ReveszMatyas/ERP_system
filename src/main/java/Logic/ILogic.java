package Logic;

import DB.JacksonIO;

import java.io.IOException;
import java.util.List;

public interface ILogic {

    static <T> List<T> getAllFromRepoAsList(String path, Class<T> clazz) throws IOException {
        return JacksonIO.getObjectListFromJson(path, clazz);
    }

    static <T> void addObjectToRepo(String path, T object) throws IOException {
        JacksonIO.appendToJsonFile(path, object);
    }

    static <T> void updateRepo(String path, List<T> objects) throws IOException {
        JacksonIO.updateJsonFile(path, objects);
    }

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

}
