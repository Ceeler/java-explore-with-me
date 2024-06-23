package ru.practicum.statmain.request;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

    @EntityGraph(attributePaths = {"event.id", "event.initiator.id"})
    List<Request> findAllByEvent_IdAndEvent_Initiator_Id(Long eventId, Long initiatorId);

    @Query("SELECT r FROM Request r " +
            "JOIN FETCH r.event " +
            "JOIN FETCH r.requester " +
            "WHERE r.event.id = :eventId AND " +
            "r.event.initiator.id = :initiatorId AND " +
            "r.id IN :ids")
    List<Request> findAllByIds(Long initiatorId, Long eventId, List<Long> ids);
}
