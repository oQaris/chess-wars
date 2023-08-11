package service;

import com.google.gson.Gson;

public class SerializationService {
    private static final Gson gson = new Gson();

    private static String serialize(final Object obj) {
        return gson.toJson(obj);
    }
}