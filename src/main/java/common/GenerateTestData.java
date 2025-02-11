package common;

import java.util.Random;
import java.util.stream.Collectors;

public class GenerateTestData {

    public String generateString() {
        String generatedStr = new Random().ints(10, 97, 122)
                .mapToObj(i -> String.valueOf((char) i))
                .collect(Collectors.joining());
        return generatedStr;
    }
}