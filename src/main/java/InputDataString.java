import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class InputDataString implements InputData {
    @Override
    public void sortFile(List<Iterator<String>> iterators, String sortType, String outFile) {
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

    @Override
    public void createFile(List<String> inFiles) {
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
}
