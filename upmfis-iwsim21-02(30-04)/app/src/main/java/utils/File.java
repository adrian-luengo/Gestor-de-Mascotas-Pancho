package utils;


import utils.results.Error;
import utils.results.Result;

import java.io.BufferedWriter;

public class File {
    static public File instance = null;

    public static File getInstance() {
        if (instance == null) {
            instance = new File();
        }
        return instance;
    }

    public Error write(String path, String content) {
        try {
            java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter(path));
            writer.write(content);
            writer.close();
        } catch (java.io.IOException e) {
            System.out.printf("Error writing file %s\n", e.getMessage());
            return new Error("Error writing file");
        }
        return null;
    }

    public Result<String> read(String path) {
        if (!new java.io.File(path).exists()) {
            return new Result<>(new Error("File does not exist"));
        }
        try {
            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(path));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            reader.close();
            return new Result<>(content.toString());
        } catch (java.io.IOException e) {
            return new Result<>(new Error("Error reading file"));
        }
    }
}
