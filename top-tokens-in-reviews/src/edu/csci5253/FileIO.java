package edu.csci5253;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

class FileIO implements Serializable {

    static void write(String lines, String outputPath) {
        try {
            Files.write(Paths.get(outputPath), lines.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("SameParameterValue")
    static List<String> readLines(String inputPath) {
        try {
            return Files.readAllLines(Paths.get(inputPath));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
