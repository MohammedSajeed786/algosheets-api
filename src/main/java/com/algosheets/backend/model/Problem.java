package com.algosheets.backend.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Problem {

    private String problemLink;
    private String problemName;
    private int numOccur;
    private int status;


}
