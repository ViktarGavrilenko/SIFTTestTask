import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class MergeSorting {
    private final static String ARG_A = "-a";
    private final static String ARG_D = "-d";
    private final static String ARG_S = "-s";
    private final static String ARG_I = "-i";
    private final static String TEMP_DIR = "temp";
    private final static int NUMBER_OF_LINES_IN_PART_FILE = 7;
    private final static int NUMBER_OF_LINES_IN_FILE = 200;


    private String sortType = ARG_A;
    private String dataType;
    private String outFile;
    private List<String> inFiles;

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

        if (sort.dataType.equals(ARG_I)) {
            sort.createIntegerFile();
        }
        if (sort.dataType.equals(ARG_S)) {
            sort.createStringFile();
        }

        sort.getFile(sort.inFiles);
    }

    public void setArg(String arg) {
        switch (arg) {
            case ARG_A:
            case ARG_D:
                sortType = arg;
                break;
            case ARG_I:
            case ARG_S:
                dataType = arg;
                break;
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
        createDir(TEMP_DIR);
        for (String inFile : inFiles) {
            try (BufferedReader reader = new BufferedReader(new FileReader(inFile))) {
                LinkedList<String> partOfFile = new LinkedList<>();
                Iterator<String> file = reader.lines().iterator();
                while (file.hasNext()) {
                    partOfFile.add(file.next());
                    if (partOfFile.size() >= NUMBER_OF_LINES_IN_PART_FILE || !file.hasNext()) {
                        if (sortType.equals(ARG_D)) {
                            Collections.reverse(partOfFile);
                        }
                        createPartOfFile(partOfFile, inFile);
                        partOfFile.clear();
                    }
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        List<Iterator<String>> iterators = new LinkedList<>();

        File dir = new File(TEMP_DIR);
        File[] arrFiles = dir.listFiles();
        if (arrFiles != null) {
            for (File file : arrFiles) {
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    iterators.add(reader.lines().iterator());
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        if (dataType.equals(ARG_I)) {
            sortIntegerFile(iterators);
        } else {
            sortStringFile(iterators);
        }
    }

    public void createDir(String dirName) {
        Path of = Path.of(dirName);
        if (!Files.exists(of)) {
            try {
                Files.createDirectory(of);
            } catch (IOException e) {
                System.err.println("Directory creation error. " + e);
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

    public void createIntegerFile() {
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

    public void createStringFile() {
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

    public void sortIntegerFile(List<Iterator<String>> iterators) {
        List<Long> values = new ArrayList<>();
        for (Iterator<String> iterator : iterators) {
            if (iterator.hasNext()) {
                values.add(Long.valueOf(iterator.next()));
            }
        }

        while (values.size() > 0) {
            String value;
            if (sortType.equals(ARG_D)) {
                value = String.valueOf(Collections.max(values));
            } else {
                value = String.valueOf(Collections.min(values));
            }

            for (int i = 0; i < values.size(); i++) {
                if (String.valueOf(values.get(i)).equals(value)) {
                    try (FileWriter writer = new FileWriter(outFile, true)) {
                        writer.write(String.valueOf(values.get(i)) + '\n');
                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    }
                    if (iterators.get(i).hasNext()) {
                        values.set(i, Long.valueOf(iterators.get(i).next()));
                    } else {
                        values.remove(i);
                        iterators.remove(i);
                    }
                }
            }
        }
    }

    public void sortStringFile(List<Iterator<String>> iterators) {
        List<String> values = new ArrayList<>();
        for (Iterator<String> iterator : iterators) {
            if (iterator.hasNext()) {
                values.add(iterator.next());
            }
        }

        while (values.size() > 0) {
            String value;
            if (sortType.equals(ARG_D)) {
                value = Collections.max(values);
            } else {
                value = Collections.min(values);
            }

            for (int i = 0; i < values.size(); i++) {
                if (String.valueOf(values.get(i)).equals(value)) {
                    try (FileWriter writer = new FileWriter(outFile, true)) {
                        writer.write(String.valueOf(values.get(i)) + '\n');
                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    }
                    if (iterators.get(i).hasNext()) {
                        values.set(i, iterators.get(i).next());
                    } else {
                        values.remove(i);
                        iterators.remove(i);
                    }
                }
            }
        }
    }
}