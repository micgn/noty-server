package de.mg.notyserver.controller;

import de.mg.notyserver.controller.delta.ActionEnum;
import de.mg.notyserver.controller.delta.req.NoteDeltaDto;
import de.mg.notyserver.controller.delta.req.NoteTagDeltaDto;
import de.mg.notyserver.controller.delta.req.TagDeltaDto;
import de.mg.notyserver.controller.delta.res.DeltaActionDto;
import de.mg.notyserver.controller.delta.res.ResponseDto;
import de.mg.notyserver.controller.mapper.DeltaMapper;
import de.mg.notyserver.model.action.NoteDelta;
import de.mg.notyserver.model.action.NoteTagDelta;
import de.mg.notyserver.model.action.TagDelta;
import de.mg.notyserver.model.content.Note;
import de.mg.notyserver.model.content.Tag;
import de.mg.notyserver.repository.NoteDeltaRepo;
import de.mg.notyserver.repository.NoteRepo;
import de.mg.notyserver.repository.NoteTagDeltaRepo;
import de.mg.notyserver.repository.TagDeltaRepo;
import de.mg.notyserver.repository.TagRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@RestController("delta/")
@Slf4j
public class DeltaController {

    @Autowired
    private NoteRepo noteRepo;
    @Autowired
    private TagRepo tagRepo;
    @Autowired
    private NoteDeltaRepo noteDeltaRepo;
    @Autowired
    private TagDeltaRepo tagDeltaRepo;
    @Autowired
    private NoteTagDeltaRepo noteTagDeltaRepo;

    @Autowired
    private DeltaMapper deltaMapper;


    @PostMapping("note")
    @Transactional
    public ResponseDto create(@RequestBody @Valid NoteDeltaDto noteDto,
                              @RequestParam("lastReceivedServerDelta") String lastReceivedServerDelta) {

        log.debug("create note");
        if (isEmpty(noteDto.getText()) ||
                noteRepo.findById(noteDto.getNoteId()).isPresent() ||
                noteRepo.findByText(noteDto.getText()).isPresent())
            return getDeltas(false, lastReceivedServerDelta);

        ResponseDto deltas = getDeltas(true, lastReceivedServerDelta);
        NoteDelta deltaToSave = deltaMapper.map2Delta(noteDto);
        deltaToSave.setAction(ActionEnum.CREATE);
        noteDeltaRepo.save(deltaToSave);

        noteRepo.save(deltaMapper.map2Entity(noteDto));
        return deltas;
    }


    @PutMapping("note")
    @Transactional
    public ResponseDto update(@RequestBody @Valid NoteDeltaDto noteDto,
                              @RequestParam("lastReceivedServerDelta") String lastReceivedServerDelta) {
        log.debug("update note");
        if (isEmpty(noteDto.getText()))
            return getDeltas(false, lastReceivedServerDelta);
        Optional<Note> entity = noteRepo.findById(noteDto.getNoteId());
        Instant updated = Instant.ofEpochMilli(noteDto.getUpdated());
        if (!entity.isPresent() || entity.get().getUpdated().isAfter(updated))
            return getDeltas(false, lastReceivedServerDelta);

        ResponseDto deltas = getDeltas(true, lastReceivedServerDelta);

        NoteDelta deltaToSave = deltaMapper.map2Delta(noteDto);
        deltaToSave.setAction(ActionEnum.UPDATE);
        noteDeltaRepo.save(deltaToSave);

        noteRepo.save(deltaMapper.map2Entity(noteDto));
        return deltas;
    }

    @DeleteMapping("note")
    @Transactional
    public ResponseDto delete(@RequestBody @Valid NoteDeltaDto noteDto,
                              @RequestParam("lastReceivedServerDelta") String lastReceivedServerDelta) {
        log.debug("delete note");
        Optional<Note> entity = noteRepo.findById(noteDto.getNoteId());
        Instant updated = Instant.ofEpochMilli(noteDto.getUpdated());
        if (!entity.isPresent() || entity.get().getUpdated().isAfter(updated))
            return getDeltas(false, lastReceivedServerDelta);

        ResponseDto deltas = getDeltas(true, lastReceivedServerDelta);

        NoteDelta deltaToSave = deltaMapper.map2Delta(noteDto);
        deltaToSave.setAction(ActionEnum.DELETE);
        noteDeltaRepo.save(deltaToSave);

        noteRepo.deleteById(noteDto.getNoteId());
        return deltas;
    }

    @PostMapping("tag")
    @Transactional
    public ResponseDto create(@RequestBody @Valid TagDeltaDto tagDto,
                              @RequestParam("lastReceivedServerDelta") String lastReceivedServerDelta) {
        log.debug("create tag");
        if (isEmpty(tagDto.getName()) ||
                tagRepo.findById(tagDto.getTagId()).isPresent() ||
                tagRepo.findByName(tagDto.getName()).isPresent())
            return getDeltas(false, lastReceivedServerDelta);

        ResponseDto deltas = getDeltas(true, lastReceivedServerDelta);

        TagDelta deltaToSave = deltaMapper.map2Delta(tagDto);
        deltaToSave.setAction(ActionEnum.CREATE);
        tagDeltaRepo.save(deltaToSave);

        tagRepo.save(deltaMapper.map2Entity(tagDto));
        return deltas;
    }

