package com.algosheets.backend.requestprocessor.impl;

import com.algosheets.backend.model.FileInfo;
import com.algosheets.backend.model.Problem;
import com.algosheets.backend.requestprocessor.FilesRequestProcessor;
import com.algosheets.backend.service.FilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class FilesRequestProcessorImpl implements FilesRequestProcessor {

    @Autowired
    FilesService filesService;

    @Override
    public List<FileInfo> getAllFiles() {
        return filesService.getAllFiles();
    }
    @Override
    public List<Problem> getProblemsFromFile(String fileId) {
        return filesService.getProblemsFromFile(fileId);
    }


    @Override
    public void updateFileContent(String fileId, List<Problem> problems) throws IOException {
        filesService.updateProblemsInCsv(fileId,problems);
    }

}
