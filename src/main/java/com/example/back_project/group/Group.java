package com.example.back_project.group;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity (name="groupdb")
public class Group {

    @Id
    @Column
    private Long id;
    private String name;
    private String description;

}
