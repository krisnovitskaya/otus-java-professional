package ru.otus.dataprocessor;

import com.google.gson.Gson;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class FileSerializer implements Serializer {

    private final String fileName;

    public FileSerializer(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void serialize(Map<String, Double> data) {
        var gson = new Gson();
        var jsonText = gson.toJson(data);
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fileName))){
            bos.write(jsonText.getBytes());
            bos.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
