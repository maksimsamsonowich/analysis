import org.apache.commons.math3.stat.inference.MannWhitneyUTest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private static final double PERFECT_VALUE = 0.05;

    private static final String OUTPUT_PATTERN = "%s %s";

    private static final String GOOD_ONE = "это хороший критерий.";

    private static final String WORTH_ONE = "это плохой критерий.";
    
    private static Map<String, List<Double>> SICK_MAP = new HashMap<>();

    private static Map<String, List<Double>> HEALTH_MAP = new HashMap<>();

    private static MannWhitneyUTest MANN_WHITNEY_UTEST = new MannWhitneyUTest();

    public static void main(String... args) throws IOException {
        readFromCsv();
        analysis();
    }

    private static void analysis() {
        List<String> keys = new ArrayList<>(SICK_MAP.keySet());

        for (int i = 0; i < SICK_MAP.size(); i++) {
            double coefficient = MANN_WHITNEY_UTEST.mannWhitneyUTest(HEALTH_MAP.get(keys.get(i)).stream().mapToDouble(Double::doubleValue).toArray(),
                    SICK_MAP.get(keys.get(i)).stream().mapToDouble(Double::doubleValue).toArray());

            if (coefficient < PERFECT_VALUE) {
                System.out.println(String.format(OUTPUT_PATTERN, keys.get(i), GOOD_ONE));
            } else {
                System.out.println(String.format(OUTPUT_PATTERN, keys.get(i), WORTH_ONE));
            }
        }
    }

    static void readFromCsv() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("src\\main\\resources\\health.csv"))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                HEALTH_MAP.put(values[0], Arrays.asList(values).subList(1, 27).stream()
                        .map(Double::parseDouble)
                        .collect(Collectors.toList()));
            }
        }

        try (BufferedReader br = new BufferedReader(new FileReader("src\\main\\resources\\sick.csv"))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                SICK_MAP.put(values[0], Arrays.asList(values).subList(1, 27).stream()
                        .map(Double::parseDouble)
                        .collect(Collectors.toList()));
            }
        }
    }

}
