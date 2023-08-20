package com.sammythybunnie.runeforged.api.uuidApi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class JsonFileWriter {

    private File baseDirectory;

    public JsonFileWriter(String subDirectory) {
        baseDirectory = FabricLoader.getInstance().getConfigDir().resolve(subDirectory).toFile();
    }

    public void writeJsonToFile(String filename, Object objectToSerialize) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        if (!baseDirectory.exists()) {
            baseDirectory.mkdirs();
        }

        File outputFile = new File(baseDirectory, filename);

        try (FileWriter writer = new FileWriter(outputFile)) {
            gson.toJson(objectToSerialize, writer);
            System.out.println("JSON data has been written to " + outputFile.getPath());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error writing JSON data to " + outputFile.getPath());
        }
    }
}

