package dev.stanuch.htmleditor.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Post extends Auditable {
    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Getter
    @Setter
    @NotEmpty(message = "Name is mandatory")
    private String name;

    @Getter
    @Setter
    @NotEmpty(message = "Content is mandatory")
    private String content;
}