    @PutMapping("tag")
    @Transactional
    public ResponseDto update(@RequestBody @Valid TagDeltaDto tagDto,
                              @RequestParam("lastReceivedServerDelta") String lastReceivedServerDelta) {
        log.debug("update tag");
        Optional<Tag> entity = tagRepo.findById(tagDto.getTagId());
        Instant updated = Instant.ofEpochMilli(tagDto.getUpdated());
        if (isEmpty(tagDto.getName()) || !entity.isPresent() || entity.get().getUpdated().isAfter(updated))
            return getDeltas(false, lastReceivedServerDelta);

        ResponseDto deltas = getDeltas(true, lastReceivedServerDelta);

        TagDelta deltaToSave = deltaMapper.map2Delta(tagDto);
        deltaToSave.setAction(ActionEnum.UPDATE);
        tagDeltaRepo.save(deltaToSave);

        tagRepo.save(deltaMapper.map2Entity(tagDto));
        return deltas;
    }

    @DeleteMapping("tag")
    @Transactional
    public ResponseDto delete(@RequestBody @Valid TagDeltaDto tagDto,
                              @RequestParam("lastReceivedServerDelta") String lastReceivedServerDelta) {
        log.debug("delete tag");
        Optional<Tag> entity = tagRepo.findById(tagDto.getTagId());
        Instant updated = Instant.ofEpochMilli(tagDto.getUpdated());
        if (!entity.isPresent() || entity.get().getUpdated().isAfter(updated))
            isEmpty(tagDto.getName());

        ResponseDto deltas = getDeltas(true, lastReceivedServerDelta);

        TagDelta deltaToSave = deltaMapper.map2Delta(tagDto);
        deltaToSave.setAction(ActionEnum.DELETE);
        tagDeltaRepo.save(deltaToSave);

        tagRepo.deleteById(tagDto.getTagId());
        return deltas;
    }

    @PostMapping("notetag")
    @Transactional
    public ResponseDto post(@RequestBody @Valid NoteTagDeltaDto noteTag,
                            @RequestParam("lastReceivedServerDelta") String lastReceivedServerDelta) {

        log.debug("create noteTag");
        return postOrDelete(noteTag, ActionEnum.CREATE, lastReceivedServerDelta);
    }

    @DeleteMapping("notetag")
    @Transactional
    public ResponseDto delete(@RequestBody @Valid NoteTagDeltaDto noteTag,
                              @RequestParam("lastReceivedServerDelta") String lastReceivedServerDelta) {

        log.debug("delete noteTag");
        return postOrDelete(noteTag, ActionEnum.DELETE, lastReceivedServerDelta);
    }

    private ResponseDto postOrDelete(NoteTagDeltaDto noteTag, ActionEnum action, String lastReceivedServerDelta) {

        List<NoteTagDelta> existingDeltas = noteTagDeltaRepo.findStartingFromLatest(noteTag.getNoteId(), noteTag.getTagId(), PageRequest.of(0, 1));
        if (!existingDeltas.isEmpty()) {
            NoteTagDelta last = existingDeltas.get(0);
            if (last.getUpdated() > noteTag.getUpdated() || last.getAction() == action)
                return getDeltas(false, lastReceivedServerDelta);
        }

        ResponseDto deltas = getDeltas(true, lastReceivedServerDelta);

        NoteTagDelta deltaToSave = deltaMapper.map2Delta(noteTag);
        deltaToSave.setAction(action);
        noteTagDeltaRepo.save(deltaToSave);

        Note noteToSave = noteRepo.findById(noteTag.getNoteId()).orElse(null);
        Tag savedTag = tagRepo.findById(noteTag.getTagId()).orElse(null);
        if (noteToSave == null || savedTag == null)
            return getDeltas(false, lastReceivedServerDelta);

        if (noteToSave.getTags() == null)
            noteToSave.setTags(new HashSet<>());
        if (action == ActionEnum.CREATE)
            noteToSave.getTags().add(savedTag);
        else if (action == ActionEnum.DELETE)
            noteToSave.getTags().removeIf(t -> t.getId().equals(savedTag.getId()));
        noteRepo.save(noteToSave);
        return deltas;
    }

    private ResponseDto getDeltas(boolean success, String lastReceivedServerDelta) {
        ResponseDto result = getDeltas(lastReceivedServerDelta);
        result.setSaved(success);
        return result;
    }


    @GetMapping("deltas")
    public ResponseDto getDeltas(@RequestParam("lastReceivedServerDelta") String lastReceivedServerDelta) {

        log.debug("get deltas");
        Long since = Long.valueOf(lastReceivedServerDelta);

        List<NoteDelta> notes = noteDeltaRepo.findAllSince(since);
        List<TagDelta> tags = tagDeltaRepo.findAllSince(since);
        List<NoteTagDelta> noteTags = noteTagDeltaRepo.findAllSince(since);

        List<DeltaActionDto> newDeltas = new ArrayList<>();
        notes.forEach(n ->
                newDeltas.add(DeltaActionDto.builder()
                        .note(deltaMapper.map(n))
                        .action(n.getAction())
                        .build()));
        tags.forEach(t ->
                newDeltas.add(DeltaActionDto.builder()
                        .tag(deltaMapper.map(t))
                        .action(t.getAction())
                        .build()));
        noteTags.forEach(nt ->
                newDeltas.add(DeltaActionDto.builder()
                        .noteTag(deltaMapper.map(nt))
                        .action(nt.getAction())
                        .build()));

        newDeltas.sort((a1, a2) -> a2.getUpdated().compareTo(a1.getUpdated()));

        return ResponseDto.builder().newDeltas(newDeltas).build();
    }

    private boolean isEmpty(String text) {
        return text == null || text.trim().length() == 0;
    }
}
