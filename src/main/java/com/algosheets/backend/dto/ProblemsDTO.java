package com.algosheets.backend.dto;

import com.algosheets.backend.model.Problem;
import lombok.Data;

import java.util.List;

@Data
public class ProblemsDTO {
    private List<Problem> problems;
}
