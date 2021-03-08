package dev.stanuch.htmleditor.entities;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Auditable {

    @CreatedDate
    @Getter
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    protected Date createdDate;

    @LastModifiedDate
    @Getter
    @Temporal(TemporalType.TIMESTAMP)
    protected Date lastModifiedDate;
}
