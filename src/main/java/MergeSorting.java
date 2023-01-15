import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class MergeSorting {
    private String sortType = "-a";
    private String dataType;
    private String outFile;
    private List<String> inFiles;

    private final static String TEMP_DIR = "temp";
    private final static int NUMBER_OF_LINES_IN_FILE = 1000;

    public String getSortType() {
        return sortType;
    }

    public String getDataType() {
        return dataType;
    }

    public String getOutFile() {
        return outFile;
    }

    public List<String> getInFiles() {
        return inFiles;
    }

    public static void main(String[] args) {
        MergeSorting sort = new MergeSorting();
        sort.initializeData(args);

        for (int i = 0; i < sort.inFiles.size(); i++) {
            List<Long> list = new ArrayList<>();
            for (int j = 0; j < 4000; j++) {
                final Random random = new Random();
                list.add(random.nextLong());
            }
            Collections.sort(list);

            try (FileWriter writer = new FileWriter(sort.inFiles.get(i), true)) {
                for (Long aLong : list) {
                    writer.write(String.valueOf(aLong) + '\n');
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }

            System.out.println(sort.readFile(sort.inFiles.get(i)));
        }
        sort.getFile(sort.inFiles);

    }

    public void setArg(String arg) {
        switch (arg) {
            case "-a", "-d" -> sortType = arg;
            case "-i", "-s" -> dataType = arg;
        }
    }

    public void initializeData(String[] data) {
        int initialIndex = 0;
        setArg(data[initialIndex++]);
        if (data[initialIndex].charAt(0) == '-') {
            setArg(data[initialIndex++]);
        }
        outFile = data[initialIndex++];
        inFiles = new ArrayList<>(Arrays.asList(data).subList(initialIndex, data.length));
    }

    public List<String> readFile(String fineName) {
        try {
            return Files.readAllLines(Paths.get(fineName));
        } catch (IOException e) {
            System.out.println("Error reading file " + fineName + ". " + e);
            throw new RuntimeException(e);
        }
    }

    public void getFile(List<String> inFiles) {
        List<Iterator<String>> iterators = new ArrayList<>();
        createDir(TEMP_DIR);
        for (String inFile : inFiles) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(inFile));
                LinkedList<String> partOfFile = new LinkedList<>();
                String element = reader.readLine();

                while (element != null) {
                    partOfFile.add(element);
                    element = reader.readLine();
                    if (partOfFile.size() >= NUMBER_OF_LINES_IN_FILE || element == null) {
                        createPartOfFile(partOfFile, inFile);
                        iterators.add(new LinkedList<>(partOfFile).iterator());
                        partOfFile.clear();
                    }
                }

//                iterators.add(reader.lines().iterator());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        List<Long> minValues = new ArrayList<>();
        for (Iterator<String> iterator : iterators) {
            if (iterator.hasNext()) {
                minValues.add(Long.valueOf(iterator.next()));
            }
        }

        while (minValues.size() > 0) {
            Long minValue = Collections.min(minValues);
            for (int i = 0; i < minValues.size(); i++) {
                if (minValues.get(i).equals(minValue)) {
//                    resultArray.add(minValues.get(i));
                    try (FileWriter writer = new FileWriter(outFile, true)) {
                        writer.write(String.valueOf(minValues.get(i)) + '\n');
                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    }
                    if (iterators.get(i).hasNext()) {
                        minValues.set(i, Long.valueOf(iterators.get(i).next()));
                    } else {
                        minValues.remove(i);
                        iterators.remove(i);
                    }
                }
            }
        }
    }

    public void createDir(String dirName) {
        if (!Files.exists(Paths.get(dirName))) {
            try {
                Files.createDirectory(Paths.get(dirName));
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }

    public void createPartOfFile(LinkedList<String> partOfFile, String nameFile) {
        try (FileWriter writer = new FileWriter(
                TEMP_DIR + "//" + partOfFile.getFirst() + "!" + nameFile + "!" + partOfFile.getLast(), true)) {
            for (String s : partOfFile) {
                writer.write(s + '\n');
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}