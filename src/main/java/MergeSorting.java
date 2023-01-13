import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class MergeSorting {
    private String sortType = "-a";
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
        Scanner in = new Scanner(System.in);
        String data = in.nextLine();
        sort.initializeData(data);

        for (int i = 0; i < sort.inFiles.size(); i++) {
            System.out.println(sort.readFile(sort.inFiles.get(i)));
        }

    }

    public void setArg(String arg) {
        switch (arg) {
            case "-a", "-d" -> sortType = arg;
            case "-i", "-s" -> dataType = arg;
        }
    }

    public void initializeData(String str) {
        String[] data = str.split(" ");
        int initialIndex = 0;
        setArg(data[initialIndex++]);
        if (data[initialIndex].charAt(0) == '-') {
            setArg(data[initialIndex++]);
        }
        outFile = data[initialIndex++];
        inFiles = new ArrayList(Arrays.asList(data).subList(initialIndex, data.length));
    }

    public List<String> readFile(String fineName) {
        try {
            return Files.readAllLines(Paths.get(fineName));
        } catch (IOException e) {
            System.out.println("Error reading file " + fineName + ". " + e);
            throw new RuntimeException(e);
        }
    }
}