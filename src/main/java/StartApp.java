public class StartApp {
    public static void main(String[] args) {
        MergeSorting sort = new MergeSorting();
        System.out.println("The application is running. Data is being sorted.");
        System.out.println("Expect it may take a long time...");
        sort.initializeData(args);
        if (sort.getDataType() == null) {
            System.err.println("No data type entered, enter '-s' for strings or '-i' for integers");
        } else {
            sort.createSortedFile(sort.getInFiles());
            System.out.println("Sorting is complete");
        }
    }
}
