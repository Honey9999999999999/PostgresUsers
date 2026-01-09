package org.example.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Article extends Content {
    @Column(name = "body")
    private String body;

    @Column(name = "viewsCount")
    private Integer viewsCount;
}