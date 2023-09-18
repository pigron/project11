package com.example.back_project.project;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Setter
@Getter
@Entity (name = "projectdb")
public class Project {

    @Id
    @Column
    private Long id;
    private String name;
    private String name_with_namespace;
    private String description;

}