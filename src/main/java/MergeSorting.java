import java.io.*;
import java.util.*;

import static utils.DataUtils.isCorrectData;
import static utils.FileUtils.createDir;
import static utils.FileUtils.deleteTempDir;

public class MergeSorting {
    private static final String ARG_A = "-a";
    private static final String ARG_D = "-d";
    private static final String ARG_S = "-s";
    private static final String ARG_I = "-i";
    private static final String TEMP_DIR = "temp";
    private static final int NUMBER_OF_LINES_IN_PART_FILE = 10000;

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

    private void setArg(String arg) {
        switch (arg) {
            case ARG_A, ARG_D -> sortType = arg;
            case ARG_I, ARG_S -> dataType = arg;
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

    public void createSortedFile(List<String> inFiles) {
        deleteTempDir(TEMP_DIR);
        splitFilesIntoParts(inFiles);
        List<Iterator<String>> iterators = getIteratorsFromFiles();
        if (dataType.equals(ARG_I)) {
            sortIntegerFile(iterators);
        } else {
            sortStringFile(iterators);
        }
    }

    private void writePartOfFile(LinkedList<String> partOfFile, String nameFile) {
        try (FileWriter writer = new FileWriter(TEMP_DIR + "//" + partOfFile.getFirst() +
                "!" + nameFile + "!" + partOfFile.getLast(), true)) {
            for (String s : partOfFile) {
                writer.write(s + '\n');
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void sortIntegerFile(List<Iterator<String>> iterators) {
        List<Integer> values = getValuesInt(iterators);
        while (!values.isEmpty()) {
            String value;
            if (sortType.equals(ARG_D)) {
                value = String.valueOf(Collections.max(values));
            } else {
                value = String.valueOf(Collections.min(values));
            }
            writeToFileInt(values, value, iterators);
        }
    }

    private List<Integer> getValuesInt(List<Iterator<String>> iterators) {
        List<Integer> values = new ArrayList<>();
        for (Iterator<String> iterator : iterators) {
            if (iterator.hasNext()) {
                values.add(Integer.valueOf(iterator.next()));
            }
        }
        return values;
    }

    private void writeToFileInt(List<Integer> values, String value, List<Iterator<String>> iterators) {
        for (int i = 0; i < values.size(); i++) {
            if (String.valueOf(values.get(i)).equals(value)) {
                try (FileWriter writer = new FileWriter(outFile, true)) {
                    writer.write(String.valueOf(values.get(i)) + '\n');
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
                if (iterators.get(i).hasNext()) {
                    values.set(i, Integer.valueOf(iterators.get(i).next()));
                } else {
                    values.remove(i);
                    iterators.remove(i);
                }
            }
        }
    }

    private void sortStringFile(List<Iterator<String>> iterators) {
        List<String> values = getValuesStr(iterators);
        while (!values.isEmpty()) {
            String value;
            if (sortType.equals(ARG_D)) {
                value = Collections.max(values);
            } else {
                value = Collections.min(values);
            }
            writeToFileStr(values, value, iterators);
        }
    }

    private List<String> getValuesStr(List<Iterator<String>> iterators) {
        List<String> values = new ArrayList<>();
        for (Iterator<String> iterator : iterators) {
            if (iterator.hasNext()) {
                values.add(iterator.next());
            }
        }
        return values;
    }

    private void writeToFileStr(List<String> values, String value, List<Iterator<String>> iterators) {
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

    private List<Iterator<String>> getIteratorsFromFiles() {
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
        return iterators;
    }

    private void splitFilesIntoParts(List<String> inFiles) {
        createDir(TEMP_DIR);
        for (String inFile : inFiles) {
            try (BufferedReader reader = new BufferedReader(new FileReader(inFile))) {
                LinkedList<String> partOfFile = new LinkedList<>();
                Iterator<String> file = reader.lines().iterator();
                splitFile(file, partOfFile, inFile);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void createPartOfFile(LinkedList<String> partOfFile, String inFile) {
        if (sortType.equals(ARG_D)) {
            Collections.reverse(partOfFile);
        }
        writePartOfFile(partOfFile, inFile);
        partOfFile.clear();
    }

    private void splitFile(Iterator<String> file, LinkedList<String> partOfFile, String inFile) {
        String lastValue = null;
        String temp;
        while (file.hasNext()) {
            temp = file.next();
            if (!temp.contains(" ")) {
                if (!isCorrectData(lastValue, temp, dataType)) {
                    System.out.println("The input file " + inFile + " is incorrectly sorted. String with value: " + temp);
                    if (!partOfFile.isEmpty()) {
                        createPartOfFile(partOfFile, inFile);
                    }
                    break;
                }
                partOfFile.add(temp);
                lastValue = temp;
                if (partOfFile.size() >= NUMBER_OF_LINES_IN_PART_FILE || !file.hasNext()) {
                    createPartOfFile(partOfFile, inFile);
                }
            } else {
                System.out.println("The line '" + temp + "' in file " + inFile + " contains a space. " +
                        "This line will not be taken into account when sorting");
            }
        }
    }
}