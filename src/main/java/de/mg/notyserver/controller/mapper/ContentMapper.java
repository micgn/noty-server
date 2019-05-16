package de.mg.notyserver.controller.mapper;

import de.mg.notyserver.controller.content.NoteDto;
import de.mg.notyserver.controller.content.TagDto;
import de.mg.notyserver.model.content.Note;
import de.mg.notyserver.model.content.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ContentMapper {

    List<NoteDto> mapNotes(List<Note> entity);

    NoteDto mapNote(Note entity);

    List<TagDto> mapTags(List<Tag> entity);

    TagDto mapTag(Tag entity);

}
