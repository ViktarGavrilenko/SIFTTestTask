package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class FileUtils {
    private static final int NUMBER_OF_LINES_IN_FILE = 4000000;

    public static void deleteTempDir(String nameDir) {
        File dir = new File(nameDir);
        File[] arrFiles;
        arrFiles = dir.listFiles();
        if (arrFiles != null) {
            try {
                for (File file : arrFiles) {
                    Files.delete(file.toPath());
                }
                Files.delete(Path.of(nameDir));
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }

    public static void createDir(String dirName) {
        Path of = Path.of(dirName);
        if (!Files.exists(of)) {
            try {
                Files.createDirectory(of);
            } catch (IOException e) {
                System.err.println("Directory creation error. " + e);
            }
        }
    }

    public static void createStringFile(List<String> inFiles) {
        for (String inFile : inFiles) {
            List<String> list = new ArrayList<>();
            for (int j = 0; j < NUMBER_OF_LINES_IN_FILE; j++) {
                final Random random = new Random();
                StringBuilder line = new StringBuilder();
                for (int k = 0; k < 21 - random.nextInt(20); k++) {
                    char c = (char) (random.nextInt(26) + 'a');
                    if (random.nextInt(3) == 2) {
                        line.append(String.valueOf(c).toUpperCase());
                    } else {
                        line.append(c);
                    }
                }
                list.add(line.toString());

            }
            Collections.sort(list);

            try (FileWriter writer = new FileWriter(inFile, true)) {
                for (String line : list) {
                    writer.write(line + '\n');
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public static void createIntegerFile(List<String> inFiles) {
        for (String inFile : inFiles) {
            List<Integer> list = new ArrayList<>();
            for (int j = 0; j < NUMBER_OF_LINES_IN_FILE; j++) {
                final Random random = new Random();
                list.add(random.nextInt(Integer.MAX_VALUE));
            }
            Collections.sort(list);

            try (FileWriter writer = new FileWriter(inFile, true)) {
                for (Integer line : list) {
                    writer.write(String.valueOf(line) + '\n');
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}