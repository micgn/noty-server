package de.mg.notyserver.controller.content;

import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class NoteDto {

    private String text;

    private LocalDate dueDate;

    private Set<TagDto> tags;
}
