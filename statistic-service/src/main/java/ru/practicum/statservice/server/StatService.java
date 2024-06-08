package ru.practicum.statservice.server;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.statservice.dto.EndpointHit;
import ru.practicum.statservice.dto.EndpointStat;

import java.time.LocalDateTime;
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
        if (unique) {
            if (uris.isEmpty())  {
                return StatMapper.toDtoList(statRepository.getStatUnique(start, end));
            }
            return StatMapper.toDtoList(statRepository.getStatUniqueInList(start, end, uris));
        }
        if (uris.isEmpty())  {
            return StatMapper.toDtoList(statRepository.getStat(start, end));
        }
        return StatMapper.toDtoList(statRepository.getStatInList(start, end, uris));
    }

}
