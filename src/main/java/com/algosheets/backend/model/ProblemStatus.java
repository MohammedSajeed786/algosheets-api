package com.algosheets.backend.model;


import lombok.Getter;

@Getter
public enum ProblemStatus {
    Unsolved(1),Solved(2),Revision(3);
    int id;

    ProblemStatus(int id){
        this.id=id;
    }

}
