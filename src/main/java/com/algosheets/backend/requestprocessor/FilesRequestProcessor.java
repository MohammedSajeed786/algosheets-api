package com.algosheets.backend.requestprocessor;

import com.algosheets.backend.model.FileInfo;
import com.algosheets.backend.model.Problem;

import java.io.IOException;
import java.util.List;

public interface FilesRequestProcessor {

    List<FileInfo> getAllFiles();

    List<Problem> getProblemsFromFile(String fileId);

    void updateFileContent(String fileId, List<Problem> problems) throws IOException;
}
