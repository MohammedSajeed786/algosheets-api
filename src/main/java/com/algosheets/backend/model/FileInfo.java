package com.algosheets.backend.model;


import lombok.Data;

@Data
public class FileInfo{
    private String id;
    private String name;
    private String mimeType;
    private String kind;
}
