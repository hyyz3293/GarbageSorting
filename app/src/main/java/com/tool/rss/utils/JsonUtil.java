package com.tool.rss.utils;

import android.text.TextUtils;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Json解析工具
 *
 * @author mos
 * @date 2017.04.27
 * @note -
 * -------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class JsonUtil {
    /** Gson */
    private static Gson sGson = new Gson();

    /**
     * 对象转json字符串
     *
     * @param result 结果对象
     * @return 结果字符串
     */
    public static String objectToString(Object result) {

        return sGson.toJson(result);
    }

    /**
     * 字符串转对象
     *
     * @param data 字符串
     * @param clazz 对象的Class
     * @return 对象
     */
    public static <T> T stringToObject(String data, Class<T> clazz) {
        return stringToObject(data, clazz, sGson);
    }

    /**
     * 字符串转对象
     *
     * @param data 字符串
     * @param clazz 对象的Class
     * @param gson 转换器
     * @return 对象
     */
    public static <T> T stringToObject(String data, Class<T> clazz, Gson gson) {
        T obj = null;
        if (gson == null) {
            gson = sGson;
        }
        try {
            obj = gson.fromJson(data, clazz);
        } catch (Exception ignored) {
        }

        return obj;
    }

    /**
     * 字符串转对象
     *
     * @param data 字符串
     * @param typeOfT 类型
     * @return 对象
     */
    public static <T> T stringToObject(String data, Type typeOfT) {
        return stringToObject(data, typeOfT, sGson);
    }

    /**
     * 字符串转对象
     *
     * @param data 字符串
     * @param typeOfT 类型
     * @param gson 转换器
     * @return 对象
     */
    public static <T> T stringToObject(String data, Type typeOfT, Gson gson) {
        T obj = null;
        if (gson == null) {
            gson = sGson;
        }
        try {
            obj = gson.fromJson(data, typeOfT);
        } catch (Exception ignored) {
        }

        return obj;
    }

    /**
     * 解析json
     *
     * @param jsonObj json对象
     * @param callback 回调
     * @param recursive 是否处理嵌套
     */
    public static void parseJson(JSONObject jsonObj, IParseCallback callback, boolean recursive) {
        if (jsonObj == null || callback == null) {

            return;
        }

        Iterator<String> itor = jsonObj.keys();
        while (itor.hasNext()) {
            try {
                String key = itor.next();
                Object valueObj = jsonObj.get(key);
                if (valueObj instanceof String) {
                    // 字符串类型
                    callback.onKeyValue(key, (String) valueObj);
                } else if (valueObj instanceof JSONObject && recursive) {
                    parseJson((JSONObject) valueObj, callback, true);
                }

            } catch (JSONException e) {
            }
        }
    }

    /**
     * 解析浅层次的json
     *
     * @param jsonString json字符串
     * @param callback 回调
     * @note 只能解析1层Json字符串，不能处理Json套Json的情况。
     */
    public static void parseFlatJson(String jsonString, IParseCallback callback) {
        if (TextUtils.isEmpty(jsonString) || callback == null) {

            return;
        }

        try {
            parseJson(new JSONObject(jsonString), callback, false);
        } catch (JSONException e) {
        }
    }

    /**
     * 解析嵌套的json
     *
     * @param jsonString json字符串
     * @param callback 回调
     */
    public static void parseRecursiveJson(String jsonString, IParseCallback callback) {
        if (TextUtils.isEmpty(jsonString) || callback == null) {

            return;
        }

        try {
            parseJson(new JSONObject(jsonString), callback, true);
        } catch (JSONException e) {
        }
    }

    /**
     * 转json字符串
     *
     * @param data json的map
     * @return json字符串
     */
    public static String toJsonString(Map<String, String> data) {
        if (data == null) {

            return "";
        }

        return sGson.toJson(data);
    }

    /**
     * mao转object
     *
     * @param data json的map
     * @param clazz 类名
     * @return json字符串
     */
    public static <T> T mapToObject(Map<String, String> data, Class<T> clazz) {
        if (data == null) {

            return null;
        }

        return sGson.fromJson(sGson.toJson(data), clazz);
    }

    /**
     * 转json映射
     *
     * @param data json字符串
     * @return json映射
     */
    public static Map<String, String> toJsonMap(String data) {
        final Map<String, String> jsonMap = new HashMap<>();

        parseFlatJson(data, new IParseCallback() {
            @Override
            public void onKeyValue(String key, String value) {
                jsonMap.put(key, value);
            }
        });

        return jsonMap;
    }

    /**
     * 查找第一次出现的值
     *
     * @param json json字符串
     * @param key key
     * @return 值
     * @note key值不区分大小写
     */
    public static String findFirstValue(String json, String key) {
        return findFirstValue(json, key, null);
    }

    /**
     * 查找第一次出现的值
     *
     * @param json json字符串
     * @param key key
     * @param defValue 若没找到的默认值
     * @return 值
     * @note key值不区分大小写
     */
    public static String findFirstValue(String json, String key, String defValue) {
        if (TextUtils.isEmpty(json) || TextUtils.isEmpty(key)) {

            return defValue;
        }

        try {
            JSONObject jsonObj = new JSONObject(json);
            return findFirstValue(jsonObj, key, defValue);
        } catch (JSONException e) {
        }

        return defValue;
    }

    /**
     * 查找第一次出现的值
     *
     * @param jsonObj json对象
     * @param key key
     * @return 值
     * @note key值不区分大小写
     */
    public static String findFirstValue(JSONObject jsonObj, String key) {
        return findFirstValue(jsonObj, key, null);
    }

    /**
     * 查找第一次出现的值
     *
     * @param jsonObj json对象
     * @param key key
     * @param defValue 若没找到的默认值
     * @return 值
     * @note key值不区分大小写
     */
    public static String findFirstValue(JSONObject jsonObj, String key, String defValue) {
        if (jsonObj == null || TextUtils.isEmpty(key)) {

            return defValue;
        }

        Iterator<String> itor = jsonObj.keys();
        while (itor.hasNext()) {
            String nextKey = itor.next();
            Object nextValue = jsonObj.opt(nextKey);
            if (nextValue != null) {
                if (nextKey.equalsIgnoreCase(key)) {
                    // 找到对应的值
                    return String.valueOf(nextValue);

                } else if (nextValue instanceof JSONObject) {
                    // 递归JSON对象
                    String result = findFirstValue((JSONObject) nextValue, key, defValue);
                    if (!TextUtils.isEmpty(result)) {

                        return result;
                    }
                }
            }
        }

        return defValue;
    }

    /**
     * 解析回调
     */
    public interface IParseCallback {
        /**
         * 收到Key和Value
         *
         * @param key 键
         * @param value 值
         */
        void onKeyValue(String key, String value);
    }
}
