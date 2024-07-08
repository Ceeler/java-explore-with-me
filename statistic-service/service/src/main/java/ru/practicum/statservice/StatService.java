package ru.practicum.statservice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


@Service
@RequiredArgsConstructor
public class StatService {

    private final StatRepository statRepository;

    public void addStat(EndpointHit dto) {
        StatEntity entity = StatMapper.toEntity(dto);
        statRepository.save(entity);
    }

    public List<EndpointStat> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique)  {

        if (uris == null || uris.isEmpty()) {
            if (unique) {
                return StatMapper.toDtoList(statRepository.getStatUnique(start, end));
            } else {
                return StatMapper.toDtoList(statRepository.getStat(start, end));
            }
        } else {
            List<StatCountHits> response = new ArrayList<>();
            for (String uri : uris) {
                if (unique) {
                    response.addAll(statRepository.getStatUniqueLikeUri(start, end, "%" + uri.toLowerCase() + "%"));
                } else {
                    response.addAll(statRepository.getStatLikeUri(start, end, "%" + uri.toLowerCase() + "%"));
                }
            }
            response.sort(Comparator.comparingLong(StatCountHits::getHits).reversed());
            return StatMapper.toDtoList(response);
        }
    }
}
