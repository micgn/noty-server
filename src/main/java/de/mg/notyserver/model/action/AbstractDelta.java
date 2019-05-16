package de.mg.notyserver.model.action;

import de.mg.notyserver.controller.delta.ActionEnum;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Data
public class AbstractDelta {

    // no updates, only creations
    @Column(nullable = false)
    private Long updated;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ActionEnum action;

}
