package com.ingeniatest.gistsproject.utilities;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.ingeniatest.gistsproject.model.File;
import com.ingeniatest.gistsproject.model.Files;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FilesDeserializer implements JsonDeserializer<Files> {

    @Override
    public Files deserialize(JsonElement element,
                             Type type,
                             JsonDeserializationContext context)
            throws JsonParseException {

        JsonObject jsonObject = element.getAsJsonObject();

        List<File> files = new ArrayList<>();

        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {

            File file = context.deserialize(entry.getValue(), File.class);
            files.add(file);
        }
        return new Files(files);
    }
}