package com.bluecc.pay;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
    public static String pretty(Object object) throws JsonProcessingException {
        String json = new ObjectMapper().writerWithDefaultPrettyPrinter()
                .writeValueAsString(object);
        return json;
    }
}

