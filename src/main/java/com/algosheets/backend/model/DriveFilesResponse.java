package com.algosheets.backend.model;

import lombok.Data;

import java.util.List;

@Data
public class DriveFilesResponse {
    List<FileInfo> files;
}
