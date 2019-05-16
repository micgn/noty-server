package de.mg.notyserver.controller.mapper;

import de.mg.notyserver.controller.delta.req.NoteDeltaDto;
import de.mg.notyserver.controller.delta.req.NoteTagDeltaDto;
import de.mg.notyserver.controller.delta.req.TagDeltaDto;
import de.mg.notyserver.model.action.NoteDelta;
import de.mg.notyserver.model.action.NoteTagDelta;
import de.mg.notyserver.model.action.TagDelta;
import de.mg.notyserver.model.content.Note;
import de.mg.notyserver.model.content.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface DeltaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "action", ignore = true)
    NoteDelta map2Delta(NoteDeltaDto dto);

    @Mapping(target = "id", source = "noteId")
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "updated", expression = "java(java.time.Instant.ofEpochMilli(dto.getUpdated()))")
    Note map2Entity(NoteDeltaDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "action", ignore = true)
    TagDelta map2Delta(TagDeltaDto dto);

    @Mapping(target = "id", source = "tagId")
    @Mapping(target = "updated", expression = "java(java.time.Instant.ofEpochMilli(dto.getUpdated()))")
    Tag map2Entity(TagDeltaDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "action", ignore = true)
    NoteTagDelta map2Delta(NoteTagDeltaDto dto);

    NoteDeltaDto map(NoteDelta entity);

    TagDeltaDto map(TagDelta entity);

    NoteTagDeltaDto map(NoteTagDelta entity);
}
