package utils;

public class DataUtils {
    private static final String ARG_S = "-s";
    private static final String ARG_I = "-i";

    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            System.out.println(str + " is not an Integer type");
            return false;
        }
    }

    public static boolean isCorrectData(String lastValue, String temp, String dataType) {
        if (lastValue != null) {
            if (dataType.equals(ARG_S) && lastValue.compareTo(temp) > 0) {
                return false;
            }
            if (dataType.equals(ARG_I)) {
                return isInteger(temp) && Integer.parseInt(lastValue) <= Integer.parseInt(temp);
            }
        }
        if (dataType.equals(ARG_I)) {
            return isInteger(temp);
        } else {
            return true;
        }
    }
}