package de.mg.notyserver.model.content;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.Instant;

@MappedSuperclass
@Data
public class AbstractContent {

    @Id
    private String id;

    @Column(nullable = false)
    private Instant updated;

}
