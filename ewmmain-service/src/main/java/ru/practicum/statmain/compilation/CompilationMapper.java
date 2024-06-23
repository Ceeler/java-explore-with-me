package ru.practicum.statmain.compilation;

import ru.practicum.statmain.compilation.dto.CompilationResponse;
import ru.practicum.statmain.event.EventMapper;

import java.util.List;
import java.util.stream.Collectors;

public class CompilationMapper {

    private CompilationMapper() {
    }

    public static CompilationResponse toDto(Compilation compilation) {
        return CompilationResponse.builder()
                .id(compilation.getId())
                .title(compilation.getName())
                .pinned(compilation.getPinned())
                .events(EventMapper.toEventShortDto(compilation.getEvents())) //TODO views
                .build();
    }

    public static List<CompilationResponse> toDto(List<Compilation> compilation) {
        return compilation.stream().map(CompilationMapper::toDto).collect(Collectors.toList());
    }

    public static Compilation toEntity(CompilationResponse dto) {
        return null;
    }

}
