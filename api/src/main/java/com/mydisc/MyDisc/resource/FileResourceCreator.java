package com.mydisc.MyDisc.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mydisc.MyDisc.entity.File;
import org.springframework.hateoas.Resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileResourceCreator {
    public static Resources<FileResource> getResources(List<File> files) {
        List<FileResource> fileResources = new ArrayList<>();

        for (File file : files) {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> body = new HashMap<>();

            fileResources.add(new FileResource(
                    file,
                    FileResourceCreator.getShortFileBody(file)
            ));
        }

        return new Resources<>(fileResources);
    }

    public static FileResource getResource(File file) {
        Map<String, String> body = FileResourceCreator.getFullFileBody(file);

        return new FileResource(file, body);
    }

    private static Map<String, String> getShortFileBody(File file) {
        Map<String, String> body = new HashMap<>();
        body.put("name", file.getName());
        body.put("type", file.getType());

        return body;
    }

    private static Map<String, String> getFullFileBody(File file) {
        Map<String, String> body = new HashMap<>();
        body.put("id", file.getId().toString());
        body.put("name", file.getName());
        body.put("type", file.getType());
        body.put("size", Long.toString(file.getSize()));

        return body;
    }
}
