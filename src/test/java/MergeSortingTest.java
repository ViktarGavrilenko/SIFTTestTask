import org.testng.Assert;
import org.testng.annotations.Test;

public class MergeSortingTest {
    @Test
    public void testSortInitializeDataIntAsc() {
        MergeSorting sort = new MergeSorting();
        sort.initializeData("-i -a out.txt in.txt");

        Assert.assertEquals(sort.getSortType(), "-a", "Incorrect sorting mode");
        Assert.assertEquals(sort.getDataType(), "-i", "Incorrect data type");
        Assert.assertEquals(sort.getOutFile(), "out.txt", "Incorrect output file name");
        Assert.assertEquals(sort.getInFiles().size(), 1, "Incorrect number of input files");
        Assert.assertEquals(sort.getInFiles().get(0), "in.txt", "Incorrect input file name");
    }

    @Test
    public void testSortInitializeDataStr() {
        MergeSorting sort = new MergeSorting();
        sort.initializeData("-s out.txt in1.txt in2.txt in3.txt");

        Assert.assertEquals(sort.getSortType(), "-a", "Incorrect sorting mode");
        Assert.assertEquals(sort.getDataType(), "-s", "Incorrect data type");
        Assert.assertEquals(sort.getOutFile(), "out.txt", "Incorrect output file name");
        Assert.assertEquals(sort.getInFiles().size(), 3, "Incorrect number of input files");
        Assert.assertEquals(sort.getInFiles().get(0), "in1.txt", "Incorrect input file name");
        Assert.assertEquals(sort.getInFiles().get(1), "in2.txt", "Incorrect input file name");
        Assert.assertEquals(sort.getInFiles().get(2), "in3.txt", "Incorrect input file name");
    }

    @Test
    public void testSortInitializeDataDescStr() {
        MergeSorting sort = new MergeSorting();
        sort.initializeData("-d -s out.txt in1.txt in2.txt");

        Assert.assertEquals(sort.getSortType(), "-d", "Incorrect sorting mode");
        Assert.assertEquals(sort.getDataType(), "-s", "Incorrect data type");
        Assert.assertEquals(sort.getOutFile(), "out.txt", "Incorrect output file name");
        Assert.assertEquals(sort.getInFiles().size(), 2, "Incorrect number of input files");
        Assert.assertEquals(sort.getInFiles().get(0), "in1.txt", "Incorrect input file name");
        Assert.assertEquals(sort.getInFiles().get(1), "in2.txt", "Incorrect input file name");
    }
}
