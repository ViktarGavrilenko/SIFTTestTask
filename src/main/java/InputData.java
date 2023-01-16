import java.util.Iterator;
import java.util.List;

public interface InputData {
    String ARG_D = "-d";
    int NUMBER_OF_LINES_IN_FILE = 100;

    void sortFile(List<Iterator<String>> iterators, String sortType, String outFile);

    void createFile(List<String> inFiles);
}
