package com.example.ibaselib.utils;

import android.app.VoiceInteractor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * time   :  2018/11/19
 * author :  Z
 * des    :
 */
public class GsonUtil {

    /**
     * 格式化对象为Json
     *
     * @param object
     * @return
     */
    public static String format(Object object) {
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(gson.toJson(object));
        String prettyJsonString = gson.toJson(je);
        //�?单处理function
        String[] lines = prettyJsonString.split("\n");
        lines = replaceFunctionQuote(lines);
        StringBuilder stringBuilder = new StringBuilder();
        for (String line : lines) {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }

    /**
     * 格式化对象为Json
     *
     * @param object
     * @return
     */
    public static String prettyFormat(Object object) {
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(gson.toJson(object));
        String prettyJsonString = gson.toJson(je);
        //�?单处理function
        String[] lines = prettyJsonString.split("\n");
        lines = replaceFunctionQuote(lines);
        StringBuilder stringBuilder = new StringBuilder();
        for (String line : lines) {
            stringBuilder.append(line + "\n");
        }
        return stringBuilder.toString();
    }

    /**
     * 处理字符串中的function�?(function(){})()，除{}中的代码外，其他地方不允许有空格
     *
     * @param lines
     * @return
     */
    public static String[] replaceFunctionQuote(String[] lines) {
        boolean function = false;
        boolean immediately = false;
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            if (!function && line.contains("\"function")) {
                function = true;
                line = line.replaceAll("\"function", "function");
            }
            if (function && line.contains("}\"")) {
                function = false;
                line = line.replaceAll("\\}\"", "\\}");
            }
            if (!immediately && line.contains("\"(function")) {
                immediately = true;
                line = line.replaceAll("\"\\(function", "\\(function");
            }
            if (immediately && line.contains("})()\"")) {
                immediately = false;
                line = line.replaceAll("\\}\\)\\(\\)\"", "\\}\\)\\(\\)");
            }
            lines[i] = line;
        }
        return lines;
    }


    /**
     * 输出Json
     *
     * @param object
     * @return
     */
    public static void print(Object object) {
        System.out.println(format(object));
    }

    /**
     * 输出Json
     *
     * @param object
     * @return
     */
    public static void printPretty(Object object) {
        System.out.println(prettyFormat(object));
    }


    /**
     * 将  Json  转换成 JavaBean
     *
     * @param jsonStr
     * @param className
     * @param <T>
     * @return
     */
    public static <T> T json2Bean(String jsonStr, Class<T> className) {
        Gson gson = new Gson();
        return gson.fromJson(jsonStr, className);
    }

    /**
     * 把 Json 字符串 转换成 JavaBean
     *
     * @param json
     * @param clazz
     * @param t
     * @param <T>
     * @return
     */
    public static <T> T getBean(String json, Class clazz, Class<T> t) {
        Gson gson = new Gson();
        Type objectType = type(t, clazz);
        return gson.fromJson(json, objectType);
    }


    public static <T> List<T> getArrayList(String json, Class<T> className) {
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(json).getAsJsonArray();
        List<T> list = new ArrayList<T>();
        for (JsonElement obj : array) {
            list.add(gson.fromJson(obj, className));
        }
        return list;
    }

    static ParameterizedType type(final Class raw, final Type... args) {
        return new ParameterizedType() {
            public Type getRawType() {
                return raw;
            }

            public Type[] getActualTypeArguments() {
                return args;
            }

            public Type getOwnerType() {
                return null;
            }
        };
    }


}
