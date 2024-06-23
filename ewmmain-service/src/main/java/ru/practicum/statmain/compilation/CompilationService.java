package ru.practicum.statmain.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.statmain.compilation.dto.CompilationCreateRequest;
import ru.practicum.statmain.compilation.dto.CompilationResponse;
import ru.practicum.statmain.event.Event;
import ru.practicum.statmain.event.EventRepository;
import ru.practicum.statmain.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompilationService {

    private final CompilationRepository compilationRepository;

    private final EventRepository eventRepository;

    public CompilationResponse addCompilation(CompilationCreateRequest request) {
        List<Event> events = request.getEvents().isEmpty() ? new ArrayList<>() :
                eventRepository.findByIds(request.getEvents());

        if (events.size() != request.getEvents().size()) {
            throw new NotFoundException("Events not found");
        }

        Compilation compilation = Compilation.builder()
                .name(request.getTitle())
                .pinned(request.isPinned())
                .events(events)
                .build();

        compilationRepository.save(compilation);

        return CompilationMapper.toDto(compilation);
    }

    public List<CompilationResponse> getCompilations(Integer from, Integer size, Boolean pinned) {
        Page<Compilation> page = compilationRepository.findAllByPinned(PageRequest.of(from / size, size), pinned);
        return CompilationMapper.toDto(page.getContent());
    }

    public CompilationResponse getCompilation(Long id) {
        return CompilationMapper.toDto(compilationRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Compilation not found")));
    }

}
