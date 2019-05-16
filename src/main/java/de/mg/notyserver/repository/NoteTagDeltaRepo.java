package de.mg.notyserver.repository;

import de.mg.notyserver.model.action.NoteTagDelta;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NoteTagDeltaRepo extends JpaRepository<NoteTagDelta, Long> {

    @Query("select nt from NoteTagDelta nt where nt.noteId = :noteId and nt.tagId = :tagId order by nt.updated desc")
    List<NoteTagDelta> findStartingFromLatest(@Param("noteId") String noteId, @Param("tagId") String tagId, Pageable pageable);

    @Query("select nt from NoteTagDelta nt where nt.updated > :since")
    List<NoteTagDelta> findAllSince(@Param("since") Long since);

    List<NoteTagDelta> findByNoteId(String noteId);

    List<NoteTagDelta> findByTagId(String tagId);

    List<NoteTagDelta> findByNoteIdAndTagId(String noteId, String tagId);
}











































































































