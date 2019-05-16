package de.mg.notyserver.repository;

import de.mg.notyserver.model.action.NoteDelta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NoteDeltaRepo extends JpaRepository<NoteDelta, Long> {

    @Query("select n from NoteDelta n where n.updated > :since")
    List<NoteDelta> findAllSince(@Param("since") Long since);

    @Query("select n from NoteDelta n where n.updated < :before")
    List<NoteDelta> findAllBefore(@Param("before") Long before);

    List<NoteDelta> findByNoteId(String noteId);
}











































































































