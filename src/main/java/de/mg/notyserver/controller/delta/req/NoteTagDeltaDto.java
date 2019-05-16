package de.mg.notyserver.controller.delta.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class NoteTagDeltaDto {

    @NotNull
    private Long updated;

    @NotBlank
    private String noteId;

    @NotBlank
    private String tagId;
}
