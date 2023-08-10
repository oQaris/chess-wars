package service;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class DeserializationService {
    private static final Gson gson = new Gson();

    private static <T> T deserialize(final String json, final Class<T> clazz)
            throws JsonSyntaxException {
        try {
            return gson.fromJson(json, clazz);
        } catch (final JsonSyntaxException e) {
            throw new JsonSyntaxException("Something went wrong with deserialization of json - " + json);
        }
    }
}
