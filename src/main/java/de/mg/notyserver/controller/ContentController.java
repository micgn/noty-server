package de.mg.notyserver.controller;

import de.mg.notyserver.controller.content.NoteDto;
import de.mg.notyserver.controller.content.TagDto;
import de.mg.notyserver.controller.mapper.ContentMapper;
import de.mg.notyserver.repository.NoteRepo;
import de.mg.notyserver.repository.TagRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("content/")
public class ContentController {

    @Autowired
    private NoteRepo noteRepo;
    @Autowired
    private TagRepo tagRepo;
    @Autowired
    private ContentMapper mapper;

    @GetMapping("notes")
    public List<NoteDto> findAllNotes() {
        return mapper.mapNotes(noteRepo.findAll());
    }

    @GetMapping("tags")
    public List<TagDto> findAllTags() {
        return mapper.mapTags(tagRepo.findAll());
    }
}
