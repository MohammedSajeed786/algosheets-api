package com.algosheets.backend.service;

import com.algosheets.backend.model.FileInfo;
import com.algosheets.backend.model.Problem;

import java.io.IOException;
import java.util.List;

public interface FilesService {

    List<FileInfo> getAllFiles();

    List<Problem> getProblemsFromFile(String fileId);

    void updateProblemsInCsv(String fileId, List<Problem> problems) throws IOException;
}
