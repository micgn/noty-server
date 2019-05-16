package de.mg.notyserver.model.action;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class TagDelta extends AbstractDelta {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String tagId;

    @Column
    private String name;

}
