package com.goit.javacore5.feature;

import com.goit.javacore5.feature.human.HumanGenerator;
import com.goit.javacore5.feature.human.HumanServiceV2;
import com.goit.javacore5.feature.storage.Storage;

import java.sql.SQLException;
import java.time.LocalDate;

public class App {
    public static void main(String[] args) throws SQLException {
        Storage storage = Storage.getInstance();

//        new DatabaseInitService().initDb(storage);

        HumanServiceV2 humanService = new HumanServiceV2(storage);
//
//        //1) Вставка 100 000 по одному - 750 ms
//        //2) Вставка 1000 000 пакетами по 10 - 750
//        //3) Вставка 100 000 пакетами по 100 - 730+
//        //4) Вставка 100 000 пакетами по 1000 - 740
//
        HumanGenerator generator = new HumanGenerator();
        String[] names = generator.generateNames(100000);
        LocalDate[] dates = generator.generateDates(100000);
        humanService.createNewHumans(names, dates);
//
//        int chunkSize = 10000;
//        String[][] nameChunks = split(names, chunkSize);
//        LocalDate[][] dateChunks = split(dates, chunkSize);
//
//        int totalNameCount = 0;
//        int totalDateCount = 0;
//
//        long start = System.currentTimeMillis();
//        for (int i = 0; i < nameChunks.length; i++) {
//            String[] nameChunk = nameChunks[i];
//            LocalDate[] dateChunk = dateChunks[i];
//
//            humanService.createNewHumans(nameChunk, dateChunk);
//
//            totalNameCount += nameChunk.length;
//            totalDateCount += dateChunk.length;
//        }
////        for (int i = 0; i < names.length; i++) {
////            humanService.createNewHuman(names[i], dates[i]);
////        }
//        long duration = System.currentTimeMillis() - start;
//        System.out.println("duration = " + duration);
//
//        System.out.println("totalNameCount = " + totalNameCount);
//        System.out.println("totalDateCount = " + totalDateCount);

//        Map<String, String> renameMap = new HashMap<>();
//        renameMap.put("John", "Ivan");
//        renameMap.put("Jennifer", "Olga");
//        humanService.rename(renameMap);
    }


    static String[][] split(String[] source, int chunkSize) {
        int chunkCount = source.length / chunkSize;
        String[][] result = new String[chunkCount][chunkSize];

        int index = 0;
        for (String[] currentChunk : result) {
            for (int j = 0; j < chunkSize; j++) {
                currentChunk[j] = source[index++];
            }
        }

        return result;
    }

    static LocalDate[][] split(LocalDate[] source, int chunkSize) {
        int chunkCount = source.length / chunkSize;
        LocalDate[][] result = new LocalDate[chunkCount][chunkSize];

        int index = 0;
        for (LocalDate[] currentChunk : result) {
            for (int j = 0; j < chunkSize; j++) {
                currentChunk[j] = source[index++];
            }
        }

        return result;
    }

}
