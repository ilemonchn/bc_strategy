package com.bc.bc_strategy.util;

import com.google.gson.Gson;

public class JsonUtil {
    private static Gson gson = new Gson();

    public static String toJson(Object o) {
        return gson.toJson(o);
    }

}
