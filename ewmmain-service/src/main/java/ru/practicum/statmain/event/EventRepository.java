package ru.practicum.statmain.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {

    @Query("SELECT e FROM Event e WHERE e.id IN :ids")
    List<Event> findByIds(List<Long> ids);

    @EntityGraph(attributePaths = {"initiator", "category", "confirmedRequests"})
    Page<Event> findByInitiator_Id(Long userId, Pageable pageable);

    @EntityGraph(attributePaths = {"initiator", "category", "confirmedRequests"})
    Optional<Event> findByIdAndInitiator_Id(Long eventId, Long userId);

    @Query("SELECT e FROM Event e " +
            "JOIN FETCH e.initiator " +
            "WHERE e.id = :eventId")
    Optional<Event> findByIdWithRequests(Long eventId);

    @Query("SELECT e FROM Event e " +
            "JOIN FETCH e.initiator " +
            "WHERE e.initiator.id = :userId AND " +
            "e.eventDate > :now AND " +
            "e.state = 'PUBLISHED'")
    List<Event> getUserActualEvents(Long userId, LocalDateTime now);

}
