import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class InputDataLong implements InputData{
    @Override
    public void sortFile(List<Iterator<String>> iterators, String sortType, String outFile) {
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

    @Override
    public void createFile(List<String> inFiles) {
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
