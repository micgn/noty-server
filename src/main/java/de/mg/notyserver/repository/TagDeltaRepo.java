package de.mg.notyserver.repository;

import de.mg.notyserver.model.action.TagDelta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TagDeltaRepo extends JpaRepository<TagDelta, Long> {

    @Query("select t from TagDelta t where t.updated > :since")
    List<TagDelta> findAllSince(@Param("since") Long since);

    @Query("select t from TagDelta t where t.updated < :before")
    List<TagDelta> findAllBefore(@Param("before") Long before);

    List<TagDelta> findByTagId(String tagId);
}











































































































