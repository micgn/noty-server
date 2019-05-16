package de.mg.notyserver.model.content;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Note extends AbstractContent {

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable
    private Set<Tag> tags;

    @Column(nullable = false)
    private String text;

    @Column
    private LocalDate dueDate;
}
