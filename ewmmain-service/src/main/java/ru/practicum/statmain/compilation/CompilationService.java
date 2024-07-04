package ru.practicum.statmain.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.statmain.compilation.dto.CompilationCreateRequest;
import ru.practicum.statmain.compilation.dto.CompilationDto;
import ru.practicum.statmain.compilation.dto.CompilationPatchRequest;
import ru.practicum.statmain.event.Event;
import ru.practicum.statmain.event.EventRepository;
import ru.practicum.statmain.exception.ConflictException;
import ru.practicum.statmain.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompilationService {

    private final CompilationRepository compilationRepository;

    private final EventRepository eventRepository;

    public CompilationDto addCompilation(CompilationCreateRequest request) {
        checkName(request.getTitle());

        List<Event> events = null;
        if (request.getEvents() == null || request.getEvents().isEmpty()) {
            events = new ArrayList<>();
        } else {
            events = eventRepository.findByIds(request.getEvents());

            if (events.size() != request.getEvents().size()) {
                throw new NotFoundException("Events not found");
            }
        }

        Compilation compilation = Compilation.builder()
                .name(request.getTitle())
                .pinned(request.isPinned())
                .events(events)
                .build();

        compilationRepository.save(compilation);

        return CompilationMapper.toDto(compilation);
    }

    public void deleteCompilation(Long id) {
        compilationRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Compilation not found"));
        compilationRepository.deleteById(id);
    }


    public CompilationDto patchCompilation(Long id, CompilationPatchRequest dto) {
        Compilation compilation = compilationRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Compilation not found"));

        if (dto.getTitle() != null) {
            checkName(dto.getTitle());
        }

        if (dto.getEvents() != null) {
            if (!dto.getEvents().isEmpty()) {
                List<Event> events = eventRepository.findByIds(dto.getEvents());
                if (events.size() != dto.getEvents().size()) {
                    throw new NotFoundException("Events not found");
                }
                compilation.setEvents(events);
            }
        }

        if (dto.getTitle() != null) {
            compilation.setName(dto.getTitle());
        }

        if (dto.getPinned() != null) {
            compilation.setPinned(dto.getPinned());
        }

        compilationRepository.save(compilation);

        return CompilationMapper.toDto(compilation);
    }

    public List<CompilationDto> getCompilations(Integer from, Integer size, Boolean pinned) {
        Page<Compilation> page = compilationRepository.findAllByPinned(PageRequest.of(from / size, size), pinned);
        return CompilationMapper.toDto(page.getContent());
    }

    public CompilationDto getCompilation(Long id) {
        return CompilationMapper.toDto(compilationRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Compilation not found")));
    }

    private void checkName(String name) {
        boolean isPresent = compilationRepository.findByName(name).isPresent();
        if (isPresent) {
            throw new ConflictException("Compilation with name " + name + " already exists");
        }
    }

}
