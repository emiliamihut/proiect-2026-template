import org.example.Main;
import org.example.PathTypes;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MainTest {
    private static final String USER_DIR = System.getProperty("user.dir");
    private static final String MAIN_PATH = String.join(File.separator, USER_DIR, "src", "main", "resources");

    @Test
    public void normalServersLoad01() {
        String filePath = prepareFilePath("01-servers-normal-load", "servers_01");
        emptyOutput(prepareFilePath("01-servers-normal-load", ""));
        Main.main(new String[]{PathTypes.SERVERS.getValue(), filePath});
        assertTrue(areFilesEqual(filePath));
    }

    @Test
    public void brokenServersLoad02() {
        String filePath = prepareFilePath("02-servers-broken-load", "servers_02");
        emptyOutput(prepareFilePath("02-servers-broken-load", ""));
        Main.main(new String[]{PathTypes.SERVERS.getValue(), filePath});
        assertTrue(areFilesEqual(filePath));
    }

    @Test
    public void normalResourceGroupsLoad03() {
        String filePath = prepareFilePath("03-resource-groups-normal-load", "groups_01");
        emptyOutput(prepareFilePath("03-resource-groups-normal-load", ""));
        Main.main(new String[]{PathTypes.GROUPS.getValue(), filePath});
        assertTrue(areFilesEqual(filePath));
    }

    @Test
    public void brokenResourceGroupsLoad04() {
        String filePath = prepareFilePath("04-resource-groups-broken-load", "groups_02");
        emptyOutput(prepareFilePath("04-resource-groups-broken-load", ""));
        Main.main(new String[]{PathTypes.GROUPS.getValue(), filePath});
        assertTrue(areFilesEqual(filePath));
    }

    @Test
    public void listenersLoad05() {
        String filePath01 = prepareFilePath("05-listeners", "servers_03");
        String filePath02 = prepareFilePath("05-listeners", "groups_03");
        String filePath03 = prepareFilePath("05-listeners", "listeners_01");
        emptyOutput(prepareFilePath("05-listeners", ""));
        Main.main(new String[]{PathTypes.LISTENER.getValue(), filePath01, filePath02, filePath03});
        assertTrue(areFilesEqual(filePath01));
        assertTrue(areFilesEqual(filePath02));
        assertTrue(areFilesEqual(filePath03));
    }

    private boolean areFilesEqual(String filePath) {
        try {
            List<String> lines1 = Files.readAllLines(Path.of(filePath + ".out"));
            List<String> lines2 = Files.readAllLines(Path.of(filePath + ".ref"));

            if (lines1.size() != lines2.size()) {
                return false;
            }

            for (int i = 0; i < lines1.size(); i++) {
                String line1 = lines1.get(i).trim();
                String line2 = lines2.get(i).trim();

                if (!line1.equals(line2)) {
                    return false;
                }
            }

            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private boolean areEventsEqual(String filePath) {
        try {
            List<String> lines1 = Files.readAllLines(Path.of(filePath + ".out"));
            List<String> lines2 = Files.readAllLines(Path.of(filePath + ".ref"));

            if (lines1.size() != lines2.size()) {
                return false;
            }

            for (String line1 : lines1) {
                if (!lines2.contains(line1)) {
                    return false;
                }
            }

            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private void emptyOutput(String filePath) {
        File[] files = new File(filePath).listFiles();

        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".out")) {
                file.delete();
            }
        }
    }

    private String prepareFilePath(String dirName, String fileName) {
        return String.join(File.separator, MAIN_PATH, dirName, fileName);
    }
}
