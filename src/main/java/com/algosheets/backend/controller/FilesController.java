package com.algosheets.backend.controller;


import com.algosheets.backend.dto.ProblemsDTO;
import com.algosheets.backend.model.FileInfo;
import com.algosheets.backend.model.Problem;
import com.algosheets.backend.requestprocessor.FilesRequestProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/files/v1")
@RestController
@CrossOrigin(origins = "*")
public class FilesController {

    @Autowired
    FilesRequestProcessor filesRequestProcessor;

    @GetMapping
    ResponseEntity<Map<String, List<FileInfo>>> getAllFiles() {

        Map<String, List<FileInfo>> response = new HashMap<>();
        response.put("files", filesRequestProcessor.getAllFiles());
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @GetMapping("{fileId}")
    ResponseEntity<Map<String, List<Problem>>> getFileContent(@PathVariable String fileId) throws IOException {

        Map<String, List<Problem>> response = new HashMap<>();
        response.put("problems", filesRequestProcessor.getProblemsFromFile(fileId));
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PutMapping("{fileId}")
    ResponseEntity<String> updateFileContent(@PathVariable String fileId, @RequestBody ProblemsDTO problemsDTO) throws IOException {
        filesRequestProcessor.updateFileContent(fileId,problemsDTO.getProblems());
        return new ResponseEntity<>("file updated successfully", HttpStatus.OK);
    }

}

