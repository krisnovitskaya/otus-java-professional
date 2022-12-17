package ru.otus.dataprocessor;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import ru.otus.model.Measurement;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ResourcesFileLoader implements Loader {
    private final String fileName;

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {
        try(BufferedInputStream bis = new BufferedInputStream(ResourcesFileLoader.class.getClassLoader().getResourceAsStream(fileName))) {
            var rawJson = new String(bis.readAllBytes());
            var gson = new Gson();
            Type listOfMeasurementType = new TypeToken<ArrayList<Measurement>>() {}.getType();
            List<Measurement> measurements = gson.fromJson(rawJson, listOfMeasurementType);
            return measurements;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
