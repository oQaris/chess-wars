package service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import dto.MoveDTO;

import java.io.IOException;

public class SerializationService {
    private static final Gson gson = new Gson();

    private static String serialize(final Object obj) {
        return gson.toJson(obj);
    }
}