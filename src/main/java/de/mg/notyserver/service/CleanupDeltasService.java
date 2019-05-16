package de.mg.notyserver.service;

import de.mg.notyserver.model.action.NoteDelta;
import de.mg.notyserver.model.action.TagDelta;
import de.mg.notyserver.repository.NoteDeltaRepo;
import de.mg.notyserver.repository.NoteTagDeltaRepo;
import de.mg.notyserver.repository.TagDeltaRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.ZoneId;
import java.util.List;

import static de.mg.notyserver.controller.delta.ActionEnum.DELETE;
import static java.lang.System.currentTimeMillis;
import static java.time.Instant.ofEpochMilli;
import static java.time.LocalDateTime.ofInstant;
import static java.time.ZoneId.of;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

@Service
@Slf4j
public class CleanupDeltasService {

    private static final ZoneId ZONE = of("Europe/Berlin");

    @Value("${noty.cleanupDelayHours:720}")
    private Long cleanupDelayHours;

    @Autowired
    private NoteDeltaRepo noteDeltaRepo;
    @Autowired
    private TagDeltaRepo tagDeltaRepo;
    @Autowired
    private NoteTagDeltaRepo noteTagDeltaRepo;


    @Scheduled(initialDelay = 20 * 1000, fixedDelay = 24 * 60 * 60 * 1000)
    @Transactional
    public void run() {

        Long before = currentTimeMillis() - cleanupDelayHours * 60 * 60 * 1000;
        log.info("cleanup before = " + ofInstant(ofEpochMilli(before), ZONE).format(ISO_LOCAL_DATE_TIME));
        long countBefore = noteDeltaRepo.count() + tagDeltaRepo.count() + noteTagDeltaRepo.count();
        log.info("total delta count = {}", countBefore);

        cleanupDeletedNotes(before);
        cleanupDeletedTags(before);

        long countAfter = noteDeltaRepo.count() + tagDeltaRepo.count() + noteTagDeltaRepo.count();
        log.info("deleted {}", countBefore - countAfter);
    }

    private void cleanupDeletedNotes(Long before) {

        List<NoteDelta> noteDeltas = noteDeltaRepo.findAllBefore(before);

        noteDeltas.stream().filter(delta -> delta.getAction() == DELETE)
                .forEach(deletionDelta -> {
                    noteDeltas.stream()
                            .filter(delta -> delta.getNoteId().equals(deletionDelta.getNoteId()))
                            .forEach(toDelete ->
                                    noteDeltaRepo.delete(toDelete));
                    noteTagDeltaRepo.deleteAll(noteTagDeltaRepo.findByNoteId(deletionDelta.getNoteId()));
                });
    }

    private void cleanupDeletedTags(Long before) {

        List<TagDelta> tagDeltas = tagDeltaRepo.findAllBefore(before);

        tagDeltas.stream().filter(delta -> delta.getAction() == DELETE)
                .forEach(deletionDelta -> {
                    tagDeltas.stream()
                            .filter(delta -> delta.getTagId().equals(deletionDelta.getTagId()))
                            .forEach(toDelete ->
                                    tagDeltaRepo.delete(toDelete));
                    noteTagDeltaRepo.deleteAll(noteTagDeltaRepo.findByTagId(deletionDelta.getTagId()));
                });
    }


}
