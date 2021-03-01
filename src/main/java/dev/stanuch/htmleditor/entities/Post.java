package dev.stanuch.htmleditor.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter
    private long id;

    @NotEmpty(message = "Name is mandatory")
    @Getter
    @Setter
    private String name;

    @NotEmpty(message = "Content is mandatory")
    @Getter
    @Setter
    private String content;
}
