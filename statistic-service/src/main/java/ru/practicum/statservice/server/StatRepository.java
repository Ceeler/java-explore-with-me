package ru.practicum.statservice.server;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<StatEntity, Long> {

    @Query("SELECT new ru.practicum.statservice.server.StatCountHits(e.app, e.uri, COUNT(e.uri)) " +
            "FROM StatEntity e " +
            "WHERE (e.timestamp BETWEEN :start AND :end) " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY COUNT(e.uri) DESC")
    List<StatCountHits> getStat(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.statservice.server.StatCountHits( e.app, e.uri, COUNT(DISTINCT e.uri)) " +
            "FROM StatEntity e " +
            "WHERE (e.timestamp BETWEEN :start AND :end) " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY COUNT(e.uri) DESC")
    List<StatCountHits> getStatUnique(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.statservice.server.StatCountHits(e.app, e.uri, COUNT(e.uri)) " +
            "FROM StatEntity e " +
            "WHERE (e.timestamp BETWEEN :start AND :end) AND " +
            "e.uri IN (:uris)" +
            "GROUP BY e.app, e.uri " +
            "ORDER BY COUNT(e.uri) DESC")
    List<StatCountHits> getStatInList(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.statservice.server.StatCountHits( e.app, e.uri, COUNT(DISTINCT e.uri)) " +
            "FROM StatEntity e " +
            "WHERE (e.timestamp BETWEEN :start AND :end) AND " +
            "e.uri IN (:uris)" +
            "GROUP BY e.app, e.uri " +
            "ORDER BY COUNT(e.uri) DESC")
    List<StatCountHits> getStatUniqueInList(LocalDateTime start, LocalDateTime end, List<String> uris);

}
