package com.dqw.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 处理JSON工具类
 */
public class JSONUtils {

    public static final ObjectMapper MAPPER = new ObjectMapper();

    public static String objToJson(Object obj) {
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
