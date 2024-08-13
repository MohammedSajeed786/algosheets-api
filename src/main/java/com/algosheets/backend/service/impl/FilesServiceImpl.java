package com.algosheets.backend.service.impl;

import com.algosheets.backend.model.FileInfo;
import com.algosheets.backend.model.Problem;
import com.algosheets.backend.model.ProblemStatus;
import com.algosheets.backend.service.AuthService;
import com.algosheets.backend.service.FilesService;
import com.algosheets.backend.service.OAuthService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;


@Service
public class FilesServiceImpl implements FilesService {

    @Autowired
    AuthService authService;

    @Autowired
    OAuthService oAuthService;

    @Override
    public List<FileInfo> getAllFiles() {


        //fetch access token
        String accessToken = authService.getAccessToken();

        //use this access token to fetch files
        return oAuthService.getAllFiles(accessToken);
    }

    @Override
    public List<Problem> getProblemsFromFile(String fileId) throws IOException {
        //fetch access token
        String accessToken = authService.getAccessToken();

        //use this access token to fetch files
        String fileContent = oAuthService.getFileContent(fileId, accessToken);

        return parseCsvContent(fileContent);
    }

    private List<Problem> parseCsvContent(String content) throws IOException {
        List<Problem> problems = new ArrayList<>();
        try (Reader reader = new StringReader(content);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.builder().setHeader().build())) {
//            CSVFormat.DEFAULT.withHeader()
            for (CSVRecord record : csvParser) {
                String link = record.get("problem_link");
                String name = record.get("problem_name");
                int numOccur = Integer.parseInt(record.get("num_occur"));
                int status = csvParser.getHeaderMap().containsKey("status") ? Integer.parseInt(record.get("status")) : ProblemStatus.Unsolved.getId();


                Problem problem = Problem.builder()
                        .problemLink(link)
                        .problemName(name)
                        .numOccur(numOccur)
                        .status(status)  // Assuming status is not provided
                        .build();

                problems.add(problem);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("exception in parsing file "+e.getMessage());
        }
        return problems;

    }

    @Override
    public void updateProblemsInCsv(String fileId, List<Problem> problems) throws IOException {
        String fileContent = convertProblemsToCsv(problems);
        oAuthService.UpdateFileContent(fileId, fileContent, authService.getAccessToken());

    }

    private String convertProblemsToCsv(List<Problem> problems) throws IOException {
        StringWriter out = new StringWriter();
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder().setHeader("problem_link", "problem_name", "num_occur", "status").build();
        CSVPrinter csvPrinter = new CSVPrinter(out, csvFormat);

        for (Problem problem : problems) {
            csvPrinter.printRecord(problem.getProblemLink(),
                    problem.getProblemName(),
                    problem.getNumOccur(),
                    problem.getStatus());
        }
        csvPrinter.flush();
        return out.toString();
    }
}
