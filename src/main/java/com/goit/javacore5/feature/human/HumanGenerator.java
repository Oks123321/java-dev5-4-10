package com.goit.javacore5.feature.human;

import java.time.LocalDate;
import java.util.Random;

public class HumanGenerator {
    private Random random = new Random();

    public String[] generateNames(int count) {
        String[] result = new String[count];

        for (int i = 0; i < count; i++) {
            result[i] = random.nextDouble() + "";
        }

        return result;
    }

    public LocalDate[] generateDates(int count) {
        LocalDate[] result = new LocalDate[count];

        for (int i = 0; i < count; i++) {
            result[i] = LocalDate.now().minusDays(random.nextInt(10000));
        }
        return result;
    }
}
