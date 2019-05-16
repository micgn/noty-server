package de.mg.notyserver.model.action;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class NoteDelta extends AbstractDelta {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String noteId;

    @Column
    private String text;

    @Column
    private LocalDate dueDate;
}
