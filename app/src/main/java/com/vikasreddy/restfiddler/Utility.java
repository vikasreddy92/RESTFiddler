package com.vikasreddy.restfiddler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Utility {

    static String getMapString(Map<String, String> map) {
        return Utility.getHashMapString(toHashMap(map));
    }

    public static HashMap<String, String> toHashMap(Map<String, String> map) {
        HashMap<String, String> hashMap = new HashMap<>();
        for(Map.Entry<String, String> entry: map.entrySet()) {
            hashMap.put(entry.getKey(), entry.getValue());
        }
        return hashMap;
    }

    public static String getMultiMapString(Map<String, List<String>> map) {
        HashMap<String, List<String>> hashMap = Utility.toMultiHashMap(map);
        return Utility.getMultiMapString(hashMap);
    }

    public static HashMap<String, List<String>> toMultiHashMap(Map<String, List<String>> map) {
        HashMap<String, List<String>> hashMap = new HashMap<>();
        for(Map.Entry<String, List<String>> entry: map.entrySet()) {
            hashMap.put(entry.getKey(), entry.getValue());
        }
        return hashMap;
    }

    public static String getHashMapString(HashMap<String, String> map) {
        StringBuilder stringBuilder = new StringBuilder();
        for(Map.Entry<String, String> entry : map.entrySet() ) {
            stringBuilder.append(entry.getKey());
            stringBuilder.append(" --> ");
            stringBuilder.append(entry.getValue());
            stringBuilder.append('\n');
        }
        return stringBuilder.toString();
    }

    public static String getMultiMapString(HashMap<String, List<String>> map) {
        StringBuilder stringBuilder = new StringBuilder();
        for(Map.Entry<String, List<String>> entry : map.entrySet() ) {
            stringBuilder.append(entry.getKey());
            stringBuilder.append(" --> ");
            stringBuilder.append(entry.getValue().toString());
            stringBuilder.append('\n');
        }
        return stringBuilder.toString();
    }

    public static String toStringWithSeparator(List<String> list, String sep) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String str: list) {
            stringBuilder.append(str);
            stringBuilder.append(sep);
        }
        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }
}
