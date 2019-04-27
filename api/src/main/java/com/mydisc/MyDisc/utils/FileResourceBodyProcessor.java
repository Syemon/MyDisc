package com.mydisc.MyDisc.utils;

import com.mydisc.MyDisc.entity.File;

import java.util.HashMap;
import java.util.Map;

public class FileResourceBodyProcessor {
    public static Map<String, String> getFullFileBody(File file) {
        Map<String, String> body = new HashMap<>();
        body.put("id", file.getId().toString());
        body.put("name", file.getName());
        body.put("type", file.getType());
        body.put("size", Long.toString(file.getSize()));

        return body;
    }
}
